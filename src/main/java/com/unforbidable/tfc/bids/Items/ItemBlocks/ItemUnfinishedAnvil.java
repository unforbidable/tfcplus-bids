package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.Interfaces.ISmeltable;
import com.dunk.tfc.api.Metal;
import com.unforbidable.tfc.bids.Blocks.BlockUnfinishedAnvil;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IExtraSmeltable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemUnfinishedAnvil extends ItemBlock implements ISize, ISmeltable, IExtraSmeltable {

    public static final String[] META_NAMES = new String[] { "Stone", "Copper", "Bronze", "Wrought Iron", "Steel", "Black Steel", "Blue Steel", "Red Steel",
        "Rose Gold", "Bismuth Bronze", "Black Bronze" };

    public ItemUnfinishedAnvil(Block block) {
        super(block);

        setMaxStackSize(1);
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        if(is.getItemDamage() < META_NAMES.length)
            return getUnlocalizedName()
                .replace("Stage1", "")
                .replace("Stage2", "")
                .replace("Stage3", "")
                .replace("Stage4", "")
                .replace("Stage5", "")
                .replace("Stage6", "")
                .concat("." + META_NAMES[is.getItemDamage()]);

        return super.getUnlocalizedName(is);
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.LARGE;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.HEAVY;
    }

    @Override
    public int getItemStackLimit(ItemStack arg0) {
        return 1;
    }

    @Override
    public short getMetalReturnAmount(ItemStack itemStack) {
        // Stage 0 = 2x double ingot etc...
        return (short) ((getStage() + 2) * 200);
    }

    @Override
    public Metal getMetalType(ItemStack itemStack) {
        ItemStack finishedAnvil = BlockUnfinishedAnvil.getFinishedAnvil(itemStack.getItemDamage());
        return ((ISmeltable) finishedAnvil.getItem()).getMetalType(finishedAnvil);
    }

    @Override
    public EnumTier getSmeltTier(ItemStack itemStack) {
        ItemStack finishedAnvil = BlockUnfinishedAnvil.getFinishedAnvil(itemStack.getItemDamage());
        return ((ISmeltable) finishedAnvil.getItem()).getSmeltTier(finishedAnvil);
    }

    @Override
    public boolean isSmeltable(ItemStack itemStack) {
        return true;
    }

    @Override
    public float getPurity(ItemStack itemStack) {
        return 1;
    }

    @Override
    public boolean isNativeOre(ItemStack itemStack) {
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(itemStack, list);
        ItemHelper.addHeatInformation(itemStack, list);
        ItemHelper.addHeatStatusInformation(itemStack, list);

        list.add(StatCollector.translateToLocal("gui.Stage" + (getStage() + 1)));

        if (getMetalType(itemStack) != null) {
            if (ItemHelper.showShiftInformation()) {
                list.add(StatCollector.translateToLocal("gui.Units") + ": " + getMetalReturnAmount(itemStack));
            } else {
                list.add(StatCollector.translateToLocal("gui.ShowHelp"));
            }
        }
    }

    public int getStage() {
        BlockUnfinishedAnvil unfinishedAnvil = (BlockUnfinishedAnvil) field_150939_a;
        return unfinishedAnvil.getStage();
    }

}
