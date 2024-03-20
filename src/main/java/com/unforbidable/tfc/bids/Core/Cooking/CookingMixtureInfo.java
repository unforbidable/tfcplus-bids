package com.unforbidable.tfc.bids.Core.Cooking;

import com.unforbidable.tfc.bids.Bids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class CookingMixtureInfo {

    private float mixtureWeight = 0f;
    private int[] foodGroups = new int[5];
    private int[] tastes = new int[5];
    private final List<FluidStack> mergedFluids = new ArrayList<FluidStack>();

    public void writeCookingMixtureInfoToNBT(NBTTagCompound nbt) {
        if (nbt != null) {
            if (mixtureWeight > 0) {
                nbt.setFloat("mixtureWeight", mixtureWeight);
            }

            nbt.setIntArray("FG", foodGroups);

            nbt.setInteger("tasteSweet", tastes[0]);
            nbt.setInteger("tasteSour", tastes[1]);
            nbt.setInteger("tasteSalty", tastes[2]);
            nbt.setInteger("tasteBitter", tastes[3]);
            nbt.setInteger("tasteUmami", tastes[4]);

            if (mergedFluids.size() > 0) {
                NBTTagList fluidTagList = new NBTTagList();
                for (FluidStack fluid : mergedFluids) {
                    NBTTagCompound fluidTag = new NBTTagCompound();
                    fluid.writeToNBT(fluidTag);
                    fluidTagList.appendTag(fluidTag);
                }
                nbt.setTag("mergedFluids", fluidTagList);
            }
        } else {
            Bids.LOG.warn("Missing Cooking Mixture NBT tags");
        }
    }

    public static CookingMixtureInfo loadCookingMixtureInfoFromNBT(NBTTagCompound nbt) {
        if (nbt != null) {
            CookingMixtureInfo info = new CookingMixtureInfo();

            info.mixtureWeight = nbt.getFloat("mixtureWeight");

            info.foodGroups = nbt.getIntArray("FG");

            info.tastes = new int[]{
                nbt.getInteger("tasteSweet"),
                nbt.getInteger("tasteSour"),
                nbt.getInteger("tasteSalty"),
                nbt.getInteger("tasteBitter"),
                nbt.getInteger("tasteUmami")
            };

            NBTTagList tagList = nbt.getTagList("mergedFluids", 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(tagList.getCompoundTagAt(i));
                if (fluid != null) {
                    info.mergedFluids.add(fluid);
                }
            }

            return info;
        } else {
            Bids.LOG.warn("Missing Cooking Mixture NBT tags");

            return new CookingMixtureInfo();
        }
    }

    public float getMixtureWeight() {
        return mixtureWeight;
    }

    public void setMixtureWeight(float mixtureWeight) {
        this.mixtureWeight = mixtureWeight;
    }

    public int[] getFoodGroups() {
        return foodGroups;
    }

    public void setFoodGroups(int[] foodGroups) {
        this.foodGroups = foodGroups;
    }

    public int[] getTastes() {
        return tastes;
    }

    public void setTastes(int[] tastes) {
        this.tastes = tastes;
    }

    public List<FluidStack> getMergedFluids() {
        return mergedFluids;
    }

}
