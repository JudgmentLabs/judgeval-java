package com.judgmentlabs.judgeval.v1.scorers.custom_scorer;

public final class CustomScorerFactory {
    public CustomScorerFactory() {
    }

    public CustomScorer get(String name) {
        return CustomScorer.builder()
                .name(name)
                .className(name)
                .serverHosted(true)
                .build();
    }

    public CustomScorer get(String name, String className) {
        return CustomScorer.builder()
                .name(name)
                .className(className)
                .serverHosted(true)
                .build();
    }
}
