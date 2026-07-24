package com.unforbidable.tfc.bids.core.gui.provider;

import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiProvider<T, G> {

    private final SimpleGuiFunction<InventoryPlayer, World, Integer, Integer, Integer, G> simpleFn;
    private final TileEntityGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> tileEntityFn;
    private final SpecialGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> specialFn;

    private GuiProvider(SimpleGuiFunction<InventoryPlayer, World, Integer, Integer, Integer, G> simpleFn,
                        TileEntityGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> tileEntityFn,
                        SpecialGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> specialFn) {
        this.simpleFn = simpleFn;
        this.tileEntityFn = tileEntityFn;
        this.specialFn = specialFn;
    }

    public static <T, G> GuiProvider<T, G> of(SimpleGuiFunction<InventoryPlayer, World, Integer, Integer, Integer, G> fn) {
        return new GuiProvider<>(fn, null, null);
    }

    public static <T, G> GuiProvider<T, G> of(TileEntityGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> fn) {
        return new GuiProvider<>(null, fn, null);
    }

    public static <T, G> GuiProvider<T, G> of(SpecialGuiFunction<InventoryPlayer, T, World, Integer, Integer, Integer, G> fn) {
        return new GuiProvider<>(null, null, fn);
    }

    @SuppressWarnings("unchecked")
    public G get(GuiProviderContext context) {
        if (simpleFn != null) {
            return simpleFn.apply(context.player.inventory, context.world, context.x, context.y, context.z);
        } else if (tileEntityFn != null) {
            TileEntity tileEntity = context.world.getTileEntity(context.x, context.y, context.z);
            return tileEntityFn.apply(context.player.inventory, (T) tileEntity, context.world, context.x, context.y, context.z);
        } else if (specialFn != null) {
            PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(context.player);
            return specialFn.apply(context.player.inventory, (T) pi.specialCraftingType, context.world, context.x, context.y, context.z);
        } else {
            return null;
        }
    }

}
