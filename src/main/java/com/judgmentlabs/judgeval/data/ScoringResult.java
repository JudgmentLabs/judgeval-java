package com.judgmentlabs.judgeval.data;

import java.util.List;

public class ScoringResult extends com.judgmentlabs.judgeval.api.models.ScoringResult {

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
            result.setScorersData(scorersData);
            return this;
        }

        public Builder scorerData(ScorerData scorerData) {
            if (result.getScorersData() == null) {
                result.setScorersData(new java.util.ArrayList<>());
            }
            @SuppressWarnings("unchecked")
            List<ScorerData> scorersData = (List<ScorerData>) result.getScorersData();
            scorersData.add(scorerData);
            return this;
        }

        public Builder dataObject(Example dataObject) {
            result.setDataObject(dataObject);
            return this;
        }

        public ScoringResult build() {
            return result;
        }
    }
}
