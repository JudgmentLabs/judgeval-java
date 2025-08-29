package com.judgmentlabs.judgeval.data;

import java.time.Instant;
import java.util.Map;

public class Example extends com.judgmentlabs.judgeval.internal.api.models.Example {

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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Example example;

        private Builder() {
            this.example = new Example();
        }

        public Builder input(String input) {
            example.setAdditionalProperty(ExampleParams.INPUT.getValue(), input);
            return this;
        }

        public Builder actualOutput(String actualOutput) {
            example.setAdditionalProperty(ExampleParams.ACTUAL_OUTPUT.getValue(), actualOutput);
            return this;
        }

        public Builder expectedOutput(String expectedOutput) {
            example.setAdditionalProperty(ExampleParams.EXPECTED_OUTPUT.getValue(), expectedOutput);
            return this;
        }

        public Builder context(String context) {
            example.setAdditionalProperty(ExampleParams.CONTEXT.getValue(), context);
            return this;
        }

        public Builder retrievalContext(String retrievalContext) {
            example.setAdditionalProperty(
                    ExampleParams.RETRIEVAL_CONTEXT.getValue(), retrievalContext);
            return this;
        }

        public Builder toolsCalled(Object toolsCalled) {
            example.setAdditionalProperty(ExampleParams.TOOLS_CALLED.getValue(), toolsCalled);
            return this;
        }

        public Builder expectedTools(Object expectedTools) {
            example.setAdditionalProperty(ExampleParams.EXPECTED_TOOLS.getValue(), expectedTools);
            return this;
        }

        public Builder additionalMetadata(Map<String, Object> metadata) {
            example.setAdditionalProperty(ExampleParams.ADDITIONAL_METADATA.getValue(), metadata);
            return this;
        }

        public Builder property(String key, Object value) {
            example.setAdditionalProperty(key, value);
            return this;
        }

        public Builder name(String name) {
            example.setName(name);
            return this;
        }

        public Example build() {
            return example;
        }
    }
}
