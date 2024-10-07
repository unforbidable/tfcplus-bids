package com.unforbidable.tfc.bids.Containers;

import com.dunk.tfc.Containers.ContainerSpecialCrafting;
import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.api.Food;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerSpecialCraftingDough extends ContainerSpecialCrafting {

    private final InventoryPlayer invPlayer;
    private final World worldObj;

    private ItemStack craftedResult;

    public ContainerSpecialCraftingDough(InventoryPlayer inventoryplayer, ItemStack is, World world, int x, int y, int z) {
        super(inventoryplayer, is, world, x, y, z);
        invPlayer = inventoryplayer;
        worldObj = world;

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
            // remove the dough
            if (!worldObj.isRemote && craftResult.getStackInSlot(0) != null) {
                if (invPlayer.getCurrentItem() != null && invPlayer.getCurrentItem().stackSize > 0) {
                    craftedResult = craftResult.getStackInSlot(0);

                    // For some reason, section is run 2x after the output is ready
                    // The current item is removed during the first run
                    ItemStack input = invPlayer.getCurrentItem();
                    Food.setWeight(craftedResult, Food.getWeight(input));
                    Food.setDecay(craftedResult, Food.getDecay(input));
                    Food.setDecayTimer(craftedResult, Food.getDecayTimer(input));

                    invPlayer.decrStackSize(invPlayer.currentItem, invPlayer.getCurrentItem().stackSize);
                } else if (craftedResult != null) {
                    // In the second run the weight and decay is set again from saved result stack
                    ItemStack result = craftResult.getStackInSlot(0);

                    Food.setWeight(result, Food.getWeight(craftedResult));
                    Food.setDecay(result, Food.getDecay(craftedResult));
                    Food.setDecayTimer(result, Food.getDecayTimer(craftedResult));
                }
            }
        }
    }

    public boolean hasPieceBeenRemoved(PlayerInfo player) {
        // Always return false to prevent the input from being consumed
        return false;
    }

}
