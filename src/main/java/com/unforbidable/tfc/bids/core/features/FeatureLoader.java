package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.features.Features;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureLoader {

    private List<FeatureEntry> features;

    public List<FeatureEntry> getFeatures() {
        if (features == null) {
            features = Arrays.stream(Features.features)
                .filter(this::validateFeature)
                .map(this::loadFeature)
                .sorted(this::sortFeatures)
                .collect(Collectors.toList());
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

    private int sortFeatures(FeatureEntry f1, FeatureEntry f2) {
        return 0;
    }

}
