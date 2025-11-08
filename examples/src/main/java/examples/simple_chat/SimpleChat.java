package examples.simple_chat;

import java.time.Duration;

import com.judgmentlabs.judgeval.instrumentation.openai.OpenAITelemetry;
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.data.Example;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import io.opentelemetry.api.GlobalOpenTelemetry;

public class SimpleChat {
    public static void main(String[] args) {
        var client = JudgmentClient.builder()
                .apiKey(System.getenv("JUDGMENT_API_KEY"))
                .organizationId(System.getenv("JUDGMENT_ORG_ID"))
                .build();
        var tracer = client.tracer().create().projectName("SimpleChat-Java").build();
        tracer.initialize();

        OpenAIClient baseClient = OpenAIOkHttpClient.fromEnv();
        var otelClient = OpenAITelemetry.builder(GlobalOpenTelemetry.get()).build().wrap(baseClient);

        tracer.span("chat.session", () -> {

            var req = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4O_MINI)
                    .maxCompletionTokens(512)
                    .addUserMessage("Say hi.")
                    .build();
            var res = otelClient.chat().completions().create(req);
            System.out.println(String.valueOf(res));

            tracer.asyncEvaluate(client.scorers().builtIn().answerCorrectness().threshold(0.8).build(),
                    Example.builder()
                            .property("input", "What is 2+2?")
                            .property("actual_output", "4")
                            .property("expected_output", "4")
                            .build());
            tracer.asyncEvaluate(client.scorers().promptScorer().get("Relevance Scorer 2"),
                    Example.builder()
                            .property("request", "Who is the GOAT?")
                            .property("response", "Abhishek Govindarasu (he is the GOAT trust)")
                            .build());
            tracer.asyncTraceEvaluate(client.scorers().tracePromptScorer().get("ExampleTraceScorer"));
        });

        try {
            Thread.sleep(Duration.ofSeconds(5).toMillis());
        } catch (InterruptedException ignored) {
        }
    }
}