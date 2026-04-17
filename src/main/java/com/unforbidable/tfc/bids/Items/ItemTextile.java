package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemTerra;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Textile.EnumTextileHint;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsEventFactory;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IHandworkToolMaterial;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemTextile extends ItemTerra implements IHandworkToolMaterial {

    private boolean hasMaterialColor = false;
    private int materialColor = 0;
    private final Set<EnumTextileHint> hints = new HashSet<EnumTextileHint>();

    public ItemTextile() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setFolder("textile");
    }

    public ItemTextile setMaterialColor(int materialColor) {
        this.materialColor = materialColor;
        this.hasMaterialColor = true;
        return this;
    }

    public ItemTextile addHint(EnumTextileHint hint) {
        this.hints.add(hint);
        return this;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" +
            getUnlocalizedName().replace("item.", ""));
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (!player.worldObj.isRemote) {
            if (count % 5 == 1) {
                player.worldObj.playSoundAtEntity(player, "step.grass", 0.5F, 1f);
            }

            if (count <= 1 && player.isUsingItem()) {
                HandworkRecipe recipe = BidsRegistry.HANDWORK_RECIPES.findMatchingRecipe(stack);
                if (recipe != null) {
                    ItemStack outputItem = recipe.getResult(stack);
                    BidsEventFactory.onHandworkItemCrafted(player, stack, outputItem, null);

                    TFC_Core.giveItemToPlayer(outputItem, player);
                    stack.stackSize -= recipe.getInput().stackSize;
                }

                player.stopUsingItem();
            }
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack is) {
        return EnumAction.block;
    }

    public int getMaxItemUseDuration(ItemStack is) {
        HandworkRecipe recipe = BidsRegistry.HANDWORK_RECIPES.findMatchingRecipe(is);
        if (recipe != null) {
            return getActualMaxItemDuration(recipe.getDuration());
        }

        return 20;
    }

    protected int getActualMaxItemDuration(int duration) {
        return Math.round(duration * BidsOptions.Crafting.handworkDurationMultiplier);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (!player.isUsingItem()) {
            HandworkRecipe recipe = BidsRegistry.HANDWORK_RECIPES.findMatchingRecipe(is);
            if (recipe != null && is.stackSize >= recipe.getInput().stackSize) {
                player.setItemInUse(is, getActualMaxItemDuration(recipe.getDuration()));
            }
        }
        return is;
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
        super.addExtraInformation(is, player, arraylist);

        if (hints.size() > 0) {
            if (ItemHelper.showShiftInformation()) {
                arraylist.add(StatCollector.translateToLocal("gui.Help"));

                for (EnumTextileHint hint : hints) {
                    arraylist.add(StatCollector.translateToLocal("gui.Help.Textile." + hint.helpString));
                }
            } else {
                arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
            }
        }
    }

    @Override
    public int getColorFromMaterial(ItemStack is, int pass) {
        return hasMaterialColor ? materialColor : getColorFromItemStack(is, pass);
    }

}
