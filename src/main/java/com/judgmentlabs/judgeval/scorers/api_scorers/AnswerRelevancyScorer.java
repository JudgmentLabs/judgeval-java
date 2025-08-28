package com.judgmentlabs.judgeval.scorers.api_scorers;

import java.util.Arrays;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.data.Example.ExampleParams;
import com.judgmentlabs.judgeval.scorers.APIScorer;

public class AnswerRelevancyScorer extends APIScorer {
    public AnswerRelevancyScorer() {
        super(APIScorerType.ANSWER_RELEVANCY);
        setRequiredParams(
                Arrays.asList(
                        ExampleParams.INPUT.getValue(), ExampleParams.ACTUAL_OUTPUT.getValue()));
    }

    public static APIScorer.Builder<AnswerRelevancyScorer> builder() {
        return APIScorer.builder(AnswerRelevancyScorer.class);
    }

    public static AnswerRelevancyScorer create() {
        return new AnswerRelevancyScorer();
    }

    public static AnswerRelevancyScorer create(double threshold) {
        return builder().threshold(threshold).build();
    }
}
