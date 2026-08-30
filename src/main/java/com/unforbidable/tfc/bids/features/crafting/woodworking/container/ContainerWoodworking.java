package com.unforbidable.tfc.bids.features.crafting.woodworking.container;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionSummary;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final Map<String, Integer> actionHistory = new HashMap<>();

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
            for (NetworkAction action : packet.getActions()) {
                boolean result = workspaceServer.performAction(action.name, action.x, action.y);

                Bids.LOG.debug("ACTION(\"{}\", {}, {}) => {}", action.name, action.x, action.y, result ? "OK" : "SUCCESS");

                if (result) {
                    if (actionHistory.containsKey(action.name)) {
                        actionHistory.put(action.name, actionHistory.get(action.name) + 1);
                    } else {
                        actionHistory.put(action.name, 1);
                    }
                }
            }

            if (packet.getDamage() > 0) {
                WoodworkingHelper.damageItem(player.inventory.getItemStack(), packet.getDamage(), player);

                player.inventory.getItemStack().damageItem(packet.getDamage(), player);
                if (player.inventory.getItemStack().stackSize == 0) {
                    player.inventory.setItemStack(null);
                }
            }

            tryToMatchCutout();
        }
    }

    private void tryToMatchCutout() {
        WorkspacePlan matchingPlan = workspaceServer.findMatchingPlan();
        if (matchingPlan != null) {
            Bids.LOG.debug("MATCH(\"{}\", {})", matchingPlan.getName(), matchingPlan.getResult().toString());

            ItemStack result = matchingPlan.getResult().copy();
            BidsEventFactory.onWoodworkingItemCrafted(player, workspaceServer.getCutout(), player.getHeldItem(), result, getSummary());

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
            BidsEventFactory.onWoodworkingItemPickedUp(player, workspaceServer.getCutout(), player.getHeldItem(), itemStack, getSummary());
        }

        player.inventory.decrStackSize(player.inventory.currentItem, 1);

        if (!world.isRemote) {
            actionHistory.clear();

            workspaceServer.reset();
        } else {
            GuiWoodworking clientGui = getClientGui();
            if (clientGui != null) {
                clientGui.resetWorkspace();
            }
        }
    }

    private List<WoodworkingActionSummary> getSummary() {
        List<WoodworkingActionSummary> footprints = new ArrayList<>();
        for (Map.Entry<String, Integer> e : actionHistory.entrySet()) {
            footprints.add(new WoodworkingActionSummary(e.getKey(), e.getValue()));
        }
        return footprints;
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
