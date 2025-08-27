package com.judgmentlabs.judgeval.scorers.api_scorers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.api.models.FetchPromptScorerRequest;
import com.judgmentlabs.judgeval.api.models.FetchPromptScorerResponse;
import com.judgmentlabs.judgeval.api.models.SavePromptScorerRequest;
import com.judgmentlabs.judgeval.api.models.SavePromptScorerResponse;
import com.judgmentlabs.judgeval.api.models.ScorerExistsRequest;
import com.judgmentlabs.judgeval.api.models.ScorerExistsResponse;
import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.exceptions.JudgmentAPIError;
import com.judgmentlabs.judgeval.scorers.APIScorer;
import com.judgmentlabs.judgeval.utils.Logger;

public class PromptScorer extends APIScorer {
    private String prompt;
    private Map<String, Double> options;
    private String judgmentApiKey;
    private String organizationId;

    public PromptScorer(String name, String prompt, double threshold, Map<String, Double> options) {
        super(APIScorerType.PROMPT_SCORER);
        this.judgmentApiKey = Env.JUDGMENT_API_KEY;
        this.organizationId = Env.JUDGMENT_ORG_ID;
        setName(name);
        this.prompt = prompt;
        this.options = options;
        setThreshold(threshold);
    }

    public static PromptScorer get(String name) {
        return get(name, Env.JUDGMENT_API_KEY, Env.JUDGMENT_ORG_ID);
    }

    public static PromptScorer get(String name, String judgmentApiKey, String organizationId) {
        try {
            JudgmentSyncClient client = new JudgmentSyncClient(Env.JUDGMENT_API_URL);
            FetchPromptScorerRequest request = new FetchPromptScorerRequest();
            request.setName(name);

            Logger.info("Fetching scorer with name: " + name);
            FetchPromptScorerResponse response = client.fetchScorer(judgmentApiKey, organizationId, request);
            com.judgmentlabs.judgeval.api.models.PromptScorer scorerConfig = response.getScorer();

            return new PromptScorer(
                    name,
                    scorerConfig.getPrompt(),
                    scorerConfig.getThreshold(),
                    (Map<String, Double>) scorerConfig.getOptions());
        } catch (IOException | InterruptedException e) {
            throw new JudgmentAPIError(500, "Failed to fetch prompt scorer: " + e.getMessage());
        }
    }

    public static PromptScorer create(String name, String prompt) {
        return create(name, prompt, 0.5, null);
    }

    public static PromptScorer create(String name, String prompt, double threshold) {
        return create(name, prompt, threshold, null);
    }

    public static PromptScorer create(String name, String prompt, double threshold, Map<String, Double> options) {
        return create(name, prompt, threshold, options, Env.JUDGMENT_API_KEY, Env.JUDGMENT_ORG_ID);
    }

    public static PromptScorer create(String name, String prompt, double threshold, Map<String, Double> options,
            String judgmentApiKey, String organizationId) {
        try {
            Logger.info("Creating PromptScorer with name: " + name);
            Logger.info("API Key: " + (judgmentApiKey != null
                    ? judgmentApiKey.substring(0, Math.min(10, judgmentApiKey.length())) + "..."
                    : "null"));
            Logger.info("Org ID: " + organizationId);
            Logger.info("API URL: " + Env.JUDGMENT_API_URL);

            JudgmentSyncClient client = new JudgmentSyncClient(Env.JUDGMENT_API_URL);

            ScorerExistsRequest existsRequest = new ScorerExistsRequest();
            existsRequest.setName(name);
            Logger.info("Checking if scorer exists with name: " + name);

            ScorerExistsResponse existsResponse = client.scorerExists(judgmentApiKey, organizationId, existsRequest);
            Logger.info("Scorer exists response: " + existsResponse.getExists());

            if (existsResponse.getExists()) {
                throw new JudgmentAPIError(400,
                        "Scorer with name " + name
                                + " already exists. Either use the existing scorer with the get() method or use a new name.");
            }

            Logger.info("Scorer does not exist, creating new one...");
            pushPromptScorer(name, prompt, threshold, options, judgmentApiKey, organizationId);
            Logger.info("Successfully created PromptScorer: " + name);

            return new PromptScorer(name, prompt, threshold, options);
        } catch (IOException | InterruptedException e) {
            Logger.error("Exception during scorer creation: " + e.getMessage());
            e.printStackTrace();
            throw new JudgmentAPIError(500, "Failed to create prompt scorer: " + e.getMessage());
        }
    }

    public void setThreshold(double threshold) {
        super.setThreshold(threshold);
        pushPromptScorer();
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
        pushPromptScorer();
        Logger.info("Successfully updated prompt for " + getName());
    }

    public void setOptions(Map<String, Double> options) {
        this.options = options;
        pushPromptScorer();
        Logger.info("Successfully updated options for " + getName());
    }

    public void appendToPrompt(String promptAddition) {
        this.prompt += promptAddition;
        pushPromptScorer();
        Logger.info("Successfully appended to prompt for " + getName());
    }

    public double getThreshold() {
        return super.getThreshold();
    }

    public String getPrompt() {
        return prompt;
    }

    public Map<String, Double> getOptions() {
        return options != null ? new HashMap<>(options) : null;
    }

    public String getScorerName() {
        return getName();
    }

    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("name", getName());
        config.put("prompt", prompt);
        config.put("threshold", getThreshold());
        config.put("options", options);
        return config;
    }

    private void pushPromptScorer() {
        pushPromptScorer(getName(), prompt, getThreshold(), options, judgmentApiKey, organizationId);
    }

    private static String pushPromptScorer(String name, String prompt, double threshold,
            Map<String, Double> options, String judgmentApiKey, String organizationId) {
        try {
            Logger.info("Pushing prompt scorer to API...");
            Logger.info("Name: " + name);
            Logger.info("Prompt: " + prompt);
            Logger.info("Threshold: " + threshold);
            Logger.info("Options: " + options);

            JudgmentSyncClient client = new JudgmentSyncClient(Env.JUDGMENT_API_URL);
            SavePromptScorerRequest request = new SavePromptScorerRequest();
            request.setName(name);
            request.setPrompt(prompt);
            request.setThreshold(threshold);
            request.setOptions(options);
            request.setIsTrace(false); // Set is_trace to false for regular prompt scorers

            Logger.info("Sending request to save scorer...");
            SavePromptScorerResponse response = client.saveScorer(judgmentApiKey, organizationId, request);
            Logger.info("Save scorer response: " + (response != null ? response.getName() : "null"));
            return response != null ? response.getName() : null;
        } catch (IOException | InterruptedException e) {
            Logger.error("Exception during save scorer: " + e.getMessage());
            e.printStackTrace();
            throw new JudgmentAPIError(500, "Failed to save prompt scorer: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "PromptScorer(name=" + getName() + ", prompt=" + prompt + ", threshold=" + getThreshold() + ", options="
                + options + ")";
    }
}