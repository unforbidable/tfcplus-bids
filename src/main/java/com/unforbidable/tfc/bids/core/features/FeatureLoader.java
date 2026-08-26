package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class FeatureLoader {

    private List<FeatureEntry> features;

    public List<FeatureEntry> getFeatures() {
        if (features == null) {
            List<FeatureEntry> unsorted = FeatureScanner.instances.stream()
                .filter(this::validateFeature)
                .map(this::loadFeature)
                .collect(Collectors.toList());

            features = sortFeatures(unsorted);
        }

        return features;
    }

    private boolean validateFeature(Feature feature) {
        if (feature.getClass().getAnnotation(FeatureName.class) == null) {
            Bids.LOG.warn("Skipping feature {} because it is missing {} annotation", feature.getClass(), FeatureName.class);
        }

        return true;
    }

    private FeatureEntry loadFeature(Feature feature) {
        return new FeatureEntry(feature, FeatureMetadata.of(feature));
    }

    private List<FeatureEntry> sortFeatures(List<FeatureEntry> unsorted) {
        Map<String, FeatureEntry> taskMap = new HashMap<>();
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Initialize maps
        for (FeatureEntry entry : unsorted) {
            taskMap.put(entry.metadata.name, entry);
            graph.putIfAbsent(entry.metadata.name, new ArrayList<>());
            inDegree.put(entry.metadata.name, 0);
        }

        // Build the directed graph based on rules
        for (FeatureEntry entry : unsorted) {
            String current = entry.metadata.name;

            // Rule 1: "Load After" means: dependency -> current
            if (entry.metadata.loadAfter != null) {
                for (String loadAfter : entry.metadata.loadAfter) {
                    if (taskMap.containsKey(loadAfter)) {
                        graph.get(loadAfter).add(current);
                        inDegree.put(current, inDegree.get(current) + 1);
                    }
                }
            }

            // Rule 2: "Load Before" means: current -> dependOn
            if (entry.metadata.loadBefore != null) {
                for (String leadBefore : entry.metadata.loadBefore) {
                    if (taskMap.containsKey(leadBefore)) {
                        graph.get(current).add(leadBefore);
                        inDegree.put(leadBefore, inDegree.get(leadBefore) + 1);
                    }
                }
            }
        }

        // Find all nodes with 0 incoming dependencies
        Queue<String> queue = new LinkedList<>();
        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.add(node);
            }
        }

        List<FeatureEntry> sortedList = new ArrayList<>();

        // Process nodes
        while (!queue.isEmpty()) {
            String current = queue.poll();
            sortedList.add(taskMap.get(current));

            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Cycle Check
        if (sortedList.size() != unsorted.size()) {
            throw new IllegalStateException("Circular dependency detected! Cannot sort elements.");
        }

        return sortedList;
    }

}
