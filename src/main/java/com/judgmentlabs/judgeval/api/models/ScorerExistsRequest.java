package com.judgmentlabs.judgeval.api.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScorerExistsRequest {
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScorerExistsRequest other = (ScorerExistsRequest) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
