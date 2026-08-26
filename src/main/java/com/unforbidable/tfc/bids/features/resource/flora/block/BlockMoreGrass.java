package com.unforbidable.tfc.bids.features.resource.flora.block;

import com.dunk.tfc.Blocks.Vanilla.BlockCustomTallGrass;
import com.dunk.tfc.Core.ColorizerFoliageTFC;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.util.accessor.BlockMetaNamesAccessor;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMoreGrass extends BlockCustomTallGrass implements BlockMetaNamesAccessor {

    private String[] metaNames;
    private IIcon[] icons;

    public BlockMoreGrass() {
        setHardness(3f);
        setBlockBounds(0.1f, 0.0f, 0.1f, 0.9f, 0.8f, 0.9f);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setStepSound(Block.soundTypeGrass);
    }

    @Override
    public Block setMetaNames(String[] names) {
        metaNames = names;

        return this;
    }

    @Override
    public String[] getMetaNames() {
        return metaNames;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        icons = new IIcon[metaNames.length];
        for (int i = 0; i < icons.length; ++i)
            icons[i] = par1IconRegister.registerIcon(Tags.MOD_ID + ":" + "plants/" + metaNames[i]);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta >= icons.length)
            meta = 0;
        return icons[meta];
    }

    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
        int meta = access.getBlockMetadata(x, y, z);
        return this.getIcon(side, meta);
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
    public void harvestBlock(World world, EntityPlayer player, int i, int j, int k, int l) {
        ItemStack is = player.inventory.getCurrentItem();
        if (is != null) {
            if (is.getItem() == TFCItems.stoneFlake) {
                createNettle(world, player, i, j, k);
                if (world.rand.nextInt(4) == 0) {
                    is.stackSize--;
                }
            } else if (OreDictionaryHelper.itemStackIsOre(is, "itemScythe")) {
                createNettle(world, player, i, j, k);
                for (int x = -1; x < 2; x++) {
                    for (int z = -1; z < 2; z++) {
                        if (world.getBlock(i + x, j, k + z) == this) {
                            createNettle(world, player, i + x, j, k + z);
                            is.damageItem(1, player);
                            if (is.stackSize == 0)
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                            world.setBlockToAir(i + x, j, k + z);
                        }
                    }
                }
            } else {
                createNettle(world, player, i, j, k);
            }
        } else {
            createNettle(world, player, i, j, k);
        }
    }

    private void createNettle(World world, EntityPlayer player, int i, int j, int k) {
        EntityItem ei = new EntityItem(world, i + 0.5F, j + 0.5F, k + 0.5F, new ItemStack(BidsItems.nettle, 1));
        world.spawnEntityInWorld(ei);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        // Skip tall grass meta shuffling
        this.checkAndDropBlock(world, x, y, z);
    }

}
