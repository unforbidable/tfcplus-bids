package com.unforbidable.tfc.bids.TileEntities;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.ChoppingBlock.ChoppingBlockCraftingInventory;
import com.unforbidable.tfc.bids.Core.ChoppingBlock.ChoppingBlockHelper;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeManager;
import com.unforbidable.tfc.bids.api.Crafting.ChoppingBlockManager;
import com.unforbidable.tfc.bids.api.Crafting.ChoppingBlockRecipe;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityChoppingBlock extends TileEntity {

    public static final int MAX_STORAGE = 4;

    static final int LARGE_ITEM_SLOT = 0;

    final ItemStack[] storage = new ItemStack[MAX_STORAGE];

    int selectedSlot = -1;

    public TileEntityChoppingBlock() {
        super();
    }

    public void setSelectedSlot(int selectedSlot) {
        if (this.selectedSlot != selectedSlot) {
            this.selectedSlot = selectedSlot;

            Bids.LOG.debug("Selected workbench slot: " + selectedSlot);
        }
    }

    public void clearSelectedSection() {
        selectedSlot = -1;
    }

    public int getSelectedSection() {
        return selectedSlot;
    }

    public ItemStack getSelectedItem() {
        if (selectedSlot != -1) {
            return getItem(selectedSlot);
        }

        return null;
    }

    public boolean isEmpty() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getItem(int slot) {
        return storage[slot];
    }

    public boolean placeItem(int slot, ItemStack itemStack, boolean tryOtherSlots) {
        final ItemStack material = getChoppingBlockItem();
        if (ChoppingBlockManager.isChoppingBlockTool(material, itemStack)) {
            // Tools are placed into slot 0
            if (storage[0] == null) {
                storage[0] = itemStack.copy();
                storage[0].stackSize = 1;

                Bids.LOG.debug("Tool placed on chopping block: " + itemStack.getDisplayName()
                        + " slot: " + slot);

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                return true;
            }

            return false;
        }

        if (storage[slot] == null && itemStack.stackSize > 0) {
            storage[slot] = itemStack.copy();
            storage[slot].stackSize = 1;

            Bids.LOG.debug("Item placed on chopping block: " + itemStack.getDisplayName()
                    + " slot: " + slot);

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            return true;
        }

        if (tryOtherSlots) {
            // Try to place the item into another slot that is free
            for (int i = 0; i < MAX_STORAGE; i++) {
                if (i != slot && placeItem(i, itemStack, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean retrieveItem(int slot, EntityPlayer player, boolean tryOtherSlots) {
        if (storage[slot] != null) {
            final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, storage[slot]);
            worldObj.spawnEntityInWorld(ei);

            Bids.LOG.debug("Item retrieved from chopping block: " + storage[slot].getDisplayName()
                    + " slot: " + slot);

            storage[slot] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            return true;
        }

        if (tryOtherSlots) {
            // Try to use the tool on another slot that has an item
            for (int i = 0; i < MAX_STORAGE; i++) {
                if (i != slot && retrieveItem(i, player, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean useTool(int slot, ItemStack itemStack, EntityPlayer player, boolean tryOtherSlots) {
        if (storage[slot] != null) {
            Bids.LOG.debug("Tool used on chopping block: " + itemStack.getDisplayName()
                    + " slot: " + slot);

            final ItemStack material = getChoppingBlockItem();
            final ChoppingBlockRecipe recipe = ChoppingBlockManager.findMatchingRecipe(material, itemStack,
                    storage[slot]);
            if (recipe != null) {
                Bids.LOG.debug("Found matching recipe for tool: " + itemStack.getDisplayName()
                        + " ingredient: " + storage[slot].getDisplayName() + "[" + storage[slot].stackSize + "]");

                final ItemStack ingredient = storage[slot].copy();
                final ItemStack result = recipe.getCraftingResult(ingredient);

                onChoppingBlockRecipeCrafted(player, result, itemStack, ingredient);

                Bids.LOG.debug("Item crafted: " + result.getDisplayName() + "[" + result.stackSize + "]");

                storage[slot] = result;

                if (!ChoppingBlockManager.isChoppingBlockInput(material, result)) {
                    ejectItemAwayFromPlayer(slot, player);
                }

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                return true;
            }
        }

        if (tryOtherSlots) {
            // Try to use the tool on another slot that has an item
            for (int i = 0; i < MAX_STORAGE; i++) {
                if (i != slot && useTool(i, itemStack, player, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    public ItemStack getChoppingBlockItem() {
        Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
        int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return new ItemStack(block, 1, metadata);
    }

    public boolean isChoppingBlockTool(ItemStack tool) {
        return ChoppingBlockManager.isChoppingBlockTool(getChoppingBlockItem(), tool);
    }

    public boolean isChoppingBlockInput(ItemStack input) {
        return ChoppingBlockManager.isChoppingBlockInput(getChoppingBlockItem(), input);
    }

    private void onChoppingBlockRecipeCrafted(EntityPlayer player, ItemStack result, ItemStack tool,
            ItemStack ingredient) {
        final ChoppingBlockCraftingInventory craftMatrix = new ChoppingBlockCraftingInventory();
        craftMatrix.setInventorySlotContents(0, tool);
        craftMatrix.setInventorySlotContents(1, ingredient);

        final ItemCraftedEvent event = new ItemCraftedEvent(player, result, craftMatrix);

        // We call our handler directly
        // but consider actually invoking the forge event
        RecipeManager.handleItemCraftedEvent(event);

        // Consume items as crafting would
        // but we only care about the tool
        // Tool could be destroyed or damaged
        // depending on recipe actions that were triggered
        // The item stack should reflect this
        craftMatrix.decrStackSize(0, 1);

        // Our tool stack will have been modified at this point
        // unless the recipe action/handler works on a copy
        // TFC damages a copy of the tool
        // so that needs to be solved if TFC handlers are to work

        // Trying to avoid some desync glitches
        // Sometimes damaged tool would appear with stack size 2 or 3
        player.inventoryContainer.detectAndSendChanges();

        // When other materials are introduced
        // consider player different sounds
        // but this works for wood
        worldObj.playSoundEffect(xCoord, yCoord, zCoord, "dig.wood",
                0.4F + (worldObj.rand.nextFloat() / 2), 0.7F + worldObj.rand.nextFloat());

        // Tool gets damaged using recipe actions
        // which set the stack size to 0
        // when the tool is completely damaged
        if (tool.stackSize == 0) {
            worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.break",
                    0.4F + (worldObj.rand.nextFloat() / 2), 0.7F + worldObj.rand.nextFloat());
        }

        Bids.LOG.debug("Tool after crafting: " + tool.getDisplayName()
                + " damage: " + tool.getItemDamage() + " stacksize: " + tool.stackSize);
    }

    public boolean ejectItemAwayFromPlayer(int slot, EntityPlayer player) {
        if (storage[slot] != null) {
            // Attempting to spawn the item behind the workbench
            // related to the player's location
            final double r1 = worldObj.rand.nextDouble();
            final double r2 = worldObj.rand.nextDouble();
            final double r3 = worldObj.rand.nextDouble();

            final double yaw = player.rotationYaw + r1 * 90 - 45;

            final double posX = xCoord + 0.5;
            final double posY = yCoord + 1.5;
            final double posZ = zCoord + 0.5;

            final double dx = -Math.sin(yaw * Math.PI / 180.0D) * 0.4D;
            final double dz = Math.cos(yaw * Math.PI / 180.0D) * 0.4D;

            final double velocityX = dx * 0.3 + dx * r2 * 0.2;
            final double velocityY = 0.1;
            final double velocityZ = dz * 0.3 + dz * r3 * 0.2;

            final EntityItem ei = new EntityItem(worldObj, posX, posY, posZ, storage[slot]);
            ei.motionX = velocityX;
            ei.motionY = velocityY;
            ei.motionZ = velocityZ;
            ei.delayBeforeCanPickup = 10;
            worldObj.spawnEntityInWorld(ei);

            storage[slot] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            return true;
        }

        return false;
    }

    public void onBlockBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, storage[i]);
                worldObj.spawnEntityInWorld(ei);

                storage[i] = null;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        final float h = ChoppingBlockHelper.getChoppingBlockBoundsHeight() + 0.5f;
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + h, zCoord + 1);
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeWorkbenchDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readWorkbenchDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeWorkbenchDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readWorkbenchDataFromNBT(tag);
    }

    public void writeWorkbenchDataToNBT(NBTTagCompound tag) {
        NBTTagList itemTagList = new NBTTagList();
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("slot", i);
                storage[i].writeToNBT(itemTag);
                itemTagList.appendTag(itemTag);
            }
        }
        tag.setTag("storage", itemTagList);
    }

    public void readWorkbenchDataFromNBT(NBTTagCompound tag) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }
    }

}
