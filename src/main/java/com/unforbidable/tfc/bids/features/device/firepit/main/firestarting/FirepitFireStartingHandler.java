package com.unforbidable.tfc.bids.features.device.firepit.main.firestarting;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.TileEntities.TEFirepit;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHandler;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingEntityScanner;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitConfig;
import com.unforbidable.tfc.bids.features.device.firepit.main.FirepitHelper;
import com.unforbidable.tfc.bids.features.device.firepit.tileentity.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FirepitFireStartingHandler extends FireStartingHandler {

    public FirepitFireStartingHandler(int priority) {
        super(priority);
    }

    @Override
    public boolean start(EntityPlayer player, World world, int x, int y, int z, int side) {
        return createFirepit(player, world, x, y, z, side, false) ||
            lightExistingFirepit(player, world, x, y, z, side, false);
    }

    @Override
    public boolean ignite(EntityPlayer player, World world, int x, int y, int z, int side) {
        return createFirepit(player, world, x, y, z, side, true) ||
            lightExistingFirepit(player, world, x, y, z, side, true);
    }

    @Override
    public boolean propagate(EntityPlayer player, World world, int x, int y, int z, int side) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEFirepit) {
            TEFirepit teFirepit = (TEFirepit) te;
            return teFirepit.fireTemp > 1;
        }

        return false;
    }

    private boolean lightExistingFirepit(EntityPlayer player, World world, int x, int y, int z, int side, boolean ignite) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEFirepit) {
            TEFirepit teFirepit = (TEFirepit) te;
            if (teFirepit.fireTemp <= 1) {
                if (ignite) {
                    // due to some bug fuel stacks cannot be reliably checked inside a firepit on client side
                    // so kindling is checked on server side only after the fire is started
                    ItemStack fuel = teFirepit.fireItemStacks[TileEntityNewFirepit.FUEL_BURN_SLOT];
                    if (fuel != null) {
                        if (!FirepitConfig.requireKindlingToLight || FirepitHelper.isKindling(fuel)) {
                            teFirepit.fireTemp = 10;
                            teFirepit.fuelTimeLeft = 20;
                            teFirepit.fuelBurnTemp = 80;
                            world.setBlockMetadataWithNotify(x, y, z, 1, 3);

                            return true;
                        } else {
                            if (player != null) {
                                TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("gui.firepit.noKindling"));
                            }
                        }
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean createFirepit(EntityPlayer player, World world, int x, int y, int z, int side, boolean ignite) {
        if (side == 1) {
            TileEntity te = world.getTileEntity(x, y, z);
            Block block = world.getBlock(x, y, z);
            if (te == null &&
                world.isAirBlock(x, y + 1, z) &&
                block.isSideSolid(world, x, y, z, ForgeDirection.UP) &&
                block.getMaterial() != Material.wood &&
                block.getMaterial() != Material.cloth) {

                if (ignite) {
                    FireStartingEntityScanner scanner = new FireStartingEntityScanner(world, x, y, z);
                    int sticks = 0;
                    for (EntityItem entityItem : scanner.getEntities()) {
                        if (OreDictionaryHelper.itemStackIsOre(entityItem.getEntityItem(), "stickWood")) {
                            sticks += entityItem.getEntityItem().stackSize;
                        }

                        if (OreDictionaryHelper.itemStackIsOre(entityItem.getEntityItem(), "stickWoodBundle")) {
                            sticks += entityItem.getEntityItem().stackSize * 3;
                        }

                        if (sticks >= 3) {
                            break;
                        }
                    }

                    if (sticks >= 3) {
                        // Sticks are not consumed, instead the fire is started lit but without any fuel
                        // Sticks are collected by the firepit automatically
                        world.setBlock(x, y + 1, z, TFCBlocks.firepit, 1, 2);

                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

}
