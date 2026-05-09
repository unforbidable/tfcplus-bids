package com.unforbidable.tfc.bids.core.features.init;

import com.unforbidable.tfc.bids.core.features.client.gui.GuiScreenSpec;
import com.unforbidable.tfc.bids.core.features.init.block.BlockSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.tileentity.TileEntitySpec;
import com.unforbidable.tfc.bids.core.features.init.gui.GuiContainerSpec;
import com.unforbidable.tfc.bids.core.features.init.item.ItemSpecBuilder;
import com.unforbidable.tfc.bids.core.gui.provider.GuiProvider;
import com.unforbidable.tfc.bids.core.gui.provider.SimpleGuiFunction;
import com.unforbidable.tfc.bids.core.gui.provider.SpecialGuiFunction;
import com.unforbidable.tfc.bids.core.gui.provider.TileEntityGuiFunction;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FeatureInitSpecBuilder {

    private final List<BlockSpecBuilder<?>> blocks = new ArrayList<>();
    private final List<ItemSpecBuilder<?>> items = new ArrayList<>();
    private final List<TileEntitySpec> tileEntities = new ArrayList<>();
    private final List<GuiContainerSpec<?, ?>> containers = new ArrayList<>();

    public <T extends Block> BlockSpecBuilder<T> block(String name, Supplier<T> block, Class<? extends ItemBlock> itemBlock) {
        BlockSpecBuilder<T> builder = new BlockSpecBuilder<>(name, block, itemBlock);
        blocks.add(builder);

        return builder;
    }

    public <T extends Block> BlockSpecBuilder<T> block(String name, Supplier<T> block) {
        return block(name, block, null);
    }

    public <T extends Item> ItemSpecBuilder<T> item(String name, Supplier<T> item) {
        ItemSpecBuilder<T> builder = new ItemSpecBuilder<>(name, item);
        items.add(builder);

        return builder;
    }

    public void tileEntity(Class<? extends TileEntity> type, String id) {
        tileEntities.add(new TileEntitySpec(type, id));
    }

    public <G extends Container> void gui(String name, SimpleGuiFunction<InventoryPlayer, World, Integer, Integer, Integer, G> fn) {
        containers.add(new GuiContainerSpec<>(name, GuiProvider.of(fn)));
    }

    public <T extends TileEntity, G extends Container> void gui(String name, TileEntityGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> fn) {
        containers.add(new GuiContainerSpec<>(name, GuiProvider.of(fn)));
    }

    public <T extends ItemStack, G extends Container> void gui(String name, SpecialGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> fn) {
        containers.add(new GuiContainerSpec<>(name, GuiProvider.of(fn)));
    }

    public FeatureInitSpec build() {
        return new FeatureInitSpec(
            blocks.stream()
                .map(BlockSpecBuilder::build)
                .collect(Collectors.toList()),
            items.stream()
                .map(ItemSpecBuilder::build)
                .collect(Collectors.toList()),
            tileEntities,
            containers);
    }

}
