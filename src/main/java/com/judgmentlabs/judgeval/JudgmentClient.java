package com.judgmentlabs.judgeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import com.judgmentlabs.judgeval.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.api.models.EvalResultsFetch;
import com.judgmentlabs.judgeval.data.EvaluationRun;
import com.judgmentlabs.judgeval.data.Example;
import com.judgmentlabs.judgeval.data.ScorerData;
import com.judgmentlabs.judgeval.data.ScoringResult;
import com.judgmentlabs.judgeval.exceptions.JudgmentRuntimeError;
import com.judgmentlabs.judgeval.exceptions.JudgmentTestError;
import com.judgmentlabs.judgeval.scorers.APIScorer;
import com.judgmentlabs.judgeval.scorers.BaseScorer;
import com.judgmentlabs.judgeval.scorers.ExampleScorer;
import com.judgmentlabs.judgeval.scorers.api_scorers.PromptScorer;
import com.judgmentlabs.judgeval.utils.Logger;

/**
 * Main client for running evaluations with Judgment Labs.
 *
 * <p>The JudgmentClient provides functionality to:
 *
 * <ul>
 *   <li>Run evaluations with multiple examples and scorers
 *   <li>Validate inputs and scorer configurations
 *   <li>Poll for evaluation results
 *   <li>Assert test results for automated testing
 * </ul>
 *
 * <h3>Basic Usage</h3>
 *
 * <pre>{@code
 * JudgmentClient client = new JudgmentClient(apiKey, organizationId);
 *
 * List<Example> examples = Arrays.asList(
 *         Example.builder()
 *                 .input("What is 2+2?")
 *                 .actualOutput("4")
 *                 .expectedOutput("4")
 *                 .build());
 *
 * List<BaseScorer> scorers = Arrays.asList(
 *         AnswerCorrectnessScorer.create(0.8));
 *
 * List<ScoringResult> results = client.runEvaluation(
 *         examples, scorers, "my-project", "test-run", "gpt-4", false);
 * }</pre>
 *
 * <h3>Test Mode</h3>
 *
 * <pre>{@code
 * // Enable test assertions
 * List<ScoringResult> results = client.runEvaluation(
 *         examples, scorers, "my-project", "test-run", "gpt-4", true);
 * // This will throw JudgmentTestError if any tests fail
 * }</pre>
 *
 * @see Example
 * @see BaseScorer
 * @see ScoringResult
 * @see JudgmentTestError
 * @since 1.0.0
 */
public class JudgmentClient {
    private final String apiKey;
    private final String organizationId;
    private final JudgmentSyncClient client;

    /**
     * Creates a new JudgmentClient with the specified API credentials.
     *
     * @param apiKey the API key for authentication (must not be null)
     * @param organizationId the organization ID (must not be null)
     * @throws NullPointerException if apiKey or organizationId is null
     */
    public JudgmentClient(String apiKey, String organizationId) {
        this.apiKey = Objects.requireNonNull(apiKey, "API key cannot be null");
        this.organizationId =
                Objects.requireNonNull(organizationId, "Organization ID cannot be null");
        this.client =
                new JudgmentSyncClient(Env.JUDGMENT_API_URL, this.apiKey, this.organizationId);
    }

    /**
     * Runs an evaluation with the specified examples and scorers.
     *
     * <p>This method submits an evaluation request to Judgment Labs and polls for results until
     * completion. The evaluation can be run in test mode to automatically assert results.
     *
     * <p>The method performs the following validations:
     *
     * <ul>
     *   <li>All examples must have the same field keys
     *   <li>Examples must contain required parameters for all scorers
     *   <li>Cannot mix local and Judgment API scorers
     *   <li>All input parameters must be valid
     * </ul>
     *
     * @param examples the examples to evaluate (must not be null or empty)
     * @param scorers the scorers to use for evaluation (must not be null or empty)
     * @param projectName the project name (must not be null or empty)
     * @param evalRunName the evaluation run name (must not be null or empty)
     * @param model the model used for generation (can be null, will use default)
     * @param assertTest whether to assert test results and throw exceptions on failures
     * @return a list of scoring results for each example
     * @throws IllegalArgumentException if inputs are invalid
     * @throws JudgmentRuntimeError if evaluation fails
     * @throws JudgmentTestError if assertTest is true and any tests fail
     */
    public List<ScoringResult> runEvaluation(
            List<Example> examples,
            List<BaseScorer> scorers,
            String projectName,
            String evalRunName,
            String model,
            boolean assertTest) {
        validateInputs(examples, scorers, projectName, evalRunName);

        if (!examples.isEmpty()) {
            Set<String> keys = examples.get(0).getFields().keySet();
            for (Example example : examples) {
                Set<String> currentKeys = example.getFields().keySet();
                if (!currentKeys.equals(keys)) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "All examples must have the same keys: %s != %s",
                                    currentKeys, keys));
                }
            }
        }

        try {
            validateScorerTypes(scorers);

            List<Object> convertedScorers = new ArrayList<>();
            for (BaseScorer scorer : scorers) {
                convertedScorers.add(scorer.toTransport());
            }
            Logger.info("Submitting scorers payload count=" + convertedScorers.size());

            EvaluationRun eval =
                    EvaluationRun.builder(projectName, evalRunName)
                            .examples(examples)
                            .scorers(convertedScorers)
                            .model(model != null ? model : Env.JUDGMENT_DEFAULT_GPT_MODEL)
                            .organizationId(organizationId)
                            .build();

            List<ScoringResult> results = runEval(eval);

            if (assertTest) {
                assertTestResults(results);
            }

            return results;

        } catch (JudgmentTestError e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    String.format(
                            "Please check your EvaluationRun object, one or more fields are invalid: %s",
                            e.getMessage()));
        } catch (Exception e) {
            throw new JudgmentRuntimeError(
                    String.format(
                            "An unexpected error occurred during evaluation: %s", e.getMessage()),
                    e);
        }
    }

    /**
     * Runs an evaluation with default model and no test assertions.
     *
     * @param examples the examples to evaluate
     * @param scorers the scorers to use for evaluation
     * @param projectName the project name
     * @param evalRunName the evaluation run name
     * @return a list of scoring results
     */
    public List<ScoringResult> runEvaluation(
            List<Example> examples,
            List<BaseScorer> scorers,
            String projectName,
            String evalRunName) {
        return runEvaluation(examples, scorers, projectName, evalRunName, null, false);
    }

    /**
     * Runs an evaluation with a single example and scorer.
     *
     * @param example the example to evaluate
     * @param scorer the scorer to use for evaluation
     * @param projectName the project name
     * @param evalRunName the evaluation run name
     * @param model the model used for generation (can be null)
     * @return a list of scoring results
     */
    public List<ScoringResult> runEvaluation(
            Example example,
            BaseScorer scorer,
            String projectName,
            String evalRunName,
            String model) {
        return runEvaluation(
                List.of(example), List.of(scorer), projectName, evalRunName, model, false);
    }

    /**
     * Runs an evaluation with a single example and scorer using default model.
     *
     * @param example the example to evaluate
     * @param scorer the scorer to use for evaluation
     * @param projectName the project name
     * @param evalRunName the evaluation run name
     * @return a list of scoring results
     */
    public List<ScoringResult> runEvaluation(
            Example example, BaseScorer scorer, String projectName, String evalRunName) {
        return runEvaluation(example, scorer, projectName, evalRunName, null);
    }

    /**
     * Validates all input parameters for the evaluation.
     *
     * @param examples the examples to validate
     * @param scorers the scorers to validate
     * @param projectName the project name to validate
     * @param evalRunName the evaluation run name to validate
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validateInputs(
            List<Example> examples,
            List<BaseScorer> scorers,
            String projectName,
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

    /**
     * Checks that all examples contain the required parameters for their scorers.
     *
     * <p>This method validates that each example has all the required parameters for the scorers
     * being used. If any parameters are missing, it logs warnings and optionally prompts the user
     * to continue.
     *
     * @param examples the examples to check
     * @param scorers the scorers to validate against
     */
    private void checkExamples(List<Example> examples, List<BaseScorer> scorers) {
        boolean promptUser = false;

        for (BaseScorer scorer : scorers) {
            if (scorer instanceof ExampleScorer) {
                ExampleScorer exampleScorer = (ExampleScorer) scorer;
                List<String> requiredParams = exampleScorer.getRequiredParams();

                for (Example example : examples) {
                    List<String> missingParams = new ArrayList<>();
                    if (requiredParams != null) {
                        for (String param : requiredParams) {
                            if (example.getAdditionalProperties().get(param) == null) {
                                missingParams.add(param);
                            }
                        }
                    }
                    if (!missingParams.isEmpty()) {
                        Logger.warning(
                                "Example is missing required parameters for scorer "
                                        + exampleScorer.getScoreType());
                        Logger.warning("Missing parameters: " + String.join(", ", missingParams));
                        Logger.warning("Example: " + example.getAdditionalProperties());
                        Logger.warning("-".repeat(40));
                        promptUser = true;
                    }
                }
            }
        }

        if (promptUser) {
            System.out.print("Do you want to continue? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            try {
                String userInput = scanner.nextLine();
                if (!"y".equalsIgnoreCase(userInput)) {
                    System.exit(0);
                } else {
                    Logger.info("Continuing...");
                }
            } catch (Exception e) {
                Logger.info("Continuing...");
            }
        }
    }

    /**
     * Validates that scorers are compatible with each other.
     *
     * <p>Currently, the system does not support mixing local scorers (ExampleScorer) with Judgment
     * API scorers (APIScorer, PromptScorer) in the same evaluation.
     *
     * @param scorers the scorers to validate
     * @throws IllegalArgumentException if incompatible scorers are mixed
     */
    private void validateScorerTypes(List<BaseScorer> scorers) {
        int customScorers = 0;
        int judgmentScorers = 0;

        for (BaseScorer scorer : scorers) {
            if (scorer instanceof ExampleScorer) {
                customScorers++;
            } else if (scorer instanceof APIScorer || scorer instanceof PromptScorer) {
                judgmentScorers++;
            }
        }

        if (customScorers > 0 && judgmentScorers > 0) {
            throw new IllegalArgumentException(
                    "We currently do not support running both local and Judgment API scorers at the same time. "
                            + "Please run your evaluation with either local scorers or Judgment API scorers, but not both.");
        }
    }

    /**
     * Submits an evaluation run and polls for results until completion.
     *
     * @param eval the evaluation run to execute
     * @return a list of scoring results
     * @throws JudgmentRuntimeError if evaluation fails
     */
    private List<ScoringResult> runEval(EvaluationRun eval) {
        try {
            Logger.info("Submitting evaluation to API...");
            Object response = client.addToRunEvalQueue(eval);
            Logger.debug("API Response: " + response);

            if (response instanceof Map) {
                Map<String, Object> responseMap = (Map<String, Object>) response;
                if (!Boolean.TRUE.equals(responseMap.get("success"))) {
                    String errorMessage = (String) responseMap.get("error");
                    throw new JudgmentRuntimeError(errorMessage);
                }
            }

            return pollEvaluationUntilComplete(eval, client);
        } catch (Exception e) {
            Logger.error(
                    "Exception details: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            throw new JudgmentRuntimeError("Failed to run evaluation", e);
        }
    }

    /**
     * Polls for evaluation results until completion with default settings.
     *
     * @param eval the evaluation run to poll
     * @param client the API client to use
     * @return a list of scoring results
     * @throws JudgmentRuntimeError if polling fails or times out
     */
    private List<ScoringResult> pollEvaluationUntilComplete(
            EvaluationRun eval, JudgmentSyncClient client) {
        return pollEvaluationUntilComplete(eval, client, 2.0, 5, 60);
    }

    /**
     * Polls for evaluation results until completion with custom settings.
     *
     * <p>This method continuously polls the API for evaluation status and results. It uses
     * exponential backoff for retries and has configurable timeouts.
     *
     * @param eval the evaluation run to poll
     * @param client the API client to use
     * @param pollIntervalSeconds the interval between polls in seconds
     * @param maxFailures the maximum number of consecutive failures before giving up
     * @param maxPollCount the maximum number of polls before timing out
     * @return a list of scoring results
     * @throws JudgmentRuntimeError if polling fails or times out
     */
    private List<ScoringResult> pollEvaluationUntilComplete(
            EvaluationRun eval,
            JudgmentSyncClient client,
            double pollIntervalSeconds,
            int maxFailures,
            int maxPollCount) {
        int pollCount = 0;
        int exceptionCount = 0;
        long startTime = System.currentTimeMillis();

        while (pollCount < maxPollCount) {
            pollCount++;
            try {
                long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                Logger.info("Running evaluation... (" + elapsed + " sec)");

                Object statusResponse =
                        client.getEvaluationStatus(
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

                Object resultsResponse = client.fetchExperimentRun(fetchRequest);

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

    /**
     * Parses raw evaluation results into structured ScoringResult objects.
     *
     * <p>This method converts the raw API response data into properly structured ScoringResult
     * objects that contain the evaluation results for each example.
     *
     * @param examplesData the raw examples data from the API response
     * @return a list of parsed ScoringResult objects
     */
    private List<ScoringResult> parseScoringResults(List<Map<String, Object>> examplesData) {
        List<ScoringResult> results = new ArrayList<>();

        for (Map<String, Object> exampleData : examplesData) {
            List<ScorerData> scorersData = parseScorerDataList(exampleData);
            boolean success =
                    scorersData.stream()
                            .allMatch(scorerData -> Boolean.TRUE.equals(scorerData.getSuccess()));

            Example example = Example.builder().name(parseString(exampleData.get("name"))).build();
            example.setExampleId(parseString(exampleData.get("example_id")));
            example.setCreatedAt(parseString(exampleData.get("created_at")));

            ScoringResult result =
                    ScoringResult.builder()
                            .success(success)
                            .scorersData(scorersData)
                            .dataObject(example)
                            .build();

            results.add(result);
        }

        return results;
    }

    /**
     * Parses the scorer data list from the API response.
     *
     * @param exampleData the example data containing scorer information
     * @return a list of parsed ScorerData objects
     */
    private List<ScorerData> parseScorerDataList(Map<String, Object> exampleData) {
        List<ScorerData> scorersData = new ArrayList<>();
        Object scorerDataObj = exampleData.get("scorer_data");

        if (scorerDataObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> scorerDataList = (List<Map<String, Object>>) scorerDataObj;

            for (Map<String, Object> rawScorerData : scorerDataList) {
                ScorerData scorerData =
                        ScorerData.builder()
                                .name(parseString(rawScorerData.get("name")))
                                .score(parseDouble(rawScorerData.get("score")))
                                .success(parseBoolean(rawScorerData.get("success")))
                                .reason(parseString(rawScorerData.get("reason")))
                                .threshold(parseDouble(rawScorerData.get("threshold")))
                                .strictMode(parseBoolean(rawScorerData.get("strict_mode")))
                                .evaluationModel(parseString(rawScorerData.get("evaluation_model")))
                                .error(parseString(rawScorerData.get("error")))
                                .additionalMetadata(
                                        parseMetadata(rawScorerData.get("additional_metadata")))
                                .build();

                scorersData.add(scorerData);
            }
        }

        return scorersData;
    }

    /**
     * Safely parses a string value from an object.
     *
     * @param value the value to parse
     * @return the string value, or null if not a string
     */
    private String parseString(Object value) {
        return value instanceof String ? (String) value : null;
    }

    /**
     * Safely parses a double value from an object.
     *
     * @param value the value to parse
     * @return the double value, or null if not a number
     */
    private Double parseDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }

    /**
     * Safely parses a boolean value from an object.
     *
     * @param value the value to parse
     * @return the boolean value, or null if not a boolean
     */
    private Boolean parseBoolean(Object value) {
        return value instanceof Boolean ? (Boolean) value : null;
    }

    /**
     * Safely parses metadata from an object.
     *
     * @param value the value to parse
     * @return the metadata map, or null if not a map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseMetadata(Object value) {
        return value instanceof Map ? (Map<String, Object>) value : null;
    }

    /**
     * Asserts test results and throws exceptions for failures.
     *
     * <p>This method analyzes the evaluation results and throws a JudgmentTestError if any tests
     * failed. It provides detailed logging of test results and comprehensive error messages for
     * debugging.
     *
     * <p>The method logs:
     *
     * <ul>
     *   <li>Overall test statistics (passed/failed counts)
     *   <li>Individual test results with detailed scorer information
     *   <li>Specific failure reasons and scores for each failed test
     * </ul>
     *
     * @param results the evaluation results to assert
     * @throws JudgmentTestError if any tests failed, with detailed error information
     */
    private void assertTestResults(List<ScoringResult> results) {
        if (results == null || results.isEmpty()) {
            throw new JudgmentTestError("No results to assert");
        }

        List<Map<String, Object>> failedCases = new ArrayList<>();

        for (ScoringResult result : results) {
            if (result.getSuccess() == null || !result.getSuccess()) {
                Map<String, Object> testCase = new HashMap<>();
                List<ScorerData> failedScorers = new ArrayList<>();

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
            Logger.info("=".repeat(80));

            int totalTests = results.size();
            int failedTests = failedCases.size();
            int passedTests = totalTests - failedTests;

            if (failedTests == 0) {
                Logger.info(
                        "ALL TESTS PASSED! "
                                + passedTests
                                + "/"
                                + totalTests
                                + " tests successful");
            } else {
                Logger.error(
                        "TEST RESULTS: "
                                + passedTests
                                + "/"
                                + totalTests
                                + " passed ("
                                + failedTests
                                + " failed)");
            }
            Logger.info("=".repeat(80));

            for (int i = 0; i < results.size(); i++) {
                int testNum = i + 1;
                ScoringResult result = results.get(i);
                if (result.getSuccess() != null && result.getSuccess()) {
                    Logger.info("Test " + testNum + ": PASSED");
                } else {
                    Logger.error("Test " + testNum + ": FAILED");
                    if (result.getScorersData() != null) {
                        List<ScorerData> scorersData = (List<ScorerData>) result.getScorersData();
                        for (ScorerData scorerData : scorersData) {
                            if (!Boolean.TRUE.equals(scorerData.getSuccess())) {
                                Logger.warning("  Scorer: " + scorerData.getName());
                                Logger.error("    Score: " + scorerData.getScore());
                                Logger.error("    Reason: " + scorerData.getReason());
                                if (scorerData.getError() != null) {
                                    Logger.error("    Error: " + scorerData.getError());
                                }
                            }
                        }
                    }
                    Logger.info("  " + "-".repeat(40));
                }
            }

            Logger.info("=".repeat(80));

            StringBuilder errorMsg = new StringBuilder("The following test cases failed: \n");
            for (Map<String, Object> failCase : failedCases) {
                @SuppressWarnings("unchecked")
                List<ScorerData> failedScorers = (List<ScorerData>) failCase.get("failed_scorers");
                for (ScorerData failScorer : failedScorers) {
                    errorMsg.append(String.format("\nScorer Name: %s\n", failScorer.getName()));
                    errorMsg.append(String.format("Threshold: %s\n", failScorer.getThreshold()));
                    errorMsg.append(String.format("Success: %s\n", failScorer.getSuccess()));
                    errorMsg.append(String.format("Score: %s\n", failScorer.getScore()));
                    errorMsg.append(String.format("Reason: %s\n", failScorer.getReason()));
                    errorMsg.append(String.format("Strict Mode: %s\n", failScorer.getStrictMode()));
                    errorMsg.append(
                            String.format(
                                    "Evaluation Model: %s\n", failScorer.getEvaluationModel()));
                    errorMsg.append(String.format("Error: %s\n", failScorer.getError()));
                    errorMsg.append(
                            String.format(
                                    "Additional Metadata: %s\n",
                                    failScorer.getAdditionalMetadata()));
                }
                errorMsg.append("-".repeat(100));
            }

            errorMsg.append(
                    String.format(
                            "\nTEST RESULTS: %d/%d passed (%d failed)",
                            passedTests, totalTests, failedTests));

            throw new JudgmentTestError(errorMsg.toString());
        }
    }
}
