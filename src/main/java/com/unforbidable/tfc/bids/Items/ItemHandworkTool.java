package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Handwork.HandworkHelper;
import com.unforbidable.tfc.bids.Core.Handwork.HandworkProgress;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public abstract class ItemHandworkTool extends ItemTerra implements ISize {

    IIcon[] overlayIcons;

    public ItemHandworkTool() {
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(1);
        setFolder("tools");
    }

    protected abstract int getNumStages();

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        HandworkProgress progress = HandworkHelper.loadHandworkProgress(stack);
        return progress != null;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        HandworkProgress progress = HandworkHelper.loadHandworkProgress(stack);
        if (progress != null) {
            return 1 - (progress.stage / (double) (getNumStages() - 1));
        }

        return 0;
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 1;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" +
            getUnlocalizedName().replace("item.", ""));
        overlayIcons = new IIcon[getNumStages()];
        for (int i = 0; i < getNumStages(); i++) {
            overlayIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + "tools/" +
                getUnlocalizedName().replace("item.", "") + ".Overlay" + i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0) {
            return itemIcon;
        } else if (pass == 1) {
            HandworkProgress progress = HandworkHelper.loadHandworkProgress(stack);
            if (progress != null) {
                return overlayIcons[Math.min(progress.stage, getNumStages() - 1)];
            } else {
                return itemIcon;
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack is, int pass) {
        if (pass == 1) {
            HandworkProgress progress = HandworkHelper.loadHandworkProgress(is);
            if (progress != null) {
                return HandworkHelper.getColorFromMaterial(progress.outputItem, pass);
            }
        }

        return super.getColorFromItemStack(is, pass);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack is) {
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack is) {
        HandworkProgress progress = HandworkHelper.loadHandworkProgress(is);
        if (progress != null) {
            // Duration per stage
            return Math.round((float)(progress.duration) / (getNumStages() - 1));
        } else {
            return 20;
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (!player.worldObj.isRemote) {
            if (count <= 1 && player.isUsingItem()) {
                HandworkProgress progress = HandworkHelper.loadHandworkProgress(stack);
                if (progress != null) {
                    progress.stage++;
                    HandworkHelper.writeHandworkProgress(stack, progress);
                }
                player.stopUsingItem();
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (!player.isUsingItem()) {
            HandworkProgress currentProgress = HandworkHelper.loadHandworkProgress(is);
            if (currentProgress != null) {
                if (currentProgress.stage >= getNumStages() - 1) {
                    if (!world.isRemote && player.isSneaking()) {
                        ItemStack outputItem = currentProgress.outputItem;
                        TFC_Core.giveItemToPlayer(outputItem, player);
                        HandworkHelper.clearHandworkProgress(is);
                    }
                } else {
                    player.setItemInUse(is, this.getMaxItemUseDuration(is));
                }
            } else {
                if (!world.isRemote && !player.isSneaking()) {
                    if (player.inventory.currentItem < 9) {
                        // Check adjacent (right side) slot
                        int i = player.inventory.currentItem + 1;
                        ItemStack ingredient = player.inventory.getStackInSlot(i);
                        if (ingredient != null) {
                            HandworkRecipe recipe = tryMatchIngredient(ingredient);
                            if (recipe != null && ingredient.stackSize >= recipe.getInput().stackSize) {
                                player.inventory.decrStackSize(i, recipe.getInput().stackSize);
                                HandworkProgress progress = new HandworkProgress(recipe.getResult(ingredient), recipe.getDuration(), 0);
                                HandworkHelper.writeHandworkProgress(is, progress);
                            }
                        }
                    }
                }
            }
        }

        return is;
    }

    protected abstract HandworkRecipe tryMatchIngredient(ItemStack is);

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        HandworkProgress progress = HandworkHelper.loadHandworkProgress(is);
        if (progress != null) {
            return super.getItemStackDisplayName(is) + " (" + progress.outputItem.getDisplayName() + ")";
        } else {
            return super.getItemStackDisplayName(is);
        }
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
        super.addExtraInformation(is, player, arraylist);

        if (ItemHelper.showShiftInformation()) {
            arraylist.add(StatCollector.translateToLocal("gui.Help"));

            HandworkProgress progress = HandworkHelper.loadHandworkProgress(is);
            if (progress != null) {
                if (progress.stage < getNumStages() - 1) {
                    arraylist.add(getHandworkProcessHelpString(progress));
                } else {
                    arraylist.add(getHandworkFinishHelpString(progress));
                }
            } else {
                arraylist.add(getHandworkStartHelpString());
            }
        } else {
            arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

    protected abstract String getHandworkStartHelpString();

    protected abstract String getHandworkProcessHelpString(HandworkProgress progress);

    protected abstract String getHandworkFinishHelpString(HandworkProgress progress);

}
