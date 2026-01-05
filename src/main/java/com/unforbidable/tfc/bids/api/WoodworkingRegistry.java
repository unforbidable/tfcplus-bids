package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingActionGroup;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingMaterial;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WoodworkingRegistry {

    private final static Map<Item, List<IWoodworkingActionGroup>> tools = new HashMap<Item, List<IWoodworkingActionGroup>>();
    private final static Map<Item, IWoodworkingMaterial> materials = new HashMap<Item, IWoodworkingMaterial>();

    public static void registerTool(Item item, final IWoodworkingActionGroup actions) {
        if (!tools.containsKey(item)) {
            tools.put(item, new ArrayList<IWoodworkingActionGroup>());
        }

        tools.get(item).add(actions);
    }

    public static List<IWoodworkingActionGroup> getToolActions(Item item) {
        return tools.get(item);
    }

    public static void registerMaterial(Item item, IWoodworkingMaterial material) {
        materials.put(item, material);
    }

    public static IWoodworkingMaterial getMaterial(Item item) {
        return materials.get(item);
    }

}
