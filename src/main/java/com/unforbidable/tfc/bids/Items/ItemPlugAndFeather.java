package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.Interfaces.ISmeltable;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;

import com.unforbidable.tfc.bids.api.Interfaces.IExtraSmeltable;
import com.unforbidable.tfc.bids.api.Interfaces.IPlugAndFeather;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemPlugAndFeather extends ItemTerra implements ISize, IPlugAndFeather, IExtraSmeltable {

    private static final Block[] RENDER_BLOCKS = new Block[] {
        TFCBlocks.woodSupportV,
        TFCBlocks.anvil,
        TFCBlocks.anvil,
        TFCBlocks.anvil2,
        TFCBlocks.anvil2,
        TFCBlocks.anvil
    };

    private static final int[] RENDER_METAS = new int[] {
        5,
        1,
        2,
        1,
        2,
        3
    };

    public ItemPlugAndFeather() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMetaNames(new String[] { "Wood", "Copper", "Bronze", "Bismuth Bronze", "Black Bronze", "Wrought Iron"});
    }

    @Override
    public int getPlugAndFeatherQuarryEquipmentTier(ItemStack itemStack) {
        switch (itemStack.getItemDamage()) {
            // copper
            case 1:
                return 1;

            // bronze
            case 2:
            case 3:
            case 4:
                return 2;

            // wrought iron
            case 5:
                return 3;

            // wood
            default:
                return 0;
        }
    }

    @Override
    public float getPlugAndFeatherDropRate(ItemStack itemStack) {
        switch (itemStack.getItemDamage()) {
            // copper
            case 1:
                return 0.90f;

            // bronze
            case 2:
            case 3:
            case 4:
                return 0.95f;

            // wrought iron
            case 5:
                return 0.98f;

            // wood
            default:
                return 0.75f;
        }
    }

    @Override
    public Block getPlugAndFeatherRenderBlock(ItemStack itemStack) {
        return RENDER_BLOCKS[itemStack.getItemDamage()];
    }

    @Override
    public int getPlugAndFeatherRenderBlockMetadata(ItemStack itemStack) {
        return RENDER_METAS[itemStack.getItemDamage()];
    }

    @Override
    public short getMetalReturnAmount(ItemStack is) {
        return 10;
    }

    @Override
    public Metal getMetalType(ItemStack is) {
        switch (is.getItemDamage()) {
            case 1:
                return Global.COPPER;

            case 2:
                return Global.BRONZE;

            case 3:
                return Global.BISMUTHBRONZE;

            case 4:
                return Global.BLACKBRONZE;

            case 5:
                return Global.WROUGHTIRON;

            // wood
            default:
                return Global.GARBAGE;
        }
    }

    @Override
    public EnumTier getSmeltTier(ItemStack is) {
        return EnumTier.TierI;
    }

    @Override
    public boolean isSmeltable(ItemStack is) {
        return true;
    }

    @Override
    public float getPurity(ItemStack is) {
        return 1;
    }

    @Override
    public boolean isNativeOre(ItemStack is) {
        return false;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        metaIcons = new IIcon[metaNames.length];
        for (int i = 0; i < metaNames.length; i++) {
            metaIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":tools/"
                + this.getUnlocalizedName().replace("item.", "") + "." + metaNames[i]);
        }
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.VERYSMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @Override
    public int getItemStackLimit(ItemStack arg0) {
        return 64;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

}
