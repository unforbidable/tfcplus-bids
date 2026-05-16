package com.unforbidable.tfc.bids.core.features.init.item;

import com.dunk.tfc.Items.ItemTerra;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.common.item.ItemCommonPotteryMold;
import com.unforbidable.tfc.bids.compat.tfc.TfcUtil;
import com.unforbidable.tfc.bids.features.building.roughstone.item.ItemRoughBrick;
import net.minecraft.item.Item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemSpec<T extends Item> {

    public final String name;
    private final Supplier<T> item;
    private final Consumer<T> apply;
    public final ContainerSpec container;
    public final FluidSpec fluid;
    public final DrinkSpec drink;
    public final OverlaySpec overlay;
    public final MoldSpec mold;
    public final MetaSpec meta;
    public final List<ItemHarvestSpec> harvests;

    public ItemSpec(String name, Supplier<T> item, Consumer<T> apply,
                    ContainerSpec container, FluidSpec fluid, DrinkSpec drink, OverlaySpec overlay,
                    MoldSpec mold, MetaSpec meta,
                    List<ItemHarvestSpec> harvests) {
        this.name = name;
        this.item = item;
        this.apply = apply;
        this.container = container;
        this.fluid = fluid;
        this.drink = drink;
        this.overlay = overlay;
        this.mold = mold;
        this.meta = meta;
        this.harvests = harvests;
    }

    public T getInstance(FeatureRegistryLookup lookup) {
        T instance = item.get();
        instance.setUnlocalizedName(name);

        if (container != null) {
            instance.setContainerItem(container.item.get(lookup));

            if (fluid != null) {
                instance.setMaxDamage(fluid.volume / 50);
            }
        }

        if (mold != null && instance instanceof ItemCommonPotteryMold) {
            ((ItemCommonPotteryMold) instance).setCounter(mold.counter);
            ((ItemCommonPotteryMold) instance).setMetals(TfcUtil.getMetalsFromNames(mold.metals));
        }

        if (meta != null && instance instanceof ItemTerra) {
            ((ItemTerra) instance).setMetaNames(meta.names);
        }

        // TODO use interface for setting meta names
        if (meta != null && instance instanceof ItemRoughBrick) {
            ((ItemRoughBrick) instance).setNames(meta.names);
        }

        for (ItemHarvestSpec spec : harvests) {
            instance.setHarvestLevel(spec.toolClass, spec.level);
        }

        if (apply != null) {
            apply.accept(instance);
        }

        return instance;
    }

}
