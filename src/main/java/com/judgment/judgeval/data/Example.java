package com.judgment.judgeval.data;

import java.time.Instant;
import java.util.Map;

public class Example extends com.judgment.judgeval.api.models.Example {

    public enum ExampleParams {
        INPUT("input"),
        ACTUAL_OUTPUT("actual_output"),
        EXPECTED_OUTPUT("expected_output"),
        CONTEXT("context"),
        RETRIEVAL_CONTEXT("retrieval_context"),
        TOOLS_CALLED("tools_called"),
        EXPECTED_TOOLS("expected_tools"),
        ADDITIONAL_METADATA("additional_metadata");

        private final String value;

        ExampleParams(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public Example() {
        super();
        setExampleId("");
        setCreatedAt(Instant.now().toString());
        setName(null);
    }

    public Map<String, Object> getFields() {
        return getAdditionalProperties();
    }
}
