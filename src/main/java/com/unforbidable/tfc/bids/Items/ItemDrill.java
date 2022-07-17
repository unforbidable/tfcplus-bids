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
import com.unforbidable.tfc.bids.Core.Quarry.QuarryDrillTarget;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsOptions;
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
        if (!player.worldObj.isRemote) {
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
            if (!isTargetSame(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, player)) {
                // When player moves the cursor to another block or side
                // cancel the drill
                // but still damage the drill
                // It makes sense and it also stops the use animation
                player.stopUsingItem();
                QuarryDrillTarget original = QuarryDrillDataAgent.getTarget(player);
                Block block = player.worldObj.getBlock(original.x, original.y, original.z);
                damageDrill(player.worldObj, original.x, original.y, original.z, stack, player, block);
                Bids.LOG.debug("Use cancelled because the target has changed");
                return;
            }
        }

        int ticks = this.getMaxItemUseDuration(stack) - count;
        if (!player.worldObj.isRemote && ticks > getDrillDuration(stack, player)) {
            player.stopUsingItem();
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK
                    && canDrillAt(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit)) {
                onBlockDrilled(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, stack, player);
                QuarryDrillDataAgent.clearPlayerData(player);
            }
        }
    }

    private boolean isTargetSame(World world, int x, int y, int z, int side, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);
        ForgeDirection d = ForgeDirection.getOrientation(side);
        if (te != null && te instanceof TileEntityQuarry) {
            return isTargetSameBlock(x - d.offsetX, y - d.offsetY, z - d.offsetZ, side, player);
        } else {
            return isTargetSameBlock(x, y, z, side, player);
        }
    }

    private boolean isTargetSameBlock(int x, int y, int z, int side, EntityPlayer player) {
        return QuarryDrillDataAgent.getTarget(player).equals(new QuarryDrillTarget(x, y, z, side));
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
                && canDrillAt(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit)
                && checkExtraEquipment(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, player);
    }

    protected boolean checkExtraEquipment(World world, int x, int y, int z, int side, EntityPlayer player) {
        // At least one stick is needed on the hotbar
        for (int i = 0; i < 9; i++) {
            ItemStack is = player.inventory.getStackInSlot(i);
            if (is != null && is.getItem() == TFCItems.stick)
                return true;
        }

        Bids.LOG.info("Drilling requires sticks on hotbar and none were found");
        return false;
    }

    protected int getBaseDrillDuration() {
        return BidsOptions.Quarry.baseDrillDuration;
    }

    protected void onBlockDrillStarted(World world, int x, int y, int z, int side, ItemStack stack,
            EntityPlayer player) {
        // Save duration for this player
        Block block = world.getBlock(x, y, z);
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
        float mult = quarriable.getDrillDurationMultiplier(block);
        int duration = (int) Math.ceil(getBaseDrillDuration() * mult);
        QuarryDrillDataAgent.setPlayerData(player, duration, x, y, z, side);
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

        damageDrill(world, x, y, z, stack, player, block);
    }

    private void damageDrill(World world, int x, int y, int z, ItemStack stack, EntityPlayer player, Block block) {
        ItemStack newStack = onDrillDamaged(stack, player, block);
        onConsumeExtraEquipment(player, block);

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

    protected void onConsumeExtraEquipment(EntityPlayer player, Block block) {
        // Consume one stick from the hotbar
        boolean consumed = false;
        for (int i = 0; i < 9; i++) {
            ItemStack is = player.inventory.getStackInSlot(i);
            if (is != null && is.getItem() == TFCItems.stick) {
                player.inventory.decrStackSize(i, 1);
                consumed = true;
                break;
            }
        }

        if (!consumed) {
            Bids.LOG.warn("No sticks were found on the hotbar to be consumed");
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
        boolean breakString = BidsOptions.Quarry.bowStringBreakChance > player.worldObj.rand.nextDouble();
        ItemStack newStack = breakString
                ? new ItemStack(TFCItems.unstrungBow)
                : new ItemStack(TFCItems.bow);
        Bids.LOG.info("Returning " + newStack.getDisplayName());
        player.inventory.setInventorySlotContents(slot, newStack);
        return newStack;
    }

}
