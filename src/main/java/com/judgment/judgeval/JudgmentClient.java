package com.judgment.judgeval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.judgment.judgeval.api.JudgmentSyncClient;
import com.judgment.judgeval.api.models.EvalResultsFetch;
import com.judgment.judgeval.api.models.ScorerConfig;
import com.judgment.judgeval.data.EvaluationRun;
import com.judgment.judgeval.data.Example;
import com.judgment.judgeval.data.ScorerData;
import com.judgment.judgeval.data.ScoringResult;
import com.judgment.judgeval.exceptions.JudgmentRuntimeError;
import com.judgment.judgeval.exceptions.JudgmentTestError;

public class JudgmentClient {
    private final String apiKey;
    private final String organizationId;

    public JudgmentClient(String apiKey, String organizationId) {
        this.apiKey = Objects.requireNonNull(apiKey, "API key cannot be null");
        this.organizationId =
                Objects.requireNonNull(organizationId, "Organization ID cannot be null");
    }

    public List<ScoringResult> runEvaluation(List<Example> examples, List<Object> scorers,
            String projectName, String evalRunName, String model, boolean assertTest) {
        validateInputs(examples, scorers, projectName, evalRunName);

        if (!examples.isEmpty()) {
            java.util.Set<String> keys = examples.get(0).getFields().keySet();
            for (Example example : examples) {
                java.util.Set<String> currentKeys = example.getFields().keySet();
                if (!currentKeys.equals(keys)) {
                    throw new IllegalArgumentException(String.format(
                            "All examples must have the same keys: %s != %s", currentKeys, keys));
                }
            }
        }

        try {
            validateScorerTypes(scorers);

            EvaluationRun eval = new EvaluationRun(projectName, evalRunName, examples, scorers,
                    model != null ? model : Env.JUDGMENT_DEFAULT_GPT_MODEL, organizationId);

            List<ScoringResult> results = runEval(eval);

            if (assertTest) {
                assertTestResults(results);
            }

            return results;

        } catch (JudgmentTestError e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(
                    "Please check your EvaluationRun object, one or more fields are invalid: %s",
                    e.getMessage()));
        } catch (Exception e) {
            throw new JudgmentRuntimeError(String.format(
                    "An unexpected error occurred during evaluation: %s", e.getMessage()), e);
        }
    }

    public List<ScoringResult> runEvaluation(List<Example> examples, List<Object> scorers,
            String projectName, String evalRunName) {
        return runEvaluation(examples, scorers, projectName, evalRunName,
                Env.JUDGMENT_DEFAULT_GPT_MODEL, false);
    }

    private void validateInputs(List<Example> examples, List<Object> scorers, String projectName,
            String evalRunName) {
        if (examples == null || examples.isEmpty()) {
            throw new IllegalArgumentException("Examples cannot be null or empty");
        }
        if (scorers == null || scorers.isEmpty()) {
            throw new IllegalArgumentException("Scorers cannot be null or empty");
        }
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        if (evalRunName == null || evalRunName.trim().isEmpty()) {
            throw new IllegalArgumentException("Evaluation run name cannot be null or empty");
        }

        checkExamples(examples, scorers);
    }

    private void checkExamples(List<Example> examples, List<Object> scorers) {
        boolean promptUser = false;

        for (Object scorer : scorers) {
            if (scorer instanceof ScorerConfig) {
                ScorerConfig scorerConfig = (ScorerConfig) scorer;
                for (Example example : examples) {
                    List<String> missingParams = new ArrayList<>();
                    if (scorerConfig.getRequiredParams() != null) {
                        for (String param : scorerConfig.getRequiredParams()) {
                            if (example.getAdditionalProperties().get(param) == null) {
                                missingParams.add(param);
                            }
                        }
                    }
                    if (!missingParams.isEmpty()) {
                        System.out.println(
                                "⚠️  WARNING: Example is missing required parameters for scorer "
                                        + scorerConfig.getScoreType());
                        System.out
                                .println("Missing parameters: " + String.join(", ", missingParams));
                        System.out.println("Example: " + example.getAdditionalProperties());
                        System.out.println("-".repeat(40));
                        promptUser = true;
                    }
                }
            }
        }

        if (promptUser) {
            System.out.print("Do you want to continue? (y/n): ");
            try {
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                String userInput = scanner.nextLine();
                if (!"y".equalsIgnoreCase(userInput)) {
                    System.exit(0);
                } else {
                    System.out.println("Continuing...");
                }
            } catch (Exception e) {
                System.out.println("Continuing...");
            }
        }
    }

    private void validateScorerTypes(List<Object> scorers) {
        int customScorers = 0;
        int judgmentScorers = 0;

        for (Object scorer : scorers) {
            if (scorer instanceof com.judgment.judgeval.api.models.BaseScorer) {
                customScorers++;
            } else if (scorer instanceof ScorerConfig) {
                judgmentScorers++;
            }
        }

        if (customScorers > 0 && judgmentScorers > 0) {
            throw new IllegalArgumentException(
                    "We currently do not support running both local and Judgment API scorers at the same time. "
                            + "Please run your evaluation with either local scorers or Judgment API scorers, but not both.");
        }
    }

    private List<ScoringResult> runEval(EvaluationRun eval) {
        JudgmentSyncClient client = new JudgmentSyncClient(Env.JUDGMENT_API_URL);
        try {
            System.out.println("Submitting evaluation to API...");
            Object response = client.addToRunEvalQueue(apiKey, organizationId, eval);
            System.out.println("API Response: " + response);

            if (response instanceof Map) {
                Map<String, Object> responseMap = (Map<String, Object>) response;
                if (!Boolean.TRUE.equals(responseMap.get("success"))) {
                    String errorMessage = (String) responseMap.get("error");
                    throw new JudgmentRuntimeError(errorMessage);
                }
            }

            return pollEvaluationUntilComplete(eval, client);
        } catch (Exception e) {
            System.err.println(
                    "Exception details: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            throw new JudgmentRuntimeError("Failed to run evaluation", e);
        }
    }

    private List<ScoringResult> pollEvaluationUntilComplete(EvaluationRun eval,
            JudgmentSyncClient client) {
        return pollEvaluationUntilComplete(eval, client, 2.0, 5, 60);
    }

    private List<ScoringResult> pollEvaluationUntilComplete(EvaluationRun eval,
            JudgmentSyncClient client, double pollIntervalSeconds, int maxFailures,
            int maxPollCount) {
        int pollCount = 0;
        int exceptionCount = 0;
        long startTime = System.currentTimeMillis();

        while (pollCount < maxPollCount) {
            pollCount++;
            try {
                long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                System.out.println("Running evaluation... (" + elapsed + " sec)");

                Object statusResponse = client.getEvaluationStatus(apiKey, organizationId,
                        eval.getId().toString(), eval.getProjectName().toString());

                if (statusResponse instanceof Map) {
                    Map<String, Object> statusMap = (Map<String, Object>) statusResponse;
                    if (!"completed".equals(statusMap.get("status"))) {
                        Thread.sleep((long) (pollIntervalSeconds * 1000));
                        continue;
                    }
                }

                EvalResultsFetch fetchRequest = new EvalResultsFetch();
                fetchRequest.setExperimentRunId(eval.getId().toString());
                fetchRequest.setProjectName(eval.getProjectName().toString());

                Object resultsResponse =
                        client.fetchExperimentRun(apiKey, organizationId, fetchRequest);

                if (resultsResponse instanceof Map) {
                    Map<String, Object> resultsMap = (Map<String, Object>) resultsResponse;
                    List<Map<String, Object>> examplesData =
                            (List<Map<String, Object>>) resultsMap.get("examples");

                    if (examplesData == null) {
                        Thread.sleep((long) (pollIntervalSeconds * 1000));
                        continue;
                    }

                    return parseScoringResults(examplesData);
                }
            } catch (Exception e) {
                exceptionCount++;
                if (exceptionCount > maxFailures) {
                    throw new JudgmentRuntimeError(
                            "Error checking evaluation status after " + pollCount + " attempts", e);
                }
                try {
                    Thread.sleep((long) (pollIntervalSeconds * 1000));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new JudgmentRuntimeError("Polling interrupted", ie);
                }
            }
        }

        throw new JudgmentRuntimeError(
                "Error checking evaluation status after " + pollCount + " attempts");
    }

    private List<ScoringResult> parseScoringResults(List<Map<String, Object>> examplesData) {
        List<ScoringResult> results = new ArrayList<>();

        for (Map<String, Object> exampleData : examplesData) {
            List<Map<String, Object>> scorerDataList =
                    (List<Map<String, Object>>) exampleData.get("scorer_data");
            List<ScorerData> scorersData = new ArrayList<>();

            boolean success = true;
            if (scorerDataList != null) {
                for (Map<String, Object> rawScorerData : scorerDataList) {
                    ScorerData scorerData = new ScorerData();
                    scorerData.setName((String) rawScorerData.get("name"));
                    Object scoreObj = rawScorerData.get("score");
                    scorerData.setScore(
                            scoreObj instanceof Number ? ((Number) scoreObj).doubleValue() : null);
                    scorerData.setSuccess((Boolean) rawScorerData.get("success"));
                    scorerData.setReason((String) rawScorerData.get("reason"));
                    Object thresholdObj = rawScorerData.get("threshold");
                    scorerData.setThreshold(
                            thresholdObj instanceof Number ? ((Number) thresholdObj).doubleValue()
                                    : null);
                    scorerData.setStrictMode((Boolean) rawScorerData.get("strict_mode"));
                    scorerData.setEvaluationModel((String) rawScorerData.get("evaluation_model"));
                    scorerData.setError((String) rawScorerData.get("error"));
                    scorerData.setAdditionalMetadata(
                            (Map<String, Object>) rawScorerData.get("additional_metadata"));

                    scorersData.add(scorerData);
                    if (!Boolean.TRUE.equals(scorerData.getSuccess())) {
                        success = false;
                    }
                }
            }

            Example example = new Example();
            example.setExampleId((String) exampleData.get("example_id"));
            example.setCreatedAt((String) exampleData.get("created_at"));
            example.setName(exampleData.get("name"));

            ScoringResult result = new ScoringResult();
            result.setSuccess(success);
            result.setScorersData(scorersData);
            result.setDataObject(example);

            results.add(result);
        }

        return results;
    }

    private void assertTestResults(List<ScoringResult> results) {
        if (results == null || results.isEmpty()) {
            throw new JudgmentTestError("No results to assert");
        }

        List<java.util.Map<String, Object>> failedCases = new ArrayList<>();

        for (ScoringResult result : results) {
            if (result.getSuccess() == null || !result.getSuccess()) {
                java.util.Map<String, Object> testCase = new java.util.HashMap<>();
                java.util.List<ScorerData> failedScorers = new ArrayList<>();

                if (result.getScorersData() != null) {
                    List<ScorerData> scorersData = (List<ScorerData>) result.getScorersData();
                    for (ScorerData scorerData : scorersData) {
                        if (!Boolean.TRUE.equals(scorerData.getSuccess())) {
                            if ("Tool Order".equals(scorerData.getName())) {
                                scorerData.setThreshold(null);
                                scorerData.setEvaluationModel(null);
                            }
                            failedScorers.add(scorerData);
                        }
                    }
                }
                testCase.put("failed_scorers", failedScorers);
                failedCases.add(testCase);
            }
        }

        if (!failedCases.isEmpty()) {
            System.out.println("\n" + "=".repeat(80));

            int totalTests = results.size();
            int failedTests = failedCases.size();
            int passedTests = totalTests - failedTests;

            if (failedTests == 0) {
                System.out.println("\u001B[1;32m🎉 ALL TESTS PASSED! " + passedTests + "/"
                        + totalTests + " tests successful\u001B[0m");
            } else {
                System.out.println("\u001B[1;31m⚠️  TEST RESULTS: " + passedTests + "/" + totalTests
                        + " passed (" + failedTests + " failed)\u001B[0m");
            }
            System.out.println("=".repeat(80) + "\n");


            for (int i = 0; i < results.size(); i++) {
                int testNum = i + 1;
                ScoringResult result = results.get(i);
                if (result.getSuccess() != null && result.getSuccess()) {
                    System.out.println("\u001B[32m✓ Test " + testNum + ": PASSED\u001B[0m");
                } else {
                    System.out.println("\u001B[31m✗ Test " + testNum + ": FAILED\u001B[0m");
                    if (result.getScorersData() != null) {
                        List<ScorerData> scorersData = (List<ScorerData>) result.getScorersData();
                        for (ScorerData scorerData : scorersData) {
                            if (!Boolean.TRUE.equals(scorerData.getSuccess())) {
                                System.out.println("  \u001B[33mScorer: " + scorerData.getName()
                                        + "\u001B[0m");
                                System.out.println("  \u001B[31m  Score: " + scorerData.getScore()
                                        + "\u001B[0m");
                                System.out.println("  \u001B[31m  Reason: " + scorerData.getReason()
                                        + "\u001B[0m");
                                if (scorerData.getError() != null) {
                                    System.out.println("  \u001B[31m  Error: "
                                            + scorerData.getError() + "\u001B[0m");
                                }
                            }
                        }
                    }
                    System.out.println("  " + "-".repeat(40));
                }
            }

            System.out.println("\n" + "=".repeat(80));


            StringBuilder errorMsg = new StringBuilder("The following test cases failed: \n");
            for (java.util.Map<String, Object> failCase : failedCases) {
                @SuppressWarnings("unchecked")
                List<ScorerData> failedScorers = (List<ScorerData>) failCase.get("failed_scorers");
                for (ScorerData failScorer : failedScorers) {
                    errorMsg.append(String.format("\nScorer Name: %s\n", failScorer.getName()));
                    errorMsg.append(String.format("Threshold: %s\n", failScorer.getThreshold()));
                    errorMsg.append(String.format("Success: %s\n", failScorer.getSuccess()));
                    errorMsg.append(String.format("Score: %s\n", failScorer.getScore()));
                    errorMsg.append(String.format("Reason: %s\n", failScorer.getReason()));
                    errorMsg.append(String.format("Strict Mode: %s\n", failScorer.getStrictMode()));
                    errorMsg.append(String.format("Evaluation Model: %s\n",
                            failScorer.getEvaluationModel()));
                    errorMsg.append(String.format("Error: %s\n", failScorer.getError()));
                    errorMsg.append(String.format("Additional Metadata: %s\n",
                            failScorer.getAdditionalMetadata()));
                }
                errorMsg.append("-".repeat(100));
            }

            errorMsg.append(String.format("\nTEST RESULTS: %d/%d passed (%d failed)", passedTests,
                    totalTests, failedTests));

            throw new JudgmentTestError(errorMsg.toString());
        }
    }
}
