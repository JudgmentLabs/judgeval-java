package com.judgmentlabs.judgeval.tracer;

public final class OpenTelemetryKeys {
    public static final class AttributeKeys {
        public static final String JUDGMENT_SPAN_KIND = "judgment.span_kind";
        public static final String JUDGMENT_INPUT = "judgment.input";
        public static final String JUDGMENT_OUTPUT = "judgment.output";
        public static final String JUDGMENT_OFFLINE_MODE = "judgment.offline_mode";
        public static final String JUDGMENT_UPDATE_ID = "judgment.update_id";
        public static final String JUDGMENT_CUSTOMER_ID = "judgment.customer_id";
        public static final String JUDGMENT_AGENT_ID = "judgment.agent_id";
        public static final String JUDGMENT_PARENT_AGENT_ID = "judgment.parent_agent_id";
        public static final String JUDGMENT_AGENT_CLASS_NAME = "judgment.agent_class_name";
        public static final String JUDGMENT_AGENT_INSTANCE_NAME = "judgment.agent_instance_name";
        public static final String JUDGMENT_IS_AGENT_ENTRY_POINT = "judgment.is_agent_entry_point";
        public static final String JUDGMENT_CUMULATIVE_LLM_COST = "judgment.cumulative_llm_cost";
        public static final String JUDGMENT_STATE_BEFORE = "judgment.state_before";
        public static final String JUDGMENT_STATE_AFTER = "judgment.state_after";
        public static final String PENDING_TRACE_EVAL = "judgment.pending_trace_eval";

        private AttributeKeys() {}
    }

    public static final class ResourceKeys {
        public static final String SERVICE_NAME = "service.name";
        public static final String TELEMETRY_SDK_LANGUAGE = "telemetry.sdk.language";
        public static final String TELEMETRY_SDK_NAME = "telemetry.sdk.name";
        public static final String TELEMETRY_SDK_VERSION = "telemetry.sdk.version";
        public static final String JUDGMENT_PROJECT_ID = "judgment.project_id";

        private ResourceKeys() {}
    }

    private OpenTelemetryKeys() {}
}
