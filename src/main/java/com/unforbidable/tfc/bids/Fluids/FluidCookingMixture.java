package com.unforbidable.tfc.bids.Fluids;

import com.dunk.tfc.Core.FluidBaseTFC;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.Cooking.CookingMixtureHelper;
import com.unforbidable.tfc.bids.Core.Cooking.CookingMixtureInfo;
import com.unforbidable.tfc.bids.api.Crafting.CookingMixture;
import com.unforbidable.tfc.bids.api.Interfaces.ICookingMixtureFluid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FluidCookingMixture extends FluidBaseTFC implements ICookingMixtureFluid {

    public FluidCookingMixture(String fluidName) {
        super(fluidName);
    }

    @Override
    public int getColor(FluidStack fs) {
        CookingMixture mixture = CookingMixtureHelper.getCookingMixture(fs);
        if (mixture != null) {
            return mixture.getColor();
        }

        return super.getColor(fs);
    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        StringBuilder sb = new StringBuilder();

        CookingMixture mixture = CookingMixtureHelper.getCookingMixture(stack);
        if (mixture != null) {
            if (!mixture.isUseDefaultName()) {
                Item mainIngredient = CookingMixtureHelper.getMainIngredient(stack);
                if (mainIngredient != null) {
                    // The name of the main ingredient first
                    // May be overridden
                    sb.append(new ItemStack(mainIngredient).getDisplayName());

                    if (mixture.isReady()) {
                        // For mixtures that are ready meals to be retrieved,
                        // translate the base name of the cooked meal item
                        sb.append(' ');
                        sb.append(StatCollector.translateToLocal(mixture.getCookedMeal().getUnlocalizedName() + ".name"));
                    } else {
                        List<FluidStack> fluids = CookingMixtureHelper.getCookingFluidMergedFluids(stack);
                        for (FluidStack fluid : fluids) {
                            if (sb.length() > 0) {
                                sb.append(" + ");
                            }
                            sb.append(fluid.getLocalizedName());
                        }

                        if (sb.length() > 0) {
                            sb.append(' ');
                        }
                        sb.append(super.getLocalizedName(stack));
                    }

                    return sb.toString();
                }
            }

            // Normally cooking mixture fluid will have ingredients
            // but for example NEI will be able to not see any
            // In such cases we use the translated name of the mixture
            // even if CookingMixture.isUseDefaultName is false

            return StatCollector.translateToLocal("cookingMixture." + mixture.getName());
        } else {
            // Cooking mixture name is not recognised
            return super.getLocalizedName(stack);
        }
    }

    @Override
    public void onCookingFluidPlaced(FluidStack fluid) {
    }

    @Override
    public void onCookingFluidCombined(FluidStack cookingFluid, FluidStack originalFluid, FluidStack combinedFluid) {
        // Combine tags
        CookingMixtureHelper.combineCookingMixtureTags(cookingFluid, combinedFluid);
    }

    @Override
    public void onCookingFluidMerged(FluidStack cookingFluid, FluidStack originalFluid, FluidStack addedFluid) {
        // Preserve the whole tag of the original fluid
        CookingMixtureHelper.copyCookingMixtureTags(cookingFluid, originalFluid);

        // And save the added fluid
        CookingMixtureHelper.addCookingFluidMergedFluid(cookingFluid, addedFluid);
    }

    @Override
    public void onCookingFluidCooked(FluidStack cookingFluid, FluidStack originalFluid) {
        // Preserve the whole tag of the original fluid
        CookingMixtureHelper.copyCookingMixtureTags(cookingFluid, originalFluid);
    }

    @Override
    public boolean isValidCookedMealContainer(FluidStack cookingFluid, ItemStack container) {
        return container.getItem() == TFCItems.potteryBowl && container.getItemDamage() > 0;
    }

    @Override
    public ItemStack retrieveCookedMeal(FluidStack cookingFluid, ItemStack container, EntityPlayer player, boolean consumeFluid) {
        CookingMixture mixture = CookingMixtureHelper.getCookingMixture(cookingFluid);
        int consumedAmount = getCookingFluidConsumedAmount(cookingFluid, container);

        if (mixture != null && mixture.isReady() && cookingFluid.amount >= consumedAmount) {
            ItemStack cookedMeal = mixture.getCookedMeal().copy();
            cookedMeal.stackSize = 1;

            // The original weight of the cooking mix affects the cooked meal weight
            CookingMixtureInfo info = CookingMixtureInfo.loadCookingMixtureInfoFromNBT(cookingFluid.tag);
            float weight = info.getMixtureWeight();
            float consumedWeight = getCookingFluidConsumedWeight(cookingFluid, container);
            float actuallyConsumedWeight = Math.min(weight, consumedWeight);
            float remainingWeight = weight - actuallyConsumedWeight;

            ItemFoodTFC.createTag(cookedMeal, actuallyConsumedWeight);

            CookingMixtureHelper.copyCookingMixtureTags(cookedMeal, cookingFluid);

            onCookedMealRetrieved(cookedMeal, cookingFluid, container);

            if (consumeFluid) {
                // Update the weight and amount
                info.setMixtureWeight(remainingWeight);
                info.writeCookingMixtureInfoToNBT(cookingFluid.tag);

                if (remainingWeight == 0) {
                    // Making sure there is no liquid left when weight has been consumed
                    // but that should always be the case
                    cookingFluid.amount = 0;
                } else {
                    cookingFluid.amount -= consumedAmount;
                }
            }

            return cookedMeal;
        } else {
            return null;
        }
    }

    @Override
    public boolean areCookingFluidsEqual(FluidStack cookingFluid, FluidStack anotherCookingFluid) {
        return cookingFluid.getFluid() == anotherCookingFluid.getFluid() &&
            CookingMixtureHelper.areCookingMixtureNamesEqual(cookingFluid, anotherCookingFluid);
    }

    protected void onCookedMealRetrieved(ItemStack cookedMeal, FluidStack cookingFluid, ItemStack container) {
        cookedMeal.getTagCompound().setInteger("bowlMeta", container.getItemDamage());
    }

    protected int getCookingFluidConsumedAmount(FluidStack cookingFluid, ItemStack container) {
        return 250;
    }

    protected float getCookingFluidConsumedWeight(FluidStack cookingFluid, ItemStack container) {
        return 20f;
    }

}
