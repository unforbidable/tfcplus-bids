package com.unforbidable.tfc.bids.features.crafting.firestarting.item;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.common.item.ItemCommonMisc;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHelper;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTinder extends ItemCommonMisc {

    public ItemTinder() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        return FireStartingHelper.getTinderDisplayNamePrefix(is) + super.getItemStackDisplayName(is);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (FireStartingHelper.isTinderSmoldering(entityItem.getEntityItem())) {
            int xCoord = (int) Math.floor(entityItem.posX);
            int yCoord = (int) Math.floor(entityItem.posY);
            int zCoord = (int) Math.floor(entityItem.posZ);

            Block block = entityItem.worldObj.getBlock(xCoord, yCoord, zCoord);
            boolean atWetBlock = TFC_Core.isWater(block) ||
                TFC_Core.isHotWater(block) ||
                TFC_Core.isExposedToRain(entityItem.worldObj, xCoord, yCoord, zCoord);

            if (entityItem.age > 40 && entityItem.age % 20 == 0) {
                if (!atWetBlock) {
                    int yCoordActual = yCoord - (int) Math.round(block.getBlockBoundsMaxY());

                    Bids.LOG.info("onEntityItemUpdate - starting fire attempt at {},{},{}", xCoord, yCoordActual, zCoord);
                    boolean ignited = BidsEventFactory.onFireStartingIgnite(null, entityItem.worldObj, xCoord, yCoordActual, zCoord, 1);
                    if (ignited) {
                        Bids.LOG.info("onEntityItemUpdate - fire started");
                        entityItem.setDead();
                    }
                }
            } else if (entityItem.age > 200) {
                Bids.LOG.info("onEntityItemUpdate - spent");
                entityItem.setDead();
            }

            if (!atWetBlock && entityItem.worldObj.isRemote && entityItem.age > 40 && entityItem.age % 5 == 0) {
                double x = entityItem.posX;
                double y = entityItem.posY;
                double z = entityItem.posZ;

                Random random = new Random();
                double dx = random.nextDouble() * 0.4 - 0.2;
                double dz = random.nextDouble() * 0.4 - 0.2;
                entityItem.worldObj.spawnParticle("smoke", x + dx, y + 0.1, z + dz, 0.0, 0.0, 0.0);
            }
        }

        return super.onEntityItemUpdate(entityItem);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side,
                             float hitX, float hitY, float hitZ) {
        if (!world.isRemote && player.isSneaking()) {
            if (FireStartingHelper.isTinderSmoldering(itemStack)) {
                boolean ignited = BidsEventFactory.onFireStartingIgnite(player, world, x, y, z, side);
                if (ignited) {
                    itemStack.stackSize--;
                    Bids.LOG.info("onItemUse - fire started");
                    return true;
                }
            } else {
                boolean canPropagate = BidsEventFactory.onFireStartingPropagate(player, world, x, y, z, side);
                if (canPropagate) {
                    Bids.LOG.info("onItemUse - ignite tinder");

                    ItemStack smolderingTinder = itemStack.copy();
                    smolderingTinder.stackSize = 1;
                    FireStartingHelper.setTinderSmolderingTemperature(smolderingTinder, true);
                    TFC_Core.giveItemToPlayer(smolderingTinder, player);

                    itemStack.stackSize--;

                    return true;
                }
            }
        }

        return false;
    }

}
