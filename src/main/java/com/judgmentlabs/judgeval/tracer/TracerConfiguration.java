package com.judgmentlabs.judgeval.tracer;

import java.util.Objects;

/**
 * Configuration for the Judgment Tracer that controls how tracing and evaluation behave.
 *
 * <p>This class encapsulates all configuration parameters needed to initialize a {@link
 * JudgevalTracer}.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * TracerConfiguration config = TracerConfiguration.builder()
 *         .projectName("my-project")
 *         .apiKey("your-api-key")
 *         .organizationId("your-org-id")
 *         .enableEvaluation(true)
 *         .build();
 *
 * Tracer tracer = Tracer.createWithConfiguration(config);
 * }</pre>
 *
 * @see JudgevalTracer
 */
public final class TracerConfiguration {
    private final String projectName;
    private final String apiKey;
    private final String organizationId;
    private final String apiUrl;
    private final boolean enableEvaluation;

    private TracerConfiguration(Builder builder) {
        this.projectName =
                Objects.requireNonNull(builder.projectName, "Project name cannot be null").trim();
        this.apiKey = Objects.requireNonNull(builder.apiKey, "API key cannot be null");
        this.organizationId =
                Objects.requireNonNull(builder.organizationId, "Organization ID cannot be null");
        this.apiUrl = Objects.requireNonNull(builder.apiUrl, "API URL cannot be null");
        this.enableEvaluation = builder.enableEvaluation;

        if (this.projectName.isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
    }

    public String projectName() {
        return projectName;
    }

    public String apiKey() {
        return apiKey;
    }

    public String organizationId() {
        return organizationId;
    }

    public String apiUrl() {
        return apiUrl;
    }

    public boolean enableEvaluation() {
        return enableEvaluation;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a default configuration with the given project name.
     *
     * <p>This method uses default values from environment variables:
     *
     * <ul>
     *   <li>API Key: {@code Env.JUDGMENT_API_KEY}
     *   <li>Organization ID: {@code Env.JUDGMENT_ORG_ID}
     *   <li>API URL: {@code Env.JUDGMENT_API_URL}
     *   <li>Evaluation: enabled
     * </ul>
     *
     * @param projectName the name of the project
     * @return a new TracerConfiguration with default values
     * @throws IllegalArgumentException if project name is null or empty
     */
    public static TracerConfiguration createDefault(String projectName) {
        return builder().projectName(projectName).build();
    }

    /**
     * Builder for creating TracerConfiguration instances.
     *
     * <p>Example usage:
     *
     * <pre>{@code
     * TracerConfiguration config = TracerConfiguration.builder()
     *         .projectName("my-project")
     *         .apiKey("custom-api-key")
     *         .organizationId("custom-org-id")
     *         .apiUrl("https://custom-api.judgmentlabs.ai")
     *         .enableEvaluation(false)
     *         .build();
     * }</pre>
     */
    public static final class Builder {
        private String projectName;
        private String apiKey = com.judgmentlabs.judgeval.Env.JUDGMENT_API_KEY;
        private String organizationId = com.judgmentlabs.judgeval.Env.JUDGMENT_ORG_ID;
        private String apiUrl = com.judgmentlabs.judgeval.Env.JUDGMENT_API_URL;
        private boolean enableEvaluation = true;

        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder apiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        public Builder enableEvaluation(boolean enableEvaluation) {
            this.enableEvaluation = enableEvaluation;
            return this;
        }

        public TracerConfiguration build() {
            return new TracerConfiguration(this);
        }
    }
}
