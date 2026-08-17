package com.unforbidable.tfc.bids.features.material.skin.container;

import com.dunk.tfc.Containers.ContainerSpecialCrafting;
import com.dunk.tfc.Core.Player.PlayerInfo;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerSpecialCraftingSkin extends ContainerSpecialCrafting {

    private final InventoryPlayer invPlayer;
    private final World worldObj;

    private boolean decreasedStackActually;

    public ContainerSpecialCraftingSkin(InventoryPlayer inventoryplayer, ItemStack is, World world, int x, int y, int z) {
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
        if (invPlayer != null && !worldObj.isRemote) {
            if (hasPieceBeenRemovedActually() && !decreasedStackActually) {
                decreasedStackActually = true;

                SkinTag tag = SkinTag.of(invPlayer.getCurrentItem());
                float weight = tag.getWeight();
                float remainingWeight = weight - SkinHelper.WEIGHT_SMALL;
                tag.setWeight(remainingWeight);

                if (remainingWeight <= 0) {
                    invPlayer.decrStackSize(invPlayer.currentItem, 1);
                }
            }
        }
    }

    private boolean hasPieceBeenRemovedActually() {
        for(int i = 0; i < this.craftMatrix.getSizeInventory(); ++i) {
            if (this.craftMatrix.getStackInSlot(i) == null) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPieceBeenRemoved(PlayerInfo player) {
        // Always return false to prevent the input from being consumed
        return false;
    }

}
