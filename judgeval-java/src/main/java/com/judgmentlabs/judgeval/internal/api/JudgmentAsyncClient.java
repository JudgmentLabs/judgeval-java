package com.judgmentlabs.judgeval.internal.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judgmentlabs.judgeval.internal.api.models.*;
import com.judgmentlabs.judgeval.utils.Logger;

public class JudgmentAsyncClient {
    private final HttpClient   client;
    private final ObjectMapper mapper;
    private final String       baseUrl;
    private final String       apiKey;
    private final String       organizationId;

    public JudgmentAsyncClient(String baseUrl, String apiKey, String organizationId) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "Base URL cannot be null");
        this.apiKey = Objects.requireNonNull(apiKey, "API key cannot be null");
        this.organizationId = Objects.requireNonNull(organizationId, "Organization ID cannot be null");
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public String getApiUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    private String buildUrl(String path, Map<String, String> queryParams) {
        StringBuilder url = new StringBuilder(baseUrl).append(path);
        if (!queryParams.isEmpty()) {
            url.append("?");
            String queryString = queryParams.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .reduce("", (a, b) -> a.isEmpty() ? b : a + "&" + b);
            url.append(queryString);
        }
        return url.toString();
    }

    private String buildUrl(String path) {
        return buildUrl(path, new HashMap<>());
    }

    private String[] buildHeaders() {
        return new String[] {
                "Content-Type",
                "application/json",
                "Authorization",
                "Bearer " + apiKey,
                "X-Organization-Id",
                organizationId
        };
    }

    private <T> T handleResponse(HttpResponse<String> response) {
        if (response.statusCode() >= 400) {
            throw new RuntimeException("HTTP Error: " + response.statusCode() + " - " + response.body());
        }
        try {
            return mapper.readValue(response.body(), new TypeReference<T>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    public CompletableFuture<Object> postOtelV1Traces() {
        String url = buildUrl("/otel/v1/traces");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(new Object());
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<TriggerRootSpanRulesResponse> postOtelTriggerRootSpanRules(
            TriggerRootSpanRulesRequest payload) {
        String url = buildUrl("/otel/trigger_root_span_rules");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<ResolveProjectResponse> postProjectsResolve(ResolveProjectRequest payload) {
        String url = buildUrl("/v1/projects/resolve/");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<AddProjectResponse> postProjects(AddProjectRequest payload) {
        String url = buildUrl("/v1/projects");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<DeleteProjectResponse> deleteProjects(String projectId) {
        String url = buildUrl("/v1/projects/" + projectId);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP DELETE " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<CreateDatasetResponse> postProjectsDatasets(String projectId,
            CreateDatasetRequest payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/datasets");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<List<DatasetInfo>> getProjectsDatasets(String projectId) {
        String url = buildUrl("/v1/projects/" + projectId + "/datasets");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<InsertExamplesResponse> postProjectsDatasetsByDatasetNameExamples(String projectId,
            String datasetName, InsertExamplesRequest payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/datasets/" + datasetName + "/examples");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<PullDatasetResponse> getProjectsDatasetsByDatasetName(String projectId,
            String datasetName) {
        String url = buildUrl("/v1/projects/" + projectId + "/datasets/" + datasetName);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<Object> postProjectsEvaluateExamples(String projectId, ExampleEvaluationRun payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/evaluate/examples");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<Object> postProjectsEvaluateTraces(String projectId, TraceEvaluationRun payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/evaluate/traces");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<LogEvalResultsResponse> postProjectsEvalResults(String projectId,
            LogEvalResultsRequest payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/eval-results");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<FetchExperimentRunResponse> getProjectsExperimentsByRunId(String projectId, String runId) {
        String url = buildUrl("/v1/projects/" + projectId + "/experiments/" + runId);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<AddToRunEvalQueueExamplesResponse> postProjectsEvalQueueExamples(String projectId,
            ExampleEvaluationRun payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/eval-queue/examples");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<AddToRunEvalQueueTracesResponse> postProjectsEvalQueueTraces(String projectId,
            TraceEvaluationRun payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/eval-queue/traces");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<FetchPromptResponse> getProjectsPromptsByName(String projectId, String name,
            String commit_id, String tag) {
        Map<String, String> queryParams = new HashMap<>();
        Optional.ofNullable(commit_id).ifPresent(v -> queryParams.put("commit_id", v));
        Optional.ofNullable(tag).ifPresent(v -> queryParams.put("tag", v));
        String url = buildUrl("/v1/projects/" + projectId + "/prompts/" + name, queryParams);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<InsertPromptResponse> postProjectsPrompts(String projectId, InsertPromptRequest payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/prompts");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<TagPromptResponse> postProjectsPromptsByNameTags(String projectId, String name,
            TagPromptRequest payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/prompts/" + name + "/tags");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<UntagPromptResponse> deleteProjectsPromptsByNameTags(String projectId, String name,
            UntagPromptRequest payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/prompts/" + name + "/tags");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP DELETE " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<GetPromptVersionsResponse> getProjectsPromptsByNameVersions(String projectId,
            String name) {
        String url = buildUrl("/v1/projects/" + projectId + "/prompts/" + name + "/versions");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<FetchPromptScorersResponse> getProjectsScorers(String projectId, String names,
            String is_trace) {
        Map<String, String> queryParams = new HashMap<>();
        Optional.ofNullable(names).ifPresent(v -> queryParams.put("names", v));
        Optional.ofNullable(is_trace).ifPresent(v -> queryParams.put("is_trace", v));
        String url = buildUrl("/v1/projects/" + projectId + "/scorers", queryParams);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<ScorerExistsResponse> getProjectsScorersByNameExists(String projectId, String name) {
        String url = buildUrl("/v1/projects/" + projectId + "/scorers/" + name + "/exists");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<UploadCustomScorerResponse> postProjectsScorersCustom(String projectId,
            UploadCustomScorerRequest payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/scorers/custom");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<CustomScorerExistsResponse> getProjectsScorersCustomByNameExists(String projectId,
            String name) {
        String url = buildUrl("/v1/projects/" + projectId + "/scorers/custom/" + name + "/exists");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<AddTraceTagsResponse> postProjectsTracesByTraceIdTags(String projectId, String traceId,
            AddTraceTagsRequest payload) {
        String url = buildUrl("/v1/projects/" + projectId + "/traces/" + traceId + "/tags");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<List<Object>> postE2eFetchTrace(E2EFetchTraceRequest payload) {
        String url = buildUrl("/v1/e2e_fetch_trace/");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

    public CompletableFuture<List<Object>> postE2eFetchSpanScore(E2EFetchSpanScoreRequest payload) {
        String url = buildUrl("/v1/e2e_fetch_span_score/");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> {
                    Logger.debug("HTTP " + r.statusCode() + " " + url);
                    return handleResponse(r);
                });
    }

}