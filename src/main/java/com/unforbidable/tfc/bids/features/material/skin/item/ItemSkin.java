package com.unforbidable.tfc.bids.features.material.skin.item;

import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.IBag;
import com.dunk.tfc.api.TFCOptions;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.features.surfaceitem.ItemSurfaceIconAccessor;
import com.unforbidable.tfc.bids.api.names.GuiNames;
import com.unforbidable.tfc.bids.common.item.ItemFoodLike;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import com.unforbidable.tfc.bids.util.GuiUtil;
import com.unforbidable.tfc.bids.util.ItemHelper;
import com.unforbidable.tfc.bids.util.accessor.ItemSpecialCraftingAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ItemSkin extends ItemFoodLike implements IBag, ItemSpecialCraftingAccessor, ItemSurfaceIconAccessor {

    private Item specialCraftingItem;
    private String surfaceIconName;

    public ItemSkin() {
        setMaxStackSize(1);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);

        noMergeRecipes();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        ItemStack specialCraftingType = getSpecialCraftingItemStack(itemstack);
        if (specialCraftingType != null && SkinTag.of(itemstack).getWeight() >= SkinHelper.WEIGHT_SMALL) {
            PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(player);
            pi.specialCraftingType = specialCraftingType;
            pi.specialCraftingTypeAlternate = null;

            GuiUtil.openGui(GuiNames.SKIN, player);
        }

        return itemstack;
    }

    @Override
    public void setSpecialCraftingItem(Item item) {
        this.specialCraftingItem = item;
    }

    @Override
    public Item getSpecialCraftingItem() {
        return specialCraftingItem;
    }

    @Override
    public ItemStack getSpecialCraftingItemStack(ItemStack itemStack) {
        if (getSpecialCraftingItem() != null) {
            return new ItemStack(getSpecialCraftingItem());
        }

        return null;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":skin/"
            + this.getUnlocalizedName().replace("item.", ""));
    }

    @Override
    public IIcon getIconIndex(ItemStack itemStack) {
        return itemIcon;
    }

    @Override
    public EnumWeight getWeight(ItemStack itemStack) {
        return EnumWeight.LIGHT;
    }

    @Override
    public EnumSize getSize(ItemStack itemStack) {
        float weight = SkinTag.of(itemStack).getWeight();
        if (weight >= SkinHelper.WEIGHT_LARGE) {
            return EnumSize.LARGE;
        } else if (weight >= SkinHelper.WEIGHT_MEDIUM) {
            return EnumSize.MEDIUM;
        } else if (weight >= SkinHelper.WEIGHT_SMALL) {
            return EnumSize.SMALL;
        } else if (weight >= SkinHelper.WEIGHT_VERY_SMALL) {
            return EnumSize.VERYSMALL;
        } else {
            return EnumSize.TINY;
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        StringBuilder name = new StringBuilder();

        if (SkinTag.of(itemStack).isSalted()) {
            name.append(TFC_Core.translate("word.salted"))
                .append(' ');
        }

        String stageName = getSkinProcessingStageUnlocalizedName(itemStack);
        if (stageName != null && stageName.length() > 0) {
            name.append(TFC_Core.translate("word." + stageName))
                .append(' ');
        }

        name.append(TFC_Core.translate(this.getUnlocalizedName(itemStack) + ".name"));

        String fluidName = getFluidUnlocalizedName(itemStack);
        if (fluidName != null) {
            name.append(" (")
                .append(TFC_Core.translate(fluidName))
                .append(')');
        }

        return name.toString();
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String animal = SkinTag.of(itemStack).getAnimal();
        if (animal.length() > 0) {
            return super.getUnlocalizedName() + "." + animal;
        }

        return super.getUnlocalizedName();
    }

    protected String getFluidUnlocalizedName(ItemStack itemStack) {
        String fluidName = SkinTag.of(itemStack).getFluid();
        if (fluidName != null && !fluidName.isEmpty()) {
            Fluid fluid = FluidRegistry.getFluid(fluidName);
            if (fluid != null) {
                return fluid.getUnlocalizedName();
            }
        }

        return null;
    }

    protected String getSkinProcessingStageUnlocalizedName(ItemStack itemStack) {
        String stage = SkinTag.of(itemStack).getStage();
        if (stage != null && stage.length() > 0) {
            return stage;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        ItemTerra.addSizeInformation(itemStack, list);

        addSkinInformation(itemStack, player, list);
    }

    protected void addSkinInformation(ItemStack itemStack, EntityPlayer player, List<String> list) {
        SkinTag tag = SkinTag.of(itemStack);

        float weight = tag.getWeight();
        list.add(TFC_Core.translate("gui.skin.amount") + " " + weight + " oz");

        float decay = tag.getDecay();
        if (decay > 0) {
            list.add(EnumChatFormatting.DARK_GRAY + TFC_Core.translate("gui.skin.decay") + " " + Helper.roundNumber(decay / weight * 100, 10) + "%");
        }

        if (TFCOptions.enableDebugMode) {
            list.add(EnumChatFormatting.DARK_GRAY + TFC_Core.translate("gui.food.decay") + ": " + decay);
            list.add(EnumChatFormatting.DARK_GRAY + "Decay Rate: " + Helper.roundNumber(this.getDecayRate(itemStack), 100));
        }

        List<String> help = new ArrayList<>();
        addShiftInformation(itemStack, player, help);
        if (help.size() > 0) {
            if (ItemHelper.showShiftInformation()) {
                list.add(StatCollector.translateToLocal("gui.Help"));
                list.addAll(help);
            } else {
                list.add(StatCollector.translateToLocal("gui.ShowHelp"));
            }
        }
    }

    protected void addShiftInformation(ItemStack itemStack, EntityPlayer player, List<String> list) {
        if (getSpecialCraftingItemStack(itemStack) != null) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Knap"));
        }

        addSkinProcessingStageShiftInformation(itemStack, player, list);
    }

    protected void addSkinProcessingStageShiftInformation(ItemStack itemStack, EntityPlayer player, List<String> list) {
    }

    @Override
    public float getFoodMaxWeight(ItemStack itemStack) {
        return SkinHelper.SKIN_MAX_WEIGHT;
    }

    @Override
    public float getDecayRate(ItemStack itemStack) {
        return SkinHelper.BASE_DECAY_RATE;
    }

    @Override
    public boolean renderDecay() {
        return true;
    }

    @Override
    public boolean renderWeight() {
        return true;
    }

    @Override
    public ItemStack[] loadBagInventory(ItemStack itemStack) {
        return null;
    }

    @Override
    public void setSurfaceIconName(String surfaceIconName) {
        this.surfaceIconName = surfaceIconName;
    }

    @Override
    public String getSurfaceIconName(ItemStack itemStack) {
        if (surfaceIconName != null) {
            return surfaceIconName;
        } else {
            return Tags.MOD_ID + ":surface/skin/" + getSurfaceIconBaseName(itemStack) + "." + getSurfaceIconStageName(itemStack);
        }
    }

    protected String getSurfaceIconBaseName(ItemStack itemStack) {
        return getUnlocalizedName().replace("item.", "");
    }

    protected String getSurfaceIconStageName(ItemStack itemStack) {
        String stage = SkinTag.of(itemStack).getStage();
        if (stage == null || stage.isEmpty()) {
            return "Fresh";
        } else {
            return stage.substring(0, 1).toUpperCase() + stage.substring(1);
        }
    }

}
