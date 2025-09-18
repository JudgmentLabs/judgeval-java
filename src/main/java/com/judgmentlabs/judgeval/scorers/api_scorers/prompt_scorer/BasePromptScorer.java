package com.judgmentlabs.judgeval.scorers.api_scorers.prompt_scorer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.exceptions.JudgmentAPIError;
import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.internal.api.models.FetchPromptScorersRequest;
import com.judgmentlabs.judgeval.internal.api.models.FetchPromptScorersResponse;
import com.judgmentlabs.judgeval.internal.api.models.SavePromptScorerRequest;
import com.judgmentlabs.judgeval.internal.api.models.SavePromptScorerResponse;
import com.judgmentlabs.judgeval.internal.api.models.ScorerExistsRequest;
import com.judgmentlabs.judgeval.internal.api.models.ScorerExistsResponse;
import com.judgmentlabs.judgeval.scorers.APIScorer;
import com.judgmentlabs.judgeval.utils.Logger;

public abstract class BasePromptScorer extends APIScorer {
    protected String prompt;
    protected Map<String, Double> options;
    protected String judgmentApiKey;
    protected String organizationId;

    protected BasePromptScorer(
            APIScorerType scoreType,
            String name,
            String prompt,
            double threshold,
            Map<String, Double> options,
            String judgmentApiKey,
            String organizationId) {
        super(scoreType);
        this.prompt = prompt;
        this.options = options;
        this.judgmentApiKey = judgmentApiKey;
        this.organizationId = organizationId;
        setName(name);
        setThreshold(threshold);
    }

    public static boolean scorerExists(String name, String judgmentApiKey, String organizationId) {
        try {
            JudgmentSyncClient client =
                    new JudgmentSyncClient(Env.JUDGMENT_API_URL, judgmentApiKey, organizationId);
            ScorerExistsRequest request = new ScorerExistsRequest();
            request.setName(name);
            ScorerExistsResponse response = client.scorerExists(request);
            return Boolean.TRUE.equals(response.getExists());
        } catch (JudgmentAPIError e) {
            if (e.getStatusCode() == 500) {
                throw new JudgmentAPIError(
                        e.getStatusCode(),
                        "The server is temporarily unavailable. Please try your request again in a few moments. Error details: "
                                + e.getMessage());
            }
            throw new JudgmentAPIError(
                    e.getStatusCode(), "Failed to check if scorer exists: " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            throw new JudgmentAPIError(
                    500,
                    "The server is temporarily unavailable. Please try your request again in a few moments. Error details: "
                            + e.getMessage());
        }
    }

    public static com.judgmentlabs.judgeval.internal.api.models.PromptScorer fetchPromptScorer(
            String name, String judgmentApiKey, String organizationId) {
        try {
            JudgmentSyncClient client =
                    new JudgmentSyncClient(Env.JUDGMENT_API_URL, judgmentApiKey, organizationId);
            FetchPromptScorersRequest request = new FetchPromptScorersRequest();
            request.setNames(java.util.Collections.singletonList(name));

            FetchPromptScorersResponse response = client.fetchScorers(request);

            if (response.getScorers() == null || response.getScorers().isEmpty()) {
                throw new JudgmentAPIError(
                        404, "Failed to fetch prompt scorer '" + name + "': not found");
            }

            return response.getScorers().get(0);
        } catch (JudgmentAPIError e) {
            if (e.getStatusCode() == 500) {
                throw new JudgmentAPIError(
                        e.getStatusCode(),
                        "The server is temporarily unavailable. Please try your request again in a few moments. Error details: "
                                + e.getMessage());
            }
            throw new JudgmentAPIError(
                    e.getStatusCode(),
                    "Failed to fetch prompt scorer '" + name + "': " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            throw new JudgmentAPIError(
                    500,
                    "The server is temporarily unavailable. Please try your request again in a few moments. Error details: "
                            + e.getMessage());
        }
    }

    public static String pushPromptScorer(
            String name,
            String prompt,
            double threshold,
            Map<String, Double> options,
            String judgmentApiKey,
            String organizationId,
            Boolean isTrace) {
        try {
            JudgmentSyncClient client =
                    new JudgmentSyncClient(Env.JUDGMENT_API_URL, judgmentApiKey, organizationId);
            SavePromptScorerRequest request = new SavePromptScorerRequest();
            request.setName(name);
            request.setPrompt(prompt);
            request.setThreshold(threshold);
            Map<String, Object> apiOptions = null;
            if (options != null) {
                apiOptions = new HashMap<>();
                for (Map.Entry<String, Double> entry : options.entrySet()) {
                    apiOptions.put(entry.getKey(), entry.getValue());
                }
            }
            request.setOptions(apiOptions);
            request.setIsTrace(isTrace);

            SavePromptScorerResponse response = client.saveScorer(request);
            return response != null ? response.getName() : null;
        } catch (JudgmentAPIError e) {
            if (e.getStatusCode() == 500) {
                throw new JudgmentAPIError(
                        e.getStatusCode(),
                        "The server is temporarily unavailable. Please try your request again in a few moments. Error details: "
                                + e.getMessage());
            }
            throw new JudgmentAPIError(
                    e.getStatusCode(), "Failed to save prompt scorer: " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            throw new JudgmentAPIError(
                    500,
                    "The server is temporarily unavailable. Please try your request again in a few moments. Error details: "
                            + e.getMessage());
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

    public Double getThreshold() {
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

    protected void pushPromptScorer() {
        pushPromptScorer(
                getName(),
                prompt,
                getThreshold(),
                options,
                judgmentApiKey,
                organizationId,
                isTrace());
    }

    protected abstract boolean isTrace();

    @Override
    public String toString() {
        return "PromptScorer(name="
                + getName()
                + ", prompt="
                + prompt
                + ", threshold="
                + getThreshold()
                + ", options="
                + options
                + ")";
    }
}
