package com.judgmentlabs.judgeval.scorers.api_scorers;

import java.util.Arrays;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.data.Example.ExampleParams;
import com.judgmentlabs.judgeval.scorers.APIScorer;

public class AnswerRelevancyScorer extends APIScorer {
    public AnswerRelevancyScorer() {
        super(APIScorerType.ANSWER_RELEVANCY);
        setRequiredParams(Arrays.asList(
                ExampleParams.INPUT.getValue(),
                ExampleParams.ACTUAL_OUTPUT.getValue()));
    }
}