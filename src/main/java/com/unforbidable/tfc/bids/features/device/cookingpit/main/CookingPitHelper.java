package com.unforbidable.tfc.bids.features.device.cookingpit.main;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingHelper;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityCookingPit;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityHideCookingPit;
import com.unforbidable.tfc.bids.util.collision.CollisionHelper;
import com.unforbidable.tfc.bids.util.collision.CollisionInfo;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class CookingPitHelper {

    public static final long TICKS_PER_HOT_STONE = 1500;
    public static final long TICKS_SPENT_THRESHOLD_TO_ALLOW_INCREASE = 100;
    public static final long COOLING_DOWN_TICKS = 200;

    public static MovingObjectPosition onCookingPitCollisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            TileEntityCookingPit te = (TileEntityCookingPit) world.getTileEntity(x, y, z);
            startVec = startVec.addVector(-x, -y, -z);
            endVec = endVec.addVector(-x, -y, -z);

            if (te.hasInputItem()) {
                Vec3 itemPos = te.getCachedBounds().getItemPos();
                float fullnessRatio = (float)te.getTotalLiquidVolume() / (float)te.getMaxFluidVolume();
                itemPos = itemPos.addVector(0, te.getCachedBounds().getMaxFluidHeight() * fullnessRatio, 0);
                AxisAlignedBB itemBounds = AxisAlignedBB.getBoundingBox(itemPos.xCoord - 0.1, itemPos.yCoord, itemPos.zCoord - 0.1,
                    itemPos.xCoord + 0.1, itemPos.yCoord + 0.2, itemPos.zCoord + 0.1);
                CollisionInfo itemCol = CollisionHelper.rayTraceAABB(itemBounds, startVec, endVec);
                if (itemCol != null) {
                    te.setInputItemSelected(true);
                } else {
                    te.setInputItemSelected(false);
                }
            } else {
                te.setInputItemSelected(false);
            }
        }

        return null;
    }

    public static boolean isBlockValidPitSide(World world, int x, int y, int z, ForgeDirection side) {
        if (side == ForgeDirection.DOWN) {
            return world.isSideSolid(x, y, z, ForgeDirection.UP);
        } else {
            return TFC_Core.isSoil(world.getBlock(x, y, z));
        }
    }

    public static boolean placeHideCookingPitAt(World world, int x, int y, int z, ItemStack itemStack, EntityPlayer player) {
        if (world.isAirBlock(x, y, z) && BidsBlocks.hideCookingPit.canBlockStay(world, x, y, z)) {
            if (!world.isRemote) {
                world.setBlock(x, y, z, BidsBlocks.hideCookingPit, 0, 3);
                TileEntityHideCookingPit te = (TileEntityHideCookingPit) world.getTileEntity(x, y, z);
                te.setHideVesselDamage(itemStack.getItemDamage());

                if (FluidContainerRegistry.isFilledContainer(itemStack)) {
                    te.interactWithItemStack(itemStack, player);
                }

                world.markBlockForUpdate(x, y, z);
            }

            return true;
        }

        return false;
    }

    public static void getHideCookingPitInfo(TileEntityHideCookingPit tileEntityCookingPit, List<String> list) {
        if (tileEntityCookingPit.getFluidStack() != null) {
            list.add(EnumChatFormatting.GRAY + CookingHelper.getFluidInfoLine(tileEntityCookingPit.getFluidStack()));
        }

        if (tileEntityCookingPit.hasInputItem()) {
            list.add(EnumChatFormatting.GOLD + CookingHelper.getItemInfoLine(tileEntityCookingPit.getInputItemStack()));
        }

        if (tileEntityCookingPit.getHeatLevel() != CookingHeatLevel.NONE) {
            long heatedUntilTicks = tileEntityCookingPit.getHeatedUntilTicks();
            long ticksRemaining = heatedUntilTicks - TFC_Time.getTotalTicks();
            if (ticksRemaining > 0) {
                int secondsRemaining = (int) Math.ceil(ticksRemaining / 20f);
                String timeRemaining = String.format(" (%d)", secondsRemaining);

                switch (tileEntityCookingPit.getHeatLevel()) {
                    case LOW:
                        list.add(EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("gui.CookingHeatLevel.Low") + timeRemaining);
                        break;

                    case MEDIUM:
                        list.add(EnumChatFormatting.RED + StatCollector.translateToLocal("gui.CookingHeatLevel.Medium") + timeRemaining);
                        break;

                    case HIGH:
                        list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("gui.CookingHeatLevel.High") + timeRemaining);
                        break;
                }
            }
        }
    }

}
