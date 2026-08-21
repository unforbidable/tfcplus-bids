package com.unforbidable.tfc.bids.core.features.registry;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.drink.DrinkRegistry;
import com.unforbidable.tfc.bids.core.drink.FluidHelper;
import com.unforbidable.tfc.bids.core.drink.registry.DrinkVessel;
import com.unforbidable.tfc.bids.core.features.client.eventhandler.EventHandlerClientSpec;
import com.unforbidable.tfc.bids.core.features.client.gui.GuiScreenSpec;
import com.unforbidable.tfc.bids.core.features.client.render.RenderBlockSpec;
import com.unforbidable.tfc.bids.core.features.client.render.RenderEntitySpec;
import com.unforbidable.tfc.bids.core.features.client.render.RenderItemSpec;
import com.unforbidable.tfc.bids.core.features.client.render.RenderTileEntitySpec;
import com.unforbidable.tfc.bids.core.features.init.block.BlockSpec;
import com.unforbidable.tfc.bids.core.features.init.entity.EntitySpec;
import com.unforbidable.tfc.bids.core.features.init.fluid.FluidSpec;
import com.unforbidable.tfc.bids.core.features.init.gui.GuiContainerSpec;
import com.unforbidable.tfc.bids.core.features.init.item.ItemSpec;
import com.unforbidable.tfc.bids.core.features.init.tileentity.TileEntitySpec;
import com.unforbidable.tfc.bids.core.features.setup.eventhandler.EventHandlerSpec;
import com.unforbidable.tfc.bids.core.features.setup.fluidcontainer.FluidContainerSpec;
import com.unforbidable.tfc.bids.core.features.setup.help.HelpGroup;
import com.unforbidable.tfc.bids.core.features.setup.ore.OreGroup;
import com.unforbidable.tfc.bids.core.features.setup.registry.MapRegistryGroup;
import com.unforbidable.tfc.bids.core.features.setup.registry.RegistryAdapter;
import com.unforbidable.tfc.bids.core.features.setup.registry.RegistryGroup;
import com.unforbidable.tfc.bids.core.features.setup.worldgen.WorldGenSpec;
import com.unforbidable.tfc.bids.core.gui.ContainerProvider;
import com.unforbidable.tfc.bids.core.gui.GuiRegistry;
import com.unforbidable.tfc.bids.core.gui.client.ClientGuiRegistry;
import com.unforbidable.tfc.bids.core.gui.client.GuiScreenProvider;
import com.unforbidable.tfc.bids.core.help.HelpRegistry;
import com.unforbidable.tfc.bids.core.help.hints.ItemHint;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
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
        Bids.LOG.debug("Init block '{}'", spec.name);

        Block block = spec.getInstance();
        int id = blockIdProvider.getBlockId(spec.name);

        BlockRegistryEntry registryEntry = new BlockRegistryEntry(spec, block, id);

        blocks.put(spec.name, registryEntry);

        return registryEntry;
    }

    public void registerBlock(BlockRegistryEntry entry) {
        Bids.LOG.debug("Register block '{}'", entry.spec.name);

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
        Bids.LOG.debug("Init item '{}'", spec.name);

        Item item = spec.getInstance();

        ItemRegistryEntry registryEntry = new ItemRegistryEntry(spec, item);

        items.put(spec.name, registryEntry);

        return registryEntry;
    }

    public void registerItem(ItemRegistryEntry entry) {
        Bids.LOG.debug("Register item '{}'", entry.spec.name);

        GameRegistry.registerItem(entry.instance, entry.instance.getUnlocalizedName());
    }

    public void registerFluid(FluidSpec<?> spec) {
        Bids.LOG.debug("Register fluid '{}'", spec.name);

        Fluid instance = spec.getInstance();
        FluidRegistry.registerFluid(instance);
    }

    public void registerTileEntity(TileEntitySpec spec) {
        Bids.LOG.debug("Register tile entity {} as '{}'",
            spec.type, spec.id);

        GameRegistry.registerTileEntity(spec.type, spec.id);
    }

    public void registerEntity(EntitySpec spec) {
        Bids.LOG.debug("Register entity {} with name '{}'",
            spec.type, spec.name);

        int entityId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(spec.type, spec.name, entityId);
        EntityRegistry.registerModEntity(spec.type, spec.name, entityId, Bids.instance, 160, 20, false);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockRenderer(RenderBlockSpec spec) {
        Bids.LOG.debug("Register simple block renderer {} for {} block(s)",
            spec.renderer.getClass(), spec.blocks.size());

        int id = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(id, spec.renderer);

        for (Class<? extends Block> type : spec.blocks) {
            if (BlockRenderIdProvider.blockRenderIds.containsKey(type)) {
                Bids.LOG.warn("Block type {} is already register in render ID provider", type.getCanonicalName());
            } else {
                BlockRenderIdProvider.blockRenderIds.put(type, id);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerTileEntitySpecialRender(RenderTileEntitySpec spec) {
        Bids.LOG.debug("Register tile special renderer {} for {} tile entity(-ies).",
            spec.renderer.getClass(), spec.tileEntities);

        for (Class<? extends TileEntity> type : spec.tileEntities) {
            ClientRegistry.bindTileEntitySpecialRenderer(type, spec.renderer);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerEntityRenderer(RenderEntitySpec spec) {
        Bids.LOG.debug("Register entity renderer {} for {} entity(-ies).",
            spec.renderer.getClass(), spec.entities);

        for (Class<? extends Entity> type : spec.entities) {
            RenderingRegistry.registerEntityRenderingHandler(type, spec.renderer);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerItemRenderer(RenderItemSpec spec) {
        Bids.LOG.debug("Register item renderer {} for {} item(s)",
            spec.renderer.getClass(), spec.items.size());

        for (Item item : spec.items) {
            MinecraftForgeClient.registerItemRenderer(item, spec.renderer);
        }
    }

    public void registerFireInfo(BlockSpec<?> spec) {
        Bids.LOG.debug("Register fire info for block '{}' {}, {}",
            spec.name, spec.fireInfo.encouragement, spec.fireInfo.flammability);

        BlockRegistryEntry block = blocks.get(spec.name);
        Blocks.fire.setFireInfo(block.instance, spec.fireInfo.encouragement, spec.fireInfo.flammability);
    }

    public void initItemFluidContainer(FluidContainerSpec spec) {
        Bids.LOG.debug("Init fluid container {} for item {}",
            spec.empty, spec.filled);

        spec.filled.setContainerItem(spec.empty);

        if (spec.partial) {
            spec.filled.setMaxDamage(spec.volume / 50);
        }
    }

    public void registerFluidContainer(FluidContainerSpec spec) {
        Bids.LOG.debug("Register fluid container for fluid '{}' volume {}{}", spec.fluid.getName(),
            spec.volume, spec.partial ? " (partial)" : "");

        if (spec.partial) {
            FluidHelper.registerPartialFluidContainer(spec.fluid, spec.empty,
                spec.emptyDamage, spec.filled, 50, spec.volume);
        } else {
            FluidContainerRegistry.registerFluidContainer(new FluidStack(spec.fluid, spec.volume),
                new ItemStack(spec.filled, 1, spec.filledDamage),
                new ItemStack(spec.empty, 1, spec.emptyDamage));
        }
    }

    public void registerDrinks(ItemSpec<?> spec) {
        Bids.LOG.debug("Register drinks for container item '{}'", spec.name);

        ItemRegistryEntry item = items.get(spec.name);
        DrinkRegistry.vessels.add(new DrinkVessel(item.instance, spec.drink.volume,
            spec.drink.pottery, spec.overlay != null ? spec.overlay.partialOverlays : new int[] {}));
    }

    public void registerOre(OreGroup ore) {
        Bids.LOG.debug("Register {} ores for '{}'", ore.items.size(), ore.name);

        ore.items.forEach(i -> OreDictionary.registerOre(ore.name, i));
    }

    public <T> void registerList(RegistryGroup<T> group) {
        Bids.LOG.debug("Register {} list values", group.values.size());

        group.values.forEach(group.registry::add);
    }

    public <S, T> void registerListAdapter(RegistryAdapter<S, T> adapter) {
        adapter.source.stream()
            .map(adapter.mapper)
            .filter(Objects::nonNull)
            .forEach(adapter.target::add);
    }

    public <K, V> void registerMap(MapRegistryGroup<K, V> group) {
        Bids.LOG.debug("Register {} map values", group.values.size());

        group.values.forEach(group.registry::add);
    }

    public void registerGuiContainer(GuiContainerSpec<?, ?> container) {
        Bids.LOG.debug("Register GUI container '{}'", container.name);

        int id = GuiRegistry.getNextAvailableId();
        GuiRegistry.guis.add(container.name, id);
        GuiRegistry.container.add(container.name, new ContainerProvider<>(id, container.provider));
    }

    @SideOnly(Side.CLIENT)
    public void registerGuiScreen(GuiScreenSpec<?, ?> screen) {
        Bids.LOG.debug("Register GUI screen '{}'", screen.name);

        Integer id = GuiRegistry.guis.get(screen.name);
        if (id != null) {
            ClientGuiRegistry.screens.add(screen.name, new GuiScreenProvider<>(id, screen.provider));
        } else {
            Bids.LOG.error("GUI container for '{}' must be registered first", screen.name);
        }
    }

    public void registerEventHandler(EventHandlerSpec spec) {
        Bids.LOG.debug("Register event handler {}", spec.instance.getClass().getCanonicalName());

        MinecraftForge.EVENT_BUS.register(spec.instance);
    }

    public void registerWorldGen(WorldGenSpec spec) {
        Bids.LOG.debug("Register world generator {}", spec.generator.getClass().getCanonicalName());

        GameRegistry.registerWorldGenerator(spec.generator, spec.priority);
    }

    public void registerItemHints(ItemSpec<?> spec) {
        if (spec.hint != null) {
            ItemRegistryEntry item = items.get(spec.name);
            for (String hint : spec.hint.hints) {
                ItemStack itemStack = new ItemStack(item.instance, 1, OreDictionary.WILDCARD_VALUE);
                HelpRegistry.hints.add(new ItemHint(itemStack, hint));
            }
        }
    }

    public void registerItemHints(HelpGroup help) {
        for (String hint : help.hints) {
            HelpRegistry.hints.add(new ItemHint(help.itemStack, hint));
        }
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
