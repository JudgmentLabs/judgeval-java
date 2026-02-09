package com.judgmentlabs.judgeval.tracer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.tracer.exporters.JudgmentSpanExporter;
import com.judgmentlabs.judgeval.tracer.exporters.NoOpSpanExporter;

import io.opentelemetry.sdk.trace.export.SpanExporter;

@ExtendWith(MockitoExtension.class)
class BaseTracerTest {
    private static final String TEST_PROJECT_NAME = "test-project";
    private static final String TEST_PROJECT_ID   = "test-project-id-123";
    @Mock
    private JudgmentSyncClient  mockClient;

    @Mock
    private ISerializer         mockSerializer;

    private TestableBaseTracer  tracer;

    @BeforeEach
    void setUp() throws Exception {
        lenient().when(mockClient.getApiUrl()).thenReturn("https://api.example.com");
        lenient().when(mockClient.getApiKey()).thenReturn("test-api-key");
        lenient().when(mockClient.getOrganizationId()).thenReturn("test-org-id");

        lenient().when(mockSerializer.serialize(any())).thenReturn("serialized");
        lenient().when(mockSerializer.serialize(any(), any())).thenReturn("serialized");

        tracer = new TestableBaseTracer(
                TEST_PROJECT_NAME,
                Optional.of(TEST_PROJECT_ID),
                true,
                mockClient,
                mockSerializer);
    }

    @Test
    void constructor_withValidParameters_setsProject() {
        assertNotNull(tracer);
        assertEquals(TEST_PROJECT_NAME, tracer.getProjectName());
        assertTrue(tracer.isEnableEvaluation());
        assertTrue(tracer.getProjectId().isPresent());
        assertEquals(TEST_PROJECT_ID, tracer.getProjectId().get());
    }

    @Test
    void constructor_withNullProjectName_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new TestableBaseTracer(
                    null,
                    Optional.of(TEST_PROJECT_ID),
                    true,
                    mockClient,
                    mockSerializer);
        });
    }

    @Test
    void constructor_withNullClient_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new TestableBaseTracer(
                    TEST_PROJECT_NAME,
                    Optional.of(TEST_PROJECT_ID),
                    true,
                    null,
                    mockSerializer);
        });
    }

    @Test
    void constructor_withNullSerializer_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new TestableBaseTracer(
                    TEST_PROJECT_NAME,
                    Optional.of(TEST_PROJECT_ID),
                    true,
                    mockClient,
                    null);
        });
    }

    @Test
    void constructor_withEmptyProjectId_hasEmptyProjectId() {
        TestableBaseTracer noProjectTracer = new TestableBaseTracer(
                TEST_PROJECT_NAME,
                Optional.empty(),
                true,
                mockClient,
                mockSerializer);

        assertFalse(noProjectTracer.getProjectId().isPresent());
    }

    @Test
    void getSpanExporter_withValidProjectId_returnsJudgmentSpanExporter() {
        SpanExporter exporter = tracer.getSpanExporter();
        assertNotNull(exporter);
        assertTrue(exporter instanceof JudgmentSpanExporter);
    }

    @Test
    void getSpanExporter_withoutProjectId_returnsNoOpSpanExporter() {
        TestableBaseTracer noProjectTracer = new TestableBaseTracer(
                TEST_PROJECT_NAME,
                Optional.empty(),
                true,
                mockClient,
                mockSerializer);

        SpanExporter exporter = noProjectTracer.getSpanExporter();
        assertNotNull(exporter);
        assertTrue(exporter instanceof NoOpSpanExporter);
    }

    @Test
    void getTracer_returnsTracer() {
        io.opentelemetry.api.trace.Tracer otelTracer = tracer.getTracer();
        assertNotNull(otelTracer);
    }

    @Test
    void setAttributes_withNull_doesNotThrow() {
        tracer.setAttributes(null);
    }

    private static class TestableBaseTracer extends BaseTracer {
        protected TestableBaseTracer(String projectName, Optional<String> projectId,
                boolean enableEvaluation, JudgmentSyncClient apiClient, ISerializer serializer) {
            super(projectName, projectId, enableEvaluation, apiClient, serializer);
        }

        @Override
        public void initialize() {
        }

        @Override
        public boolean forceFlush(int timeoutMillis) {
            return false;
        }

        @Override
        public void shutdown(int timeoutMillis) {
        }
    }
}
