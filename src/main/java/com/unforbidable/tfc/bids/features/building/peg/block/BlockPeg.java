package com.unforbidable.tfc.bids.features.building.peg.block;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.features.building.peg.entity.EntityPegLeashKnot;
import com.unforbidable.tfc.bids.util.LeashHelper;
import com.unforbidable.tfc.bids.util.fence.FenceConnections;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockPeg extends Block {

    public BlockPeg() {
        super(Material.wood);

        setHardness(2f);
        setBlockBounds(0.4375f, 0, 0.4375f, 0.5625f, 0.8f, 0.5625f);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        // Collision should be disabled at least for knot entity else it gets pushed above the peg
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
    }

    @Override
    public int damageDropped(int metadata) {
        if (metadata > 0) {
            // Restoring item durability but usually some is lost due to metadata rounding
            int damage = (int)Math.ceil((metadata) / 15f * BidsItems.hardenedDiggingStick.getMaxDamage());
            Bids.LOG.info("Dropped digging stick, meta: {} -> damage: {}", metadata, damage);
            return damage;
        }

        return 0;
    }

    @Override
    public Item getItemDropped(int metadata, Random rand, int fortune) {
        return BidsItems.hardenedDiggingStick;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon(Tags.MOD_ID + ":Wooden Peg");
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return TFC_Core.isSoil(world.getBlock(x, y - 1, z));
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (!canBlockStay(world, x, y, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
                                    int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            return LeashHelper.leashToBlock(world, x, y, z, player, EntityPegLeashKnot.class);
        }

        return false;
    }

}
