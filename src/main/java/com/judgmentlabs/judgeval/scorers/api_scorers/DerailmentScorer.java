package com.judgmentlabs.judgeval.scorers.api_scorers;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.scorers.APIScorer;

public class DerailmentScorer extends APIScorer {
    public DerailmentScorer() {
        super(APIScorerType.DERAILMENT);
    }
}