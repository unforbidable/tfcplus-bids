package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TEBarrel;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Core.Player.PlayerStateManager;
import com.unforbidable.tfc.bids.Core.Player.PlayerStats;
import com.unforbidable.tfc.bids.api.BidsOptions;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

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
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                && canPlayerUseSoapAt(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, player)) {

                if (player.worldObj.isRemote) {
                    SoapUsageState state = new SoapUsageState();
                    state.blockX = mop.blockX;
                    state.blockY = mop.blockY;
                    state.blockZ = mop.blockZ;
                    PlayerStateManager.setPlayerState(player, state);
                }

                return true;
            }
        }

        return false;
    }

    private boolean canPlayerUseSoapAt(World world, int x, int y, int z, int sideHit, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEBarrel && !((TEBarrel) te).getSealed()) {
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
                if (ticksSinceLastSoapUsageRewarded > TFC_Time.HOUR_LENGTH * BidsOptions.Miscellaneous.soapUsageRewardCoolDown) {
                    int n = BidsOptions.Miscellaneous.soapUsageRewardXP;
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
        } else {
            SoapUsageState state = PlayerStateManager.getPlayerState(player, SoapUsageState.class, false);
            Random rand = new Random();

            if (count % 2 == 0 && count < 35) {
                float r0 = rand.nextFloat() * 0.6F;
                float r1 = rand.nextFloat() * 0.6F;
                float r2 = rand.nextFloat() * 0.6F;
                float r3 = rand.nextFloat() * 0.6F;
                player.worldObj.spawnParticle("splash", (double) (state.blockX + 0.5f - r0 + 0.3F), (double) state.blockY + 1.2F, (double) (state.blockZ + 0.5f - r2 + 0.3F), 0.0, 0.0, 0.0);
                player.worldObj.spawnParticle("splash", (double) (state.blockX + 0.5f + r1 - 0.3F), (double) state.blockY + 1.2F, (double) (state.blockZ + 0.5f + r3 - 0.3F), 0.0, 0.0, 0.0);
            }

            if (count % 2 == 0 && count > 25) {
                float r0 = rand.nextFloat() * 0.6F;
                float r1 = rand.nextFloat() * 0.6F;
                float r2 = rand.nextFloat() * 0.6F;
                float r3 = rand.nextFloat() * 0.6F;
                player.worldObj.spawnParticle("bubble", (double) (state.blockX + 0.5f - r0 + 0.3F), (double) state.blockY + 0.8F, (double) (state.blockZ + 0.5f - r2 + 0.3F), 0.0, 0.0, 0.0);
                player.worldObj.spawnParticle("bubble", (double) (state.blockX + 0.5f + r1 - 0.3F), (double) state.blockY + 0.8F, (double) (state.blockZ + 0.5f + r3 - 0.3F), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int count) {
        if (world.isRemote) {
            PlayerStateManager.clearPlayerState(player, SoapUsageState.class);
        }
    }

    private static class SoapUsageState {
        public int blockX;
        public int blockY;
        public int blockZ;
    }

}
