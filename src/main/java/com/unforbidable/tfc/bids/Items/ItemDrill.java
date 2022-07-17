package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryDrillDataAgent;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryHelper;
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

    static final int MAX_USE_DURATION = 72000;
    static final int BASE_DRILL_DURATION = 40;

    final private ToolMaterial material;

    public ItemDrill(ToolMaterial material) {
        super();
        this.material = material;
        maxStackSize = 1;
        setCreativeTab(BidsCreativeTabs.BidsDefault);
        setMaxDamage(material.getMaxUses() / 2);
        setNoRepair();
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
        return EnumSize.VERYSMALL;
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
        return MAX_USE_DURATION;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        int ticks = this.getMaxItemUseDuration(stack) - count;
        if (!player.worldObj.isRemote && ticks > getDrillDuration(stack, player)) {
            player.clearItemInUse();
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK
                    && canDrillAt(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit)) {
                onBlockDrilled(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, stack, player);
                QuarryDrillDataAgent.clearPlayerData(player);
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

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote && canPlayerDrill(player)) {
            TileEntity te = world.getTileEntity(x, y, z);
            ForgeDirection d = ForgeDirection.getOrientation(side);
            if (te != null && te instanceof TileEntityQuarry) {
                onBlockDrillStarted(player.worldObj, x - d.offsetX, y - d.offsetY, z - d.offsetZ, side, stack, player);
            } else {
                onBlockDrillStarted(player.worldObj, x, y, z, side, stack, player);
            }
        }

        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    protected boolean canPlayerDrill(EntityPlayer player) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
        return mop != null && mop.typeOfHit == MovingObjectType.BLOCK
                && canDrillAt(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
    }

    protected int getBaseDrillDuration() {
        return BASE_DRILL_DURATION;
    }

    protected void onBlockDrillStarted(World world, int x, int y, int z, int side, ItemStack stack,
            EntityPlayer player) {
        // Save duration for this player
        Block block = world.getBlock(x, y, z);
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
        float mult = quarriable.getDrillDurationMultiplier(block);
        int duration = (int) Math.ceil(getBaseDrillDuration() * mult);
        QuarryDrillDataAgent.setPlayerData(player, duration);
        Bids.LOG.debug("Set drill duration: " + duration);
    }

    protected int getDrillDuration(ItemStack stack, EntityPlayer player) {
        if (!QuarryDrillDataAgent.hasPlayerData(player)) {
            Bids.LOG.warn("Missing drill duration info for player");
            return getBaseDrillDuration();
        }

        return QuarryDrillDataAgent.getDuration(player);
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
            return QuarryHelper.canQuarryBlockAt(world, x, y, z, block, side);
        }
    }

    protected void onBlockDrilled(World world, int x, int y, int z, int side, ItemStack stack, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);
        Block block = world.getBlock(x, y, z);
        ForgeDirection d = ForgeDirection.getOrientation(side);
        if (te != null && te instanceof TileEntityQuarry) {
            // Add another wedge to the quarry here
            TileEntityQuarry quarry = (TileEntityQuarry) te;
            quarry.onQuarryDrilled();
            Bids.LOG.info("Existing quarry drilled at: " + x + ", " + y + ", " + z);

            // Get the quarried block instead of the quarry block
            // as needed later for drill damaging
            block = world.getBlock(x - d.offsetX, y - d.offsetY, z - d.offsetZ);
        } else if (QuarryRegistry.isBlockQuarriable(block)) {
            // Place quarry when block is drilled for the first time
            // at the corresponding side
            world.setBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ, BidsBlocks.quarry, side, 3);
            Bids.LOG.info("Quarry started at: " + x + ", " + y + ", " + z + " side " + side);
        }

        ItemStack newStack = onDrillDamaged(stack, player, block);

        double x2 = x + 0.5D;
        double y2 = y + 0.5D;
        double z2 = z + 0.5D;

        world.playSoundEffect(x2, y2, z2, "dig.stone",
                0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());

        // Item is different if it the drill was destroyed
        if (stack.getItem() != newStack.getItem()) {
            world.playSoundEffect(x2, y2, z2, "random.break",
                    0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());
        }
    }

    protected ItemStack onDrillDamaged(ItemStack stack, EntityPlayer player, Block block) {
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
        if (quarriable == null) {
            Bids.LOG.warn("Expected quarriable block, but got a " + block.getUnlocalizedName());
            return stack;
        }

        int damage = quarriable.getDrillDamage(block);
        boolean destroyed = stack.getItemDamage() + damage >= getMaxDamage();

        stack.damageItem(damage, player);
        Bids.LOG.info("Drill took damage: " + damage);

        if (destroyed) {
            Bids.LOG.info("Drill was destroyed");
            return onDrillDestroyed(stack, player);
        }

        return stack;
    }

    protected ItemStack onDrillDestroyed(ItemStack stack, EntityPlayer player) {
        int slot = player.inventory.currentItem;
        player.inventory.decrStackSize(slot, 1);
        ItemStack newStack = player.worldObj.rand.nextInt(5) == 0
                ? new ItemStack(TFCItems.unstrungBow)
                : new ItemStack(TFCItems.bow);
        Bids.LOG.info("Returning " + newStack.getDisplayName());
        player.inventory.setInventorySlotContents(slot, newStack);
        return newStack;
    }

}
