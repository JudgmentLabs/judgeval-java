package com.judgmentlabs.judgeval.api.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScorerExistsResponse {
    @JsonProperty("exists")
    private Boolean exists;

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScorerExistsResponse other = (ScorerExistsResponse) obj;
        return Objects.equals(exists, other.exists);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(exists);
    }
}
