package com.judgmentlabs.judgeval.v1.scorers.built_in;

public final class BuiltInScorersFactory {
    public BuiltInScorersFactory() {
    }

    public AnswerCorrectnessScorer.Builder answerCorrectness() {
        return AnswerCorrectnessScorer.builder();
    }

    public AnswerRelevancyScorer.Builder answerRelevancy() {
        return AnswerRelevancyScorer.builder();
    }

    public FaithfulnessScorer.Builder faithfulness() {
        return FaithfulnessScorer.builder();
    }

    public InstructionAdherenceScorer.Builder instructionAdherence() {
        return InstructionAdherenceScorer.builder();
    }

    public DerailmentScorer.Builder derailment() {
        return DerailmentScorer.builder();
    }
}
