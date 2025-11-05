package examples.v1_quick_start;

import com.judgmentlabs.judgeval.data.Example;
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.built_in.AnswerCorrectnessScorer;
import com.judgmentlabs.judgeval.v1.scorers.built_in.FaithfulnessScorer;
import com.judgmentlabs.judgeval.v1.scorers.custom_scorer.CustomScorer;
import com.judgmentlabs.judgeval.v1.scorers.prompt_scorer.PromptScorer;
import com.judgmentlabs.judgeval.v1.tracer.Tracer;

public class V1QuickStart {
    public static void main(String[] args) {
        System.out.println("=== Judgeval SDK V1 Quick Start ===\n");

        System.out.println("1. Initialize JudgmentClient");
        JudgmentClient client = JudgmentClient.builder()
                .apiKey(System.getenv("JUDGMENT_API_KEY"))
                .organizationId(System.getenv("JUDGMENT_ORG_ID"))
                .build();
        System.out.println("   Client initialized\n");

        System.out.println("2. Create and initialize Tracer");
        Tracer tracer = client.tracer().create()
                .projectName("quickstart-project")
                .enableEvaluation(true)
                .build();
        tracer.initialize();
        System.out.println("   Tracer initialized for project: quickstart-project\n");

        System.out.println("3. Use Tracer for distributed tracing");
        tracer.span("example-operation", () -> {
            tracer.setLLMSpan();
            tracer.setInput("What is the capital of France?");

            String llmOutput = "The capital of France is Paris.";

            tracer.setOutput(llmOutput);
            System.out.println("   Traced operation with input/output");

            System.out.println();

            System.out.println("4. Access PromptScorer (fetch existing)");
            try {
                PromptScorer existingScorer = client.scorers()
                        .promptScorer()
                        .get("example-scorer");
                System.out.println("   Retrieved PromptScorer: " + existingScorer.getName());
            } catch (Exception e) {
                System.out.println("   Note: Scorer 'example-scorer' not found (expected for first run)");
            }
            System.out.println();

            System.out.println("5. Create new PromptScorer");
            PromptScorer newScorer = client.scorers()
                    .promptScorer()
                    .create()
                    .name("kindness-scorer")
                    .prompt("Did the assistant respond kindly and respectfully?")
                    .threshold(0.7)
                    .build();
            System.out.println("   Created PromptScorer: " + newScorer.getName());
            System.out.println("   Threshold: " + newScorer.getThreshold());
            System.out.println();

            System.out.println("6. Use TracePromptScorer");
            try {
                PromptScorer traceScorer = client.scorers()
                        .tracePromptScorer()
                        .create()
                        .name("trace-quality-scorer")
                        .prompt("Does the entire trace show high quality reasoning?")
                        .threshold(0.8)
                        .build();
                System.out.println("   Created TracePromptScorer: " + traceScorer.getName());
            } catch (Exception e) {
                System.out.println("   TracePromptScorer creation demo");
            }
            System.out.println();

            System.out.println("7. Use CustomScorer");
            CustomScorer customScorer = client.scorers()
                    .customScorer()
                    .get("my-custom-scorer", "MyCustomScorerClass");
            System.out.println("   Created CustomScorer: " + customScorer.getName());
            System.out.println();

            System.out.println("8. Use Built-in Scorers");
            AnswerCorrectnessScorer correctnessScorer = client.scorers()
                    .builtIn()
                    .answerCorrectness()
                    .threshold(0.8)
                    .build();
            System.out.println("   Created AnswerCorrectnessScorer with threshold: "
                    + correctnessScorer.getThreshold());

            FaithfulnessScorer faithfulnessScorer = client.scorers()
                    .builtIn()
                    .faithfulness()
                    .build();
            System.out.println("   Created FaithfulnessScorer with default threshold: "
                    + faithfulnessScorer.getThreshold());
            System.out.println();

            System.out.println("9. Run Evaluation");

            System.out.println("10. Complete workflow example");
            tracer.span("complete-llm-call", () -> {
                tracer.setLLMSpan();
                tracer.setInput("Explain quantum computing in simple terms");

                String response = "Quantum computing uses quantum mechanics to process information...";

                tracer.setOutput(response);

                Example evaluationExample = Example.builder()
                        .property("input", "Explain quantum computing in simple terms")
                        .property("actual_output", response)
                        .property("expected_output", "A clear, simple explanation")
                        .build();
                tracer.asyncEvaluate(client.scorers().builtIn().answerCorrectness().build(), evaluationExample);

                System.out.println("    Traced LLM call with evaluation example ready");
            });
            System.out.println();
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
