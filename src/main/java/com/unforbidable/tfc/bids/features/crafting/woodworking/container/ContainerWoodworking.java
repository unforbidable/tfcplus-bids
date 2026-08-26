package com.unforbidable.tfc.bids.features.crafting.woodworking.container;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
import com.unforbidable.tfc.bids.api.meta.MorePowderMeta;
import com.unforbidable.tfc.bids.api.names.WoodworkingMaterialNames;
import com.unforbidable.tfc.bids.common.container.inventory.IInventorySlotTracker;
import com.unforbidable.tfc.bids.common.container.inventory.InventoryCraftingTracked;
import com.unforbidable.tfc.bids.common.container.slot.SlotOutputOnlyTracked;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.gui.GuiWoodworking;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.WoodworkingHelper;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.WorkspacePlan;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.WorkspaceServer;
import com.unforbidable.tfc.bids.features.crafting.woodworking.network.NetworkAction;
import com.unforbidable.tfc.bids.features.crafting.woodworking.network.WoodworkingPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerWoodworking extends ContainerTFC implements PacketHandler<WoodworkingPacket>, IInventorySlotTracker {

    private final InventoryCrafting outputInv = new InventoryCraftingTracked(this, 1, 1);

    private final World world;
    private final WorkspaceServer workspaceServer;
    private float sawdustAmount = 0;

    public ContainerWoodworking(InventoryPlayer inventory, World world, int x, int y, int z) {
        workspaceServer = createServer(inventory.player.getHeldItem());

        this.world = world;
        player = inventory.player;
        bagsSlotNum = player.inventory.currentItem;
        buildLayout();
        PlayerInventory.buildInventoryLayout(this, inventory, 8, 154, true, true);
    }

    private WorkspaceServer createServer(ItemStack heldItem) {
        if (heldItem != null) {
            WoodworkingMaterial material = WoodworkingHelper.getWoodworkingMaterial(heldItem);
            List<WorkspacePlan> plans = WoodworkingHelper.getWoodworkingPlans(heldItem);
            if (material != null && !plans.isEmpty()) {
                return new WorkspaceServer(material, plans);
            }
        }

        Bids.LOG.warn("Woodworking server cannot be initialized.");

        // create a dummy server
        return new WorkspaceServer(WoodworkingRegistry.materials.get(m -> m.getOreName().equals("logWood")), new ArrayList<WorkspacePlan>() {});
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
    public void handleNetworkPacket(WoodworkingPacket packet) {
        if (!world.isRemote && packet.getEvent() == WoodworkingPacket.EVENT_PERFORM_ACTION) {
            WoodworkingMaterial material = WoodworkingHelper.getWoodworkingMaterial(player.getHeldItem());
            float sawdustMaterialMultiplier = material != null ? getSawdustMaterialMultiplier(material) : 0;

            for (NetworkAction action : packet.getActions()) {
                boolean result = workspaceServer.performAction(action.name, action.x, action.y);

                if (result) {
                    sawdustAmount += getSawdustAmountForAction(action.name) * sawdustMaterialMultiplier;
                }

                Bids.LOG.debug("ACTION(\"{}\", {}, {}) => {}", action.name, action.x, action.y, result ? "OK" : "SUCCESS");
            }

            player.inventory.getItemStack().damageItem(packet.getDamage(), player);
            if (player.inventory.getItemStack().stackSize == 0) {
                player.inventory.setItemStack(null);
            }

            tryToMatchCutout();
        }
    }

    private float getSawdustMaterialMultiplier(WoodworkingMaterial material) {
        switch (material.getMaterialName()) {
            case WoodworkingMaterialNames.WOOD_THICK:
                return 1;
            case WoodworkingMaterialNames.WOOD_FLAT:
                return 0.5f;
        }

        return 0;
    }

    private float getSawdustAmountForAction(String actionName) {
        if (actionName.startsWith("saw")) {
            // Sawing a whole length of a thick material gives 1 sawdust
            return 1 / 25f;
        } else if (actionName.startsWith("drill")) {
            // Drilling 15 holes in flat gives 1 sawdust
            return 1 / 7.5f;
        }

        return 0;
    }

    private void tryToMatchCutout() {
        WorkspacePlan matchingPlan = workspaceServer.findMatchingPlan();
        if (matchingPlan != null) {
            Bids.LOG.debug("MATCH(\"{}\", {})", matchingPlan.getName(), matchingPlan.getResult().toString());

            ItemStack result = matchingPlan.getResult().copy();
            BidsEventFactory.onWoodworkingItemCrafted(player, workspaceServer.getCutout(), player.getHeldItem(), result);

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
            BidsEventFactory.onWoodworkingItemPickedUp(player, workspaceServer.getCutout(), player.getHeldItem(), itemStack);
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
                ItemStack is = new ItemStack(BidsItems.morePowder, totalAmount, MorePowderMeta.SAWDUST);
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
