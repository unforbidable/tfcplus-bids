package com.unforbidable.tfc.bids.core.features.init.item;

import com.dunk.tfc.Items.ItemTerra;
import com.unforbidable.tfc.bids.common.item.ItemCommonPotteryMold;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.compat.tfc.TfcUtil;
import com.unforbidable.tfc.bids.util.accessor.ItemMetaNamesAccessor;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.item.Item;

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
    public final FoodSpec food;
    public final SmokeSpec smoke;

    public ItemSpec(String name, Supplier<T> item, Consumer<T> apply,
                    ContainerSpec container, FluidSpec fluid, DrinkSpec drink, OverlaySpec overlay,
                    MoldSpec mold, MetaSpec meta,
                    List<ItemHarvestSpec> harvests,
                    FoodSpec food, SmokeSpec smoke) {
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
        this.food = food;
        this.smoke = smoke;
    }

    public T getInstance() {
        T instance = item.get();
        instance.setUnlocalizedName(name);

        if (container != null) {
            instance.setContainerItem(container.item.get());

            if (fluid != null && fluid.partial) {
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

        if (meta != null && instance instanceof ItemMetaNamesAccessor) {
            ((ItemMetaNamesAccessor) instance).setMetaNames(meta.names);
        }

        if (food != null && instance instanceof ItemExtraFood) {
            ((ItemExtraFood) instance).decayRate = food.decayRate;
            ((ItemExtraFood) instance).waterPercentage = food.waterPercentage;
            ((ItemExtraFood) instance).edible = food.edible;
            ((ItemExtraFood) instance).canBeUsedRaw = food.canBeUsedRaw;
            ((ItemExtraFood) instance).poisonOnRaw = food.poisonOnRaw;
            ((ItemExtraFood) instance).guaranteedPoisonOnRaw = food.guaranteedPoisonOnRaw;
        }

        if (smoke != null && instance instanceof ItemExtraFood) {
            ((ItemExtraFood) instance).setCanSmoke();
            ((ItemExtraFood) instance).setSmokeAbsorbMultiplier(smoke.smokeAbsorbMultiplier);
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
