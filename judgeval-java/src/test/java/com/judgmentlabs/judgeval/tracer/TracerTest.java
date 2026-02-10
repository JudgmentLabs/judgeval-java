package com.judgmentlabs.judgeval.tracer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;

@ExtendWith(MockitoExtension.class)
class TracerTest {
    private static final String TEST_PROJECT_NAME = "test-project";
    private static final String TEST_PROJECT_ID   = "test-project-id-123";

    @Mock
    private JudgmentSyncClient  mockClient;

    @BeforeEach
    void setUp() throws Exception {
        lenient().when(mockClient.getApiUrl()).thenReturn("https://api.example.com");
        lenient().when(mockClient.getApiKey()).thenReturn("test-api-key");
        lenient().when(mockClient.getOrganizationId()).thenReturn("test-org-id");
    }

    @Test
    void builder_returnsBuilder() {
        Tracer.Builder builder = Tracer.builder();
        assertNotNull(builder);
    }

    @Test
    void builder_withNullProjectName_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            Tracer.builder()
                    .client(mockClient)
                    .projectName(null)
                    .projectId(Optional.of(TEST_PROJECT_ID))
                    .build();
        });
    }

    @Test
    void builder_withNullClient_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            Tracer.builder()
                    .projectName(TEST_PROJECT_NAME)
                    .projectId(Optional.of(TEST_PROJECT_ID))
                    .client(null)
                    .build();
        });
    }
}
