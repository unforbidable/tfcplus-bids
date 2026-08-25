package com.unforbidable.tfc.bids.features.material.skin.item;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class ItemDehairedSkin extends ItemFreshSkin {

    protected IIcon dehairedIcon;
    protected IIcon tannedIcon;
    protected IIcon driedIcon;
    protected IIcon workedIcon;

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_MEDIUM, SkinTagAccess.STAGE_DEHAIRED));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_DEHAIRED));
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        dehairedIcon = registerer.registerIcon(Tags.MOD_ID + ":skin/Dehaired Skin.Dehaired");
        tannedIcon = registerer.registerIcon(Tags.MOD_ID + ":skin/Dehaired Skin.Tanned");
        driedIcon = registerer.registerIcon(Tags.MOD_ID + ":skin/Dehaired Skin.Dried");
        workedIcon = registerer.registerIcon(Tags.MOD_ID + ":skin/Dehaired Skin.Worked");
    }

    @Override
    public IIcon getIconIndex(ItemStack itemStack) {
        return getIconFromDamage(itemStack.getItemDamage());
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        switch (damage) {
            case 0:
                return dehairedIcon;
            case 1:
                return tannedIcon;
            case 2:
                return driedIcon;
            case 3:
                return workedIcon;
        }

        return dehairedIcon;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIconIndex(stack);
    }

    @Override
    public ItemStack getSpecialCraftingItemStack(ItemStack itemStack) {
        if (SkinTag.of(itemStack).isStage(SkinTagAccess.STAGE_PRESERVED)) {
            return super.getSpecialCraftingItemStack(itemStack);
        }

        return null;
    }

    @Override
    public int getDamage(ItemStack itemStack) {
        String stage = SkinTag.of(itemStack).getStage();
        switch (stage) {
            case SkinTagAccess.STAGE_DEHAIRED:
                return 0;
            case SkinTagAccess.STAGE_TANNED:
                return 1;
            case SkinTagAccess.STAGE_DRIED:
                return 2;
            case SkinTagAccess.STAGE_WORKED:
                return 3;
        }

        //Bids.LOG.warn("Wrong stage {} for item {}", stage, itemStack);

        return 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName(itemStack).replace("item.Dehaired Skin", "item.Skin");
    }

    @Override
    protected float getSkinProcessingStageDecayMultiplier(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) {
            return 1f / 8;
        }
        if (tag.isStage(SkinTagAccess.STAGE_TANNED) || tag.isStage(SkinTagAccess.STAGE_DRIED) || tag.isStage(SkinTagAccess.STAGE_WORKED)) {
            return 1f / 64;
        }

        return 1f;
    }

    @Override
    protected String getSurfaceIconBaseName(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_DEHAIRED) || tag.isStage(SkinTagAccess.STAGE_DRIED) || tag.isStage(SkinTagAccess.STAGE_WORKED)) {
            return "Dehaired Skin." + getSurfaceIconStageName(itemStack);
        }

        return super.getSurfaceIconBaseName(itemStack);
    }

    @Override
    protected void addSkinProcessingStageShiftInformation(ItemStack itemStack, EntityPlayer player, List<String> list) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Dehaired"));
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Dehaired2"));
        } else if (tag.isStage(SkinTagAccess.STAGE_TANNED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Tanned"));
        } else if (tag.isStage(SkinTagAccess.STAGE_DRIED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Dried"));
        } else if (tag.isStage(SkinTagAccess.STAGE_WORKED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Worked"));
        }
    }

}
