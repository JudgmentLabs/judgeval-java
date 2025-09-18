package com.judgmentlabs.judgeval.data;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.judgmentlabs.judgeval.internal.api.models.ScorerConfig;

public class TraceEvaluationRun
        extends com.judgmentlabs.judgeval.internal.api.models.TraceEvaluationRun {

    private String organizationId;

    public TraceEvaluationRun() {
        super();
        setId(UUID.randomUUID().toString());
        setCreatedAt(Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
    }

    public TraceEvaluationRun(
            String projectName,
            String evalName,
            List<ScorerConfig> scorers,
            String model,
            String organizationId,
            List<List<String>> traceAndSpanIds) {
        this();
        setProjectName(projectName);
        setEvalName(evalName);
        setModel(model);
        setOrganizationId(organizationId);
        setTraceAndSpanIds(convertTraceAndSpanIds(traceAndSpanIds));
        setJudgmentScorers(scorers);
    }

    private static List<List<Object>> convertTraceAndSpanIds(List<List<String>> traceAndSpanIds) {
        if (traceAndSpanIds == null || traceAndSpanIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "Trace and span IDs are required for trace evaluations.");
        }

        List<List<Object>> converted = new java.util.ArrayList<>();
        for (List<String> pair : traceAndSpanIds) {
            if (pair == null || pair.size() != 2) {
                throw new IllegalArgumentException(
                        "Each trace and span ID pair must contain exactly 2 elements.");
            }
            converted.add(List.of(pair.get(0), pair.get(1)));
        }
        return converted;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        if (!super.equals(obj))
            return false;
        TraceEvaluationRun other = (TraceEvaluationRun) obj;
        return Objects.equals(organizationId, other.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), organizationId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(String projectName, String evalName) {
        return new Builder(projectName, evalName);
    }

    public static final class Builder {
        private String projectName;
        private String evalName;
        private List<ScorerConfig> scorers;
        private String model;
        private String organizationId;
        private List<List<String>> traceAndSpanIds;

        private Builder() {
        }

        private Builder(String projectName, String evalName) {
            this.projectName = projectName;
            this.evalName = evalName;
        }

        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder evalName(String evalName) {
            this.evalName = evalName;
            return this;
        }

        public Builder scorers(List<ScorerConfig> scorers) {
            this.scorers = scorers;
            return this;
        }

        public Builder scorer(ScorerConfig scorer) {
            if (this.scorers == null) {
                this.scorers = new java.util.ArrayList<>();
            }
            this.scorers.add(scorer);
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder traceAndSpanIds(List<List<String>> traceAndSpanIds) {
            this.traceAndSpanIds = traceAndSpanIds;
            return this;
        }

        public Builder traceAndSpanId(String traceId, String spanId) {
            if (this.traceAndSpanIds == null) {
                this.traceAndSpanIds = new java.util.ArrayList<>();
            }
            this.traceAndSpanIds.add(List.of(traceId, spanId));
            return this;
        }

        public TraceEvaluationRun build() {
            if (projectName == null || projectName.trim().isEmpty()) {
                throw new IllegalArgumentException("Project name is required");
            }
            if (evalName == null || evalName.trim().isEmpty()) {
                throw new IllegalArgumentException("Evaluation name is required");
            }
            if (scorers == null || scorers.isEmpty()) {
                throw new IllegalArgumentException("At least one scorer is required");
            }
            if (traceAndSpanIds == null || traceAndSpanIds.isEmpty()) {
                throw new IllegalArgumentException(
                        "At least one trace and span ID pair is required");
            }

            return new TraceEvaluationRun(
                    projectName, evalName, scorers, model, organizationId, traceAndSpanIds);
        }
    }
}
