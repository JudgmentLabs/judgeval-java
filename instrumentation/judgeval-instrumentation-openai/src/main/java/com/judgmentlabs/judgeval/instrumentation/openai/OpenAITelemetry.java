package com.judgmentlabs.judgeval.instrumentation.openai;

import io.opentelemetry.api.OpenTelemetry;

public final class OpenAITelemetry {
    private OpenAITelemetry() {
    }

    public static io.opentelemetry.instrumentation.openai.v1_1.OpenAITelemetryBuilder builder(
            OpenTelemetry openTelemetry) {
        return io.opentelemetry.instrumentation.openai.v1_1.OpenAITelemetry.builder(openTelemetry);
    }

    public static io.opentelemetry.instrumentation.openai.v1_1.OpenAITelemetry create(OpenTelemetry openTelemetry) {
        return io.opentelemetry.instrumentation.openai.v1_1.OpenAITelemetry.create(openTelemetry);
    }
}
