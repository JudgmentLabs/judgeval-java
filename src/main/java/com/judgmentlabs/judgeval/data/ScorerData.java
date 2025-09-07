package com.judgmentlabs.judgeval.data;

import java.util.Map;

public class ScorerData extends com.judgmentlabs.judgeval.internal.api.models.ScorerData {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final ScorerData scorerData;

        private Builder() {
            this.scorerData = new ScorerData();
        }

        public Builder name(String name) {
            scorerData.setName(name);
            return this;
        }

        public Builder score(Double score) {
            scorerData.setScore(score);
            return this;
        }

        public Builder success(Boolean success) {
            scorerData.setSuccess(success);
            return this;
        }

        public Builder reason(String reason) {
            scorerData.setReason(reason);
            return this;
        }

        public Builder threshold(Double threshold) {
            scorerData.setThreshold(threshold);
            return this;
        }

        public Builder strictMode(Boolean strictMode) {
            scorerData.setStrictMode(strictMode);
            return this;
        }

        public Builder evaluationModel(String evaluationModel) {
            if (evaluationModel != null) {
                scorerData.setEvaluationModel(java.util.List.of(evaluationModel));
            } else {
                scorerData.setEvaluationModel(null);
            }
            return this;
        }

        public Builder evaluationModels(java.util.List<String> evaluationModels) {
            scorerData.setEvaluationModel(evaluationModels);
            return this;
        }

        public Builder error(String error) {
            scorerData.setError(error);
            return this;
        }

        public Builder additionalMetadata(Map<String, Object> additionalMetadata) {
            scorerData.setAdditionalMetadata(additionalMetadata);
            return this;
        }

        public Builder metadata(String key, Object value) {
            if (scorerData.getAdditionalMetadata() == null) {
                scorerData.setAdditionalMetadata(new java.util.HashMap<>());
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> metadata = (Map<String, Object>) scorerData.getAdditionalMetadata();
            metadata.put(key, value);
            return this;
        }

        public ScorerData build() {
            return scorerData;
        }
    }
}
