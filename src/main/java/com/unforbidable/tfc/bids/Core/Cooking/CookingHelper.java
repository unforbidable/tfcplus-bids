package com.unforbidable.tfc.bids.Core.Cooking;

import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionHelper;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionInfo;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.CookingPotBounds;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPot;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class CookingHelper {
    public static final int META_COOKING_POT_HAS_LID = 8;

    public static int getCookingPotInventoryRenderMetadata(TileEntityCookingPot tileEntityCookingPot, int defaultMeta) {
        // Using metadata to specify how the inventory block is rendered
        if (tileEntityCookingPot.hasLid()) {
            return META_COOKING_POT_HAS_LID + defaultMeta;
        } else {
            return defaultMeta;
        }
    }

    public static void getCookingPotInfo(TileEntityCookingPot tileEntityCookingPot, List<String> list, boolean includeHeat) {
        if (tileEntityCookingPot.getTopLayerFluidStack() != null) {
            list.add(getFluidInfoLine(tileEntityCookingPot.getTopLayerFluidStack()));
        }

        if (tileEntityCookingPot.getPrimaryFluidStack() != null) {
            list.add(getFluidInfoLine(tileEntityCookingPot.getPrimaryFluidStack()));
        }

        if (tileEntityCookingPot.hasSteamingMesh()) {
            list.add(EnumChatFormatting.WHITE + "" + StatCollector.translateToLocal("gui.SteamingMesh"));
        }

        if (tileEntityCookingPot.hasInputItem()) {
            list.add(getItemInfoLine(tileEntityCookingPot.getInputItemStack()));
        }

        if (includeHeat) {
            switch (tileEntityCookingPot.getHeatLevel()) {
                case NONE:
                    list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.CookingHeatLevel.None"));
                    break;

                case LOW:
                    list.add(EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("gui.CookingHeatLevel.Low"));
                    break;

                case MEDIUM:
                    list.add(EnumChatFormatting.RED + StatCollector.translateToLocal("gui.CookingHeatLevel.Medium"));
                    break;

                case HIGH:
                    list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("gui.CookingHeatLevel.High"));
                    break;
            }
        }
    }

    private static String getFluidInfoLine(FluidStack fluidStack) {
        return EnumChatFormatting.BLUE + fluidStack.getFluid().getLocalizedName(fluidStack) + " (" + fluidStack.amount + " mB)";
    }

    private static String getItemInfoLine(ItemStack itemStack) {
        return EnumChatFormatting.GOLD + itemStack.getDisplayName() + (itemStack.stackSize > -1 ? (" (" + itemStack.stackSize + "x)") : "");
    }

    public static MovingObjectPosition onCookingPotCollisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            TileEntityCookingPot te = (TileEntityCookingPot) world.getTileEntity(x, y, z);
            Block block = world.getBlock(x, y, z);

            startVec = startVec.addVector(-x, -y, -z);
            endVec = endVec.addVector(-x, -y, -z);

            AxisAlignedBB potBounds = te.getCachedBounds().getEntireBounds();
            CollisionInfo potCol = CollisionHelper.rayTraceAABB(potBounds, startVec, endVec);
            if (potCol != null) {
                return setBlockBoundsFromCollision(x, y, z, block, potBounds, potCol);
            }

            CollisionInfo groundCol = CollisionHelper.rayTraceAABB(CookingPotBounds.GROUND_BOUNDS, startVec, endVec);
            if (groundCol != null) {
                return setBlockBoundsFromCollision(x, y, z, block, CookingPotBounds.GROUND_BOUNDS, groundCol);
            }
        }

        return null;
    }

    private static MovingObjectPosition setBlockBoundsFromCollision(int x, int y, int z, Block block, AxisAlignedBB bounds, CollisionInfo col) {
        //block.setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);

        return new MovingObjectPosition(x, y, z,
            col.side,
            col.hitVec.addVector(x, y, z));
    }
}
