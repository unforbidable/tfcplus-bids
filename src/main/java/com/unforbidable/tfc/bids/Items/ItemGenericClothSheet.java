package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.Core.TFC_Textures;
import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.dunk.tfc.Items.ItemLeather;
import com.dunk.tfc.Items.Tools.ItemKnife;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemGenericClothSheet extends ItemLeather {

    ResourceLocation res;
    boolean[][] clothingAlpha;

    public ItemGenericClothSheet() {
        super();

        setCreativeTab(BidsCreativeTabs.tabMaterials);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(player);
        pi.specialCraftingType = new ItemStack(specialCraftingType, 1, itemstack.getItemDamage());
        boolean hasKnife = false;
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            if (player.inventory.mainInventory[i] != null
                    && player.inventory.mainInventory[i].getItem() instanceof ItemKnife) {
                hasKnife = true;
            }
        }

        if (hasKnife) {
            if (specialCraftingTypeAlternate != null)
                pi.specialCraftingTypeAlternate = specialCraftingTypeAlternate;
            else
                pi.specialCraftingTypeAlternate = null;

            player.openGui(TerraFirmaCraft.instance, 28, player.worldObj, (int) player.posX,
                    (int) player.posY, (int) player.posZ);
        }

        return itemstack;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "armor/clothing/"
                + this.getUnlocalizedName().replace("item.", ""));

        res = new ResourceLocation(Tags.MOD_ID, "textures/items/armor/clothing/"
                + this.getUnlocalizedName().replace("item.", "Flat ") + ".png");

        clothingAlpha = TFC_Textures.loadClothingPattern(res);
    }

    @Override
    public ResourceLocation getFlatTexture() {
        return res;
    }

    @Override
    public boolean[][] getClothingAlpha() {
        return clothingAlpha;
    }

}
