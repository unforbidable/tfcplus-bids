package com.unforbidable.tfc.bids.Items.ItemBlocks;

import java.util.List;

import com.dunk.tfc.api.TFC_ItemHeat;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleLiquidStorage;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public abstract class ItemCrucible extends ItemBlock implements ISize {

    public ItemCrucible(Block block) {
        super(block);
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 1;
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.HUGE;
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.HEAVY;
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.SHORT;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);

        NBTTagCompound tag = is.getTagCompound();
        if (tag != null) {
            int liquidTemp = tag.getInteger("liquidTemp");
            CrucibleLiquidStorage liquid = new CrucibleLiquidStorage();
            liquid.readFromNBT(tag);

            if (liquid.getVolume() > 0) {
                for (String line : liquid.getInfo(StatCollector.translateToLocal("gui.symbol.BulletPoint") + " ")) {
                    list.add(EnumChatFormatting.GRAY + line);
                }

                if (liquidTemp > 0) {
                    String liquidTempString = TFC_ItemHeat.getHeatColor(liquidTemp, Integer.MAX_VALUE);
                    list.add(liquidTempString);
                }
            } else {
                list.add(StatCollector.translateToLocal("gui.Empty"));
            }
        }
    }

}
