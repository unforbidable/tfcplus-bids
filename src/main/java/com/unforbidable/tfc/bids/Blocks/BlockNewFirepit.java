package com.unforbidable.tfc.bids.Blocks;

import java.util.Random;

import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.Blocks.Devices.BlockFirepit;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.Pottery.ItemPotteryBlowpipe;
import com.dunk.tfc.Items.Tools.ItemFirestarter;
import com.dunk.tfc.TileEntities.TEFirepit;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFirepit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockNewFirepit extends BlockFirepit {

    public BlockNewFirepit() {
        super();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX,
            float hitY, float hitZ) {
        // We want the super method to do everything
        // except open the GUI
        // The current solution is to do what super does ourselves
        // and open our own GUI
        // Unfortunately this means any update to TFCPlus firepit
        // needs to be reflected here
        if (!world.isRemote) {
            if (!handleInteraction(world, x, y, z, entityplayer, side)) {

            }

            entityplayer.openGui(TerraFirmaCraft.instance, 20, world, x, y, z);
        }

        return true;
    }

    private boolean handleInteraction(World world, int x, int y, int z, EntityPlayer entityplayer, int side) {
        ItemStack equippedItem = entityplayer.getCurrentEquippedItem();
        TEFirepit te = (TEFirepit) world.getTileEntity(x, y, z);
        if (equippedItem != null) {
            Item item = equippedItem.getItem();

            if (item instanceof ItemPotteryBlowpipe && equippedItem.getItemDamage() == 1
                    && entityplayer.isSneaking()) {
                return true;
            }

            if (!drunkChanceInteract(entityplayer)) {
                // burn the player
                if (te.fireTemp > 100) {
                    entityplayer.attackEntityFrom(DamageSource.inFire, 10);
                }

                return true;
            }

            if (item == Item.getItemFromBlock(TFCBlocks.torchOff)
                    && te.fireTemp > 100) {
                entityplayer.inventory.consumeInventoryItem(Item.getItemFromBlock(TFCBlocks.torchOff));
                TFC_Core.giveItemToPlayer(new ItemStack(TFCBlocks.torch), entityplayer);

                return true;
            }

            if ((item instanceof ItemFirestarter || item instanceof ItemFlintAndSteel)
                    && te.fireTemp < 210 && te.fireItemStacks[5] != null) {
                te.fireTemp = 300;

                if (item instanceof ItemFlintAndSteel) {
                    Random rand = new Random();
                    world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "fire.ignite", 1.0F,
                            rand.nextFloat() * 0.4F + 0.8F);
                }

                equippedItem.damageItem(1, entityplayer);
                world.setBlockMetadataWithNotify(x, y, z, 1, 3);

                return true;
            }
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityNewFirepit();
    }

}
