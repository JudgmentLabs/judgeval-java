package examples.simple_chat;

import java.time.Duration;

import com.judgmentlabs.judgeval.instrumentation.openai.OpenAITelemetry;
import com.judgmentlabs.judgeval.tracer.Tracer;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import io.opentelemetry.api.GlobalOpenTelemetry;

public class SimpleChat {
    public static void main(String[] args) {
        var tracer = Tracer.createDefault("SimpleChat-Java");
        tracer.initialize();

        System.out.println("init tracer");
        OpenAIClient baseClient;
        try {
            baseClient = OpenAIOkHttpClient.fromEnv();
        } catch (Exception e) {
            System.err.println("Failed to init OpenAI client from env: " + e);
            return;
        }
        var otelClient = OpenAITelemetry.builder(GlobalOpenTelemetry.get()).build().wrap(baseClient);

        tracer.span("chat.session", () -> {
            tracer.setGeneralSpan();
            tracer.span("llm.call", () -> {
                tracer.setLLMSpan();
                System.out.println("building request");
                var req = ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4O_MINI)
                        .maxCompletionTokens(512)
                        .addUserMessage("Say hi.")
                        .build();
                tracer.setInput(req);
                try {
                    System.out.println("sending request");
                    var res = otelClient.chat().completions().create(req);
                    tracer.setOutput(res);
                    System.out.println(String.valueOf(res));
                } catch (Throwable e) {
                    System.err.println(e.toString());
                    e.printStackTrace();
                }
            });
        });

        try {
            Thread.sleep(Duration.ofSeconds(5).toMillis());
        } catch (InterruptedException ignored) {
        }
        System.out.println("done");
    }
}
