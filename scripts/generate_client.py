#!/usr/bin/env python3

import json
import os
import re
import shutil
import sys
from typing import Any, Dict, List, Optional, Set

import httpx

INCLUDE_PREFIXES = ["/v1", "/otel"]
HTTP_METHODS = {"GET", "POST", "PUT", "PATCH", "DELETE"}
SUCCESS_STATUS_CODES = {"200", "201"}
SCHEMA_REF_PREFIX = "#/components/schemas/"

JAVA_TYPE_MAP = {
    "string": "String",
    "integer": "Integer",
    "number": "Double",
    "boolean": "Boolean",
    "object": "Object",
}

BASE_PACKAGE = "com.judgmentlabs.judgeval.internal.api"
MODELS_PACKAGE = f"{BASE_PACKAGE}.models"
OUTPUT_DIR = "judgeval-java/src/main/java/com/judgmentlabs/judgeval/internal/api"
MODELS_DIR = f"{OUTPUT_DIR}/models"


def to_camel_case(name: str) -> str:
    parts = name.replace("-", "_").split("_")
    return parts[0] + "".join(word.capitalize() for word in parts[1:])


def to_pascal_case(name: str) -> str:
    if "_" in name:
        parts = name.split("_")
        return "".join(part[0].upper() + part[1:] if part else "" for part in parts)
    if name:
        return name[0].upper() + name[1:]
    return name


def to_snake_case(name: str) -> str:
    name = re.sub("(.)([A-Z][a-z]+)", r"\1_\2", name)
    return re.sub("([a-z0-9])([A-Z])", r"\1_\2", name).lower()


def resolve_ref(ref: str) -> str:
    assert ref.startswith(SCHEMA_REF_PREFIX)
    return to_pascal_case(ref.replace(SCHEMA_REF_PREFIX, ""))


def collect_schemas_with_id(spec: Dict[str, Any]) -> Dict[str, Dict[str, Any]]:
    schemas_by_id: Dict[str, Dict[str, Any]] = {}

    def collect_from_value(value: Any) -> None:
        if isinstance(value, dict):
            if "$id" in value:
                schema_id = value["$id"]
                if schema_id not in schemas_by_id:
                    schemas_by_id[schema_id] = {
                        k: v for k, v in value.items() if k != "$id"
                    }
                    print(f"  Collected schema: {schema_id}", file=sys.stderr)
            if "$ref" not in value:
                for v in value.values():
                    collect_from_value(v)
        elif isinstance(value, list):
            for item in value:
                collect_from_value(item)

    print("Collecting schemas with $id from OpenAPI spec...", file=sys.stderr)
    collect_from_value(spec)
    print(
        f"Collected {len(schemas_by_id)} schemas: {sorted(schemas_by_id.keys())}",
        file=sys.stderr,
    )
    return schemas_by_id


def extract_dependencies(
    schema: Dict[str, Any],
    schemas_by_id: Dict[str, Dict[str, Any]],
    visited: Optional[Set[str]] = None,
) -> Set[str]:
    if visited is None:
        visited = set()

    dependencies: Set[str] = set()

    if "$ref" in schema:
        return dependencies

    schema_id = schema.get("$id")
    if schema_id and schema_id in visited:
        return dependencies
    if schema_id:
        visited.add(schema_id)
        full_schema = schemas_by_id.get(schema_id, schema)
    else:
        full_schema = schema

    if "properties" in full_schema and isinstance(full_schema["properties"], dict):
        for prop_schema in full_schema["properties"].values():
            if isinstance(prop_schema, dict):
                if "$id" in prop_schema:
                    dependencies.add(prop_schema["$id"])
                dependencies.update(
                    extract_dependencies(prop_schema, schemas_by_id, visited)
                )

    if "items" in full_schema and isinstance(full_schema["items"], dict):
        items = full_schema["items"]
        if "$id" in items:
            dependencies.add(items["$id"])
        dependencies.update(extract_dependencies(items, schemas_by_id, visited))

    for union_key in ("anyOf", "oneOf", "allOf"):
        if union_key in full_schema and isinstance(full_schema[union_key], list):
            for item in full_schema[union_key]:
                if isinstance(item, dict):
                    if "$id" in item:
                        dependencies.add(item["$id"])
                    dependencies.update(
                        extract_dependencies(item, schemas_by_id, visited)
                    )

    if "additionalProperties" in full_schema and isinstance(
        full_schema["additionalProperties"], dict
    ):
        dependencies.update(
            extract_dependencies(
                full_schema["additionalProperties"], schemas_by_id, visited
            )
        )

    return dependencies


def find_used_schemas(
    spec: Dict[str, Any], schemas_by_id: Dict[str, Dict[str, Any]]
) -> Set[str]:
    used_schemas: Set[str] = set()

    for path, path_item in spec.get("paths", {}).items():
        if not any(path.startswith(prefix) for prefix in INCLUDE_PREFIXES):
            continue
        for method, operation in path_item.items():
            if not isinstance(operation, dict):
                continue

            request_body = operation.get("requestBody", {})
            if request_body:
                for content in request_body.get("content", {}).values():
                    if "schema" in content:
                        schema = content["schema"]
                        if "$id" in schema:
                            sid = schema["$id"]
                            used_schemas.add(sid)
                            if sid in schemas_by_id:
                                used_schemas.update(
                                    extract_dependencies(
                                        schemas_by_id[sid], schemas_by_id
                                    )
                                )

            for response in operation.get("responses", {}).values():
                if not isinstance(response, dict):
                    continue
                for content in response.get("content", {}).values():
                    if "schema" in content:
                        schema = content["schema"]
                        if "$id" in schema:
                            sid = schema["$id"]
                            used_schemas.add(sid)
                            if sid in schemas_by_id:
                                used_schemas.update(
                                    extract_dependencies(
                                        schemas_by_id[sid], schemas_by_id
                                    )
                                )
                        else:
                            used_schemas.update(
                                extract_dependencies(schema, schemas_by_id)
                            )

    return used_schemas


def get_java_type(schema: Any, schemas_by_id: Dict[str, Dict[str, Any]]) -> str:
    if not isinstance(schema, dict):
        if isinstance(schema, list) and schema:
            return f"List<{get_java_type(schema[0], schemas_by_id)}>"
        return "Object"

    if "$ref" in schema:
        return resolve_ref(schema["$ref"])

    if "$id" in schema:
        schema_data = schemas_by_id.get(schema["$id"], schema)
        if schema_data.get("type") == "array":
            items = schema_data.get("items", {})
            if items:
                return f"List<{get_java_type(items, schemas_by_id)}>"
            return "List<Object>"
        return schema["$id"]

    for union_key in ("anyOf", "oneOf", "allOf"):
        if union_key in schema and isinstance(schema[union_key], list):
            types = set()
            for item in schema[union_key]:
                if isinstance(item, dict) and item.get("type") == "null":
                    continue
                types.add(get_java_type(item, schemas_by_id))
            non_null = types - {"null"}
            if len(non_null) == 1:
                return list(non_null)[0]
            if not non_null:
                return "Object"
            print(
                f"Union type with multiple non-null types: {non_null}", file=sys.stderr
            )
            return "Object"

    schema_type = schema.get("type", "object")

    if schema_type == "array":
        items = schema.get("items", {})
        if items:
            if isinstance(items, dict):
                return f"List<{get_java_type(items, schemas_by_id)}>"
            elif isinstance(items, list) and items:
                return f"List<{get_java_type(items[0], schemas_by_id)}>"
        return "List<Object>"

    return JAVA_TYPE_MAP.get(schema_type, "Object")


def get_schema_name_from_id(
    schema: Dict[str, Any], schemas_by_id: Dict[str, Dict[str, Any]]
) -> Optional[str]:
    if "$ref" in schema:
        return resolve_ref(schema["$ref"])
    if "$id" in schema:
        schema_data = schemas_by_id.get(schema["$id"], schema)
        if schema_data.get("type") == "array":
            return get_java_type(schema_data, schemas_by_id)
        return schema["$id"]
    return None


def get_method_name_from_operation(
    operation: Dict[str, Any], path: str, method: str
) -> str:
    operation_id = operation.get("operationId")
    if operation_id:
        name = to_snake_case(operation_id)
        name = re.sub(r"^(get|post|put|patch|delete)_v1_", r"\1_", name)
        name = re.sub(r"_by_project_id", "", name)
        name = name.replace("-", "_")
        return to_camel_case(name)

    name = re.sub(r"\{[^}]+\}", "", path)
    name = name.strip("/").replace("/", "_").replace("-", "_")
    name = re.sub(r"_+", "_", name).strip("_")
    if not name:
        return "index"
    if name.startswith("v1_"):
        name = name[3:]
    elif name == "v1":
        name = "index"
    return to_camel_case(name)


def extract_path_params(path: str) -> List[Dict[str, Any]]:
    return [
        {"name": match.group(1), "required": True, "type": "String", "in": "path"}
        for match in re.finditer(r"\{(\w+)\}", path)
    ]


def get_query_parameters(operation: Dict[str, Any]) -> List[Dict[str, Any]]:
    return [
        {
            "name": param["name"],
            "required": param.get("required", False),
            "type": "String",
        }
        for param in operation.get("parameters", [])
        if param.get("in") == "query"
    ]


def get_request_schema(
    operation: Dict[str, Any], schemas_by_id: Dict[str, Dict[str, Any]]
) -> Optional[str]:
    request_body = operation.get("requestBody", {})
    if not request_body:
        return None
    content = request_body.get("content", {})
    if "application/json" in content:
        schema = content["application/json"].get("schema", {})
        if schema:
            return get_schema_name_from_id(schema, schemas_by_id)
    return None


def get_response_schema(
    operation: Dict[str, Any], schemas_by_id: Dict[str, Dict[str, Any]]
) -> Optional[str]:
    responses = operation.get("responses", {})
    for status_code in SUCCESS_STATUS_CODES:
        if status_code in responses:
            response = responses[status_code]
            content = response.get("content", {})
            if "application/json" in content:
                schema = content["application/json"].get("schema", {})
                if schema:
                    return get_schema_name_from_id(schema, schemas_by_id)
            elif "text/plain" in content:
                schema = content["text/plain"].get("schema", {})
                if schema:
                    return get_schema_name_from_id(schema, schemas_by_id)
    return None


def generate_model_class(
    class_name: str, schema: Dict[str, Any], schemas_by_id: Dict[str, Dict[str, Any]]
) -> Optional[str]:
    if schema.get("type") == "array":
        return None

    required_fields = set(schema.get("required", []))

    lines = [
        f"package {MODELS_PACKAGE};",
        "",
        "import com.fasterxml.jackson.annotation.JsonAnyGetter;",
        "import com.fasterxml.jackson.annotation.JsonAnySetter;",
        "import com.fasterxml.jackson.annotation.JsonProperty;",
        "import java.util.HashMap;",
        "import java.util.List;",
        "import java.util.Map;",
        "import java.util.Objects;",
        "",
        f"public class {class_name} {{",
    ]

    fields = []
    getters = []
    setters = []
    equals_parts = []
    hash_parts = []

    if "properties" in schema:
        for field_name, prop_schema in schema["properties"].items():
            java_type = get_java_type(prop_schema, schemas_by_id)
            camel_name = to_camel_case(field_name)
            pascal_name = camel_name[0].upper() + camel_name[1:]

            fields.extend(
                [
                    f'    @JsonProperty("{field_name}")',
                    f"    private {java_type} {camel_name};",
                ]
            )

            getters.extend(
                [
                    f"    public {java_type} get{pascal_name}() {{",
                    f"        return {camel_name};",
                    "    }",
                ]
            )

            setters.extend(
                [
                    f"    public void set{pascal_name}({java_type} {camel_name}) {{",
                    f"        this.{camel_name} = {camel_name};",
                    "    }",
                ]
            )

            equals_parts.append(f"Objects.equals({camel_name}, other.{camel_name})")
            hash_parts.append(camel_name)

    if fields:
        lines.extend(fields)
        lines.append("")

    lines.extend(
        [
            "    private Map<String, Object> additionalProperties = new HashMap<>();",
            "",
            "    @JsonAnyGetter",
            "    public Map<String, Object> getAdditionalProperties() {",
            "        return additionalProperties;",
            "    }",
            "",
            "    @JsonAnySetter",
            "    public void setAdditionalProperty(String name, Object value) {",
            "        additionalProperties.put(name, value);",
            "    }",
            "",
        ]
    )

    equals_parts.append(
        "Objects.equals(additionalProperties, other.additionalProperties)"
    )
    hash_parts.append("Objects.hashCode(additionalProperties)")

    lines.extend(getters)
    lines.append("")
    lines.extend(setters)
    lines.append("")

    equals_expr = " && ".join(equals_parts) if equals_parts else "true"
    hash_expr = ", ".join(hash_parts) if hash_parts else ""

    lines.extend(
        [
            "    @Override",
            "    public boolean equals(Object obj) {",
            "        if (this == obj) return true;",
            "        if (obj == null || getClass() != obj.getClass()) return false;",
            f"        {class_name} other = ({class_name}) obj;",
            f"        return {equals_expr};",
            "    }",
            "",
            "    @Override",
            "    public int hashCode() {",
            f"        return Objects.hash({hash_expr});",
            "    }",
            "}",
        ]
    )

    return "\n".join(lines)


def generate_url_expr(path: str, path_params: List[Dict[str, Any]]) -> str:
    if not path_params:
        return f'"{path}"'

    parts = []
    remaining = path
    for param in path_params:
        placeholder = f"{{{param['name']}}}"
        idx = remaining.index(placeholder)
        if idx > 0:
            parts.append(f'"{remaining[:idx]}"')
        parts.append(to_camel_case(param["name"]))
        remaining = remaining[idx + len(placeholder) :]
    if remaining:
        parts.append(f'"{remaining}"')

    return " + ".join(parts)


def generate_method_signature(
    method_name: str,
    request_type: Optional[str],
    path_params: List[Dict[str, Any]],
    query_params: List[Dict[str, Any]],
    response_type: str,
    is_async: bool,
) -> str:
    params = []

    for param in path_params:
        params.append(f"String {to_camel_case(param['name'])}")

    for param in query_params:
        if param["required"]:
            params.append(f"String {param['name']}")

    if request_type:
        params.append(f"{request_type} payload")

    for param in query_params:
        if not param["required"]:
            params.append(f"String {param['name']}")

    return_type = (
        f"CompletableFuture<{response_type}>" if is_async else response_type
    )
    throws = "" if is_async else " throws IOException, InterruptedException"

    return f"    public {return_type} {method_name}({', '.join(params)}){throws} {{"


def generate_method_body(
    path: str,
    method: str,
    request_type: Optional[str],
    path_params: List[Dict[str, Any]],
    query_params: List[Dict[str, Any]],
    response_type: str,
    is_async: bool,
) -> str:
    lines = []

    if query_params:
        lines.append("        Map<String, String> queryParams = new HashMap<>();")
        for param in query_params:
            pname = param["name"]
            if param["required"]:
                lines.append(f'        queryParams.put("{pname}", {pname});')
            else:
                lines.append(
                    f'        Optional.ofNullable({pname}).ifPresent(v -> queryParams.put("{pname}", v));'
                )

    url_expr = generate_url_expr(path, path_params)
    query_arg = ", queryParams" if query_params else ""
    lines.append(f"        String url = buildUrl({url_expr}{query_arg});")

    if method in ["GET", "DELETE"]:
        lines.extend(
            [
                "        HttpRequest request = HttpRequest.newBuilder()",
                f"                .{method}()",
                "                .uri(URI.create(url))",
                "                .headers(buildHeaders())",
                "                .build();",
            ]
        )
    else:
        payload_expr = "payload" if request_type else "new Object()"
        if is_async:
            lines.extend(
                [
                    "        String jsonPayload;",
                    "        try {",
                    f"            jsonPayload = mapper.writeValueAsString({payload_expr});",
                    "        } catch (Exception e) {",
                    '            throw new RuntimeException("Failed to serialize payload", e);',
                    "        }",
                ]
            )
        else:
            lines.append(
                f"        String jsonPayload = mapper.writeValueAsString({payload_expr});"
            )

        lines.extend(
            [
                "        HttpRequest request = HttpRequest.newBuilder()",
                f"                .{method}(HttpRequest.BodyPublishers.ofString(jsonPayload))",
                "                .uri(URI.create(url))",
                "                .headers(buildHeaders())",
                "                .build();",
            ]
        )

    lines.append(
        f'        Logger.debug("HTTP {method} " + url);'
    )

    if is_async:
        lines.extend(
            [
                "        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())",
                "                .thenApply(r -> {",
                '                    Logger.debug("HTTP " + r.statusCode() + " " + url);',
                "                    return handleResponse(r);",
                "                });",
            ]
        )
    else:
        lines.append(
            "        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());"
        )
        lines.append(
            '        Logger.debug("HTTP " + response.statusCode() + " " + url);'
        )
        if response_type == "Object" or response_type.startswith("List<"):
            lines.append("        return handleResponse(response);")
        else:
            lines.append(
                f"        return mapper.readValue(response.body(), {response_type}.class);"
            )

    return "\n".join(lines)


def generate_client_class(
    class_name: str, methods: List[Dict[str, Any]], is_async: bool
) -> str:
    imports = [
        f"package {BASE_PACKAGE};",
        "",
        "import com.fasterxml.jackson.annotation.JsonInclude;",
        "import com.fasterxml.jackson.core.type.TypeReference;",
        "import com.fasterxml.jackson.databind.ObjectMapper;",
        "import java.io.IOException;",
        "import java.net.URI;",
        "import java.net.http.HttpClient;",
        "import java.net.http.HttpRequest;",
        "import java.net.http.HttpResponse;",
        "import java.util.HashMap;",
        "import java.util.List;",
        "import java.util.Map;",
        "import java.util.Objects;",
        "import java.util.Optional;",
        f"import {MODELS_PACKAGE}.*;",
        "import com.judgmentlabs.judgeval.utils.Logger;",
    ]

    if is_async:
        imports.append("import java.util.concurrent.CompletableFuture;")

    lines = imports + [
        "",
        f"public class {class_name} {{",
        "    private final HttpClient client;",
        "    private final ObjectMapper mapper;",
        "    private final String baseUrl;",
        "    private final String apiKey;",
        "    private final String organizationId;",
        "",
        f"    public {class_name}(String baseUrl, String apiKey, String organizationId) {{",
        '        this.baseUrl = Objects.requireNonNull(baseUrl, "Base URL cannot be null");',
        '        this.apiKey = Objects.requireNonNull(apiKey, "API key cannot be null");',
        '        this.organizationId = Objects.requireNonNull(organizationId, "Organization ID cannot be null");',
        "        this.client = HttpClient.newBuilder()",
        "                .version(HttpClient.Version.HTTP_1_1)",
        "                .build();",
        "        this.mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);",
        "    }",
        "",
        "    public String getApiUrl() {",
        "        return baseUrl;",
        "    }",
        "",
        "    public String getApiKey() {",
        "        return apiKey;",
        "    }",
        "",
        "    public String getOrganizationId() {",
        "        return organizationId;",
        "    }",
        "",
        "    private String buildUrl(String path, Map<String, String> queryParams) {",
        "        StringBuilder url = new StringBuilder(baseUrl).append(path);",
        "        if (!queryParams.isEmpty()) {",
        '            url.append("?");',
        "            String queryString = queryParams.entrySet().stream()",
        '                    .map(entry -> entry.getKey() + "=" + entry.getValue())',
        '                    .reduce("", (a, b) -> a.isEmpty() ? b : a + "&" + b);',
        "            url.append(queryString);",
        "        }",
        "        return url.toString();",
        "    }",
        "",
        "    private String buildUrl(String path) {",
        "        return buildUrl(path, new HashMap<>());",
        "    }",
        "",
        "    private String[] buildHeaders() {",
        "        return new String[] {",
        '            "Content-Type",',
        '            "application/json",',
        '            "Authorization",',
        '            "Bearer " + apiKey,',
        '            "X-Organization-Id",',
        "            organizationId",
        "        };",
        "    }",
        "",
    ]

    throws = "" if is_async else " throws IOException"
    lines.extend(
        [
            f"    private <T> T handleResponse(HttpResponse<String> response){throws} {{",
            "        if (response.statusCode() >= 400) {",
            '            throw new RuntimeException("HTTP Error: " + response.statusCode() + " - " + response.body());',
            "        }",
            "        try {",
            "            return mapper.readValue(response.body(), new TypeReference<T>() {});",
            "        } catch (Exception e) {",
            '            throw new RuntimeException("Failed to parse response", e);',
            "        }",
            "    }",
            "",
        ]
    )

    for method_info in methods:
        lines.append(
            generate_method_signature(
                method_info["name"],
                method_info["request_type"],
                method_info["path_params"],
                method_info["query_params"],
                method_info["response_type"],
                is_async,
            )
        )
        lines.append(
            generate_method_body(
                method_info["path"],
                method_info["method"],
                method_info["request_type"],
                method_info["path_params"],
                method_info["query_params"],
                method_info["response_type"],
                is_async,
            )
        )
        lines.append("    }")
        lines.append("")

    lines.append("}")
    return "\n".join(lines)


def main():
    spec_file = (
        sys.argv[1] if len(sys.argv) > 1 else "http://localhost:10001/openapi/json"
    )

    if spec_file.startswith("http"):
        r = httpx.get(spec_file)
        r.raise_for_status()
        spec = r.json()
    else:
        with open(spec_file, "r") as f:
            spec = json.load(f)

    schemas_by_id = collect_schemas_with_id(spec)
    used_schemas = find_used_schemas(spec, schemas_by_id)
    print(f"Used schemas: {sorted(used_schemas)}", file=sys.stderr)

    if os.path.exists(MODELS_DIR):
        print(f"Clearing existing models directory: {MODELS_DIR}", file=sys.stderr)
        shutil.rmtree(MODELS_DIR)
    os.makedirs(MODELS_DIR, exist_ok=True)

    for schema_id in sorted(used_schemas):
        if schema_id in schemas_by_id:
            model_code = generate_model_class(
                schema_id, schemas_by_id[schema_id], schemas_by_id
            )
            if model_code:
                with open(f"{MODELS_DIR}/{schema_id}.java", "w") as f:
                    f.write(model_code)
                print(f"Generated model: {schema_id}", file=sys.stderr)

    methods: List[Dict[str, Any]] = []
    for path, path_data in spec.get("paths", {}).items():
        if not any(path.startswith(prefix) for prefix in INCLUDE_PREFIXES):
            continue
        for method, operation in path_data.items():
            if not isinstance(operation, dict) or method.upper() not in HTTP_METHODS:
                continue

            method_name = get_method_name_from_operation(
                operation, path, method.upper()
            )
            request_schema = get_request_schema(operation, schemas_by_id)
            response_schema = get_response_schema(operation, schemas_by_id)
            path_params = extract_path_params(path)
            query_params = get_query_parameters(operation)

            print(
                f"{method_name} req={request_schema} resp={response_schema} "
                f"path_params={path_params} query_params={query_params}",
                file=sys.stderr,
            )

            methods.append(
                {
                    "name": method_name,
                    "path": path,
                    "method": method.upper(),
                    "request_type": request_schema,
                    "path_params": path_params,
                    "query_params": query_params,
                    "response_type": response_schema if response_schema else "Object",
                }
            )

    os.makedirs(OUTPUT_DIR, exist_ok=True)

    for is_async, cls_name in [
        (False, "JudgmentSyncClient"),
        (True, "JudgmentAsyncClient"),
    ]:
        client_code = generate_client_class(cls_name, methods, is_async)
        with open(f"{OUTPUT_DIR}/{cls_name}.java", "w") as f:
            f.write(client_code)
        print(f"Generated: {OUTPUT_DIR}/{cls_name}.java", file=sys.stderr)


if __name__ == "__main__":
    main()
