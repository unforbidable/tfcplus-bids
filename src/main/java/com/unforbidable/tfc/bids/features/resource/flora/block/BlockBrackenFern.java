package com.unforbidable.tfc.bids.features.resource.flora.block;

import com.dunk.tfc.Blocks.Vanilla.BlockCustomTallGrass;
import com.dunk.tfc.Core.ColorizerFoliageTFC;
import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.features.resource.flora.main.FernGrowthHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrackenFern extends BlockCustomTallGrass {

    public BlockBrackenFern() {
        setHardness(8f);
        setBlockBounds(0.1f, 0.0f, 0.1f, 0.9f, 1.8f, 0.9f);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setStepSound(Block.soundTypeGrass);
    }

    @Override
    public Material getMaterial() {
        return Material.plants;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        blockIcon = par1IconRegister.registerIcon(Tags.MOD_ID + ":" + "plants/" + getTextureName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return blockIcon;
    }

    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
        int meta = access.getBlockMetadata(x, y, z);
        return this.getIcon(side, meta);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);

        if (FernGrowthHelper.tryToSpreadFern(world, x, y, z, random)) {
            world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
        }
    }

    @Override
    public int tickRate(World world) {
        return 200;
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
    }

    @Override
    public int getRenderColor(int par1) {
        return par1 == 0 ? 16777215 : ColorizerFoliageTFC.getFoliageColorBasic();
    }

    @Override
    public int colorMultiplier(IBlockAccess bAccess, int x, int y, int z) {
        return TerraFirmaCraft.proxy.grassColorMultiplier(bAccess, x, y, z);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return this.canThisPlantGrowOnThisBlock(world.getBlock(x, y - 1, z));
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int l) {
        int month = TFC_Time.getSeasonAdjustedMonth(z);
        float baseWeight = month >= TFC_Time.SEPTEMBER ? 4f : 1f;
        float weight = Math.round(baseWeight + world.rand.nextFloat() * 2);
        ItemStack is = ItemExtraFood.createTag(new ItemStack(BidsItems.fernRhizome), weight);
        EntityItem ei = new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, is);
        world.spawnEntityInWorld(ei);
    }

}
