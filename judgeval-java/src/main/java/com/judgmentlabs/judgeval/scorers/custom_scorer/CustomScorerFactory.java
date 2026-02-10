package com.judgmentlabs.judgeval.scorers.custom_scorer;

import java.util.Optional;

import com.judgmentlabs.judgeval.utils.Guards;

/**
 * Factory for creating custom scorer instances.
 */
public final class CustomScorerFactory {
    private final Optional<String> projectId;

    public CustomScorerFactory(Optional<String> projectId) {
        this.projectId = projectId;
    }

    /**
     * Creates a custom scorer with the specified name.
     *
     * @param name
     *            the scorer name
     * @return the configured custom scorer, or null if project ID is not set
     */
    public CustomScorer get(String name) {
        return get(name, name);
    }

    /**
     * Creates a custom scorer with the specified name and class name.
     *
     * @param name
     *            the scorer name
     * @param className
     *            the class name
     * @return the configured custom scorer, or null if project ID is not set
     */
    public CustomScorer get(String name, String className) {
        return Guards.expectProjectId(projectId)
                .map(pid -> CustomScorer.builder()
                        .name(name)
                        .className(className)
                        .projectId(pid)
                        .build())
                .orElse(null);
    }
}
