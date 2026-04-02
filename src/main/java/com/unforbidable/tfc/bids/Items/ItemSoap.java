package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TEBarrel;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Core.Player.PlayerStats;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class ItemSoap extends ItemFoodLike {

    @Override
    public int getMaxItemUseDuration(ItemStack is) {
        return 50;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack is) {
        return EnumAction.block;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (canPlayerUseSoap(player)) {
            player.setItemInUse(is, getMaxItemUseDuration(is));
        }

        return is;
    }

    private boolean canPlayerUseSoap(EntityPlayer player) {
        PlayerStats playerStats = PlayerStats.of(player);
        long ticksSinceLastSoapUsage = TFC_Time.getTotalTicks() - playerStats.lastSoapUsageTicks;
        if (ticksSinceLastSoapUsage > 60) {
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
            return mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                && canPlayerUseSoapAt(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, player);
        }

        return false;
    }

    private boolean canPlayerUseSoapAt(World world, int x, int y, int z, int sideHit, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEBarrel) {
            FluidStack fs = ((TEBarrel) te).getFluidStack();
            return fs != null && fs.getFluid() == TFCFluids.FRESHWATER;
        } else {
            Block b = world.getBlock(x, y, z);
            return TFC_Core.isFreshWater(b);
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (!player.worldObj.isRemote) {
            if (count % 5 == 1) {
                player.worldObj.playSoundAtEntity(player, "step.grass", 0.5F, 0.8f);
            }

            if (count <= 1 && player.isUsingItem()) {
                float weight = Food.getWeight(stack);
                float consumedWeight = Math.min(weight, 1f);
                float reducedWeight = weight - consumedWeight;
                if (reducedWeight <= 0) {
                    stack.stackSize = 0;
                } else {
                    Food.setWeight(stack, reducedWeight);
                }

                PlayerStats playerStats = PlayerStats.of(player);
                playerStats.lastSoapUsageTicks = TFC_Time.getTotalTicks();

                long ticksSinceLastSoapUsageRewarded = TFC_Time.getTotalTicks() - playerStats.lastSoapUsageRewardedTicks;
                if (ticksSinceLastSoapUsageRewarded > TFC_Time.HOUR_LENGTH * 6) {
                    int n = 5;
                    while (n > 0) {
                        int split = EntityXPOrb.getXPSplit(n);
                        player.worldObj.spawnEntityInWorld(new EntityXPOrb(player.worldObj, player.posX, player.posY, player.posZ, split));
                        n -= split;
                    }

                    playerStats.lastSoapUsageRewardedTicks = TFC_Time.getTotalTicks();
                }

                playerStats.save(true);

                player.worldObj.playSoundAtEntity(player, "random.splash", 0.2F, 0.6f);

                player.inventoryContainer.detectAndSendChanges();
                player.stopUsingItem();
            }
        }
    }

    @Override
    public ItemStack onEaten(ItemStack is, World world, EntityPlayer player) {
        return is;
    }

}
