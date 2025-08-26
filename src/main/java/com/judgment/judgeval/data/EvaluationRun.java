package com.judgment.judgeval.data;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.judgment.judgeval.api.models.BaseScorer;
import com.judgment.judgeval.api.models.ScorerConfig;

public class EvaluationRun extends com.judgment.judgeval.api.models.EvaluationRun {

    private String organizationId;

    public EvaluationRun() {
        super();
        setId(UUID.randomUUID().toString());
        setCreatedAt(Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
    }

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
        setExamples((List<com.judgment.judgeval.api.models.Example>) (List<?>) examples);
        setModel(model);
        setOrganizationId(organizationId);

        if (scorers != null) {
            List<BaseScorer> customScorers = new java.util.ArrayList<>();
            List<ScorerConfig> judgmentScorers = new java.util.ArrayList<>();

            for (Object scorer : scorers) {
                if (scorer instanceof BaseScorer) {
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
