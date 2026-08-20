package com.unforbidable.tfc.bids.features.material.skin.item;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.features.material.skin.SkinConfig;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemFreshSkin extends ItemSkin {

    protected IIcon tannedIcon;
    protected IIcon dehairedIcon;

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        String animal = this == BidsItems.genericFur ? "deerTFC" : (this == BidsItems.genericSkin ? "pigTFC" : null);

        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_FLESHED, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN, tag -> tag.setAnimal(animal).setSalted()));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_PRESERVED, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_PREPARED, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_PREPARED, tag -> tag.setAnimal(animal).setFluid(TFCFluids.LIMEWATER.getName())));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_DEHAIRED, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_TANNED, tag -> tag.setAnimal(animal)));

        if (this == BidsItems.genericFur) {
            list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_DEHAIRED));
            list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_TANNED));
        }
    }

    @Override
    public boolean onUpdate(ItemStack is, World world, int x, int y, int z) {
        SkinTag tag = SkinTag.of(is);
        if (tag.isStage(SkinTagAccess.STAGE_CLEAN)) {
            float decayPercent = tag.getDecay() / tag.getWeight();
            if (decayPercent > SkinConfig.hairDamageDecayPercent) {
                tag.setStage(SkinTagAccess.STAGE_PREPARED);
                tag.setDecayTimer((int) (tag.getDecayTimer() + TFC_Time.HOUR_LENGTH));
            }
        }

        return false;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        super.registerIcons(registerer);

        tannedIcon = registerer.registerIcon(Tags.MOD_ID + ":skin/Skin.Tanned");
        dehairedIcon = registerer.registerIcon(Tags.MOD_ID + ":skin/Skin.Dehaired");
    }

    @Override
    public IIcon getIconIndex(ItemStack itemStack) {
        String stage = SkinTag.of(itemStack).getStage();
        if (stage.equals(SkinTagAccess.STAGE_DEHAIRED)) {
            return dehairedIcon;
        } else if (stage.equals(SkinTagAccess.STAGE_TANNED)) {
            return tannedIcon;
        } else {
            return super.getIconIndex(itemStack);
        }
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage == 8) {
            return dehairedIcon;
        } else if (damage == 9) {
            return tannedIcon;
        } else {
            return super.getIconFromDamage(damage);
        }
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
            case SkinTagAccess.STAGE_FLESHED:
                return 1;
            case SkinTagAccess.STAGE_CLEAN:
                return 2;
            case SkinTagAccess.STAGE_PREPARED:
                return 3;
            case SkinTagAccess.STAGE_PRESERVED:
                return 6;
            case SkinTagAccess.STAGE_DEHAIRED:
                return 8;
            case SkinTagAccess.STAGE_TANNED:
                return 9;
        }

        return 0;
    }

    @Override
    public float getDecayRate(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_PRESERVED)) {
            return 0;
        }

        float saltedModifier = tag.isSalted() ? 0.25f : 1f;
        float stageModifier = getSkinProcessingStageDecayMultiplier(itemStack);
        float fluidModifier = getFluidDecayMultiplier(itemStack);

        return super.getDecayRate(itemStack) * saltedModifier * stageModifier * fluidModifier;
    }

    protected float getSkinProcessingStageDecayMultiplier(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_FLESHED)) {
            return 1f / 4;
        }
        if (tag.isStage(SkinTagAccess.STAGE_CLEAN) || tag.isStage(SkinTagAccess.STAGE_PREPARED) || tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) {
            return 1f / 8;
        }
        if (tag.isStage(SkinTagAccess.STAGE_TANNED)) {
            return 1f / 64;
        }

        return 1f;
    }

    protected float getFluidDecayMultiplier(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if ((tag.isStage(SkinTagAccess.STAGE_PREPARED) || tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) &&
            (tag.isFluid(TFCFluids.LIMEWATER.getName()) || tag.isFluid(BidsFluids.weakWoodAshLye.getName()))) {
            return 1f / 8;
        }

        return 1f;
    }

    @Override
    protected String getSurfaceIconBaseName(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage("") || tag.isStage(SkinTagAccess.STAGE_FLESHED) || tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) {
            // Same icon for all skins for fleshing
            // as it looks the same from the flesh side
            // Dehaired skin looks the same from the hair side as well
            return "Skin";
        }

        return super.getSurfaceIconBaseName(itemStack);
    }

    @Override
    protected String getFluidUnlocalizedName(ItemStack itemStack) {
        if (SkinTag.of(itemStack).isFluid(TFCFluids.TANNIN.getName())) {
            // Do not show tannin fluid as the tanned stage is indicative enough
            return null;
        } else {
            return super.getFluidUnlocalizedName(itemStack);
        }
    }

    @Override
    protected void addSkinInformation(ItemStack itemStack, EntityPlayer player, List<String> list) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_CLEAN)) {
            float decayPercent = tag.getDecay() / tag.getWeight();
            if (decayPercent > SkinConfig.hairDamageDecayPercent - SkinConfig.hairDamageDecayPercent * 0.5f) {
                list.add(EnumChatFormatting.RED + StatCollector.translateToLocal("gui.skin.hairDamageWarning"));
                list.add(EnumChatFormatting.RED + StatCollector.translateToLocal("gui.skin.hairDamageWarning2"));
            }
        } else if (tag.isStage(SkinTagAccess.STAGE_PREPARED) && !tag.hasFluid()) {
            list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("gui.skin.hairDamaged"));
            list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("gui.skin.hairDamaged2"));
        }

        super.addSkinInformation(itemStack, player, list);
    }

    @Override
    protected void addSkinProcessingStageShiftInformation(ItemStack itemStack, EntityPlayer player, List<String> list) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage("")) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Fresh"));
        } else if (tag.isStage(SkinTagAccess.STAGE_FLESHED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Fleshed"));
        } else if (tag.isStage(SkinTagAccess.STAGE_CLEAN)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Clean"));
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Clean2"));
        } else if (tag.isStage(SkinTagAccess.STAGE_PRESERVED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Preserved"));
        } else if (tag.isStage(SkinTagAccess.STAGE_PREPARED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Prepared"));
        } else if (tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Dehaired"));
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Dehaired2"));
        } else if (tag.isStage(SkinTagAccess.STAGE_TANNED)) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Tanned"));
        }
    }

}
