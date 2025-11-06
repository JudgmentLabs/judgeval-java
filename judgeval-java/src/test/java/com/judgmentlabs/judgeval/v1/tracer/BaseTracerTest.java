package com.judgmentlabs.judgeval.v1.tracer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.judgmentlabs.judgeval.internal.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameRequest;
import com.judgmentlabs.judgeval.internal.api.models.ResolveProjectNameResponse;
import com.judgmentlabs.judgeval.v1.tracer.exporters.JudgmentSpanExporter;
import com.judgmentlabs.judgeval.v1.tracer.exporters.NoOpSpanExporter;

import io.opentelemetry.sdk.trace.export.SpanExporter;

@ExtendWith(MockitoExtension.class)
class BaseTracerTest {
    private static final String TEST_PROJECT_NAME = "test-project";
    private static final String TEST_PROJECT_ID   = "test-project-id-123";
    private static final String TEST_API_KEY      = "test-key";
    private static final String TEST_ORG_ID       = "test-org";
    private static final String TEST_API_URL      = "https://api.test.com";
    @Mock
    private JudgmentSyncClient  mockClient;

    @Mock
    private ISerializer         mockSerializer;

    private TestableBaseTracer  tracer;

    @BeforeEach
    void setUp() throws Exception {
        ResolveProjectNameResponse response = new ResolveProjectNameResponse();
        response.setProjectId(TEST_PROJECT_ID);

        lenient().when(mockClient.projectsResolve(any(ResolveProjectNameRequest.class)))
                .thenReturn(response);

        lenient().when(mockSerializer.serialize(any())).thenReturn("serialized");
        lenient().when(mockSerializer.serialize(any(), any())).thenReturn("serialized");

        tracer = new TestableBaseTracer(
                TEST_PROJECT_NAME,
                TEST_API_KEY,
                TEST_ORG_ID,
                TEST_API_URL,
                true,
                mockClient,
                mockSerializer);
    }

    @Test
    void constructor_withValidParameters_resolvesProject() {
        assertNotNull(tracer);
        assertEquals(TEST_PROJECT_NAME, tracer.getProjectName());
        assertEquals(TEST_API_KEY, tracer.getApiKey());
        assertEquals(TEST_ORG_ID, tracer.getOrganizationId());
        assertEquals(TEST_API_URL, tracer.getApiUrl());
        assertTrue(tracer.isEnableEvaluation());
        assertTrue(tracer.getProjectId().isPresent());
        assertEquals(TEST_PROJECT_ID, tracer.getProjectId().get());
    }

    @Test
    void constructor_withNullProjectName_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new TestableBaseTracer(
                    null,
                    TEST_API_KEY,
                    TEST_ORG_ID,
                    TEST_API_URL,
                    true,
                    mockClient,
                    mockSerializer);
        });
    }

    @Test
    void constructor_withNullApiKey_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new TestableBaseTracer(
                    TEST_PROJECT_NAME,
                    null,
                    TEST_ORG_ID,
                    TEST_API_URL,
                    true,
                    mockClient,
                    mockSerializer);
        });
    }

    @Test
    void constructor_withNullOrganizationId_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new TestableBaseTracer(
                    TEST_PROJECT_NAME,
                    TEST_API_KEY,
                    null,
                    TEST_API_URL,
                    true,
                    mockClient,
                    mockSerializer);
        });
    }

    @Test
    void constructor_withNullApiUrl_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new TestableBaseTracer(
                    TEST_PROJECT_NAME,
                    TEST_API_KEY,
                    TEST_ORG_ID,
                    null,
                    true,
                    mockClient,
                    mockSerializer);
        });
    }

    @Test
    void constructor_withFailedProjectResolution_hasEmptyProjectId() throws Exception {
        when(mockClient.projectsResolve(any(ResolveProjectNameRequest.class)))
                .thenThrow(new RuntimeException("Project not found"));

        TestableBaseTracer failedTracer = new TestableBaseTracer(
                TEST_PROJECT_NAME,
                TEST_API_KEY,
                TEST_ORG_ID,
                TEST_API_URL,
                true,
                mockClient,
                mockSerializer);

        assertFalse(failedTracer.getProjectId().isPresent());
    }

    @Test
    void getSpanExporter_withValidProjectId_returnsJudgmentSpanExporter() {
        SpanExporter exporter = tracer.getSpanExporter();
        assertNotNull(exporter);
        assertTrue(exporter instanceof JudgmentSpanExporter);
    }

    @Test
    void getSpanExporter_withoutProjectId_returnsNoOpSpanExporter() throws Exception {
        when(mockClient.projectsResolve(any(ResolveProjectNameRequest.class)))
                .thenReturn(null);

        TestableBaseTracer failedTracer = new TestableBaseTracer(
                TEST_PROJECT_NAME,
                TEST_API_KEY,
                TEST_ORG_ID,
                TEST_API_URL,
                true,
                mockClient,
                mockSerializer);

        SpanExporter exporter = failedTracer.getSpanExporter();
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
        protected TestableBaseTracer(String projectName, String apiKey, String organizationId, String apiUrl,
                boolean enableEvaluation, JudgmentSyncClient apiClient, ISerializer serializer) {
            super(projectName, apiKey, organizationId, apiUrl, enableEvaluation, apiClient, serializer);
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
