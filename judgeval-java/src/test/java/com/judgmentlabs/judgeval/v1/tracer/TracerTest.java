package com.judgmentlabs.judgeval.v1.tracer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TracerTest {
    private static final String TEST_PROJECT_NAME = "test-project";
    private static final String TEST_API_KEY      = "test-key";
    private static final String TEST_ORG_ID       = "test-org";

    @Test
    void builder_returnsBuilder() {
        Tracer.Builder builder = Tracer.builder();
        assertNotNull(builder);
    }

    @Test
    void builder_withNullProjectName_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            Tracer.builder()
                    .projectName(null)
                    .apiKey(TEST_API_KEY)
                    .organizationId(TEST_ORG_ID)
                    .build();
        });
    }

    @Test
    void builder_withNullApiKey_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            Tracer.builder()
                    .projectName(TEST_PROJECT_NAME)
                    .apiKey(null)
                    .organizationId(TEST_ORG_ID)
                    .build();
        });
    }

    @Test
    void builder_withNullOrganizationId_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            Tracer.builder()
                    .projectName(TEST_PROJECT_NAME)
                    .apiKey(TEST_API_KEY)
                    .organizationId(null)
                    .build();
        });
    }
}
