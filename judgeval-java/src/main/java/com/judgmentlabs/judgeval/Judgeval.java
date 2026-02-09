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
        this.apiUrl = builder.apiUrl != null ? builder.apiUrl : Env.JUDGMENT_API_URL;
        this.internalClient = new JudgmentSyncClient(apiUrl, apiKey, organizationId);
        this.projectId = resolveProjectId(projectName);

        if (projectId.isEmpty()) {
            Logger.warning("Project '" + projectName + "' not found. "
                    + "Some operations requiring project_id will be skipped.");
        }
    }

    public TracerFactory tracer() {
        return new TracerFactory(internalClient, projectName, projectId);
    }

    public ScorersFactory scorers() {
        return new ScorersFactory(internalClient, projectId);
    }

    public EvaluationFactory evaluation() {
        return new EvaluationFactory(internalClient, projectId, projectName);
    }

    public Optional<String> getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

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

    public static class Builder {
        private String apiKey         = Env.JUDGMENT_API_KEY;
        private String organizationId = Env.JUDGMENT_ORG_ID;
        private String apiUrl         = Env.JUDGMENT_API_URL;
        private String projectName;

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

        public Judgeval build() {
            return new Judgeval(this);
        }
    }
}
