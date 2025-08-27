package com.judgmentlabs.judgeval.scorers.api_scorers;

import java.util.Arrays;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.data.Example.ExampleParams;
import com.judgmentlabs.judgeval.scorers.APIScorer;

public class FaithfulnessScorer extends APIScorer {
    public FaithfulnessScorer() {
        super(APIScorerType.FAITHFULNESS);
        setRequiredParams(Arrays.asList(
                ExampleParams.INPUT.getValue(),
                ExampleParams.ACTUAL_OUTPUT.getValue(),
                ExampleParams.RETRIEVAL_CONTEXT.getValue()));
    }
}