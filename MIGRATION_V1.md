# Migration Guide: v0 to v1

This guide shows how to migrate from the deprecated v0 API to the new v1 API in `judgeval-java`.

## Client Initialization

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;

JudgmentClient client = JudgmentClient.builder()
    .apiKey("your-api-key")           // or use JUDGMENT_API_KEY env var
    .organizationId("your-org-id")    // or use JUDGMENT_ORG_ID env var
    .apiUrl("https://api.judgmentlabs.ai")  // optional, defaults to production
    .build();
```

The client automatically creates an internal `JudgmentSyncClient` that is passed to all child objects.

## 1. Tracer Migration

### Basic Tracer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.tracer.Tracer;

Tracer tracer = Tracer.createDefault("my-project");
tracer.initialize();
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.tracer.Tracer;

JudgmentClient client = JudgmentClient.builder().build();

Tracer tracer = client.tracer()
    .create()
    .projectName("my-project")
    .build();

tracer.initialize();
```

### Tracer with Custom Configuration

**Before (v0):**

```java
import com.judgmentlabs.judgeval.tracer.Tracer;
import com.judgmentlabs.judgeval.tracer.TracerConfiguration;

TracerConfiguration config = TracerConfiguration.builder()
    .projectName("my-project")
    .apiKey("key")
    .organizationId("org")
    .enableEvaluation(true)
    .build();

Tracer tracer = Tracer.createWithConfiguration(config);
tracer.initialize();
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.tracer.Tracer;

JudgmentClient client = JudgmentClient.builder()
    .apiKey("key")
    .organizationId("org")
    .build();

Tracer tracer = client.tracer()
    .create()
    .projectName("my-project")
    .enableEvaluation(true)
    .build();

tracer.initialize();
```

### Using Tracer Methods

All `BaseTracer` methods remain unchanged:

```java
tracer.setAttribute("key", "value");
tracer.setInput(inputData);
tracer.setOutput(outputData);
tracer.setLLMSpan();
tracer.asyncEvaluate(scorer, example);
tracer.asyncTraceEvaluate(scorer);

tracer.span("operation", () -> {
    // your code
});

tracer.forceFlush(5000);
tracer.shutdown(5000);
```

## 2. PromptScorer Migration

### Fetching Existing Scorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.prompt_scorer.PromptScorer;

PromptScorer scorer = PromptScorer.get("my-scorer");
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.prompt_scorer.PromptScorer;

JudgmentClient client = JudgmentClient.builder().build();

PromptScorer scorer = client.scorers()
    .promptScorer()
    .get("my-scorer");
```

### Creating New Scorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.prompt_scorer.PromptScorer;
import java.util.Map;

PromptScorer scorer = new PromptScorer(
    "accuracy-checker",
    "Does the output accurately answer the question?",
    0.7,
    Map.of("yes", 1.0, "no", 0.0)
);
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.prompt_scorer.PromptScorer;
import java.util.Map;

JudgmentClient client = JudgmentClient.builder().build();

PromptScorer scorer = client.scorers()
    .promptScorer()
    .create()
    .name("accuracy-checker")
    .prompt("Does the output accurately answer the question?")
    .threshold(0.7)
    .options(Map.of("yes", 1.0, "no", 0.0))
    .build();
```

## 3. TracePromptScorer Migration

### Fetching Existing Trace Scorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.prompt_scorer.TracePromptScorer;

TracePromptScorer scorer = TracePromptScorer.get("my-scorer");
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.prompt_scorer.TracePromptScorer;

JudgmentClient client = JudgmentClient.builder().build();

TracePromptScorer scorer = client.scorers()
    .promptScorer()
    .getTrace("my-scorer");
```

### Creating New Trace Scorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.prompt_scorer.TracePromptScorer;
import java.util.Map;

TracePromptScorer scorer = new TracePromptScorer(
    "response-quality",
    "Does this trace show a high-quality response flow?",
    0.75,
    null
);
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.prompt_scorer.TracePromptScorer;

JudgmentClient client = JudgmentClient.builder().build();

TracePromptScorer scorer = client.scorers()
    .promptScorer()
    .createTrace()
    .name("response-quality")
    .prompt("Does this trace show a high-quality response flow?")
    .threshold(0.75)
    .build();
```

## 4. CustomScorer Migration

### Basic Custom Scorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.custom_scorer.CustomScorer;

CustomScorer scorer = CustomScorer.get("my-custom-scorer");
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.custom_scorer.CustomScorer;

JudgmentClient client = JudgmentClient.builder().build();

CustomScorer scorer = client.scorers()
    .customScorer()
    .get("my-custom-scorer");
```

### Custom Scorer with Class Name

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.custom_scorer.CustomScorer;

CustomScorer scorer = CustomScorer.get("my-scorer", "MyCustomScorerClass");
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.custom_scorer.CustomScorer;

JudgmentClient client = JudgmentClient.builder().build();

CustomScorer scorer = client.scorers()
    .customScorer()
    .get("my-scorer", "MyCustomScorerClass");
```

## 5. Built-in Scorers Migration

### AnswerCorrectnessScorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.AnswerCorrectnessScorer;

AnswerCorrectnessScorer scorer = AnswerCorrectnessScorer.create();

AnswerCorrectnessScorer scorerWithThreshold = AnswerCorrectnessScorer.create(0.8);

AnswerCorrectnessScorer customScorer = AnswerCorrectnessScorer.builder()
    .threshold(0.7)
    .name("custom-correctness")
    .build();
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.built_in.AnswerCorrectnessScorer;

JudgmentClient client = JudgmentClient.builder().build();

AnswerCorrectnessScorer scorer = client.scorers()
    .builtIn()
    .answerCorrectness()
    .build();

AnswerCorrectnessScorer scorerWithThreshold = client.scorers()
    .builtIn()
    .answerCorrectness()
    .threshold(0.8)
    .build();

AnswerCorrectnessScorer customScorer = client.scorers()
    .builtIn()
    .answerCorrectness()
    .threshold(0.7)
    .name("custom-correctness")
    .build();
```

### AnswerRelevancyScorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.AnswerRelevancyScorer;

AnswerRelevancyScorer scorer = AnswerRelevancyScorer.create();
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.built_in.AnswerRelevancyScorer;

JudgmentClient client = JudgmentClient.builder().build();

AnswerRelevancyScorer scorer = client.scorers()
    .builtIn()
    .answerRelevancy()
    .build();
```

### FaithfulnessScorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.FaithfulnessScorer;

FaithfulnessScorer scorer = FaithfulnessScorer.create();
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.built_in.FaithfulnessScorer;

JudgmentClient client = JudgmentClient.builder().build();

FaithfulnessScorer scorer = client.scorers()
    .builtIn()
    .faithfulness()
    .build();
```

### InstructionAdherenceScorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.InstructionAdherenceScorer;

InstructionAdherenceScorer scorer = InstructionAdherenceScorer.create();
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.built_in.InstructionAdherenceScorer;

JudgmentClient client = JudgmentClient.builder().build();

InstructionAdherenceScorer scorer = client.scorers()
    .builtIn()
    .instructionAdherence()
    .build();
```

### DerailmentScorer

**Before (v0):**

```java
import com.judgmentlabs.judgeval.scorers.api_scorers.DerailmentScorer;

DerailmentScorer scorer = DerailmentScorer.create();
```

**After (v1):**

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.scorers.built_in.DerailmentScorer;

JudgmentClient client = JudgmentClient.builder().build();

DerailmentScorer scorer = client.scorers()
    .builtIn()
    .derailment()
    .build();
```

## Complete Example: Before and After

### Before (v0)

```java
import com.judgmentlabs.judgeval.tracer.Tracer;
import com.judgmentlabs.judgeval.scorers.api_scorers.prompt_scorer.PromptScorer;
import com.judgmentlabs.judgeval.scorers.api_scorers.AnswerCorrectnessScorer;
import com.judgmentlabs.judgeval.data.Example;

public class OldExample {
    public static void main(String[] args) {
        Tracer tracer = Tracer.createDefault("my-project");
        tracer.initialize();

        PromptScorer promptScorer = PromptScorer.get("accuracy-checker");

        AnswerCorrectnessScorer builtInScorer = AnswerCorrectnessScorer.create(0.8);

        Example example = Example.builder()
            .property("input", "What is 2+2?")
            .property("actual_output", "4")
            .property("expected_output", "4")
            .build();

        tracer.span("evaluate", () -> {
            tracer.setInput("What is 2+2?");
            tracer.setOutput("4");
            tracer.asyncEvaluate(promptScorer, example);
            tracer.asyncEvaluate(builtInScorer, example);
        });

        tracer.forceFlush(5000);
        tracer.shutdown(5000);
    }
}
```

### After (v1)

```java
import com.judgmentlabs.judgeval.v1.JudgmentClient;
import com.judgmentlabs.judgeval.v1.tracer.Tracer;
import com.judgmentlabs.judgeval.v1.scorers.prompt_scorer.PromptScorer;
import com.judgmentlabs.judgeval.v1.scorers.built_in.AnswerCorrectnessScorer;
import com.judgmentlabs.judgeval.data.Example;

public class NewExample {
    public static void main(String[] args) {
        JudgmentClient client = JudgmentClient.builder()
            .apiKey(System.getenv("JUDGMENT_API_KEY"))
            .organizationId(System.getenv("JUDGMENT_ORG_ID"))
            .build();

        Tracer tracer = client.tracer()
            .create()
            .projectName("my-project")
            .enableEvaluation(true)
            .build();

        tracer.initialize();

        PromptScorer promptScorer = client.scorers()
            .promptScorer()
            .get("accuracy-checker");

        AnswerCorrectnessScorer builtInScorer = client.scorers()
            .builtIn()
            .answerCorrectness()
            .threshold(0.8)
            .build();

        Example example = Example.builder()
            .property("input", "What is 2+2?")
            .property("actual_output", "4")
            .property("expected_output", "4")
            .build();

        tracer.span("evaluate", () -> {
            tracer.setInput("What is 2+2?");
            tracer.setOutput("4");
            tracer.asyncEvaluate(promptScorer, example);
            tracer.asyncEvaluate(builtInScorer, example);
        });

        tracer.forceFlush(5000);
        tracer.shutdown(5000);
    }
}
```

