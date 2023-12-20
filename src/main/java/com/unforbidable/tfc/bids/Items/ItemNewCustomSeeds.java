package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemCustomSeeds;
import com.dunk.tfc.TileEntities.TECrop;
import com.dunk.tfc.TileEntities.TEFarmland;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.Crops.BidsCropIndex;
import com.unforbidable.tfc.bids.Core.Crops.CropCultivation;
import com.unforbidable.tfc.bids.Core.Crops.CropHelper;
import com.unforbidable.tfc.bids.Core.Crops.BidsCropManager;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemNewCustomSeeds extends ItemCustomSeeds {

    protected final int cropId;

    public ItemNewCustomSeeds(int cropId) {
        super(cropId);

        this.cropId = cropId;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":food/"
            + this.getUnlocalizedName().replace("item.", ""));
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (side == 1 && !world.isRemote) {
            if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack)) {
                Block var8 = world.getBlock(x, y, z);
                TileEntity tef = world.getTileEntity(x, y, z);
                if ((var8 == TFCBlocks.tilledSoil || var8 == TFCBlocks.tilledSoil2) && world.isAirBlock(x, y + 1, z) && tef instanceof TEFarmland && !((TEFarmland) tef).fallow) {

                    BidsCropIndex crop = BidsCropManager.findCropById(cropId);

                    if (crop.needsSunlight && !TECrop.hasSunlight(world, x, y + 1, z)) {
                        TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("gui.seeds.failedSun"));
                        return false;
                    } else if (crop.requiresLadder && !player.inventory.hasItem(Item.getItemFromBlock(Blocks.ladder))) {
                        TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("gui.seeds.failedLadder"));
                        return false;
                    } else if (crop.requiresPole && !player.inventory.hasItem(TFCItems.pole)) {
                        TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("gui.seeds.failedPole"));
                        return false;
                    } else if (TFC_Climate.getHeightAdjustedTemp(world, x, y, z) <= crop.minAliveTemp && !crop.dormantInFrost) {
                        TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("gui.seeds.failedTemp"));
                        return false;
                    } else {
                        CropHelper.placeNewFarmlandAt(world, x, y, z);

                        world.setBlock(x, y + 1, z, BidsBlocks.newCrops);

                        TECrop te = (TECrop)world.getTileEntity(x, y + 1, z);
                        te.cropId = this.cropId;
                        world.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
                        world.markBlockForUpdate(x, y, z);
                        --stack.stackSize;
                        if (crop.requiresLadder) {
                            player.inventory.consumeInventoryItem(Item.getItemFromBlock(Blocks.ladder));
                            player.inventoryContainer.detectAndSendChanges();
                        } else if (crop.requiresPole) {
                            player.inventory.consumeInventoryItem(TFCItems.pole);
                            player.inventoryContainer.detectAndSendChanges();
                        }

                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
        super.addInformation(is, player, arraylist, flag);

        BidsCropIndex crop = BidsCropManager.findCropById(cropId);
        if (crop.requiresLadder) {
            arraylist.add(EnumChatFormatting.GRAY + TFC_Core.translate("gui.SeedRequiresLadder"));
        } else if (crop.requiresPole) {
            arraylist.add(EnumChatFormatting.GRAY + TFC_Core.translate("gui.SeedRequiresPole"));
        }

        if (crop.cropCultivation != null) {
            for (CropCultivation cultivation : crop.cropCultivation) {
                ItemStack seedItemStack = new ItemStack(cultivation.getCultivatedSeedItem());
                arraylist.add(EnumChatFormatting.GRAY + TFC_Core.translate("gui.SeedCultivation") + " " + EnumChatFormatting.WHITE + seedItemStack.getDisplayName());
            }
        }

        if (crop.dormantInFrost) {
            arraylist.add(EnumChatFormatting.GRAY + TFC_Core.translate("gui.SeedAutumnSowing"));
        }
    }

}
