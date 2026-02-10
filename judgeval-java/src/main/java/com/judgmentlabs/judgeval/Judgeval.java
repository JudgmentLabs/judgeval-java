package com.judgmentlabs.judgeval;

import java.util.Objects;
import java.util.Optional;

import com.judgmentlabs.judgeval.evaluation.EvaluationFactory;
import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectRequest;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectResponse;
import com.judgmentlabs.judgeval.scorers.ScorersFactory;
import com.judgmentlabs.judgeval.tracer.TracerFactory;
import com.judgmentlabs.judgeval.utils.Logger;

/**
 * Main entry point for the Judgment SDK. Provides access to tracer, scorer, and
 * evaluation factories.
 */
public class Judgeval {
    private final String             apiKey;
    private final String             organizationId;
    private final String             apiUrl;
    private final String             projectName;
    private final Optional<String>   projectId;
    private final JudgmentSyncClient internalClient;

    protected Judgeval(Builder builder) {
        this.apiKey = Objects.requireNonNull(builder.apiKey, "apiKey required");
        this.organizationId = Objects.requireNonNull(builder.organizationId, "organizationId required");
        this.projectName = Objects.requireNonNull(builder.projectName, "projectName required");
        this.apiUrl = Optional.ofNullable(builder.apiUrl).orElse(Env.JUDGMENT_API_URL);
        this.internalClient = new JudgmentSyncClient(apiUrl, apiKey, organizationId);
        this.projectId = resolveProjectId(projectName);

        if (projectId.isEmpty()) {
            Logger.warning("Project '" + projectName + "' not found. "
                    + "Some operations requiring project_id will be skipped.");
        }
    }

    /**
     * Returns a factory for creating tracers.
     *
     * @return the tracer factory
     */
    public TracerFactory tracer() {
        return new TracerFactory(internalClient, projectName, projectId);
    }

    /**
     * Returns a factory for creating scorers.
     *
     * @return the scorer factory
     */
    public ScorersFactory scorers() {
        return new ScorersFactory(internalClient, projectId);
    }

    /**
     * Returns a factory for creating evaluations.
     *
     * @return the evaluation factory
     */
    public EvaluationFactory evaluation() {
        return new EvaluationFactory(internalClient, projectId, projectName);
    }

    public Optional<String> getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    /**
     * Creates a new builder for configuring a Judgeval.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    private Optional<String> resolveProjectId(String name) {
        try {
            ResolveProjectRequest request = new ResolveProjectRequest();
            request.setProjectName(name);
            ResolveProjectResponse response = internalClient.postProjectsResolve(request);
            return Optional.ofNullable(response.getProjectId()).map(Object::toString);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Builder for configuring and creating Judgeval instances.
     */
    public static class Builder {
        private String apiKey         = Env.JUDGMENT_API_KEY;
        private String organizationId = Env.JUDGMENT_ORG_ID;
        private String apiUrl         = Env.JUDGMENT_API_URL;
        private String projectName;

        /**
         * Sets the project name used for project-scoped operations.
         *
         * @param projectName
         *            the project name
         * @return this builder
         */
        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        /**
         * Sets the API key for authentication.
         *
         * @param apiKey
         *            the API key
         * @return this builder
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Sets the organization ID.
         *
         * @param organizationId
         *            the organization ID
         * @return this builder
         */
        public Builder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        /**
         * Sets the API URL.
         *
         * @param apiUrl
         *            the API URL
         * @return this builder
         */
        public Builder apiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        /**
         * Builds and returns a new Judgeval instance.
         *
         * @return the configured Judgeval
         */
        public Judgeval build() {
            return new Judgeval(this);
        }
    }
}
