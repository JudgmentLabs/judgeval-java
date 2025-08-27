package com.judgmentlabs.judgeval.scorers;

import java.util.ArrayList;
import java.util.List;

import com.judgmentlabs.judgeval.data.Example;

public abstract class ExampleScorer extends BaseScorer {

    public ExampleScorer() {
        super();
        setScoreType("Custom");
    }

    public List<String> getRequiredParams() {
        return new ArrayList<>();
    }

    public abstract double scoreExample(Example example);
}
