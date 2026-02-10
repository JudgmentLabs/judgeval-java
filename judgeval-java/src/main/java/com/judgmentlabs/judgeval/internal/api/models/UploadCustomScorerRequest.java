package com.judgmentlabs.judgeval.internal.api.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadCustomScorerRequest {
    @JsonProperty("scorer_name")
    private String              scorerName;
    @JsonProperty("scorer_code")
    private String              scorerCode;
    @JsonProperty("requirements_text")
    private String              requirementsText;
    @JsonProperty("class_name")
    private String              className;
    @JsonProperty("overwrite")
    private Boolean             overwrite;
    @JsonProperty("scorer_type")
    private String              scorerType;
    @JsonProperty("response_type")
    private String              responseType;
    @JsonProperty("version")
    private Double              version;

    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getScorerName() {
        return scorerName;
    }

    public String getScorerCode() {
        return scorerCode;
    }

    public String getRequirementsText() {
        return requirementsText;
    }

    public String getClassName() {
        return className;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public String getScorerType() {
        return scorerType;
    }

    public String getResponseType() {
        return responseType;
    }

    public Double getVersion() {
        return version;
    }

    public void setScorerName(String scorerName) {
        this.scorerName = scorerName;
    }

    public void setScorerCode(String scorerCode) {
        this.scorerCode = scorerCode;
    }

    public void setRequirementsText(String requirementsText) {
        this.requirementsText = requirementsText;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    public void setScorerType(String scorerType) {
        this.scorerType = scorerType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UploadCustomScorerRequest other = (UploadCustomScorerRequest) obj;
        return Objects.equals(scorerName, other.scorerName) && Objects.equals(scorerCode, other.scorerCode)
                && Objects.equals(requirementsText, other.requirementsText)
                && Objects.equals(className, other.className) && Objects.equals(overwrite, other.overwrite)
                && Objects.equals(scorerType, other.scorerType) && Objects.equals(responseType, other.responseType)
                && Objects.equals(version, other.version)
                && Objects.equals(additionalProperties, other.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scorerName, scorerCode, requirementsText, className, overwrite, scorerType, responseType,
                version, Objects.hashCode(additionalProperties));
    }
}