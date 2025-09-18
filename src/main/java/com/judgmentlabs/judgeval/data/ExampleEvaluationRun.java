package com.judgmentlabs.judgeval.data;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.judgmentlabs.judgeval.internal.api.models.ScorerConfig;

public class ExampleEvaluationRun
        extends com.judgmentlabs.judgeval.internal.api.models.ExampleEvaluationRun {

    private String organizationId;

    public ExampleEvaluationRun() {
        super();
        setId(UUID.randomUUID().toString());
        setCreatedAt(Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
    }

    public ExampleEvaluationRun(
            String projectName,
            String evalName,
            List<Example> examples,
            List<ScorerConfig> scorers,
            String model,
            String organizationId) {
        this();
        setProjectName(projectName);
        setEvalName(evalName);
        @SuppressWarnings("unchecked")
        List<com.judgmentlabs.judgeval.internal.api.models.Example> internalExamples =
                (List<com.judgmentlabs.judgeval.internal.api.models.Example>) (List<?>) examples;
        setExamples(internalExamples);
        setModel(model);
        setOrganizationId(organizationId);
        setCustomScorers(new java.util.ArrayList<>());
        setJudgmentScorers(scorers);
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        ExampleEvaluationRun other = (ExampleEvaluationRun) obj;
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
        private List<Example> examples;
        private List<ScorerConfig> scorers;
        private String model;
        private String organizationId;

        private Builder() {}

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

        public Builder examples(List<Example> examples) {
            this.examples = examples;
            return this;
        }

        public Builder example(Example example) {
            if (this.examples == null) {
                this.examples = new java.util.ArrayList<>();
            }
            this.examples.add(example);
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

        public ExampleEvaluationRun build() {
            if (projectName == null || projectName.trim().isEmpty()) {
                throw new IllegalArgumentException("Project name is required");
            }
            if (evalName == null || evalName.trim().isEmpty()) {
                throw new IllegalArgumentException("Evaluation name is required");
            }
            if (examples == null || examples.isEmpty()) {
                throw new IllegalArgumentException("At least one example is required");
            }
            if (scorers == null || scorers.isEmpty()) {
                throw new IllegalArgumentException("At least one scorer is required");
            }

            return new ExampleEvaluationRun(
                    projectName, evalName, examples, scorers, model, organizationId);
        }
    }
}
