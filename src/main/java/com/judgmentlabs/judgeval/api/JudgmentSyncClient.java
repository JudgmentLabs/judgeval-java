package com.judgmentlabs.judgeval.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judgmentlabs.judgeval.api.models.EvalResults;
import com.judgmentlabs.judgeval.api.models.EvalResultsFetch;
import com.judgmentlabs.judgeval.api.models.EvaluationRun;
import com.judgmentlabs.judgeval.api.models.FetchPromptScorerRequest;
import com.judgmentlabs.judgeval.api.models.FetchPromptScorerResponse;
import com.judgmentlabs.judgeval.api.models.SavePromptScorerRequest;
import com.judgmentlabs.judgeval.api.models.SavePromptScorerResponse;
import com.judgmentlabs.judgeval.api.models.ScorerExistsRequest;
import com.judgmentlabs.judgeval.api.models.ScorerExistsResponse;

public class JudgmentSyncClient {
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;

    public JudgmentSyncClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.mapper = new ObjectMapper();
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

    private String[] buildHeaders(String apiKey, String organizationId) {
        if (apiKey == null || organizationId == null) {
            throw new IllegalArgumentException("API key and organization ID cannot be null");
        }
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

    public Object addToRunEvalQueue(String apiKey, String organizationId, EvaluationRun payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/add_to_run_eval_queue/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders(apiKey, organizationId))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public Object logEvalResults(String apiKey, String organizationId, EvalResults payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/log_eval_results/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders(apiKey, organizationId))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public Object fetchExperimentRun(String apiKey, String organizationId, EvalResultsFetch payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/fetch_experiment_run/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders(apiKey, organizationId))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public Object getEvaluationStatus(String apiKey, String organizationId, String experiment_run_id,
            String project_name) throws IOException, InterruptedException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("experiment_run_id", experiment_run_id);
        queryParams.put("project_name", project_name);
        String url = buildUrl("/get_evaluation_status/", queryParams);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .headers(buildHeaders(apiKey, organizationId))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public ScorerExistsResponse scorerExists(String apiKey, String organizationId, ScorerExistsRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/scorer_exists/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders(apiKey, organizationId))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ScorerExistsResponse.class);
    }

    public SavePromptScorerResponse saveScorer(String apiKey, String organizationId, SavePromptScorerRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/save_scorer/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders(apiKey, organizationId))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), SavePromptScorerResponse.class);
    }

    public FetchPromptScorerResponse fetchScorer(String apiKey, String organizationId, FetchPromptScorerRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/fetch_scorer/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders(apiKey, organizationId))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), FetchPromptScorerResponse.class);
    }

}