package com.judgmentlabs.judgeval.data;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.judgmentlabs.judgeval.api.models.BaseScorer;
import com.judgmentlabs.judgeval.api.models.ScorerConfig;

public class EvaluationRun extends com.judgmentlabs.judgeval.api.models.EvaluationRun {

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
        eval.setExamples((List<com.judgmentlabs.judgeval.api.models.Example>) (List<?>) examples);
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
        eval.setExamples((List<com.judgmentlabs.judgeval.api.models.Example>) (List<?>) examples);
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
        setExamples((List<com.judgmentlabs.judgeval.api.models.Example>) (List<?>) examples);
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
}
