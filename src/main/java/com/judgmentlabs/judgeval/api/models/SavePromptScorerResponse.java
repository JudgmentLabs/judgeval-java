package com.judgmentlabs.judgeval.api.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SavePromptScorerResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("name")
    private String name;

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SavePromptScorerResponse other = (SavePromptScorerResponse) obj;
        return Objects.equals(message, other.message) && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message) + Objects.hashCode(name);
    }
}
