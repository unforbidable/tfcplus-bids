package com.unforbidable.tfc.bids.core.features.init.block;

import com.unforbidable.tfc.bids.features.building.roughstone.block.BlockRoughStone;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.unforbidable.tfc.bids.util.accessor.BlockMetaNamesAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class BlockSpec<T extends Block> {

    public final String name;
    private final Supplier<T> block;
    public final Class<? extends ItemBlock> itemType;
    private final Consumer<T> apply;
    public final BlockFireInfoSpec fireInfo;
    public final BlockHarvestSpec harvest;
    public final MetaSpec meta;
    public final float hardness;
    public final String texture;

    public BlockSpec(String name, Supplier<T> block, Class<? extends ItemBlock> itemType, Consumer<T> apply,
                     BlockFireInfoSpec fireInfo, BlockHarvestSpec harvest, MetaSpec meta, float hardness, String texture) {
        this.name = name;
        this.itemType = itemType;
        this.block = block;
        this.apply = apply;
        this.fireInfo = fireInfo;
        this.harvest = harvest;
        this.meta = meta;
        this.hardness = hardness;
        this.texture = texture;
    }

    public T getInstance() {
        T instance = block.get();
        instance.setBlockName(name);

        if (hardness >= 0) {
            instance.setHardness(hardness);
        }

        if (texture != null) {
            instance.setBlockTextureName(texture);
        }

        if (harvest != null) {
            instance.setHarvestLevel(harvest.toolClass, harvest.level);
        }

        if (meta != null && instance instanceof BlockMetaNamesAccessor) {
            ((BlockMetaNamesAccessor)instance).setMetaNames(meta.names);
        }

        if (apply != null) {
            apply.accept(instance);
        }

        return instance;
    }

}
