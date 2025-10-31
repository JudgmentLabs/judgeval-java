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
        });

        try {
            Thread.sleep(Duration.ofSeconds(5).toMillis());
        } catch (InterruptedException ignored) {
        }
    }
}
