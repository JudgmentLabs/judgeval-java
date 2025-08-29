package com.judgmentlabs.judgeval.data;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.judgmentlabs.judgeval.internal.api.models.BaseScorer;
import com.judgmentlabs.judgeval.internal.api.models.ScorerConfig;

public class EvaluationRun extends com.judgmentlabs.judgeval.internal.api.models.EvaluationRun {

    private String organizationId;

    public EvaluationRun() {
        super();
        setId(UUID.randomUUID().toString());
        setCreatedAt(Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
    }

    // Factory method for local scorers (BaseScorer)
    public static EvaluationRun createWithLocalScorers(
            String projectName,
            String evalName,
            List<Example> examples,
            List<com.judgmentlabs.judgeval.scorers.BaseScorer> localScorers,
            String model,
            String organizationId) {
        EvaluationRun eval = new EvaluationRun();
        eval.setProjectName(projectName);
        eval.setEvalName(evalName);
        eval.setExamples(
                (List<com.judgmentlabs.judgeval.internal.api.models.Example>) (List<?>) examples);
        eval.setModel(model);
        eval.setOrganizationId(organizationId);

        if (localScorers != null) {
            List<BaseScorer> customScorers = new java.util.ArrayList<>();

            for (com.judgmentlabs.judgeval.scorers.BaseScorer scorer : localScorers) {
                customScorers.add((BaseScorer) scorer);
            }

            eval.setCustomScorers(customScorers);
            eval.setJudgmentScorers(new java.util.ArrayList<>());
        }

        eval.validateScorerLists();
        return eval;
    }

    // Factory method for API scorers (ScorerConfig)
    public static EvaluationRun createWithApiScorers(
            String projectName,
            String evalName,
            List<Example> examples,
            List<ScorerConfig> apiScorers,
            String model,
            String organizationId) {
        EvaluationRun eval = new EvaluationRun();
        eval.setProjectName(projectName);
        eval.setEvalName(evalName);
        eval.setExamples(
                (List<com.judgmentlabs.judgeval.internal.api.models.Example>) (List<?>) examples);
        eval.setModel(model);
        eval.setOrganizationId(organizationId);

        if (apiScorers != null) {
            eval.setCustomScorers(new java.util.ArrayList<>());
            eval.setJudgmentScorers(apiScorers);
        }

        eval.validateScorerLists();
        return eval;
    }

    // Generic constructor for mixed types (Object)
    public EvaluationRun(
            String projectName,
            String evalName,
            List<Example> examples,
            List<Object> scorers,
            String model,
            String organizationId) {
        this();
        setProjectName(projectName);
        setEvalName(evalName);
        setExamples(
                (List<com.judgmentlabs.judgeval.internal.api.models.Example>) (List<?>) examples);
        setModel(model);
        setOrganizationId(organizationId);

        if (scorers != null) {
            List<BaseScorer> customScorers = new java.util.ArrayList<>();
            List<ScorerConfig> judgmentScorers = new java.util.ArrayList<>();

            for (Object scorer : scorers) {
                if (scorer instanceof com.judgmentlabs.judgeval.scorers.BaseScorer) {
                    customScorers.add((BaseScorer) scorer);
                } else if (scorer instanceof ScorerConfig) {
                    judgmentScorers.add((ScorerConfig) scorer);
                }
            }

            setCustomScorers(customScorers);
            setJudgmentScorers(judgmentScorers);
        }

        validateScorerLists();
    }

    private void validateScorerLists() {
        List<BaseScorer> customScorers = getCustomScorers();
        List<ScorerConfig> judgmentScorers = getJudgmentScorers();

        if ((customScorers == null || customScorers.isEmpty())
                && (judgmentScorers == null || judgmentScorers.isEmpty())) {
            throw new IllegalArgumentException(
                    "At least one of custom_scorers or judgment_scorers must be provided.");
        }

        if (customScorers != null
                && !customScorers.isEmpty()
                && judgmentScorers != null
                && !judgmentScorers.isEmpty()) {
            throw new IllegalArgumentException(
                    "Only one of custom_scorers or judgment_scorers can be provided, not both.");
        }
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
        EvaluationRun other = (EvaluationRun) obj;
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
        private List<com.judgmentlabs.judgeval.scorers.BaseScorer> localScorers;
        private List<ScorerConfig> apiScorers;
        private List<Object> mixedScorers;
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

        public Builder localScorers(
                List<com.judgmentlabs.judgeval.scorers.BaseScorer> localScorers) {
            this.localScorers = localScorers;
            return this;
        }

        public Builder localScorer(com.judgmentlabs.judgeval.scorers.BaseScorer scorer) {
            if (this.localScorers == null) {
                this.localScorers = new java.util.ArrayList<>();
            }
            this.localScorers.add(scorer);
            return this;
        }

        public Builder apiScorers(List<ScorerConfig> apiScorers) {
            this.apiScorers = apiScorers;
            return this;
        }

        public Builder apiScorer(ScorerConfig scorer) {
            if (this.apiScorers == null) {
                this.apiScorers = new java.util.ArrayList<>();
            }
            this.apiScorers.add(scorer);
            return this;
        }

        public Builder scorers(List<Object> scorers) {
            this.mixedScorers = scorers;
            return this;
        }

        public Builder scorer(Object scorer) {
            if (this.mixedScorers == null) {
                this.mixedScorers = new java.util.ArrayList<>();
            }
            this.mixedScorers.add(scorer);
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

        public EvaluationRun build() {
            if (projectName == null || projectName.trim().isEmpty()) {
                throw new IllegalArgumentException("Project name is required");
            }
            if (evalName == null || evalName.trim().isEmpty()) {
                throw new IllegalArgumentException("Evaluation name is required");
            }
            if (examples == null || examples.isEmpty()) {
                throw new IllegalArgumentException("At least one example is required");
            }

            // Determine which type of scorers to use
            if (localScorers != null && !localScorers.isEmpty()) {
                return createWithLocalScorers(
                        projectName, evalName, examples, localScorers, model, organizationId);
            } else if (apiScorers != null && !apiScorers.isEmpty()) {
                return createWithApiScorers(
                        projectName, evalName, examples, apiScorers, model, organizationId);
            } else if (mixedScorers != null && !mixedScorers.isEmpty()) {
                return new EvaluationRun(
                        projectName, evalName, examples, mixedScorers, model, organizationId);
            } else {
                throw new IllegalArgumentException("At least one scorer is required");
            }
        }
    }
}
