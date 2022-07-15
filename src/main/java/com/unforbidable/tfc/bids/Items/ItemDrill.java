package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemDrill extends Item implements ISize {

    final private ToolMaterial material;

    public ItemDrill(ToolMaterial material) {
        super();
        this.material = material;
        setMaxDamage(1000);
        setCreativeTab(BidsCreativeTabs.BidsDefault);
    }

    public ToolMaterial getMaterial() {
        return material;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = getUnlocalizedName().replace("item.", "")
                .replace("IgIn ", "").replace("IgEx ", "").replace("Sed ", "").replace("MM ", "");
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":tools/" + name);
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.SMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack is) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack is) {
        return 20;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (!player.worldObj.isRemote && count == 1) {
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
                onBlockDrilled(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (canPlayerDrill(player)) {
            player.setItemInUse(is, getMaxItemUseDuration(is));
        }
        return is;
    }

    boolean canPlayerDrill(EntityPlayer player) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
        return mop != null && mop.typeOfHit == MovingObjectType.BLOCK
                && canDrillAt(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
    }

    protected boolean canDrillAt(World world, int x, int y, int z, int side) {
        TileEntity te = world.getTileEntity(x, y, z);
        Block block = world.getBlock(x, y, z);
        if (te != null && te instanceof TileEntityQuarry) {
            // Can existing quarry be drilled more?
            TileEntityQuarry quarry = (TileEntityQuarry) te;
            return !quarry.isQuarryReady();
        } else {
            // Can a block be quarried?
            IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
            if (quarriable != null) {
                return quarriable.canQuarryBlockAt(world, x, y, z, side);
            }

            return false;
        }
    }

    protected void onBlockDrilled(World world, int x, int y, int z, int side) {
        TileEntity te = world.getTileEntity(x, y, z);
        Block block = world.getBlock(x, y, z);
        if (te != null && te instanceof TileEntityQuarry) {
            // Add another wedge to the quarry here
            TileEntityQuarry quarry = (TileEntityQuarry) te;
            quarry.onQuarryDrilled();
            Bids.LOG.info("Existing quarry drilled at: " + x + ", " + y + ", " + z);
        } else if (QuarryRegistry.isBlockQuarriable(block)) {
            // Place quarry when block is drilled for the first time
            // at the corresponding side
            ForgeDirection d = ForgeDirection.getOrientation(side);
            world.setBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ, BidsBlocks.quarry, side, 3);
            Bids.LOG.info("Quarry started at: " + x + ", " + y + ", " + z + " side " + side);
        }
    }

}
