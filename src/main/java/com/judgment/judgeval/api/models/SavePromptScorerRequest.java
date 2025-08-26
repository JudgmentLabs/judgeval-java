package com.judgment.judgeval.api.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SavePromptScorerRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("threshold")
    private Double threshold;

    @JsonProperty("options")
    private Object options;

    public String getName() {
        return name;
    }

    public String getPrompt() {
        return prompt;
    }

    public Double getThreshold() {
        return threshold;
    }

    public Object getOptions() {
        return options;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setOptions(Object options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SavePromptScorerRequest other = (SavePromptScorerRequest) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(prompt, other.prompt)
                && Objects.equals(threshold, other.threshold)
                && Objects.equals(options, other.options);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name)
                + Objects.hashCode(prompt)
                + Objects.hashCode(threshold)
                + Objects.hashCode(options);
    }
}
