package com.unforbidable.tfc.bids.core.features;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import cpw.mods.fml.common.discovery.ASMDataTable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FeatureScanner {

    static List<Feature> instances;

    public static void discoverFeatures(ASMDataTable asmData) {
        Set<ASMDataTable.ASMData> data = asmData.getAll(FeatureName.class.getCanonicalName());

        instances = new ArrayList<>();
        for (ASMDataTable.ASMData d : data) {
            String className = d.getClassName();

            Bids.LOG.info("Discovered feature class {}", className);
            Feature instance = createFeatureInstance(className);
            if (instance != null) {
                instances.add(instance);
            }
        }

        Bids.LOG.info("Discovered {} features", instances.size());
    }

    private static Feature createFeatureInstance(String className) {
        try {
            Class<?> type = Class.forName(className);
            if (Feature.class.isAssignableFrom(type)) {
                return (Feature) type.getConstructor().newInstance();
            } else {
                Bids.LOG.warn("Feature annotated type {} does not extend {}", type.getCanonicalName(), Feature.class.getCanonicalName());

                return null;
            }
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | NoSuchMethodException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
