package com.unforbidable.tfc.bids.Containers;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Containers.Inventories.IInventorySlotTracker;
import com.unforbidable.tfc.bids.Containers.Inventories.InventoryCraftingTracked;
import com.unforbidable.tfc.bids.Containers.Slots.SlotOutputOnlyTracked;
import com.unforbidable.tfc.bids.Core.Network.IMessageHandlingContainer;
import com.unforbidable.tfc.bids.Core.Woodworking.Network.NetworkAction;
import com.unforbidable.tfc.bids.Core.Woodworking.Network.WoodworkingMessage;
import com.unforbidable.tfc.bids.Core.Woodworking.Plans.PlanInstance;
import com.unforbidable.tfc.bids.Core.Woodworking.WoodworkingHelper;
import com.unforbidable.tfc.bids.Core.Woodworking.Workspace.WorkspaceServer;
import com.unforbidable.tfc.bids.Gui.GuiWoodworking;
import com.unforbidable.tfc.bids.api.BidsEventFactory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerWoodworking extends ContainerTFC implements IMessageHandlingContainer<WoodworkingMessage>, IInventorySlotTracker {

    private final InventoryCrafting outputInv = new InventoryCraftingTracked(this, 1, 1);

    private final World world;
    private final WorkspaceServer workspaceServer;

    public ContainerWoodworking(InventoryPlayer inventory, World world, int x, int y, int z) {
        workspaceServer = new WorkspaceServer(WoodworkingHelper.getWoodworkingMaterial(inventory.player.getHeldItem()),
            WoodworkingHelper.getWoodworkingPlans(inventory.player.getHeldItem()));

        this.world = world;
        player = inventory.player;
        bagsSlotNum = player.inventory.currentItem;
        buildLayout();
        PlayerInventory.buildInventoryLayout(this, inventory, 8, 154, true, true);
    }

    protected void buildLayout() {
        this.addSlotToContainer(new SlotOutputOnlyTracked(outputInv, 0, 152, 126));
    }

    @SideOnly(Side.CLIENT)
    private GuiWoodworking getClientGui() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiWoodworking) {
            return (GuiWoodworking) Minecraft.getMinecraft().currentScreen;
        } else {
            return null;
        }
    }

    @Override
    public void onContainerMessage(WoodworkingMessage message) {
        if (!world.isRemote && message.getEvent() == WoodworkingMessage.EVENT_PERFORM_ACTION) {
            for (NetworkAction action : message.getActions()) {
                boolean result = workspaceServer.performAction(action.name, action.x, action.y);

                Bids.LOG.info("ACTION(\"{}\", {}, {}) => {}", action.name, action.x, action.y, result ? "OK" : "SUCCESS");
            }

            player.inventory.getItemStack().damageItem(message.getDamage(), player);
            if (player.inventory.getItemStack().stackSize == 0) {
                player.inventory.setItemStack(null);
            }

            world.playSoundEffect(player.posX, player.posY, player.posZ, "dig.wood",
                0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());

            tryToMatchCutout();
        }
    }

    private void tryToMatchCutout() {
        PlanInstance matchingPlan = workspaceServer.findMatchingPlan();
        if (matchingPlan != null) {
            ItemStack result = matchingPlan.getResult().copy();
            BidsEventFactory.onWoodworkingItemCrafted(player, player.getHeldItem(), result);

            outputInv.setInventorySlotContents(0, result);
        } else {
            outputInv.setInventorySlotContents(0, null);
        }
    }

    private boolean placeItemInOutputSlot(ItemStack result) {
        ItemStack output = outputInv.getStackInSlot(0);

        if (output == null) {
            outputInv.setInventorySlotContents(0, result);
            return true;
        } else if (output.isItemEqual(result) && ItemStack.areItemStackTagsEqual(output, result)) {
            int newStackSize = output.stackSize + result.stackSize;
            if (newStackSize <= output.getMaxStackSize()) {
                output.stackSize = newStackSize;
                outputInv.setInventorySlotContents(0, output);
                return true;
            }
        }

        return false;
    }

    @Override
    public void onSlotChanged(IInventory inventory, Slot slot) {
    }

    @Override
    public void onPickupFromSlot(IInventory inventory, Slot slot, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote) {
            BidsEventFactory.onWoodworkingItemPickedUp(player, player.getHeldItem(), itemStack);
        }

        player.inventory.decrStackSize(player.inventory.currentItem, 1);

        if (!world.isRemote) {
            workspaceServer.reset();
        } else {
            GuiWoodworking clientGui = getClientGui();
            if (clientGui != null) {
                clientGui.resetWorkspace();
            }
        }
    }

}
