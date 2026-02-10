package com.judgmentlabs.judgeval.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScoringResult {
    @JsonProperty("success")
    private Boolean             success;
    @JsonProperty("scorers_data")
    private List<ScorerData>    scorersData;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String key, Object value) {
        additionalProperties.put(key, value);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ScorerData> getScorersData() {
        return scorersData;
    }

    public void setScorersData(List<ScorerData> scorersData) {
        this.scorersData = scorersData;
    }

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
                result.setScorersData(new ArrayList<>());
            }
            result.getScorersData().add(scorerData);
            return this;
        }

        public Builder dataObject(Example dataObject) {
            if (dataObject != null) {
                result.setAdditionalProperty("example", dataObject);
            }
            return this;
        }

        public ScoringResult build() {
            return result;
        }
    }
}
