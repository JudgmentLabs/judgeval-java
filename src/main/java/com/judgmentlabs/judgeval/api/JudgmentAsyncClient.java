package com.judgmentlabs.judgeval.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judgmentlabs.judgeval.api.models.*;

public class JudgmentAsyncClient {
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;

    public JudgmentAsyncClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    private String buildUrl(String path, Map<String, String> queryParams) {
        StringBuilder url = new StringBuilder(baseUrl).append(path);
        if (!queryParams.isEmpty()) {
            url.append("?");
            String queryString =
                    queryParams.entrySet().stream()
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
            throw new RuntimeException(
                    "HTTP Error: " + response.statusCode() + " - " + response.body());
        }
        try {
            return mapper.readValue(response.body(), new TypeReference<T>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    public CompletableFuture<Object> addToRunEvalQueue(
            String apiKey, String organizationId, EvaluationRun payload) {
        String url = buildUrl("/add_to_run_eval_queue/");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders(apiKey, organizationId))
                        .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::handleResponse);
    }

    public CompletableFuture<Object> logEvalResults(
            String apiKey, String organizationId, EvalResults payload) {
        String url = buildUrl("/log_eval_results/");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders(apiKey, organizationId))
                        .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::handleResponse);
    }

    public CompletableFuture<Object> fetchExperimentRun(
            String apiKey, String organizationId, EvalResultsFetch payload) {
        String url = buildUrl("/fetch_experiment_run/");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders(apiKey, organizationId))
                        .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::handleResponse);
    }

    public CompletableFuture<Object> getEvaluationStatus(
            String apiKey, String organizationId, String experiment_run_id, String project_name) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("experiment_run_id", experiment_run_id);
        queryParams.put("project_name", project_name);
        String url = buildUrl("/get_evaluation_status/", queryParams);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(url))
                        .headers(buildHeaders(apiKey, organizationId))
                        .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::handleResponse);
    }

    public CompletableFuture<ScorerExistsResponse> scorerExists(
            String apiKey, String organizationId, ScorerExistsRequest payload) {
        String url = buildUrl("/scorer_exists/");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders(apiKey, organizationId))
                        .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::handleResponse);
    }

    public CompletableFuture<SavePromptScorerResponse> saveScorer(
            String apiKey, String organizationId, SavePromptScorerRequest payload) {
        String url = buildUrl("/save_scorer/");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders(apiKey, organizationId))
                        .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::handleResponse);
    }

    public CompletableFuture<FetchPromptScorerResponse> fetchScorer(
            String apiKey, String organizationId, FetchPromptScorerRequest payload) {
        String url = buildUrl("/fetch_scorer/");
        String jsonPayload;
        try {
            jsonPayload = mapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders(apiKey, organizationId))
                        .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::handleResponse);
    }
}
