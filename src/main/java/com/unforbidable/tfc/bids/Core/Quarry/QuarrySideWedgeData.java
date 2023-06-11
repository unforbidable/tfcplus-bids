package com.unforbidable.tfc.bids.Core.Quarry;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class QuarrySideWedgeData {

    public final ForgeDirection direction;
    public final List<ItemStack> storage;

    public QuarrySideWedgeData(ForgeDirection direction) {
        this.direction = direction;
        this.storage = new ArrayList<ItemStack>();
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setByte("d", (byte)direction.ordinal());

        NBTTagList itemTagList = new NBTTagList();
        for (ItemStack wedge: storage) {
            NBTTagCompound itemTag = new NBTTagCompound();
            wedge.writeToNBT(itemTag);
            itemTagList.appendTag(itemTag);
        }
        tag.setTag("s", itemTagList);

        // Only for backward compatibility
        tag.setByte("c", (byte)storage.size());
    }

    public static QuarrySideWedgeData readFromNBT(NBTTagCompound tag) {
        ForgeDirection d = ForgeDirection.getOrientation(tag.getByte("d"));

        QuarrySideWedgeData data = new QuarrySideWedgeData(d);

        if (tag.hasKey("s")) {
            NBTTagList itemTagList = tag.getTagList("s", 10);
            for (int i = 0; i < itemTagList.tagCount(); i++) {
                NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
                data.storage.add(ItemStack.loadItemStackFromNBT(itemTag));
            }
        } else {
            int count = tag.getByte("c");

            Bids.LOG.warn("Upgrading quarry wedges count: " + count);

            for (int i = 0; i < count; i++) {
                data.storage.add(new ItemStack(BidsItems.plugAndFeather, 1, 0));
            }
        }

        return data;
    }

}
