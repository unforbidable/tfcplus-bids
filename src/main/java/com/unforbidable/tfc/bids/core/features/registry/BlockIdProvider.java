package com.unforbidable.tfc.bids.core.features.registry;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.api.annotations.BlockId;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BlockIdProvider {

    private Map<String, Integer> map;

    public int getBlockId(String name) {
        if (map == null) {
            map = createMap();
        }

        if (map.containsKey(name)) {
            return map.get(name);
        } else {
            Bids.LOG.warn("Unable to find ID for block '{}'", name);

            return Integer.MAX_VALUE;
        }
    }

    private static Map<String, Integer> createMap() {
        Map<String, Integer> map = new HashMap<>();

        for (Field field : BlockNames.class.getFields()) {
            String name = getFieldValue(field);
            Integer id = getFieldBlockIdAnnotationValue(field);
            if (name != null && id != null) {
                map.put(name, id);
            } else {
                Bids.LOG.warn("Unable to reflect field '{}' in {}", field.getName(), BlockNames.class);
            }
        }

        return map;
    }

    private static String getFieldValue(Field field) {
        try {
            return (String)field.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private static Integer getFieldBlockIdAnnotationValue(Field field) {
        BlockId blockId = field.getAnnotation(BlockId.class);
        if (blockId != null) {
            return blockId.value();
        } else {
            return null;
        }
    }

}
