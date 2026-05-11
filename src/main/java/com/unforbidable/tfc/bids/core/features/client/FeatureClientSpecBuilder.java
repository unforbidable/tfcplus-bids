package com.unforbidable.tfc.bids.core.features.client;

import com.unforbidable.tfc.bids.core.features.client.block.BlockClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.client.eventhandler.EventHandlerClientSpecCollector;
import com.unforbidable.tfc.bids.core.features.client.gui.GuiScreenSpec;
import com.unforbidable.tfc.bids.core.features.client.item.ItemClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.client.keybinding.KeyBindingClientHelper;
import com.unforbidable.tfc.bids.core.features.client.nei.NeiRegistryHelper;
import com.unforbidable.tfc.bids.core.features.client.tileentity.TileEntityClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.client.waila.WailaRegistryHelper;
import com.unforbidable.tfc.bids.core.gui.provider.GuiProvider;
import com.unforbidable.tfc.bids.core.gui.provider.SimpleGuiFunction;
import com.unforbidable.tfc.bids.core.gui.provider.SpecialGuiFunction;
import com.unforbidable.tfc.bids.core.gui.provider.TileEntityGuiFunction;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureClientSpecBuilder {

    private final List<BlockClientSpecBuilder> blocks = new ArrayList<>();
    private final List<ItemClientSpecBuilder> items = new ArrayList<>();
    private final List<TileEntityClientSpecBuilder> tileEntities = new ArrayList<>();
    private final List<GuiScreenSpec<?, ?>> screens = new ArrayList<>();
    private final KeyBindingClientHelper keys = new KeyBindingClientHelper();
    private final EventHandlerClientSpecCollector handlers = new EventHandlerClientSpecCollector();
    private final WailaRegistryHelper waila = new WailaRegistryHelper();
    private final NeiRegistryHelper nei = new NeiRegistryHelper();

    public BlockClientSpecBuilder block(String name) {
        BlockClientSpecBuilder builder = new BlockClientSpecBuilder(name);
        blocks.add(builder);

        return builder;
    }

    public ItemClientSpecBuilder item(String name) {
        ItemClientSpecBuilder builder = new ItemClientSpecBuilder(name);
        items.add(builder);

        return builder;
    }

    public TileEntityClientSpecBuilder tileEntity(Class<? extends TileEntity> type) {
        TileEntityClientSpecBuilder builder = new TileEntityClientSpecBuilder(type);
        tileEntities.add(builder);

        return builder;
    }

    public <G extends GuiScreen> void gui(String name, SimpleGuiFunction<InventoryPlayer, World, Integer, Integer, Integer, G> fn) {
        screens.add(new GuiScreenSpec<>(name, GuiProvider.of(fn)));
    }

    public <T extends TileEntity, G extends GuiScreen> void gui(String name, TileEntityGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> fn) {
        screens.add(new GuiScreenSpec<>(name, GuiProvider.of(fn)));
    }

    public <T extends ItemStack, G extends GuiScreen> void gui(String name, SpecialGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> fn) {
        screens.add(new GuiScreenSpec<>(name, GuiProvider.of(fn)));
    }

    public KeyBindingClientHelper keys() {
        return keys;
    }

    public EventHandlerClientSpecCollector event() {
        return handlers;
    }

    public WailaRegistryHelper waila() {
        return waila;
    }

    public NeiRegistryHelper nei() {
        return nei;
    }

    public FeatureClientSpec build() {
        return new FeatureClientSpec(
            blocks.stream()
                .map(BlockClientSpecBuilder::build)
                .collect(Collectors.toList()),
            items.stream()
                .map(ItemClientSpecBuilder::build)
                .collect(Collectors.toList()),
            tileEntities.stream()
                .map(TileEntityClientSpecBuilder::build)
                .collect(Collectors.toList()),
            screens,
            handlers.build());
    }

}
