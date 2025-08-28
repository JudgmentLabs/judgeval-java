package com.judgmentlabs.judgeval.tracer;

import java.util.Objects;

/**
 * Configuration for the Judgment Tracer that controls how tracing and
 * evaluation behave.
 * 
 * <p>
 * This class encapsulates all configuration parameters needed to initialize a
 * {@link Tracer}.
 * It provides a builder pattern for easy configuration and validation of
 * required parameters.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
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
 * @see Tracer
 * @since 1.0.0
 */
public final class TracerConfiguration {
    private final String projectName;
    private final String apiKey;
    private final String organizationId;
    private final String apiUrl;
    private final boolean enableEvaluation;

    /**
     * Private constructor used by the builder.
     * 
     * @param builder the builder containing configuration values
     * @throws IllegalArgumentException if project name is empty after trimming
     * @throws NullPointerException     if any required field is null
     */
    private TracerConfiguration(Builder builder) {
        this.projectName = Objects.requireNonNull(builder.projectName, "Project name cannot be null").trim();
        this.apiKey = Objects.requireNonNull(builder.apiKey, "API key cannot be null");
        this.organizationId = Objects.requireNonNull(builder.organizationId, "Organization ID cannot be null");
        this.apiUrl = Objects.requireNonNull(builder.apiUrl, "API URL cannot be null");
        this.enableEvaluation = builder.enableEvaluation;

        if (this.projectName.isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
    }

    /**
     * Gets the project name for this configuration.
     * 
     * @return the project name (never null, trimmed)
     */
    public String projectName() {
        return projectName;
    }

    /**
     * Gets the API key for authentication with Judgment Labs.
     * 
     * @return the API key (never null)
     */
    public String apiKey() {
        return apiKey;
    }

    /**
     * Gets the organization ID for this configuration.
     * 
     * @return the organization ID (never null)
     */
    public String organizationId() {
        return organizationId;
    }

    /**
     * Gets the API URL for Judgment Labs services.
     * 
     * @return the API URL (never null)
     */
    public String apiUrl() {
        return apiUrl;
    }

    /**
     * Gets whether evaluation is enabled for this configuration.
     * 
     * <p>
     * When enabled, the tracer will automatically evaluate spans using configured
     * scorers.
     * When disabled, evaluation calls will be ignored.
     * </p>
     * 
     * @return true if evaluation is enabled, false otherwise
     */
    public boolean enableEvaluation() {
        return enableEvaluation;
    }

    /**
     * Creates a new builder for constructing a TracerConfiguration.
     * 
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a default configuration with the given project name.
     * 
     * <p>
     * This method uses default values from environment variables:
     * <ul>
     * <li>API Key: {@code Env.JUDGMENT_API_KEY}</li>
     * <li>Organization ID: {@code Env.JUDGMENT_ORG_ID}</li>
     * <li>API URL: {@code Env.JUDGMENT_API_URL}</li>
     * <li>Evaluation: enabled</li>
     * </ul>
     * </p>
     * 
     * @param projectName the name of the project (must not be null or empty)
     * @return a new TracerConfiguration with default values
     * @throws IllegalArgumentException if project name is null or empty
     */
    public static TracerConfiguration createDefault(String projectName) {
        return builder()
                .projectName(projectName)
                .build();
    }

    /**
     * Builder for creating TracerConfiguration instances.
     * 
     * <p>
     * This builder provides a fluent API for configuring all aspects of the tracer.
     * All fields have sensible defaults, so only the project name is strictly
     * required.
     * </p>
     * 
     * <p>
     * Example usage:
     * </p>
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

        /**
         * Sets the project name for this configuration.
         * 
         * <p>
         * The project name is used to identify your project in the Judgment Labs
         * dashboard.
         * It must be unique within your organization.
         * </p>
         * 
         * @param projectName the project name (must not be null or empty)
         * @return this builder for method chaining
         * @throws IllegalArgumentException if project name is null or empty
         */
        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        /**
         * Sets the API key for authentication.
         * 
         * <p>
         * The API key is used to authenticate requests to Judgment Labs services.
         * You can find your API key in the Judgment Labs dashboard.
         * </p>
         * 
         * @param apiKey the API key (must not be null)
         * @return this builder for method chaining
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Sets the organization ID.
         * 
         * <p>
         * The organization ID identifies your organization within Judgment Labs.
         * This is typically provided when you set up your account.
         * </p>
         * 
         * @param organizationId the organization ID (must not be null)
         * @return this builder for method chaining
         */
        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        /**
         * Sets the API URL for Judgment Labs services.
         * 
         * <p>
         * This is typically only needed for custom deployments or testing.
         * The default URL points to the production Judgment Labs API.
         * </p>
         * 
         * @param apiUrl the API URL (must not be null)
         * @return this builder for method chaining
         */
        public Builder apiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        /**
         * Sets whether evaluation is enabled.
         * 
         * <p>
         * When enabled, the tracer will automatically evaluate spans using configured
         * scorers.
         * When disabled, evaluation calls will be ignored, which can be useful for
         * testing
         * or when you want to disable evaluation temporarily.
         * </p>
         * 
         * @param enableEvaluation true to enable evaluation, false to disable
         * @return this builder for method chaining
         */
        public Builder enableEvaluation(boolean enableEvaluation) {
            this.enableEvaluation = enableEvaluation;
            return this;
        }

        /**
         * Builds a new TracerConfiguration with the current builder state.
         * 
         * <p>
         * This method validates all required fields and creates an immutable
         * TracerConfiguration instance.
         * </p>
         * 
         * @return a new TracerConfiguration instance
         * @throws IllegalArgumentException if project name is null or empty
         * @throws NullPointerException     if any required field is null
         */
        public TracerConfiguration build() {
            return new TracerConfiguration(this);
        }
    }
}