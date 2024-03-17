package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemMeal;
import com.dunk.tfc.Handlers.Network.ItemPotterySmashPacket;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.ISmashable;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.Cooking.CookingMixtureHelper;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Crafting.CookingMixture;
import com.unforbidable.tfc.bids.api.Interfaces.ICookingMixtureItem;
import com.unforbidable.tfc.bids.api.Interfaces.IMoreSandwich;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class ItemCookingMixture extends ItemMeal implements ISmashable, IMoreSandwich, ICookingMixtureItem {

    protected final float[] ingredientWeights = new float[] { 0, 20, 8, 8, 4 };
    protected final float foodMaxWeight = 40;

    IIcon overlayIcon;

    public ItemCookingMixture() {
        setMaxStackSize(1);

        hasSubtypes = false;
        metaNames = null;
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
    public FluidStack getCookingMixFluid(ItemStack is) {
        String mixtureName = CookingMixtureHelper.getCookingMixtureName(is);
        FluidStack fs = CookingMixtureHelper.createCookingMixtureFluidStack(mixtureName, getFluidAmount());
        CookingMixtureHelper.initCookingMixtureTags(fs, is);
        return fs;
    }

    protected int getFluidAmount() {
        return 500;
    }

    @Override
    public ItemStack getEmptyContainer(ItemStack is) {
        return new ItemStack(BidsItems.largeClayBowl, 1, 1);
    }

    @Override
    public int getItemStackLimit(ItemStack is)
    {
        return 1;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        overlayIcon = registerer.registerIcon(Tags.MOD_ID + ":pottery/Large Bowl.Overlay");
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0) {
            return getEmptyContainer(stack).getItem().getIcon(getEmptyContainer(stack), pass);
        } else if (pass == 1) {
            return overlayIcon;
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack is, int pass) {
        return pass == 1
            ? getMixtureColor(is)
            : super.getColorFromItemStack(new ItemStack(TFCItems.potteryJug, 1, 1), pass);
    }

    protected int getMixtureColor(ItemStack is) {
        CookingMixture mixture = CookingMixtureHelper.getCookingMixture(is);
        if (mixture != null) {
            return mixture.getColor();
        }

        return 0;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
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
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.none;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        return is;
    }

    @Override
    public void onCrafted(ItemStack is, EntityPlayer player) {
        Food.setDecayTimer(is, (int) TFC_Time.getTotalHours() + 24);
        Food.setDecayRate(is, 2f);
    }

    @Override
    public boolean canSmash(ItemStack item) {
        return true;
    }

    @Override
    public void onSmashed(ItemStack stack, World world, double x, double y, double z) {
        if (stack != null && stack.getItem() != null && stack.getItem() instanceof ISmashable) {
            world.playSoundEffect(x, y, z, "terrafirmacraftplus:item.ceramicbreak", 1.0F, 0.8F + world.rand.nextFloat() * 0.4F);
            ItemPotterySmashPacket smashPkt = new ItemPotterySmashPacket(Item.getIdFromItem(stack.getItem()), x, y, z);
            NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 32.0);
            TerraFirmaCraft.PACKET_PIPELINE.sendToAllAround(smashPkt, tp);
        }
    }

    @Override
    public void smashAnimate(World world, double x, double y, double z) {
        String smashBlock = "blockdust_" + Block.getIdFromBlock(Blocks.glass) + "_0";

        for(double i = 0.0; i < 6.283185307179586; i += 0.7853981633974483) {
            world.spawnParticle(smashBlock, x, y, z, Math.cos(i) * 0.2, 0.15 + world.rand.nextDouble() * 0.1, Math.sin(i) * 0.2);
        }
    }

}
