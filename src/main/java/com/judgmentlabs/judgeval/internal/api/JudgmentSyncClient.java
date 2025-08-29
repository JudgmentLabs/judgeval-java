package com.judgmentlabs.judgeval.internal.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.judgmentlabs.judgeval.internal.api.models.*;

public class JudgmentSyncClient {
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private final String apiKey;
    private final String organizationId;

    public JudgmentSyncClient(String baseUrl, String apiKey, String organizationId) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.organizationId = organizationId;
        this.client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
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

    private String[] buildHeaders() {
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
            throw new RuntimeException(
                    "HTTP Error: " + response.statusCode() + " - " + response.body());
        }
        try {
            return mapper.readValue(response.body(), new TypeReference<T>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    public Object addToRunEvalQueue(EvaluationRun payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/add_to_run_eval_queue/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders())
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public Object logEvalResults(EvalResults payload) throws IOException, InterruptedException {
        String url = buildUrl("/log_eval_results/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders())
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public Object fetchExperimentRun(EvalResultsFetch payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/fetch_experiment_run/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders())
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public Object getEvaluationStatus(String experiment_run_id, String project_name)
            throws IOException, InterruptedException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("experiment_run_id", experiment_run_id);
        queryParams.put("project_name", project_name);
        String url = buildUrl("/get_evaluation_status/", queryParams);
        HttpRequest request =
                HttpRequest.newBuilder().GET().uri(URI.create(url)).headers(buildHeaders()).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    public ScorerExistsResponse scorerExists(ScorerExistsRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/scorer_exists/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders())
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ScorerExistsResponse.class);
    }

    public SavePromptScorerResponse saveScorer(SavePromptScorerRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/save_scorer/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders())
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), SavePromptScorerResponse.class);
    }

    public FetchPromptScorerResponse fetchScorer(FetchPromptScorerRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/fetch_scorer/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders())
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), FetchPromptScorerResponse.class);
    }

    public ResolveProjectNameResponse projectsResolve(ResolveProjectNameRequest payload)
            throws IOException, InterruptedException {
        String url = buildUrl("/projects/resolve/");
        String jsonPayload = mapper.writeValueAsString(payload);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .uri(URI.create(url))
                        .headers(buildHeaders())
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ResolveProjectNameResponse.class);
    }
}
