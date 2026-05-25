package com.unforbidable.tfc.bids.core.features.client;

import com.unforbidable.tfc.bids.core.features.client.eventhandler.EventHandlerClientSpecCollector;
import com.unforbidable.tfc.bids.core.features.client.gui.GuiScreenSpec;
import com.unforbidable.tfc.bids.core.features.client.keybinding.KeyBindingClientHelper;
import com.unforbidable.tfc.bids.core.features.client.nei.NeiRegistryHelper;
import com.unforbidable.tfc.bids.core.features.client.render.RenderBlockBuilder;
import com.unforbidable.tfc.bids.core.features.client.render.RenderItemBuilder;
import com.unforbidable.tfc.bids.core.features.client.render.RenderTileEntityBuilder;
import com.unforbidable.tfc.bids.core.features.client.waila.WailaRegistryHelper;
import com.unforbidable.tfc.bids.core.gui.provider.GuiProvider;
import com.unforbidable.tfc.bids.core.gui.provider.SimpleGuiFunction;
import com.unforbidable.tfc.bids.core.gui.provider.SpecialGuiFunction;
import com.unforbidable.tfc.bids.core.gui.provider.TileEntityGuiFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

public class FeatureClientSpecBuilder {

    private final List<RenderBlockBuilder> blocks = new ArrayList<>();
    private final List<RenderItemBuilder> items = new ArrayList<>();
    private final List<RenderTileEntityBuilder> tileEntities = new ArrayList<>();
    private final List<GuiScreenSpec<?, ?>> screens = new ArrayList<>();
    private final KeyBindingClientHelper keys = new KeyBindingClientHelper();
    private final EventHandlerClientSpecCollector handlers = new EventHandlerClientSpecCollector();
    private final WailaRegistryHelper waila = new WailaRegistryHelper();
    private final NeiRegistryHelper nei = new NeiRegistryHelper();
    private final List<Runnable> runs = new ArrayList<>();

    public RenderBlockBuilder render(ISimpleBlockRenderingHandler renderer) {
        RenderBlockBuilder builder = new RenderBlockBuilder(renderer);
        blocks.add(builder);

        return builder;
    }

    public RenderItemBuilder render(IItemRenderer renderer) {
        RenderItemBuilder builder = new RenderItemBuilder(renderer);
        items.add(builder);

        return builder;
    }

    public RenderTileEntityBuilder render(TileEntitySpecialRenderer renderer) {
        RenderTileEntityBuilder builder = new RenderTileEntityBuilder(renderer);
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

    public void run(Runnable apply) {
        runs.add(apply);
    }

    public FeatureClientSpec build() {
        return new FeatureClientSpec(
            blocks.stream()
                .map(RenderBlockBuilder::build)
                .collect(Collectors.toList()),
            items.stream()
                .map(RenderItemBuilder::build)
                .collect(Collectors.toList()),
            tileEntities.stream()
                .map(RenderTileEntityBuilder::build)
                .collect(Collectors.toList()),
            screens,
            runs, handlers.build());
    }

}
