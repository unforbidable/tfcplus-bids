package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Devices.BlockFirepit;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.Pottery.ItemPotteryBlowpipe;
import com.dunk.tfc.Items.Tools.ItemCustomShovel;
import com.dunk.tfc.Items.Tools.ItemFirestarter;
import com.dunk.tfc.TileEntities.TEFirepit;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsGui;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockNewFirepit extends BlockFirepit {

    private IIcon iconOn;
    private IIcon iconOff;

    public BlockNewFirepit() {
        super();

        setHardness(1);
        setLightLevel(0);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.03125F, 1.0F);
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
                entityplayer.openGui(Bids.instance, BidsGui.newFirepitGui, world, x, y, z);
            }
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

            if (item == Item.getItemFromBlock(TFCBlocks.candleOff)
                && te.fireTemp > 100) {
                entityplayer.inventory.consumeInventoryItem(Item.getItemFromBlock(TFCBlocks.candleOff));
                TFC_Core.giveItemToPlayer(new ItemStack(TFCBlocks.candle), entityplayer);

                return true;
            }

            if ((item instanceof ItemFirestarter || item instanceof ItemFlintAndSteel)
                    && te.fireTemp < 210 && te.fireItemStacks[5] != null) {
                // No longer 100% chance of success
                // and kindling is required
                final ItemStack kindling = te.fireItemStacks[5];
                final IFirepitFuelMaterial fuel = BidsRegistry.FIREPIT_FUEL.get(kindling.getItem());

                if (fuel != null && fuel.getFuelKindlingQuality(kindling) > 0) {
                    float chance = fuel.getFuelKindlingQuality(kindling);

                    if (item instanceof ItemFlintAndSteel) {
                        Random rand = new Random();
                        world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "fire.ignite", 1.0F,
                                rand.nextFloat() * 0.4F + 0.8F);

                        // 100% chance with straw kindling
                        // 50% chance without
                        chance *= 2;
                    } else if (item instanceof ItemFirestarter) {
                        // 100% chance with bark fiber kindling
                        // 50% chance with straw kindling
                        // 25% chance without
                        chance *= 1f;
                    }

                    Bids.LOG.debug("Chance to start fire: " + chance);

                    if (world.rand.nextDouble() < chance) {
                        te.fireTemp = 300;

                        world.setBlockMetadataWithNotify(x, y, z, 1, 3);
                    }

                    equippedItem.damageItem(1, entityplayer);
                }

                return true;
            }

            // Extract ash using a shovel when the fire is out
            if (item instanceof ItemCustomShovel && te.ashNumber > 0 && te.fireTemp <= 1F) {
                TFC_Core.giveItemToPlayer(new ItemStack(TFCItems.powder, te.ashNumber, 13), entityplayer);
                te.ashNumber = 0;

                equippedItem.damageItem(1, entityplayer);

                return true;
            }
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityNewFirepit();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.newFirepitRenderId;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        iconOn = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + "Firepit On");
        iconOff = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + "Firepit Off");
    }

    @Override
    public IIcon getIcon(int side, int damage) {
        if (damage > 0) {
            return iconOn;
        }

        return iconOff;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TEFirepit te = (TEFirepit) world.getTileEntity(x, y, z);
        if (te.fireTemp >= 1) {
            return Math.min(Math.round((te.fireTemp / 750) * 10), 10) + 5;
        } else {
            return 0;
        }
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        // As opposed to TFC firepit
        // only smoke particles are spawned
        // while proper fire is rendered instead

        int meta = world.getBlockMetadata(x, y, z);
        if (meta >= 1) {
            if (rand.nextInt(24) == 0) {
                world.playSoundEffect((double)x, (double)y, (double)z, "fire.fire", 0.4F + rand.nextFloat() / 2.0F, 0.7F + rand.nextFloat());
            }

            float f = (float)x + 0.5F;
            float f1 = (float)y + 0.1F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float)z + 0.5F;
            float f4 = rand.nextFloat() * 0.6F;
            float f5 = rand.nextFloat() * -0.6F;
            if (world.getBlock(x, y + 1, z) == TFCBlocks.vessel) {
                float offset = 0.3F;
                if (rand.nextBoolean()) {
                    world.spawnParticle("smoke", (double)(-offset + f + f4 - 0.3F), (double)f1, (double)(-offset + f2 + f5 + 0.3F), 0.0, 0.0, 0.0);
                    world.spawnParticle("smoke", (double)(offset + f + f5 + 0.3F), (double)f1, (double)(-offset + f2 + f4 - 0.3F), 0.0, 0.0, 0.0);
                } else {
                    world.spawnParticle("smoke", (double)(-offset + f + f4 - 0.3F), (double)f1, (double)(offset + f2 + f5 + 0.3F), 0.0, 0.0, 0.0);
                    world.spawnParticle("smoke", (double)(offset + f + f5 + 0.3F), (double)f1, (double)(offset + f2 + f4 - 0.3F), 0.0, 0.0, 0.0);
                }
            } else {
                world.spawnParticle("smoke", (double)(f + f4 - 0.3F), (double)f1, (double)(f2 + f5 + 0.3F), 0.0, 0.0, 0.0);
                world.spawnParticle("smoke", (double)(f + f5 + 0.3F), (double)f1, (double)(f2 + f4 - 0.3F), 0.0, 0.0, 0.0);
            }
        }

    }

}
