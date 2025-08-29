package com.judgmentlabs.judgeval.scorers.api_scorers;

import java.util.Arrays;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.data.Example.ExampleParams;
import com.judgmentlabs.judgeval.scorers.APIScorer;

public class AnswerCorrectnessScorer extends APIScorer {
    public AnswerCorrectnessScorer() {
        super(APIScorerType.ANSWER_CORRECTNESS);
        setRequiredParams(
                Arrays.asList(
                        ExampleParams.INPUT.getValue(),
                        ExampleParams.ACTUAL_OUTPUT.getValue(),
                        ExampleParams.EXPECTED_OUTPUT.getValue()));
    }

    public static APIScorer.Builder<AnswerCorrectnessScorer> builder() {
        return APIScorer.builder(AnswerCorrectnessScorer.class);
    }

    public static AnswerCorrectnessScorer create() {
        return new AnswerCorrectnessScorer();
    }

    public static AnswerCorrectnessScorer create(double threshold) {
        return builder().threshold(threshold).build();
    }
}
