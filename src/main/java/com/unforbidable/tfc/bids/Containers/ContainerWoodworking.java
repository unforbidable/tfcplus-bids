package com.unforbidable.tfc.bids.Containers;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.dunk.tfc.Core.TFC_Core;
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
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingMaterial;
import com.unforbidable.tfc.bids.api.WoodworkingRegistry;
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

import java.util.Random;

public class ContainerWoodworking extends ContainerTFC implements IMessageHandlingContainer<WoodworkingMessage>, IInventorySlotTracker {

    private final InventoryCrafting outputInv = new InventoryCraftingTracked(this, 1, 1);

    private final World world;
    private final WorkspaceServer workspaceServer;
    private float sawdustAmount = 0;

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
            IWoodworkingMaterial material = WoodworkingRegistry.findMaterialForItem(player.getHeldItem().getItem());
            float sawdustMaterialMultiplier = material != null ? getSawdustMaterialMultiplier(material) : 0;

            for (NetworkAction action : message.getActions()) {
                boolean result = workspaceServer.performAction(action.name, action.x, action.y);

                if (result) {
                    sawdustAmount += getSawdustAmountForAction(action.name) * sawdustMaterialMultiplier;
                }

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

    private float getSawdustMaterialMultiplier(IWoodworkingMaterial material) {
        switch (material.getType()) {
            case WOOD_THICK:
                return 1;
            case WOOD_FLAT:
                return 0.5f;
        }

        return 0;
    }

    private float getSawdustAmountForAction(String actionName) {
        if (actionName.startsWith("saw_cut_")) {
            // Sawing a whole length of a thick material gives 1 sawdust
            return 1 / 25f;
        } else if (actionName.startsWith("drill_")) {
            // Drilling 15 holes in flat gives 1 sawdust
            return 1 / 7.5f;
        }

        return 0;
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
            // Sawdust is collected during the sawing and drilling
            // only dropped when the output is retrieved
            dropSawdust();

            workspaceServer.reset();
        } else {
            GuiWoodworking clientGui = getClientGui();
            if (clientGui != null) {
                clientGui.resetWorkspace();
            }
        }
    }

    private void dropSawdust() {
        if (sawdustAmount > 0) {
            int integralAmount = (int) Math.floor(sawdustAmount);
            float partialAmount = sawdustAmount - integralAmount;
            int totalAmount = integralAmount + (new Random().nextFloat() < partialAmount ? 1 : 0);

            if (totalAmount > 0) {
                ItemStack is = new ItemStack(BidsItems.morePowder, totalAmount, 0);
                TFC_Core.giveItemToPlayer(is, player);
            }

            sawdustAmount = 0;
        }
    }

    @Override
    public ItemStack transferStackInSlotTFC(EntityPlayer player, int slotNum)
    {
        ItemStack origStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            origStack = slotStack.copy();

            if (slotNum < 1 && !this.mergeItemStack(slotStack, 1, inventorySlots.size(), true))
                return null;

            if (slotStack.stackSize <= 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();

            if (slotStack.stackSize == origStack.stackSize)
                return null;

            slot.onPickupFromSlot(player, slotStack);
        }

        return origStack;
    }

}
