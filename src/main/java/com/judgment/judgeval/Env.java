package com.judgment.judgeval;

public final class Env {
    private Env() {}

    public static final String JUDGMENT_API_KEY = getEnvVar("JUDGMENT_API_KEY");
    public static final String JUDGMENT_ORG_ID = getEnvVar("JUDGMENT_ORG_ID");
    public static final String JUDGMENT_API_URL =
            getEnvVar("JUDGMENT_API_URL", "https://api.judgmentlabs.ai");
    public static final String JUDGMENT_DEFAULT_GPT_MODEL =
            getEnvVar("JUDGMENT_DEFAULT_GPT_MODEL", "gpt-4");

    private static String getEnvVar(String varName) {
        return getEnvVar(varName, null);
    }

    private static String getEnvVar(String varName, String defaultValue) {
        String value = System.getenv(varName);
        return value != null ? value : defaultValue;
    }
}
