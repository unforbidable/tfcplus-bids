package com.unforbidable.tfc.bids.util.harvest;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class HarvestHelper {

    public static void harvestStraw(EntityPlayer player, World world, int x, int y, int z, Block block, int metadata) {
        world.setBlockToAir(x, y, z);

        ItemStack is = player.inventory.getCurrentItem();
        if (is != null) {
            if (is.getItem() == TFCItems.stoneFlake) {
                createStraw(world, player, x, y, z);
                if (world.rand.nextInt(4) == 0) {
                    is.stackSize--;
                }
            } else {
                // If the tall grass block has no hardness
                // tools do not take damage automatically
                float hardness = block.getBlockHardness(world, x, y, z);
                if (hardness == 0) {
                    is.damageItem(1, player);
                }

                if (OreDictionaryHelper.itemStackIsOre(is, "itemScythe")) {
                    createStraw(world, player, x, y, z);
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            if (world.getBlock(x + i, y, z + j) == block) {
                                createStraw(world, player, x + i, y, z + j);
                                world.setBlockToAir(x + i, y, z + j);
                                is.damageItem(1, player);
                                if (is.stackSize == 0) {
                                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                                    break;
                                }
                            }
                        }
                    }
                } else if (ForgeHooks.isToolEffective(is, block, metadata)) {
                    createStraw(world, player, x, y, z);
                }
            }
        }
    }

    private static void createStraw(World world, EntityPlayer player, int x, int y, int z) {
        EntityItem ei = new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, new ItemStack(TFCItems.straw));
        world.spawnEntityInWorld(ei);
    }

}
