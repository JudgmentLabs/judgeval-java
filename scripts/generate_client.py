#!/usr/bin/env python3

import json
import sys
import re
from typing import Any, Dict, List, Optional, Set
import httpx

JUDGEVAL_PATHS = [
    "/log_eval_results/",
    "/fetch_experiment_run/",
    "/add_to_run_eval_queue/",
    "/get_evaluation_status/",
    "/save_scorer/",
    "/fetch_scorer/",
    "/scorer_exists/",
]


def resolve_ref(ref: str) -> str:
    assert ref.startswith(
        "#/components/schemas/"
    ), "Reference must start with #/components/schemas/"
    return ref.replace("#/components/schemas/", "")


def to_camel_case(name: str) -> str:
    parts = name.replace("-", "_").split("_")
    return parts[0] + "".join(word.capitalize() for word in parts[1:])


def to_class_name(name: str) -> str:
    camel_case = to_camel_case(name)
    return camel_case[0].upper() + camel_case[1:]


def get_method_name_from_path(path: str, method: str) -> str:
    clean_path = path.strip("/").replace("/", "_").replace("-", "_")
    return to_camel_case(clean_path)


def get_query_parameters(operation: Dict[str, Any]) -> List[Dict[str, Any]]:
    parameters = operation.get("parameters", [])
    query_params = []

    for param in parameters:
        if param.get("in") == "query":
            param_info = {
                "name": param["name"],
                "required": param.get("required", False),
                "type": param.get("schema", {}).get("type", "string"),
            }
            query_params.append(param_info)

    return query_params


def get_request_schema(operation: Dict[str, Any]) -> Optional[str]:
    request_body = operation.get("requestBody", {})
    if not request_body:
        return None

    content = request_body.get("content", {})
    if "application/json" in content:
        schema = content["application/json"].get("schema", {})
        if "$ref" in schema:
            return resolve_ref(schema["$ref"])

    return None


def get_response_schema(operation: Dict[str, Any]) -> Optional[str]:
    responses = operation.get("responses", {})
    for status_code in ["200", "201"]:
        if status_code in responses:
            response = responses[status_code]
            content = response.get("content", {})
            if "application/json" in content:
                schema = content["application/json"].get("schema", {})
                if "$ref" in schema:
                    return resolve_ref(schema["$ref"])

    return None


def extract_dependencies(
    schema: Dict[str, Any], visited: Optional[Set[str]] = None
) -> Set[str]:
    if visited is None:
        visited = set()

    dependencies: Set[str] = set()
    schema_key = json.dumps(schema, sort_keys=True)

    if schema_key in visited:
        return dependencies

    visited.add(schema_key)

    if "$ref" in schema:
        ref_name = resolve_ref(schema["$ref"])
        dependencies.add(ref_name)
        return dependencies

    if "anyOf" in schema:
        for s in schema["anyOf"]:
            dependencies.update(extract_dependencies(s, visited))

    if "oneOf" in schema:
        for s in schema["oneOf"]:
            dependencies.update(extract_dependencies(s, visited))

    if "allOf" in schema:
        for s in schema["allOf"]:
            dependencies.update(extract_dependencies(s, visited))

    if "properties" in schema:
        for prop_schema in schema["properties"].values():
            dependencies.update(extract_dependencies(prop_schema, visited))

    if "items" in schema:
        dependencies.update(extract_dependencies(schema["items"], visited))

    if "additionalProperties" in schema and isinstance(
        schema["additionalProperties"], dict
    ):
        dependencies.update(
            extract_dependencies(schema["additionalProperties"], visited)
        )

    return dependencies


def find_used_schemas(spec: Dict[str, Any]) -> Set[str]:
    used_schemas = set()
    schemas = spec.get("components", {}).get("schemas", {})

    for path in JUDGEVAL_PATHS:
        if path in spec["paths"]:
            path_data = spec["paths"][path]
            for method, operation in path_data.items():
                if method.upper() in ["GET", "POST", "PUT", "PATCH", "DELETE"]:
                    request_schema = get_request_schema(operation)
                    response_schema = get_response_schema(operation)

                    if request_schema:
                        used_schemas.add(request_schema)
                    if response_schema:
                        used_schemas.add(response_schema)

    changed = True
    while changed:
        changed = False
        new_schemas = set()

        for schema_name in used_schemas:
            if schema_name in schemas:
                schema = schemas[schema_name]
                deps = extract_dependencies(schema)
                for dep in deps:
                    if dep in schemas and dep not in used_schemas:
                        new_schemas.add(dep)
                        changed = True

        used_schemas.update(new_schemas)

    return used_schemas


def get_java_type(schema: Dict[str, Any]) -> str:
    if "$ref" in schema:
        ref = resolve_ref(schema["$ref"])
        return to_class_name(ref)

    if "type" not in schema:
        return "Object"

    schema_type = schema["type"]
    if schema_type == "string":
        return "String"
    elif schema_type == "integer":
        return "Integer"
    elif schema_type == "number":
        return "Double"
    elif schema_type == "boolean":
        return "Boolean"
    elif schema_type == "array":
        items = schema.get("items", {})
        if items:
            return f"List<{get_java_type(items)}>"
        return "List<Object>"
    elif schema_type == "object":
        return "Object"
    else:
        return "Object"


def generate_model_class(className: str, schema: Dict[str, Any]) -> str:
    lines = [
        "package com.judgmentlabs.judgeval.api.models;",
        "",
        "import com.fasterxml.jackson.annotation.JsonProperty;",
        "import com.fasterxml.jackson.annotation.JsonAnySetter;",
        "import com.fasterxml.jackson.annotation.JsonAnyGetter;",
        "import java.util.List;",
        "import java.util.Map;",
        "import java.util.HashMap;",
        "import java.util.Objects;",
        "",
        f"public class {className} {{",
    ]

    fields = []
    getters = []
    setters = []
    equals_parts = []
    hashCode_parts = []

    if "properties" in schema:
        for field_name, property_schema in schema["properties"].items():
            java_type = get_java_type(property_schema)
            camel_case_name = to_camel_case(field_name)

            fields.append(f'    @JsonProperty("{field_name}")')
            fields.append(f"    private {java_type} {camel_case_name};")

            getters.append(
                f"    public {java_type} get{to_class_name(camel_case_name)}() {{"
            )
            getters.append(f"        return {camel_case_name};")
            getters.append("    }")

            setters.append(
                f"    public void set{to_class_name(camel_case_name)}({java_type} {camel_case_name}) {{"
            )
            setters.append(f"        this.{camel_case_name} = {camel_case_name};")
            setters.append("    }")

            equals_parts.append(
                f"Objects.equals({camel_case_name}, other.{camel_case_name})"
            )
            hashCode_parts.append(f"Objects.hashCode({camel_case_name})")

    if fields:
        lines.extend(fields)
        lines.append("")

    # Add additional properties support if additionalProperties is true
    if schema.get("additionalProperties") is True:
        lines.append(
            "    private Map<String, Object> additionalProperties = new HashMap<>();"
        )
        lines.append("")
        lines.append("    @JsonAnyGetter")
        lines.append("    public Map<String, Object> getAdditionalProperties() {")
        lines.append("        return additionalProperties;")
        lines.append("    }")
        lines.append("")
        lines.append("    @JsonAnySetter")
        lines.append(
            "    public void setAdditionalProperty(String name, Object value) {"
        )
        lines.append("        additionalProperties.put(name, value);")
        lines.append("    }")
        lines.append("")
        equals_parts.append(
            "Objects.equals(additionalProperties, other.additionalProperties)"
        )
        hashCode_parts.append("Objects.hashCode(additionalProperties)")

    lines.extend(getters)
    lines.append("")
    lines.extend(setters)
    lines.append("")

    lines.append("    @Override")
    lines.append("    public boolean equals(Object obj) {")
    lines.append("        if (this == obj) return true;")
    lines.append(
        "        if (obj == null || getClass() != obj.getClass()) return false;"
    )
    lines.append(f"        {className} other = ({className}) obj;")
    if equals_parts:
        lines.append(f"        return {' && '.join(equals_parts)};")
    else:
        lines.append("        return true;")
    lines.append("    }")
    lines.append("")
    lines.append("    @Override")
    lines.append("    public int hashCode() {")
    if hashCode_parts:
        lines.append(f"        return {' + '.join(hashCode_parts)};")
    else:
        lines.append("        return 0;")
    lines.append("    }")
    lines.append("}")

    return "\n".join(lines)


def generate_method_signature(
    method_name: str,
    request_type: Optional[str],
    query_params: List[Dict[str, Any]],
    response_type: str,
    is_async: bool,
) -> str:
    if is_async:
        signature = f"    public CompletableFuture<{response_type}> "
    else:
        signature = f"    public {response_type} "

    signature += f"{method_name}("

    params = ["String apiKey", "String organizationId"]

    for param in query_params:
        if param["required"]:
            params.append(f"String {param['name']}")

    if request_type:
        params.append(f"{request_type} payload")

    for param in query_params:
        if not param["required"]:
            params.append(f"String {param['name']}")

    signature += ", ".join(params)
    signature += ")"

    if is_async:
        signature += " {"
    else:
        signature += " throws IOException, InterruptedException {"

    return signature


def generate_method_body(
    method_name: str,
    path: str,
    method: str,
    request_type: Optional[str],
    query_params: List[Dict[str, Any]],
    is_async: bool,
) -> str:
    lines = []

    if query_params:
        lines.append("        Map<String, String> queryParams = new HashMap<>();")
        for param in query_params:
            param_name = param["name"]
            if param["required"]:
                lines.append(f'        queryParams.put("{param_name}", {param_name});')
            else:
                lines.append(f"        if ({param_name} != null) {{")
                lines.append(
                    f'            queryParams.put("{param_name}", {param_name});'
                )
                lines.append("        }")

    lines.append(f'        String url = buildUrl("{path}"')
    if query_params:
        lines.append(", queryParams")
    lines.append(");")

    if method == "GET":
        lines.append("        HttpRequest request = HttpRequest.newBuilder()")
        lines.append("                .GET()")
        lines.append("                .uri(URI.create(url))")
        lines.append("                .headers(buildHeaders(apiKey, organizationId))")
        lines.append("                .build();")
    elif method == "DELETE":
        lines.append("        HttpRequest request = HttpRequest.newBuilder()")
        lines.append("                .DELETE()")
        lines.append("                .uri(URI.create(url))")
        lines.append("                .headers(buildHeaders(apiKey, organizationId))")
        lines.append("                .build();")
    else:
        if is_async:
            lines.append("        String jsonPayload;")
            lines.append("        try {")
            payload_expr = "payload" if request_type else "new Object()"
            lines.append(
                f"            jsonPayload = mapper.writeValueAsString({payload_expr});"
            )
            lines.append("        } catch (Exception e) {")
            lines.append(
                '            throw new RuntimeException("Failed to serialize payload", e);'
            )
            lines.append("        }")
        else:
            payload_expr = "payload" if request_type else "new Object()"
            lines.append(
                f"        String jsonPayload = mapper.writeValueAsString({payload_expr});"
            )

        lines.append("        HttpRequest request = HttpRequest.newBuilder()")
        lines.append(
            f"                .{method}(HttpRequest.BodyPublishers.ofString(jsonPayload))"
        )
        lines.append("                .uri(URI.create(url))")
        lines.append("                .headers(buildHeaders(apiKey, organizationId))")
        lines.append("                .build();")

    if is_async:
        lines.append(
            "        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())"
        )
        lines.append("                .thenApply(this::handleResponse);")
    else:
        lines.append(
            "        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());"
        )
        lines.append("        return handleResponse(response);")

    return "\n".join(lines)


def generate_client_class(
    className: str, methods: List[Dict[str, Any]], is_async: bool
) -> str:
    lines = [
        "package com.judgmentlabs.judgeval.api;",
        "",
        "import com.fasterxml.jackson.databind.ObjectMapper;",
        "import com.fasterxml.jackson.core.type.TypeReference;",
        "import java.io.IOException;",
        "import java.net.URI;",
        "import java.net.http.HttpClient;",
        "import java.net.http.HttpRequest;",
        "import java.net.http.HttpResponse;",
        "import java.util.HashMap;",
        "import java.util.Map;",
        "import com.judgmentlabs.judgeval.api.models.*;",
    ]

    if is_async:
        lines.append("import java.util.concurrent.CompletableFuture;")

    lines.extend(
        [
            "",
            f"public class {className} {{",
            "    private final HttpClient client;",
            "    private final ObjectMapper mapper;",
            "    private final String baseUrl;",
            "",
            f"    public {className}(String baseUrl) {{",
            "        this.baseUrl = baseUrl;",
            "        this.client = HttpClient.newHttpClient();",
            "        this.mapper = new ObjectMapper();",
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
            "    private String[] buildHeaders(String apiKey, String organizationId) {",
            "        return new String[]{",
            '                "Content-Type", "application/json",',
            '                "Authorization", "Bearer " + apiKey,',
            '                "X-Organization-Id", organizationId',
            "        };",
            "    }",
            "",
        ]
    )

    if is_async:
        lines.append(
            "    private <T> T handleResponse(HttpResponse<String> response) {"
        )
    else:
        lines.append(
            "    private <T> T handleResponse(HttpResponse<String> response) throws IOException {"
        )

    lines.extend(
        [
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
        method_name = method_info["name"]
        path = method_info["path"]
        http_method = method_info["method"]
        request_type = method_info["request_type"]
        query_params = method_info["query_params"]
        response_type = method_info["response_type"]

        signature = generate_method_signature(
            method_name, request_type, query_params, response_type, is_async
        )
        lines.append(signature)

        body = generate_method_body(
            method_name, path, http_method, request_type, query_params, is_async
        )
        lines.append(body)
        lines.append("    }")
        lines.append("")

    lines.append("}")
    return "\n".join(lines)


def generate_api_files(spec: Dict[str, Any]) -> None:
    used_schemas = find_used_schemas(spec)
    schemas = spec.get("components", {}).get("schemas", {})

    import os
    import shutil

    models_dir = "src/main/java/com/judgmentlabs/judgeval/api/models"

    if os.path.exists(models_dir):
        print(f"Clearing existing models directory: {models_dir}", file=sys.stderr)
        shutil.rmtree(models_dir)

    os.makedirs(models_dir, exist_ok=True)

    print("Generating model classes...", file=sys.stderr)
    for schema_name in used_schemas:
        if schema_name in schemas:
            className = to_class_name(schema_name)
            model_class = generate_model_class(className, schemas[schema_name])

            with open(f"{models_dir}/{className}.java", "w") as f:
                f.write(model_class)

            print(f"Generated model: {className}", file=sys.stderr)

    filtered_paths = {
        path: spec_data
        for path, spec_data in spec["paths"].items()
        if path in JUDGEVAL_PATHS
    }

    for path in JUDGEVAL_PATHS:
        if path not in spec["paths"]:
            print(f"Path {path} not found in OpenAPI spec", file=sys.stderr)

    sync_methods = []
    async_methods = []

    for path, path_data in filtered_paths.items():
        for method, operation in path_data.items():
            if method.upper() in ["GET", "POST", "PUT", "PATCH", "DELETE"]:
                method_name = get_method_name_from_path(path, method.upper())
                request_schema = get_request_schema(operation)
                response_schema = get_response_schema(operation)
                query_params = get_query_parameters(operation)

                print(
                    f"{method_name} {request_schema} {response_schema} {query_params}",
                    file=sys.stderr,
                )

                request_type = to_class_name(request_schema) if request_schema else None
                response_type = (
                    to_class_name(response_schema) if response_schema else "Object"
                )

                method_info = {
                    "name": method_name,
                    "path": path,
                    "method": method.upper(),
                    "request_type": request_type,
                    "query_params": query_params,
                    "response_type": response_type,
                }

                sync_methods.append(method_info)
                async_methods.append(method_info)

    sync_client = generate_client_class("JudgmentSyncClient", sync_methods, False)
    async_client = generate_client_class("JudgmentAsyncClient", async_methods, True)

    import os

    api_dir = "src/main/java/com/judgmentlabs/judgeval/api"
    os.makedirs(api_dir, exist_ok=True)

    with open(f"{api_dir}/JudgmentSyncClient.java", "w") as f:
        f.write(sync_client)

    with open(f"{api_dir}/JudgmentAsyncClient.java", "w") as f:
        f.write(async_client)

    print("Generated files:", file=sys.stderr)
    print(f"  {api_dir}/JudgmentSyncClient.java", file=sys.stderr)
    print(f"  {api_dir}/JudgmentAsyncClient.java", file=sys.stderr)


def main():
    spec_file = (
        sys.argv[1] if len(sys.argv) > 1 else "http://localhost:8000/openapi.json"
    )

    try:
        if spec_file.startswith("http"):
            with httpx.Client() as client:
                response = client.get(spec_file)
                response.raise_for_status()
                spec = response.json()
        else:
            with open(spec_file, "r") as f:
                spec = json.load(f)

        generate_api_files(spec)

    except Exception as e:
        print(f"Error generating API client: {e}", file=sys.stderr)
        sys.exit(1)


if __name__ == "__main__":
    main()
