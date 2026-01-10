package com.unforbidable.tfc.bids.Containers;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Containers.Slots.SlotOutputOnly;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.IMessageHandlingContainer;
import com.unforbidable.tfc.bids.Core.Woodworking.Network.NetworkAction;
import com.unforbidable.tfc.bids.Core.Woodworking.Network.WoodworkingMessage;
import com.unforbidable.tfc.bids.Core.Woodworking.Plans.PlanInstance;
import com.unforbidable.tfc.bids.Core.Woodworking.WoodworkingHelper;
import com.unforbidable.tfc.bids.Core.Woodworking.Workspace.WorkspaceServer;
import com.unforbidable.tfc.bids.Gui.GuiWoodworking;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerWoodworking extends ContainerTFC implements IMessageHandlingContainer<WoodworkingMessage> {

    private final InventoryCrafting outputInv = new InventoryCrafting(this, 1, 1);

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
        this.addSlotToContainer(new SlotOutputOnly(outputInv, 0, 152, 126));
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!player.worldObj.isRemote) {
            dropInventory(player, outputInv);
        }
    }

    private void dropInventory(EntityPlayer player, InventoryCrafting inventoryCrafting) {
        ItemStack itemstack = inventoryCrafting.getStackInSlotOnClosing(0);
        if (itemstack != null) {
            player.dropPlayerItemWithRandomChoice(itemstack, false);
        }

        inventoryCrafting.setInventorySlotContents(0, (ItemStack) null);
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
                workspaceServer.performAction(action.name, action.x, action.y);
            }

            player.inventory.getItemStack().damageItem(message.getDamage(), player);
            if (player.inventory.getItemStack().stackSize == 0) {
                player.inventory.setItemStack(null);
            }

            tryToMatchCutout();
        }

        if (world.isRemote && message.getEvent() == WoodworkingMessage.EVENT_RESET_WORKSPACE) {
            GuiWoodworking clientGui = getClientGui();
            if (clientGui != null) {
                clientGui.resetWorkspace();
            }
        }
    }

    private void tryToMatchCutout() {
        PlanInstance matchingPlan = workspaceServer.findMatchingPlan();
        if (matchingPlan != null) {
            ItemStack result = matchingPlan.getResult().copy();
            if (!placeItemInOutputSlot(result)) {
                player.dropPlayerItemWithRandomChoice(result, false);
            }

            player.inventory.decrStackSize(player.inventory.currentItem, 1);

            workspaceServer.reset();

            WoodworkingMessage message = new WoodworkingMessage();
            message.setEvent(WoodworkingMessage.EVENT_RESET_WORKSPACE);
            Bids.network.sendTo(message, (EntityPlayerMP) player);
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

}
