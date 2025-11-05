package com.judgmentlabs.judgeval.scorers.api_scorers;

import com.judgmentlabs.judgeval.data.APIScorerType;
import com.judgmentlabs.judgeval.scorers.APIScorer;

/**
 * @deprecated Use {@link com.judgmentlabs.judgeval.v1.JudgmentClient} instead.
 * 
 *             <p>
 *             Migration example:
 * 
 *             <pre>{@code
 * // Old way:
 * DerailmentScorer scorer = DerailmentScorer.create();
 * 
 * // New way:
 * JudgmentClient client = JudgmentClient.builder().build();
 * DerailmentScorer scorer = client.scorers().builtIn().derailment().build();
 * }</pre>
 */
@Deprecated
public class DerailmentScorer extends APIScorer {
    public DerailmentScorer() {
        super(APIScorerType.DERAILMENT);
    }

    public static APIScorer.Builder<DerailmentScorer> builder() {
        return APIScorer.builder(DerailmentScorer.class);
    }

    public static DerailmentScorer create() {
        return new DerailmentScorer();
    }

    public static DerailmentScorer create(double threshold) {
        return builder().threshold(threshold)
                .build();
    }
}
