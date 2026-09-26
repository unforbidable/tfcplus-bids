package com.unforbidable.tfc.bids.core.features.registry;

import com.unforbidable.tfc.bids.Bids;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import java.util.Locale;

public class FeatureRegistryLookup {

    private final FeatureRegistry registry;

    public FeatureRegistryLookup(FeatureRegistry registry) {
        this.registry = registry;
    }

    public Block block(String name) {
        BlockRegistryEntry block = registry.blocks.get(name);
        if (block != null) {
            return block.instance;
        } else {
            Bids.LOG.warn("Block '{}' not (yet) initialized, ensure lambda is used within block/item setup", name);

            return null;
        }
    }

    public Item item(String name) {
        ItemRegistryEntry item = registry.items.get(name);
        if (item != null) {
            return item.instance;
        } else {
            Bids.LOG.warn("Item '{}' not (yet) initialized, ensure lambda is used within block/item setup", name);

            return null;
        }
    }

    public Fluid fluid(String name) {
        Fluid fluid = FluidRegistry.getFluid(name.toLowerCase(Locale.ENGLISH));
        if (fluid != null) {
            return fluid;
        } else {
            Bids.LOG.warn("Fluid '{}' not (yet) initialized, ensure lambda is used within block/item setup", name);

            return null;
        }
    }

}
