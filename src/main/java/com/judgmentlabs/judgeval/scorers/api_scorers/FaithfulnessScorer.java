package com.judgmentlabs.judgeval.scorers.api_scorers;

import java.util.Arrays;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.scorers.APIScorer;

public class FaithfulnessScorer extends APIScorer {
    public FaithfulnessScorer() {
        super(APIScorerType.FAITHFULNESS);
        setRequiredParams(Arrays.asList("input", "actual_output", "retrieval_context"));
    }

    public static APIScorer.Builder<FaithfulnessScorer> builder() {
        return APIScorer.builder(FaithfulnessScorer.class);
    }

    public static FaithfulnessScorer create() {
        return new FaithfulnessScorer();
    }

    public static FaithfulnessScorer create(double threshold) {
        return builder().threshold(threshold).build();
    }
}
