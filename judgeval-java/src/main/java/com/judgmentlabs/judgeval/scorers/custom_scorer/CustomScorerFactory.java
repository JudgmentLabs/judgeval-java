package com.judgmentlabs.judgeval.scorers.custom_scorer;

import java.util.Optional;

public final class CustomScorerFactory {
    private final Optional<String> projectId;

    public CustomScorerFactory(Optional<String> projectId) {
        this.projectId = projectId;
    }

    public CustomScorer get(String name) {
        return CustomScorer.builder()
                .name(name)
                .className(name)
                .projectId(projectId.orElse(""))
                .build();
    }

    public CustomScorer get(String name, String className) {
        return CustomScorer.builder()
                .name(name)
                .className(className)
                .projectId(projectId.orElse(""))
                .build();
    }
}
