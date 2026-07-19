package com.unforbidable.tfc.bids.compat.tfc.registry.values;

import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRaw;
import com.dunk.tfc.api.HeatRegistry;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.item.ItemStack;

public class HeatValueStage extends RegistryStage<HeatValue> {

    public static final HeatValueStage instance = new HeatValueStage();

    @Override
    public void add(HeatValue value) {
        Bids.LOG.info("Register TFC heat index for {}", value.input);

        try {
            HeatRaw raw = new HeatRaw(value.specificHeat, value.meltTemp);
            HeatIndex heatIndex = new HeatIndex(value.input, raw, value.output)
                .setKeepNBT(value.keepNbt);
            HeatRegistry.getInstance().addIndex(heatIndex);
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to register TFC heat index for {}: {}", value.input, ex.getMessage(), ex);
        }
    }

    @Override
    public void clone(Predicate<HeatValue> predicate, List<Function<HeatValue, HeatValue>> mappers) {
        Bids.LOG.info("Clone TFC heat index");

        try {
            HeatValue existing = HeatRegistry.getInstance().getHeatList().stream()
                .map(hi -> new HeatValue(hi.input, hi.specificHeat, hi.meltTemp, new ItemStack(hi.getOutputItem(), 1, hi.getOutputDamage()), hi.keepNBT))
                .filter(predicate)
                .findAny().orElseThrow(() -> new RuntimeException("No heat index matching given criteria was found"));

            for (Function<HeatValue, HeatValue> mapper : mappers) {
                HeatValue value = mapper.apply(existing);

                add(value);
            }

        } catch (Exception ex) {
            Bids.LOG.warn("Failed to clone TFC heat index: {}", ex.getMessage(), ex);
        }
    }

}
