package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.core.features.annotations.FeatureLoadAfter;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureLoadBefore;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;

public class FeatureMetadata {

    public final String name;
    public final String[] loadBefore;
    public final String[] loadAfter;

    public FeatureMetadata(String name, String[] loadBefore, String[] loadAfter) {
        this.name = name;
        this.loadBefore = loadBefore;
        this.loadAfter = loadAfter;
    }

    public static FeatureMetadata of(Feature feature) {
        FeatureName featureNameAnnotation = feature.getClass().getAnnotation(FeatureName.class);
        String name = featureNameAnnotation.value();

        FeatureLoadBefore loadBeforeAnnotation = feature.getClass().getAnnotation(FeatureLoadBefore.class);
        String[] loadBefore = loadBeforeAnnotation != null ? loadBeforeAnnotation.value() : null;

        FeatureLoadAfter loadAfterAnnotation = feature.getClass().getAnnotation(FeatureLoadAfter.class);
        String[] loadAfter = loadAfterAnnotation != null ? loadAfterAnnotation.value() : null;

        return new FeatureMetadata(name, loadBefore, loadAfter);
    }

}
