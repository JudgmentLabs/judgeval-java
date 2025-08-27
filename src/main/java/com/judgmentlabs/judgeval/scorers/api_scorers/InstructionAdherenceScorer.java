package com.judgmentlabs.judgeval.scorers.api_scorers;

import java.util.Arrays;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.data.Example.ExampleParams;
import com.judgmentlabs.judgeval.scorers.APIScorer;

public class InstructionAdherenceScorer extends APIScorer {
    public InstructionAdherenceScorer(double threshold) {
        super(APIScorerType.INSTRUCTION_ADHERENCE);
        setThreshold(threshold);
        setRequiredParams(Arrays.asList(
                ExampleParams.INPUT.getValue(),
                ExampleParams.ACTUAL_OUTPUT.getValue()));
        setName("Instruction Adherence");
    }
}