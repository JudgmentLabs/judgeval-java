package com.judgmentlabs.judgeval.scorers.api_scorers;

import java.util.Arrays;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.scorers.APIScorer;

public class InstructionAdherenceScorer extends APIScorer {
    public InstructionAdherenceScorer() {
        super(APIScorerType.INSTRUCTION_ADHERENCE);
        setRequiredParams(Arrays.asList("input", "actual_output"));
        setName("Instruction Adherence");
    }

    public static APIScorer.Builder<InstructionAdherenceScorer> builder() {
        return APIScorer.builder(InstructionAdherenceScorer.class);
    }

    public static InstructionAdherenceScorer create() {
        return new InstructionAdherenceScorer();
    }

    public static InstructionAdherenceScorer create(double threshold) {
        return builder().threshold(threshold)
                .build();
    }
}
