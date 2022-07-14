package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        Block block = world.getBlock(x, y, z);
        // Block must be quarriable
        if (canDrillBlock(block)) {
            ForgeDirection sidesToCheck[] = new ForgeDirection[] { ForgeDirection.UP, ForgeDirection.NORTH,
                    ForgeDirection.WEST };
            int airCount = 0;
            for (ForgeDirection dir : sidesToCheck) {
                // Either of the opposing side must be exposed to air to count
                ForgeDirection opp = dir.getOpposite();
                if (world.isAirBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)
                        || world.isAirBlock(x + opp.offsetX, y + opp.offsetY, z + opp.offsetZ)) {
                    airCount++;
                }
            }
            return airCount == 3;
        }

        return false;
    }

    protected boolean canDrillBlock(Block block) {
        return block == TFCBlocks.stoneMM || block == TFCBlocks.stoneSed;
    }

    protected static void onBlockDrilled(World world, int x, int y, int z, int side) {
        Bids.LOG.info("Block drilled at: " + x + ", " + y + ", " + z + " side " + side);
    }

}
