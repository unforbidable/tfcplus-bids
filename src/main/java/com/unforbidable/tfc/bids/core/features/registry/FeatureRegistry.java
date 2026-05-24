package com.unforbidable.tfc.bids.core.features.registry;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.drink.DrinkRegistry;
import com.unforbidable.tfc.bids.core.drink.FluidHelper;
import com.unforbidable.tfc.bids.core.drink.registry.DrinkVessel;
import com.unforbidable.tfc.bids.core.features.client.block.BlockClientSpec;
import com.unforbidable.tfc.bids.core.features.client.eventhandler.EventHandlerClientSpec;
import com.unforbidable.tfc.bids.core.features.client.gui.GuiScreenSpec;
import com.unforbidable.tfc.bids.core.features.client.item.ItemClientSpec;
import com.unforbidable.tfc.bids.core.features.client.tileentity.TileEntityClientSpec;
import com.unforbidable.tfc.bids.core.features.init.block.BlockSpec;
import com.unforbidable.tfc.bids.core.features.init.gui.GuiContainerSpec;
import com.unforbidable.tfc.bids.core.features.init.item.ItemSpec;
import com.unforbidable.tfc.bids.core.features.init.tileentity.TileEntitySpec;
import com.unforbidable.tfc.bids.core.features.setup.eventhandler.EventHandlerSpec;
import com.unforbidable.tfc.bids.core.features.setup.ore.OreGroup;
import com.unforbidable.tfc.bids.core.features.setup.registry.MapRegistryGroup;
import com.unforbidable.tfc.bids.core.features.setup.registry.RegistryGroup;
import com.unforbidable.tfc.bids.core.gui.ContainerProvider;
import com.unforbidable.tfc.bids.core.gui.GuiRegistry;
import com.unforbidable.tfc.bids.core.gui.client.ClientGuiRegistry;
import com.unforbidable.tfc.bids.core.gui.client.GuiScreenProvider;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class FeatureRegistry {

    final Map<String, BlockRegistryEntry> blocks = new HashMap<>();
    final Map<String, ItemRegistryEntry> items = new HashMap<>();

    public final FeatureRegistryLookup lookup = new FeatureRegistryLookup(this);

    private int lastRegisteredBlockId = 0;
    private final Map<Integer, BlockRegistryEntry> registeredBlocksById = new HashMap<>();

    private final BlockIdProvider blockIdProvider = new BlockIdProvider();

    public BlockRegistryEntry initBlock(BlockSpec<?> spec) {
        Bids.LOG.info("Init block '{}'", spec.name);

        Block block = spec.getInstance();
        int id = blockIdProvider.getBlockId(spec.name);

        BlockRegistryEntry registryEntry = new BlockRegistryEntry(spec, block, id);

        blocks.put(spec.name, registryEntry);

        return registryEntry;
    }

    public void registerBlock(BlockRegistryEntry entry) {
        Bids.LOG.info("Register block '{}'", entry.spec.name);

        if (registeredBlocksById.containsKey(entry.id)) {
            Bids.LOG.warn("Block integrity cannot be assured because block {} requests subscribes to ID {} which has already been claimed by block {}",
                entry.spec.name, entry.id, registeredBlocksById.get(entry.id).spec.name);
        } else {
            if (registeredBlocksById.size() > 0) {
                int expectedBlockId = lastRegisteredBlockId + 1;
                if (expectedBlockId != entry.id) {
                    Bids.LOG.warn("Block integrity cannot be assured because block {} subscribes to ID {} but ID {} was expected",
                        entry.spec.name, entry.id, expectedBlockId);
                }
            }

            registeredBlocksById.put(entry.id, entry);
        }

        if (entry.spec.itemType != null) {
            GameRegistry.registerBlock(entry.instance, entry.spec.itemType, entry.spec.name);
        } else {
            GameRegistry.registerBlock(entry.instance, entry.spec.name);
        }

        lastRegisteredBlockId = entry.id;
    }

    public ItemRegistryEntry initItem(ItemSpec<?> spec) {
        Bids.LOG.info("Init item '{}'", spec.name);

        Item item = spec.getInstance();

        ItemRegistryEntry registryEntry = new ItemRegistryEntry(spec, item);

        items.put(spec.name, registryEntry);

        return registryEntry;
    }

    public void registerItem(ItemRegistryEntry entry) {
        Bids.LOG.info("Register item '{}'", entry.spec.name);

        GameRegistry.registerItem(entry.instance, entry.spec.name);
    }

    public void registerTileEntity(TileEntitySpec spec) {
        Bids.LOG.info("Register tile entity {} as '{}'",
            spec.type, spec.id);

        GameRegistry.registerTileEntity(spec.type, spec.id);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockRender(BlockClientSpec spec) {
        Bids.LOG.info("Register simple block renderer {} for block '{}'",
            spec.blockRender.getClass(), spec.name);

        int id = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(id, spec.blockRender);

        BlockRenderIdProvider.blockRenderIds.put(spec.name, id);
    }

    @SideOnly(Side.CLIENT)
    public void registerTileEntitySpecialRender(TileEntityClientSpec spec) {
        Bids.LOG.info("Register tile special renderer {} for type '{}'",
            spec.tileEntitySpecialRender.getClass(), spec.type);

        ClientRegistry.bindTileEntitySpecialRenderer(spec.type, spec.tileEntitySpecialRender);
    }

    @SideOnly(Side.CLIENT)
    public void registerItemRenderer(ItemClientSpec spec) {
        Bids.LOG.info("Register item renderer {} for item '{}'",
            spec.itemRender.getClass(), spec.name);

        ItemRegistryEntry item = items.get(spec.name);
        MinecraftForgeClient.registerItemRenderer(item.instance, spec.itemRender);
    }

    public void registerFireInfo(BlockSpec<?> spec) {
        Bids.LOG.info("Register fire info for block '{}' {}, {}",
            spec.name, spec.fireInfo.encouragement, spec.fireInfo.flammability);

        BlockRegistryEntry block = blocks.get(spec.name);
        Blocks.fire.setFireInfo(block.instance, spec.fireInfo.encouragement, spec.fireInfo.flammability);
    }

    public void registerFluidContainer(ItemSpec<?> spec) {
        Bids.LOG.info("Register item as fluid container '{}' ({})", spec.name,
            spec.fluid.partial ? "partial" : spec.meta != null ? "multi" : "single");

        ItemRegistryEntry item = items.get(spec.name);

        if (spec.fluid.partial) {
            FluidHelper.registerPartialFluidContainer(spec.fluid.fluid, spec.container.item.get(),
                spec.container.emptyItemDamage, item.instance, 50, spec.fluid.volume);
        } else {
            if (spec.meta != null) {
                for (int i = 0; i < spec.meta.names.length; i++) {
                    FluidContainerRegistry.registerFluidContainer(new FluidStack(spec.fluid.fluid, spec.fluid.volume),
                        new ItemStack(item.instance, 1, i),
                        new ItemStack(spec.container.item.get(), 1, i + spec.container.emptyItemDamage));
                }
            } else {
                FluidContainerRegistry.registerFluidContainer(new FluidStack(spec.fluid.fluid, spec.fluid.volume),
                    new ItemStack(item.instance, 1, 0),
                    new ItemStack(spec.container.item.get(), 1, spec.container.emptyItemDamage));
            }
        }
    }

    public void registerDrinks(ItemSpec<?> spec) {
        Bids.LOG.info("Register drinks for container item '{}'", spec.name);

        ItemRegistryEntry item = items.get(spec.name);
        DrinkRegistry.vessels.add(new DrinkVessel(item.instance, spec.drink.volume,
            spec.drink.pottery, spec.overlay != null ? spec.overlay.partialOverlays : new int[] {0, 100}));
    }

    public void registerOre(OreGroup ore) {
        Bids.LOG.info("Register {} ores for '{}'", ore.items.size(), ore.name);

        ore.items.forEach(i -> OreDictionary.registerOre(ore.name, i));
    }

    public <T> void registerList(RegistryGroup<T> group) {
        Bids.LOG.info("Register {} list values", group.values.size());

        group.values.forEach(group.registry::add);
    }

    public <K, V> void registerMap(MapRegistryGroup<K, V> group) {
        Bids.LOG.info("Register {} map values", group.values.size());

        group.values.forEach(group.registry::add);
    }

    public void registerGuiContainer(GuiContainerSpec<?, ?> container) {
        Bids.LOG.info("Register GUI container '{}'", container.name);

        int id = GuiRegistry.getNextAvailableId();
        GuiRegistry.guis.add(container.name, id);
        GuiRegistry.container.add(container.name, new ContainerProvider<>(id, container.provider));
    }

    @SideOnly(Side.CLIENT)
    public void registerGuiScreen(GuiScreenSpec<?, ?> screen) {
        Bids.LOG.info("Register GUI screen '{}'", screen.name);

        Integer id = GuiRegistry.guis.get(screen.name);
        if (id != null) {
            ClientGuiRegistry.screens.add(screen.name, new GuiScreenProvider<>(id, screen.provider));
        } else {
            Bids.LOG.error("GUI container for '{}' must be registered first", screen.name);
        }
    }

    public void registerEventHandler(EventHandlerSpec spec) {
        MinecraftForge.EVENT_BUS.register(spec.instance);
    }

    @SideOnly(Side.CLIENT)
    public void registerClientEventHandler(EventHandlerClientSpec spec) {
        MinecraftForge.EVENT_BUS.register(spec.instance);
    }

    public void check() {
        for (Object o : Item.itemRegistry) {
            Item item = (Item) o;
            if (item != null && item.getCreativeTab() != null) {
                List<ItemStack> list = new ArrayList<>();
                item.getSubItems(item, item.getCreativeTab(), list);

                for (ItemStack is : list) {
                    if (is.getItem() == null) {
                        Bids.LOG.error("NULL item returned as sub item for " + item.getUnlocalizedName());
                    }
                }
            }
        }
    }

}
