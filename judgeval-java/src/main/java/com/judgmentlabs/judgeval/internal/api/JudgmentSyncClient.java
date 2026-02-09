package com.judgmentlabs.judgeval.internal.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judgmentlabs.judgeval.internal.api.models.*;
import com.judgmentlabs.judgeval.utils.Logger;

public class JudgmentSyncClient {
    private final HttpClient   client;
    private final ObjectMapper mapper;
    private final String       baseUrl;
    private final String       apiKey;
    private final String       organizationId;

    public JudgmentSyncClient(String baseUrl, String apiKey, String organizationId) {
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

    private <T> T handleResponse(HttpResponse<String> response) throws IOException {
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

    public Object postOtelV1Traces() throws IOException, InterruptedException {
        String url = buildUrl("/otel/v1/traces");
        String jsonPayload = mapper.writeValueAsString(new Object());
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return handleResponse(response);
    }

    public TriggerRootSpanRulesResponse postOtelTriggerRootSpanRules(TriggerRootSpanRulesRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/otel/trigger_root_span_rules");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), TriggerRootSpanRulesResponse.class);
    }

    public ResolveProjectResponse postProjectsResolve(ResolveProjectRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/resolve/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), ResolveProjectResponse.class);
    }

    public AddProjectResponse postProjects(AddProjectRequest payload) throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), AddProjectResponse.class);
    }

    public DeleteProjectResponse deleteProjects(String projectId) throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP DELETE " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), DeleteProjectResponse.class);
    }

    public CreateDatasetResponse postProjectsDatasets(String projectId, CreateDatasetRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/datasets");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), CreateDatasetResponse.class);
    }

    public List<DatasetInfo> getProjectsDatasets(String projectId) throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/datasets");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return handleResponse(response);
    }

    public InsertExamplesResponse postProjectsDatasetsByDatasetNameExamples(String projectId, String datasetName,
            InsertExamplesRequest payload) throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/datasets/" + datasetName + "/examples");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), InsertExamplesResponse.class);
    }

    public PullDatasetResponse getProjectsDatasetsByDatasetName(String projectId, String datasetName)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/datasets/" + datasetName);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), PullDatasetResponse.class);
    }

    public Object postProjectsEvaluateExamples(String projectId, ExampleEvaluationRun payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/evaluate/examples");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return handleResponse(response);
    }

    public Object postProjectsEvaluateTraces(String projectId, TraceEvaluationRun payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/evaluate/traces");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return handleResponse(response);
    }

    public LogEvalResultsResponse postProjectsEvalResults(String projectId, LogEvalResultsRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/eval-results");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), LogEvalResultsResponse.class);
    }

    public FetchExperimentRunResponse getProjectsExperimentsByRunId(String projectId, String runId)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/experiments/" + runId);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), FetchExperimentRunResponse.class);
    }

    public AddToRunEvalQueueExamplesResponse postProjectsEvalQueueExamples(String projectId,
            ExampleEvaluationRun payload) throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/eval-queue/examples");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), AddToRunEvalQueueExamplesResponse.class);
    }

    public AddToRunEvalQueueTracesResponse postProjectsEvalQueueTraces(String projectId, TraceEvaluationRun payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/eval-queue/traces");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), AddToRunEvalQueueTracesResponse.class);
    }

    public FetchPromptResponse getProjectsPromptsByName(String projectId, String name, String commit_id, String tag)
            throws IOException, InterruptedException {
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
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), FetchPromptResponse.class);
    }

    public InsertPromptResponse postProjectsPrompts(String projectId, InsertPromptRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/prompts");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), InsertPromptResponse.class);
    }

    public TagPromptResponse postProjectsPromptsByNameTags(String projectId, String name, TagPromptRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/prompts/" + name + "/tags");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), TagPromptResponse.class);
    }

    public UntagPromptResponse deleteProjectsPromptsByNameTags(String projectId, String name,
            UntagPromptRequest payload) throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/prompts/" + name + "/tags");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP DELETE " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), UntagPromptResponse.class);
    }

    public GetPromptVersionsResponse getProjectsPromptsByNameVersions(String projectId, String name)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/prompts/" + name + "/versions");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), GetPromptVersionsResponse.class);
    }

    public FetchPromptScorersResponse getProjectsScorers(String projectId, String names, String is_trace)
            throws IOException, InterruptedException {
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
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), FetchPromptScorersResponse.class);
    }

    public ScorerExistsResponse getProjectsScorersByNameExists(String projectId, String name)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/scorers/" + name + "/exists");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), ScorerExistsResponse.class);
    }

    public UploadCustomScorerResponse postProjectsScorersCustom(String projectId, UploadCustomScorerRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/scorers/custom");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), UploadCustomScorerResponse.class);
    }

    public CustomScorerExistsResponse getProjectsScorersCustomByNameExists(String projectId, String name)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/scorers/custom/" + name + "/exists");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP GET " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), CustomScorerExistsResponse.class);
    }

    public AddTraceTagsResponse postProjectsTracesByTraceIdTags(String projectId, String traceId,
            AddTraceTagsRequest payload) throws IOException, InterruptedException {
        String url = buildUrl("/v1/projects/" + projectId + "/traces/" + traceId + "/tags");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return mapper.readValue(response.body(), AddTraceTagsResponse.class);
    }

    public List<Object> postE2eFetchTrace(E2EFetchTraceRequest payload) throws IOException, InterruptedException {
        String url = buildUrl("/v1/e2e_fetch_trace/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return handleResponse(response);
    }

    public List<Object> postE2eFetchSpanScore(E2EFetchSpanScoreRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/v1/e2e_fetch_span_score/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        Logger.debug("HTTP POST " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Logger.debug("HTTP " + response.statusCode() + " " + url);
        return handleResponse(response);
    }

}