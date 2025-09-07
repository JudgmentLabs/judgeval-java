package com.judgmentlabs.judgeval.data;

import java.util.List;

public class ScoringResult extends com.judgmentlabs.judgeval.internal.api.models.ScoringResult {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final ScoringResult result;

        private Builder() {
            this.result = new ScoringResult();
        }

        public Builder success(Boolean success) {
            result.setSuccess(success);
            return this;
        }

        public Builder scorersData(List<ScorerData> scorersData) {
            result.setScorersData(convertScorerDataToInternal(scorersData));
            return this;
        }

        public Builder scorerData(ScorerData scorerData) {
            if (result.getScorersData() == null) {
                result.setScorersData(new java.util.ArrayList<>());
            }
            @SuppressWarnings("unchecked")
            List<com.judgmentlabs.judgeval.internal.api.models.ScorerData> scorersData = (List<com.judgmentlabs.judgeval.internal.api.models.ScorerData>) result
                    .getScorersData();
            scorersData.add(convertScorerDataToInternal(scorerData));
            return this;
        }

        public Builder dataObject(Example dataObject) {
            // TODO: Handle Example to TraceSpan conversion or make dataObject more flexible
            // For now, store in additional properties
            if (dataObject != null) {
                result.setAdditionalProperty("example", dataObject);
            }
            return this;
        }

        public ScoringResult build() {
            return result;
        }

        private static List<com.judgmentlabs.judgeval.internal.api.models.ScorerData> convertScorerDataToInternal(
                List<ScorerData> dataList) {
            if (dataList == null) {
                return null;
            }
            List<com.judgmentlabs.judgeval.internal.api.models.ScorerData> result = new java.util.ArrayList<>();
            for (ScorerData data : dataList) {
                result.add(convertScorerDataToInternal(data));
            }
            return result;
        }

        private static com.judgmentlabs.judgeval.internal.api.models.ScorerData convertScorerDataToInternal(
                ScorerData data) {
            com.judgmentlabs.judgeval.internal.api.models.ScorerData internal = new com.judgmentlabs.judgeval.internal.api.models.ScorerData();
            internal.setName(data.getName());
            internal.setScore(data.getScore());
            internal.setSuccess(data.getSuccess());
            internal.setReason(data.getReason());
            internal.setThreshold(data.getThreshold());
            internal.setStrictMode(data.getStrictMode());
            internal.setEvaluationModel(data.getEvaluationModel());
            internal.setError(data.getError());
            internal.setAdditionalMetadata(data.getAdditionalMetadata());
            return internal;
        }
    }
}
