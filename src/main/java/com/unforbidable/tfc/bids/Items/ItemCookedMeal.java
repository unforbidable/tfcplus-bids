package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.Player.FoodStatsTFC;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Sounds;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Food.ItemMeal;
import com.dunk.tfc.Items.ItemDrink;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.FoodRegistry;
import com.dunk.tfc.api.TFCCrafting;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.Cooking.CookingMixtureHelper;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.Interfaces.ICookedMeal;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ItemCookedMeal extends ItemMeal implements ICookedMeal {

    protected final float[] ingredientWeights = new float[] { 0, 10, 4, 4, 2 };
    protected final float foodMaxWeight = 20;

    public ItemCookedMeal() {
        setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        StringBuilder sb = new StringBuilder();

        String mainIngredientName = CookingMixtureHelper.getMainIngredientName(is);
        if (mainIngredientName != null) {
            // Add main ingredient name before the item name
            sb.append(mainIngredientName);
            sb.append(' ');
        }

        sb.append(super.getItemStackDisplayName(is));

        return sb.toString();
    }

    @Override
    public int getItemStackLimit(ItemStack is)
    {
        return 1;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        if (this.metaNames == null) {
            if (this.iconString != null)
                this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "food/" + this.getIconString());
            else
                this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "food/" + this.getUnlocalizedName().replace("item.", ""));
        } else {
            metaIcons = new IIcon[metaNames.length];
            for (int i = 0; i < metaNames.length; i++) {
                metaIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + "food/" + metaNames[i]);
            }

            this.itemIcon = metaIcons[0];
        }
    }

    @Override
    public float[] getFoodWeights() {
        return ingredientWeights;
    }

    @Override
    public float getFoodMaxWeight(ItemStack is) {
        return foodMaxWeight;
    }

    @Override
    public void onCookedMealCreated(ItemStack is, EntityPlayer player) {
        // Cooked meals stay fresh for 1 day but after that they rapidly decay
        Food.setDecayTimer(is, (int) TFC_Time.getTotalHours() + 24);
        Food.setDecayRate(is, 4f);
    }

    @Override
    protected void addFGInformation(ItemStack is, List<String> arraylist) {
        // Before adding food group names
        // add liquid names
        List<FluidStack> fluids = CookingMixtureHelper.getCookedMealFluids(is);
        for (FluidStack fluid : fluids) {
            EnumFoodGroup foodGroup = getFluidFoodGroup(fluid);
            if (foodGroup != null && foodGroup != EnumFoodGroup.None) {
                arraylist.add(ItemFoodTFC.getFoodGroupColor(foodGroup) + getFluidString(fluid));
            } else {
                //arraylist.add(EnumChatFormatting.AQUA + getFluidString(fluid));
            }
        }

        super.addFGInformation(is, arraylist);
    }

    protected String getFluidString(FluidStack fluid) {
        return TFC_Core.translate(fluid.getLocalizedName());
    }

    private EnumFoodGroup getFluidFoodGroup(FluidStack fluid) {
        // Try to put the fluid into a bottle
        // and if it is drinkable, get the food group
        // At the time of writing, only milk and beer have food group
        ItemStack fluidBottle = FluidContainerRegistry.fillFluidContainer(fluid, new ItemStack(TFCItems.glassBottle));
        if (fluidBottle != null && fluidBottle.getItem() instanceof ItemDrink) {
            return ((ItemDrink) fluidBottle.getItem()).getFoodGroup();
        }

        return null;
    }

    @Override
    protected float getFillingBoost() {
        return 1.5f;
    }

    @Override
    public ItemStack onEaten(ItemStack is, World world, EntityPlayer player) {
        world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        FoodStatsTFC foodstats = TFC_Core.getPlayerFoodStats(player);
        if (!world.isRemote) {
            if (is.hasTagCompound()) {
                float weight = Food.getWeight(is);
                float decay = Math.max(Food.getDecay(is), 0.0F);
                float eatAmount = this.getEatAmount(foodstats, weight - decay);
                float tasteFactor = foodstats.getTasteFactor(is);
                int[] fg = Food.getFoodGroups(is);
                float[] nWeights = this.getNutritionalWeights(fg);

                List<FluidStack> fluids = CookingMixtureHelper.getCookedMealFluids(is);

                // Fluid has been added which reduces the nutrition gain
                float nutritionMultiplier = fluids.size() > 0 ? 0.5f : 1f;

                for(int i = 0; i < fg.length; ++i) {
                    if (fg[i] != -1) {
                        foodstats.addNutrition(FoodRegistry.getInstance().getFoodGroup(fg[i]), eatAmount * nWeights[i] * 2.5F * nutritionMultiplier);
                    }
                }

                int waterToRestore = 0;
                for (FluidStack fluid : fluids) {
                    EnumFoodGroup foodGroup = getFluidFoodGroup(fluid);
                    if (foodGroup != null && foodGroup != EnumFoodGroup.None) {
                        // Any fluids that have food group
                        // contribute to the nutrition
                        foodstats.addNutrition(foodGroup, eatAmount * getFoodMaxWeight(is) * 0.5F * (1f / fluids.size()) * 2.5F);
                    }

                    // All fluids contribute to water restore
                    waterToRestore += (int) Math.floor(eatAmount / getFoodMaxWeight(is) * fluid.amount * 8);
                }

                if (waterToRestore > 0) {
                    foodstats.restoreWater(player, waterToRestore);
                }

                foodstats.stomachLevel += eatAmount * getFillingBoost();
                foodstats.setSatisfaction(foodstats.getSatisfaction() + eatAmount / 3.0F * tasteFactor, fg);
                if (FoodStatsTFC.reduceFood(is, eatAmount)) {
                    is.stackSize = 0;
                }
            } else {
                String error = TFC_Core.translate("error.error") + " " + is.getUnlocalizedName() + " " + TFC_Core.translate("error.NBT") + " " + TFC_Core.translate("error.Contact");
                TerraFirmaCraft.LOG.error(error);
                TFC_Core.sendInfoMessage(player, new ChatComponentText(error));
            }
        }

        TFC_Core.setPlayerFoodStats(player, foodstats);

        // If the last of the cooked meal has been eaten
        if (is.stackSize == 0) {
            return returnEmptyContainer(world, player, is);
        } else {
            return is;
        }
    }

    protected ItemStack returnEmptyContainer(World world, EntityPlayer player, ItemStack is) {
        // Blows always break OR 50% chance the bowl is broken, and the sound is played
        if ((TFCCrafting.enableBowlsAlwaysBreak || world.rand.nextInt(50) == 0) && is.stackTagCompound.getInteger("bowlMeta")==1 )
        {
            world.playSoundAtEntity(player, TFC_Sounds.CERAMICBREAK, 0.7f, player.worldObj.rand.nextFloat() * 0.2F + 0.8F);
        }
        // If the bowl didn't break, try to add it to an existing stack of bowls in the inventory
        else if (is.stackTagCompound.getInteger("bowlMeta")==1 && !player.inventory.addItemStackToInventory(new ItemStack(TFCItems.potteryBowl, 1, 1)))
        {
            // If the bowl can't be fit in the inventory, put it in the newly emptied held slot
            return new ItemStack(TFCItems.potteryBowl, 1, 1);
        }
        else if (is.stackTagCompound.getInteger("bowlMeta")==2 && !player.inventory.addItemStackToInventory(new ItemStack(TFCItems.potteryBowl, 1, 2)))
        {
            // If the bowl can't be fit in the inventory, put it in the newly emptied held slot
            return new ItemStack(TFCItems.potteryBowl, 1, 2);
        }

        return is;
    }

}
