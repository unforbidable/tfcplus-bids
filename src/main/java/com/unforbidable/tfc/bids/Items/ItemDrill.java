package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryDrillDataAgent;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryDrillTarget;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Interfaces.IPlugAndFeather;
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
import net.minecraft.util.StatCollector;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class ItemDrill extends Item implements ISize {

    static final int MAX_USE_DURATION = 72000;

    private int equipmentTier;
    private float durationMultiplier = 1f;

    public ItemDrill(ToolMaterial material) {
        super();
        maxStackSize = 1;
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxDamage(material.getMaxUses());
        setNoRepair();
    }

    public ItemDrill setEquipmentTier(int equipmentTier) {
        this.equipmentTier = equipmentTier;

        return this;
    }

    public ItemDrill setDurationMultiplier(float durationMultiplier) {
        this.durationMultiplier = durationMultiplier;

        return this;
    }

    public int getDrillEquipmentTier(ItemStack itemStack) {
        return equipmentTier;
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
                if (original != null) {
                    Block block = player.worldObj.getBlock(original.x, original.y, original.z);
                    int metadata = player.worldObj.getBlockMetadata(original.x, original.y, original.z);
                    damageDrill(player.worldObj, original.x, original.y, original.z, stack, player, block, metadata);
                }
                Bids.LOG.debug("Use cancelled because the target has changed");
                return;
            }
        }

        int ticks = this.getMaxItemUseDuration(stack) - count;
        if (!player.worldObj.isRemote && ticks > getDrillDuration(stack, player)) {
            player.stopUsingItem();
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK
                    && canPlayerDrillAt(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, player)) {
                onBlockDrilled(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, stack, player);
                QuarryDrillDataAgent.clearPlayerData(player);
            }
        }
    }

    private boolean isTargetSame(World world, int x, int y, int z, int side, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityQuarry) {
            ForgeDirection d = ((TileEntityQuarry) te).getQuarryOrientation();
            return isTargetSameBlock(x - d.offsetX, y - d.offsetY, z - d.offsetZ, d.ordinal(), player);
        } else {
            return isTargetSameBlock(x, y, z, side, player);
        }
    }

    private boolean isTargetSameBlock(int x, int y, int z, int side, EntityPlayer player) {
        return QuarryDrillDataAgent.getTarget(player) != null && QuarryDrillDataAgent.getTarget(player).equals(new QuarryDrillTarget(x, y, z, side));
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
            if (te instanceof TileEntityQuarry) {
                ForgeDirection d = ((TileEntityQuarry) te).getQuarryOrientation();
                onBlockDrillStarted(player.worldObj, x - d.offsetX, y - d.offsetY, z - d.offsetZ, d.ordinal(), stack,
                        player);
            } else {
                onBlockDrillStarted(player.worldObj, x, y, z, side, stack, player);
            }
        }

        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    protected boolean canPlayerDrill(EntityPlayer player) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
        return mop != null && mop.typeOfHit == MovingObjectType.BLOCK
                && canPlayerDrillAt(player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, player);
    }

    protected boolean canPlayerDrillAt(World world, int x, int y, int z, int side, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityQuarry) {
            // Can existing quarry be drilled more?
            TileEntityQuarry quarry = (TileEntityQuarry) te;

            // Get the quarried block instead of the quarry block
            ForgeDirection d = quarry.getQuarryOrientation();
            Block block = world.getBlock(x - d.offsetX, y - d.offsetY, z - d.offsetZ);
            int metadata = world.getBlockMetadata(x - d.offsetX, y - d.offsetY, z - d.offsetZ);

            return !quarry.isQuarryReady() && canPlayerDrillBlock(block, metadata, player);
        } else {
            // Can a block be quarried?
            Block block = world.getBlock(x, y, z);
            int metadata = world.getBlockMetadata(x, y, z);

            return QuarryHelper.canQuarryBlockAt(world, x, y, z, block, side) && canPlayerDrillBlock(block, metadata, player);
        }
    }

    public boolean canPlayerDrillBlock(Block block, int metadata, EntityPlayer player) {
        Bids.LOG.debug("canPlayerDrillBlock " + block.getUnlocalizedName() + " " + metadata);

        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
        if (quarriable == null) {
            Bids.LOG.warn("Trying to continue drilling a quarry but the quarried block " + block.getUnlocalizedName() + ":" + metadata + " is not quarriable.");
            return false;
        }

        ItemStack equippedItem = player.getCurrentEquippedItem();
        if (equippedItem != null && equippedItem.getItem() instanceof ItemDrill) {
            int drillQuarryEquipmentTier = ((ItemDrill) equippedItem.getItem()).getDrillEquipmentTier(equippedItem);
            if (!quarriable.isSufficientEquipmentTier(block, metadata, drillQuarryEquipmentTier)) {
                Bids.LOG.debug("Insufficient drill tier");
                return false;
            }

            // At least one plug and feather is needed on the hotbar
            for (int i = 0; i < 9; i++) {
                ItemStack is = player.inventory.getStackInSlot(i);
                if (is != null && is.getItem() instanceof IPlugAndFeather) {
                    int plugAndFeatherQuarryEquipmentTier = ((IPlugAndFeather) is.getItem()).getPlugAndFeatherQuarryEquipmentTier(is);
                    if (quarriable.isSufficientEquipmentTier(block, metadata, plugAndFeatherQuarryEquipmentTier)) {
                        return true;
                    }
                }
            }

            Bids.LOG.debug("Insufficient plug and feather tier");
        }

        return false;
    }

    protected int getBaseDrillDuration() {
        return Math.round(BidsOptions.Quarry.baseDrillDuration * durationMultiplier);
    }

    protected void onBlockDrillStarted(World world, int x, int y, int z, int side, ItemStack stack,
            EntityPlayer player) {
        // Save duration for this player
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
        float mult = quarriable.getDrillDurationMultiplier(block, metadata);
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

    protected void onBlockDrilled(World world, int x, int y, int z, int side, ItemStack stack, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);
        Block block = world.getBlock(x, y, z);

        if (te instanceof TileEntityQuarry) {
            // Add another wedge to the quarry here
            TileEntityQuarry quarry = (TileEntityQuarry) te;

            // Get the quarried block instead of the quarry block
            // as needed later for drill damaging
            ForgeDirection d = quarry.getQuarryOrientation();
            Block actualBlock = world.getBlock(x - d.offsetX, y - d.offsetY, z - d.offsetZ);
            int metadata = world.getBlockMetadata(x - d.offsetX, y - d.offsetY, z - d.offsetZ);;
            damageDrill(world, x, y, z, stack, player, actualBlock, metadata);
            ItemStack consumedPlugAndFeather = consumePlugAndFeather(player, actualBlock, metadata);
            if (consumedPlugAndFeather != null) {
                quarry.onQuarryDrilled(consumedPlugAndFeather);
                Bids.LOG.debug("Existing quarry drilled at: " + x + ", " + y + ", " + z + " side " + d);
            }
        } else if (QuarryRegistry.isBlockQuarriable(block)) {
            int metadata = world.getBlockMetadata(x, y, z);
            damageDrill(world, x, y, z, stack, player, block, metadata);
            ItemStack consumedPlugAndFeather = consumePlugAndFeather(player, block, metadata);
            if (consumedPlugAndFeather != null) {
                // Place quarry when block is drilled for the first time
                // at the corresponding side
                ForgeDirection d = ForgeDirection.getOrientation(side);
                world.setBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ, BidsBlocks.quarry, side, 3);
                Bids.LOG.debug("Quarry started at: " + x + ", " + y + ", " + z + " side " + side);

                TileEntityQuarry quarry = (TileEntityQuarry) world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
                quarry.initializeWedges();
                quarry.onQuarryDrilled(consumedPlugAndFeather);
            }
        }
    }

    private void damageDrill(World world, int x, int y, int z, ItemStack stack, EntityPlayer player, Block block, int metadata) {
        ItemStack newStack = onDrillDamaged(stack, player, block, metadata);

        double x2 = x + 0.5D;
        double y2 = y + 0.5D;
        double z2 = z + 0.5D;

        world.playSoundEffect(x2, y2, z2, "dig.stone",
                0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());

        // Item is different if the drill was destroyed
        if (stack.getItem() != newStack.getItem()) {
            world.playSoundEffect(x2, y2, z2, "random.break",
                    0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());
        }
    }

    protected ItemStack consumePlugAndFeather(EntityPlayer player, Block block, int metadata) {
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);

        // Consume one stick from the hotbar
        for (int i = 0; i < 9; i++) {
            ItemStack is = player.inventory.getStackInSlot(i);
            if (is != null && is.getItem() instanceof IPlugAndFeather) {
                int plugAndFeatherQuarryEquipmentTier = ((IPlugAndFeather) is.getItem()).getPlugAndFeatherQuarryEquipmentTier(is);
                if (quarriable.isSufficientEquipmentTier(block, metadata, plugAndFeatherQuarryEquipmentTier)) {
                    ItemStack consumed = player.inventory.getStackInSlot(i).copy();
                    consumed.stackSize = 1;
                    player.inventory.decrStackSize(i, 1);
                    return consumed;
                }
            }
        }

        Bids.LOG.warn("No sufficient 'Plug and Feathers' were found on the hotbar to be consumed");

        return null;
    }

    protected ItemStack onDrillDamaged(ItemStack stack, EntityPlayer player, Block block, int metadata) {
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
        if (quarriable == null) {
            Bids.LOG.warn("Expected quarriable block, but got a " + block.getUnlocalizedName());
            return stack;
        }

        int damage = quarriable.getDrillDamage(block, metadata);
        boolean destroyed = stack.getItemDamage() + damage >= getMaxDamage();

        stack.damageItem(damage, player);
        Bids.LOG.debug("Drill took damage: " + damage);

        if (destroyed) {
            Bids.LOG.debug("Drill was destroyed");
            return onDrillDestroyed(stack, player);
        }

        return stack;
    }

    protected ItemStack onDrillDestroyed(ItemStack stack, EntityPlayer player) {
        int slot = player.inventory.currentItem;
        player.inventory.decrStackSize(slot, 1);

        ItemStack newStack = null;

        boolean bowstringBroke = BidsOptions.Quarry.bowStringBreakChance > player.worldObj.rand.nextDouble();

        if (BidsOptions.Quarry.enableDrillAutoRepair) {
            List<ItemStack> bowstrings = bowstringBroke ? OreDictionary.getOres("materialStringDecent", false) : null;

            // Try to repair the drill automatically
            // Stick and drill head is needed on the hot bar
            // If the bowstring has broken too,
            // a new one will be needed too
            ItemStack foundToolHandle = null;
            ItemStack foundDrillHead = null;
            ItemStack foundBowstring = null;

            for (int i = 0; i < 9; i++) {
                ItemStack is = player.inventory.getStackInSlot(i);
                if (is != null) {
                    if (foundToolHandle == null && isToolHandle(is)) {
                        foundToolHandle = is;
                    } else if (foundDrillHead == null && isDrillHead(is)) {
                        foundDrillHead = is;
                    } else if (bowstringBroke
                            && foundBowstring == null && isBowstring(is, bowstrings)) {
                        foundBowstring = is;
                    }
                }
            }

            if (foundToolHandle != null && foundDrillHead != null
                    && (!bowstringBroke || foundBowstring != null)) {
                newStack = getDrillForHead(foundDrillHead);

                if (newStack != null) {
                    player.inventory.consumeInventoryItem(foundToolHandle.getItem());
                    player.inventory.consumeInventoryItem(foundDrillHead.getItem());

                    if (bowstringBroke) {
                        player.inventory.consumeInventoryItem(foundBowstring.getItem());
                    }
                }
            }

            Bids.LOG.debug("Needed drill head found: "
                    + (foundDrillHead != null ? foundDrillHead.getDisplayName() : "none"));
            Bids.LOG.debug("Needed tool handle found: "
                    + (foundToolHandle != null ? foundToolHandle.getDisplayName() : "none"));
            if (bowstringBroke) {
                Bids.LOG.debug("Needed bowstring found: "
                        + (foundBowstring != null ? foundBowstring.getDisplayName() : "none"));
            }
        }

        if (newStack == null) {
            // Unable to auto-repair the drill
            // so we return the broken part
            newStack = bowstringBroke ? new ItemStack(TFCItems.unstrungBow) : new ItemStack(TFCItems.bow);
        }

        Bids.LOG.debug("Returning " + newStack.getDisplayName());
        player.inventory.setInventorySlotContents(slot, newStack);
        return newStack;
    }

    protected boolean isToolHandle(ItemStack itemStack) {
        return itemStack.getItem() == TFCItems.stick;
    }

    protected boolean isDrillHead(ItemStack itemStack) {
        return itemStack.getItem() == BidsItems.mMStoneDrillHead
                || itemStack.getItem() == BidsItems.sedStoneDrillHead
                || itemStack.getItem() == BidsItems.igExStoneDrillHead
                || itemStack.getItem() == BidsItems.igInStoneDrillHead;
    }

    protected boolean isBowstring(ItemStack itemStack, List<ItemStack> ores) {
        for (ItemStack ore : ores) {
            if (ore.getItem() == itemStack.getItem()
                    && (ore.getItemDamage() == itemStack.getItemDamage()
                            || ore.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                return true;
            }
        }

        return false;
    }

    protected ItemStack getDrillForHead(ItemStack drillHead) {
        if (drillHead.getItem() == BidsItems.mMStoneDrillHead) {
            return new ItemStack(BidsItems.mMStoneDrill);
        } else if (drillHead.getItem() == BidsItems.sedStoneDrillHead) {
            return new ItemStack(BidsItems.sedStoneDrill);
        } else if (drillHead.getItem() == BidsItems.igExStoneDrillHead) {
            return new ItemStack(BidsItems.igExStoneDrill);
        } else if (drillHead.getItem() == BidsItems.igInStoneDrillHead) {
            return new ItemStack(BidsItems.igInStoneDrill);
        }

        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);

        if (ItemHelper.showShiftInformation()) {
            list.add(StatCollector.translateToLocal("gui.Help"));
            list.add(StatCollector.translateToLocal("gui.Help.Drill"));
            list.add(StatCollector.translateToLocal("gui.Help.Drill2"));
        } else {
            list.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

}
