package com.judgmentlabs.judgeval.utils;

import java.util.Optional;

/**
 * Validation guards for runtime preconditions.
 */
public final class Guards {

    private Guards() {
    }

    /**
     * Validates that a project ID exists. Logs an error with the caller method
     * name and returns empty if missing.
     *
     * @param projectId
     *            the project ID to validate
     * @return the project ID if present, or empty if missing
     */
    public static Optional<String> expectProjectId(Optional<String> projectId) {
        return Optional.ofNullable(projectId)
                .flatMap(pid -> pid)
                .or(() -> {
                    StackTraceElement[] stack = Thread.currentThread().getStackTrace();
                    String caller = stack.length >= 3 ? stack[2].getMethodName() : "unknown";
                    Logger.error("project_id is not set. " + caller + "() will be skipped.");
                    return Optional.empty();
                });
    }
}
