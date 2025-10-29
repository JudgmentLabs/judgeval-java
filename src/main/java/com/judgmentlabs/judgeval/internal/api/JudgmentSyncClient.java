package com.judgmentlabs.judgeval.internal.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judgmentlabs.judgeval.internal.api.models.EvalResults;
import com.judgmentlabs.judgeval.internal.api.models.EvalResultsFetch;
import com.judgmentlabs.judgeval.internal.api.models.ExampleEvaluationRun;
import com.judgmentlabs.judgeval.internal.api.models.FetchPromptScorersRequest;
import com.judgmentlabs.judgeval.internal.api.models.FetchPromptScorersResponse;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameRequest;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameResponse;
import com.judgmentlabs.judgeval.internal.api.models.SavePromptScorerRequest;
import com.judgmentlabs.judgeval.internal.api.models.SavePromptScorerResponse;
import com.judgmentlabs.judgeval.internal.api.models.ScorerExistsRequest;
import com.judgmentlabs.judgeval.internal.api.models.ScorerExistsResponse;

public class JudgmentSyncClient {
    private final HttpClient   client;
    private final ObjectMapper mapper;
    private final String       baseUrl;
    private final String       apiKey;
    private final String       organizationId;

    public JudgmentSyncClient(@NotNull String baseUrl, @NotNull String apiKey, @NotNull String organizationId) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "Base URL cannot be null");
        this.apiKey = Objects.requireNonNull(apiKey, "API key cannot be null");
        this.organizationId = Objects.requireNonNull(organizationId, "Organization ID cannot be null");
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.mapper = new ObjectMapper();
    }

    private String buildUrl(String path, Map<String, String> queryParams) {
        StringBuilder url = new StringBuilder(baseUrl).append(path);
        if (!queryParams.isEmpty()) {
            url.append("?");
            String queryString = queryParams.entrySet()
                    .stream()
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
        return new String[] { "Content-Type", "application/json", "Authorization", "Bearer " + apiKey,
                "X-Organization-Id", organizationId };
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

    public Object addToRunEvalQueue(@NotNull ExampleEvaluationRun payload) throws IOException, InterruptedException {
        String url = buildUrl("/add_to_run_eval_queue/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public Object logEvalResults(@NotNull EvalResults payload) throws IOException, InterruptedException {
        String url = buildUrl("/log_eval_results/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public Object fetchExperimentRun(@NotNull EvalResultsFetch payload) throws IOException, InterruptedException {
        String url = buildUrl("/fetch_experiment_run/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public ScorerExistsResponse scorerExists(@NotNull ScorerExistsRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/scorer_exists/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ScorerExistsResponse.class);
    }

    public SavePromptScorerResponse saveScorer(@NotNull SavePromptScorerRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/save_scorer/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), SavePromptScorerResponse.class);
    }

    public FetchPromptScorersResponse fetchScorers(@NotNull FetchPromptScorersRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/fetch_scorers/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), FetchPromptScorersResponse.class);
    }

    public ResolveProjectNameResponse projectsResolve(@NotNull ResolveProjectNameRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/projects/resolve/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .uri(URI.create(url))
                .headers(buildHeaders())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ResolveProjectNameResponse.class);
    }

}
