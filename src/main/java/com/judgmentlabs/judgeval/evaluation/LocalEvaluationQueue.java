package com.judgmentlabs.judgeval.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import com.judgmentlabs.judgeval.Env;
import com.judgmentlabs.judgeval.api.JudgmentSyncClient;
import com.judgmentlabs.judgeval.api.models.EvalResults;
import com.judgmentlabs.judgeval.api.models.ScoringResult;
import com.judgmentlabs.judgeval.data.EvaluationRun;
import com.judgmentlabs.judgeval.utils.Logger;

public class LocalEvaluationQueue {
    private final BlockingQueue<EvaluationRun> queue;
    private final int maxConcurrent;
    private final int numWorkers;
    private final JudgmentSyncClient apiClient;
    private final List<Thread> workerThreads;
    private volatile boolean shutdown = false;
    private final AtomicInteger unfinishedTasks;

    public LocalEvaluationQueue() {
        this(4, 4);
    }

    public LocalEvaluationQueue(int maxConcurrent) {
        this(maxConcurrent, 4);
    }

    public LocalEvaluationQueue(int maxConcurrent, int numWorkers) {
        if (numWorkers <= 0) {
            throw new IllegalArgumentException("numWorkers must be a positive integer.");
        }
        this.queue = new LinkedBlockingQueue<>();
        this.maxConcurrent = maxConcurrent;
        this.numWorkers = numWorkers;
        this.apiClient =
                new JudgmentSyncClient(
                        Env.JUDGMENT_API_URL, Env.JUDGMENT_API_KEY, Env.JUDGMENT_ORG_ID);
        this.workerThreads = new ArrayList<>();
        this.unfinishedTasks = new AtomicInteger(0);
    }

    public void enqueue(EvaluationRun evaluationRun) {
        if (shutdown) {
            throw new IllegalStateException("Queue is shutdown");
        }
        queue.offer(evaluationRun);
        unfinishedTasks.incrementAndGet();
    }

    public void runAll() {
        runAll(null);
    }

    public void runAll(BiConsumer<EvaluationRun, List<ScoringResult>> callback) {
        while (!queue.isEmpty()) {
            try {
                EvaluationRun run = queue.poll(1, TimeUnit.SECONDS);
                if (run == null) continue;

                List<ScoringResult> results = processRun(run);
                if (callback != null) {
                    callback.accept(run, results);
                }
                unfinishedTasks.decrementAndGet();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                Logger.error("Error processing evaluation run: " + e.getMessage());
                unfinishedTasks.decrementAndGet();
            }
        }
    }

    public List<Thread> startWorkers() {
        if (!workerThreads.isEmpty()) {
            return new ArrayList<>(workerThreads);
        }

        for (int i = 0; i < numWorkers; i++) {
            final int workerId = i;
            Thread worker =
                    new Thread(
                            () -> {
                                while (!shutdown) {
                                    try {
                                        EvaluationRun run = queue.poll(1, TimeUnit.SECONDS);
                                        if (run == null) continue;

                                        try {
                                            List<ScoringResult> results = processRun(run);

                                            EvalResults evalResults = new EvalResults();
                                            evalResults.setResults(results);
                                            evalResults.setRun(run);

                                            apiClient.logEvalResults(evalResults);
                                            Logger.info(
                                                    "Worker "
                                                            + workerId
                                                            + " processed evaluation run: "
                                                            + run.getEvalName());
                                        } catch (Exception e) {
                                            Logger.error(
                                                    "Worker "
                                                            + workerId
                                                            + " error processing "
                                                            + run.getEvalName()
                                                            + ": "
                                                            + e.getMessage());
                                        } finally {
                                            unfinishedTasks.decrementAndGet();
                                        }
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        break;
                                    }
                                }
                            });
            worker.setName("LocalEvaluationQueue-Worker-" + workerId);
            worker.setDaemon(true);
            worker.start();
            workerThreads.add(worker);
        }

        return new ArrayList<>(workerThreads);
    }

    public Thread startWorker(BiConsumer<EvaluationRun, List<ScoringResult>> callback) {
        List<Thread> threads = startWorkers();
        return threads.isEmpty() ? null : threads.get(0);
    }

    public boolean waitForCompletion(Long timeout) {
        try {
            if (timeout == null) {
                while (!queue.isEmpty() || unfinishedTasks.get() > 0) {
                    Thread.sleep(100);
                }
                return true;
            } else {
                long startTime = System.currentTimeMillis();
                while (!queue.isEmpty() || unfinishedTasks.get() > 0) {
                    if (System.currentTimeMillis() - startTime > timeout) {
                        return false;
                    }
                    Thread.sleep(100);
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void stopWorkers() {
        if (workerThreads.isEmpty()) {
            return;
        }

        shutdown = true;

        for (Thread thread : workerThreads) {
            if (thread.isAlive()) {
                thread.interrupt();
                try {
                    thread.join(5000);
                    if (thread.isAlive()) {
                        Logger.warning(
                                "Worker thread "
                                        + thread.getName()
                                        + " did not shut down gracefully");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        workerThreads.clear();
        shutdown = false;
    }

    private List<ScoringResult> processRun(EvaluationRun evaluationRun) {
        Logger.info("Processing evaluation run: " + evaluationRun.getEvalName());

        if (evaluationRun.getCustomScorers() == null
                || evaluationRun.getCustomScorers().isEmpty()) {
            throw new IllegalArgumentException(
                    "LocalEvaluationQueue only supports runs with local scorers (BaseScorer). "
                            + "Found only APIScorerConfig instances.");
        }

        return executeScoring(
                evaluationRun.getExamples(),
                evaluationRun.getCustomScorers(),
                evaluationRun.getModel(),
                0,
                maxConcurrent / numWorkers,
                false);
    }

    private List<ScoringResult> executeScoring(
            List<com.judgmentlabs.judgeval.api.models.Example> examples,
            List<com.judgmentlabs.judgeval.api.models.BaseScorer> scorers,
            String model,
            int throttleValue,
            int maxConcurrent,
            boolean showProgress) {

        List<ScoringResult> results = new ArrayList<>();

        for (com.judgmentlabs.judgeval.api.models.Example example : examples) {
            for (com.judgmentlabs.judgeval.api.models.BaseScorer scorer : scorers) {
                try {
                    ScoringResult result = executeSingleScoring(scorer, example, model);
                    results.add(result);
                } catch (Exception e) {
                    Logger.error(
                            "Error scoring example with "
                                    + scorer.getClass().getSimpleName()
                                    + ": "
                                    + e.getMessage());
                    ScoringResult errorResult = new ScoringResult();
                    errorResult.setSuccess(false);
                    errorResult.setName(scorer.getName());
                    errorResult.setAdditionalProperty("error", e.getMessage());
                    results.add(errorResult);
                }
            }
        }

        return results;
    }

    private ScoringResult executeSingleScoring(
            com.judgmentlabs.judgeval.api.models.BaseScorer scorer,
            com.judgmentlabs.judgeval.api.models.Example example,
            String model) {

        ScoringResult result = new ScoringResult();
        result.setName(scorer.getName());

        try {
            Double threshold = scorer.getThreshold();
            if (threshold == null) {
                threshold = 0.5;
            }

            result.setSuccess(true);
            result.setAdditionalProperty("score", 0.5);
            result.setAdditionalProperty("exampleId", example.getExampleId());
            result.setAdditionalProperty("threshold", threshold);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setAdditionalProperty("error", e.getMessage());
        }

        return result;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public int getQueueSize() {
        return queue.size();
    }

    public int getUnfinishedTasks() {
        return unfinishedTasks.get();
    }
}
