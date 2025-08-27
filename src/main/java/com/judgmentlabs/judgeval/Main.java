package com.judgmentlabs.judgeval;

import java.util.HashMap;
import java.util.Map;

import com.judgmentlabs.judgeval.scorers.api_scorers.PromptScorer;
import com.judgmentlabs.judgeval.utils.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.info("Starting PromptScorer test...");

        try {
            // Test creating a new PromptScorer
            Logger.info("Creating a new PromptScorer...");
            Map<String, Double> options = new HashMap<>();
            options.put("yes", 1.0);
            options.put("no", 0.0);

            PromptScorer scorer =
                    PromptScorer.create(
                            "test-scorer-" + System.currentTimeMillis(),
                            "Did the chatbot answer the user's question in a kind way?",
                            0.5,
                            options);

            Logger.info("Created scorer: " + scorer.toString());

            // Test getting the scorer
            Logger.info("Fetching the scorer...");
            PromptScorer fetchedScorer = PromptScorer.get(scorer.getScorerName());
            Logger.info("Fetched scorer: " + fetchedScorer.toString());

            // Test updating the scorer
            Logger.info("Updating the scorer threshold...");
            fetchedScorer.setThreshold(0.7);
            Logger.info("Updated threshold to 0.7");

            // Test appending to prompt
            Logger.info("Appending to prompt...");
            fetchedScorer.appendToPrompt(" Please be specific in your evaluation.");
            Logger.info("Appended to prompt");

            // Test getting config
            Logger.info("Getting scorer config...");
            Map<String, Object> config = fetchedScorer.getConfig();
            Logger.info("Config: " + config);

            Logger.info("PromptScorer test completed successfully!");

        } catch (Exception e) {
            Logger.error("Error during PromptScorer test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
