package com.unforbidable.tfc.bids.Containers;

import com.dunk.tfc.Containers.ContainerSpecialCrafting;
import com.dunk.tfc.Core.Player.PlayerInfo;
import com.unforbidable.tfc.bids.api.BidsItems;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerSpecialCraftingBarkFibre extends ContainerSpecialCrafting {

    private final InventoryPlayer invPlayer;
    private boolean decreasedStackReally = false;

    public ContainerSpecialCraftingBarkFibre(InventoryPlayer inventoryplayer, World world, int x, int y, int z) {
        super(inventoryplayer, new ItemStack(BidsItems.flatBarkFiber), world, x, y, z);
        invPlayer = inventoryplayer;

        // This will allow the output to be created
        // even without consuming the input
        setDecreasedStack(true);
    }

    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        super.onCraftMatrixChanged(matrix);

        // invPlayer will be null during super init,
        // before our constructor is run
        if (invPlayer != null) {
            // Once output is available
            // remove 5 fibers from stack
            if (craftResult.getStackInSlot(0) != null && !decreasedStackReally) {
                final int newStackSize = Math.max(invPlayer.getCurrentItem().stackSize - 5, 0);
                invPlayer.getCurrentItem().stackSize = newStackSize;
                decreasedStackReally = true;
            }
        }
    }

    public boolean hasPieceBeenRemoved(PlayerInfo player) {
        // Always return false to prevent the input from being consumed
        return false;
    }

}
