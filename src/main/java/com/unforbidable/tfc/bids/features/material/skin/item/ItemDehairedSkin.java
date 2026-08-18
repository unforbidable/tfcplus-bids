package com.unforbidable.tfc.bids.features.material.skin.item;

import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import java.util.List;

public class ItemDehairedSkin extends ItemFreshSkin {

    protected IIcon tannedIcon;

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_DEHAIRED));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_TANNED));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_DEHAIRED, tag -> tag.setAnimal("deerTFC")));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_TANNED, tag -> tag.setAnimal("deerTFC")));
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        super.registerIcons(registerer);

        tannedIcon = registerer.registerIcon(Tags.MOD_ID + ":skin/"
            + this.getUnlocalizedName().replace("item.", "") + ".Tanned");
    }

    @Override
    public IIcon getIconIndex(ItemStack itemStack) {
        if (SkinTag.of(itemStack).getStage().equals(SkinTagAccess.STAGE_TANNED)) {
            return tannedIcon;
        } else {
            return super.getIconIndex(itemStack);
        }
    }

    @Override
    public int getDamage(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_TANNED)) {
            return 1;
        }

        return 0;
    }

    @Override
    protected void addSkinProcessingStageShiftInformation(ItemStack itemStack, EntityPlayer player, List<String> list) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Dehaired"));
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Dehaired2"));
        } else if (tag.isStage(SkinTagAccess.STAGE_TANNED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Tanned"));
        }
    }

}
