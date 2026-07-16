package com.unforbidable.tfc.bids.features.crafting.woodworking.gui;

import com.dunk.tfc.GUI.GuiContainerTFC;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionSide;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.container.ContainerWoodworking;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.WoodworkingHelper;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.WorkspacePlan;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.WorkspaceAction;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.WorkspaceClient;
import com.unforbidable.tfc.bids.features.crafting.woodworking.network.NetworkAction;
import com.unforbidable.tfc.bids.features.crafting.woodworking.network.WoodworkingPacket;
import com.unforbidable.tfc.bids.util.GuiHelper;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiWoodworking extends GuiContainerTFC {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/gui/gui_woodworking.png");

    public static final int WORKSPACE_COLOR = 0xffd7bc42;
    public static final int WORKSPACE_PLAN_CUTOUT_COLOR = 0xfff1f1ae;
    public static final int WORKSPACE_PLAN_ERRONEOUS_CUTOUT_COLOR = 0xfffdb1b1;
    public static final int WORKSPACE_BORDER_COLOR = 0xffffffff;
    public static final int WORKSPACE_CUTOUT_COLOR = 0xffffffff;
    public static final int ACTION_CUTOUT_GHOST_COLOR = 0x7711aa22;
    public static final int ACTION_CUTOUT_PREVIEW_COLOR = 0xff116622;
    public static final int ACTION_BLOCKED_CLEARANCE_COLOR = 0x44cc3311;

    private final WorkspaceClient workspaceClient;

    private Point repeatingActionPos;
    private List<WorkspaceAction> performedActions = new ArrayList<WorkspaceAction>();

    private final Random rand;

    public GuiWoodworking(InventoryPlayer inventory, World world, int x, int y, int z) {
        super(new ContainerWoodworking(inventory, world, x, y, z), 176, 149);

        rand = new Random();
        workspaceClient = createClient(inventory.player.getHeldItem());

        drawInventory = true;
    }

    private WorkspaceClient createClient(ItemStack heldItem) {
        if (heldItem != null) {
            WoodworkingMaterial material = WoodworkingHelper.getWoodworkingMaterial(heldItem);
            List<WorkspacePlan> plans = WoodworkingHelper.getWoodworkingPlans(heldItem);
            if (material != null && !plans.isEmpty()) {
                return new WorkspaceClient(material, plans);
            }
        }

        Bids.LOG.warn("Woodworking client cannot be initialized.");

        // create a dummy client
        return new WorkspaceClient(WoodworkingRegistry.materials.get(m -> m.getOreName().equals("logWood")), new ArrayList<WorkspacePlan>() {});
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void initGui() {
        super.initGui();

        buttonList.clear();

        int xOffset = guiLeft + 151;
        int yOffset = guiTop + 103;
        int iconsPerRow = 2;

        int i = 0;
        int x = 0;
        int y = 0;
        for (WorkspacePlan plan : workspaceClient.getPlans()) {
            buttonList.add(i, new GuiWoodworkingSelectPlanButton(i, xOffset - x * 18, yOffset - y * 18, 16, 16, plan.getResult(), this,
                getPlanTooltipText(plan)));

            i++;
            if (x < iconsPerRow - 1) {
                x++;
            } else {
                x = 0;
                y++;
            }
        }

        workspaceClient.initGui(guiLeft + 5, guiTop + 5, 112, 138);
    }

    private String getPlanTooltipText(WorkspacePlan plan) {
        String localizedPlanName = StatCollector.translateToLocal("gui.plans." + plan.getName());
        if (plan.getResult().stackSize > 1) {
            return plan.getResult().stackSize + "x " + localizedPlanName;
        } else {
            return localizedPlanName;
        }
    }

    @Override
    public void drawTooltip(int mx, int my, String text) {
        List<String> list = new ArrayList<String>();
        list.add(text);
        this.drawHoveringTextZLevel(list, mx, my + 15, this.fontRendererObj, 400);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        drawGui(TEXTURE);

        if (repeatingActionPos == null) {
            workspaceClient.setMouseLocation(GuiHelper.getMousePosition());
        }

        ItemStack tool = mc.thePlayer.inventory.getItemStack();
        if (tool != null) {
            workspaceClient.setTool(WoodworkingHelper.getWoodworkingTool(tool));
        } else {
            workspaceClient.setTool(null);
        }

        if (mc.thePlayer.getHeldItem() != null) {
            GuiHelper.drawRect(workspaceClient.getBorder(), WORKSPACE_BORDER_COLOR);
            GuiHelper.drawRect(workspaceClient.getWorkspaceRect(), WORKSPACE_COLOR);

            if (workspaceClient.hasPlan()) {
                GuiHelper.drawTriangles(workspaceClient.getSelectedPlanCutout(), WORKSPACE_PLAN_CUTOUT_COLOR);
            }

            GuiHelper.drawTriangles(workspaceClient.getCurrentCutout(), WORKSPACE_CUTOUT_COLOR);

            if (workspaceClient.hasPlan()) {
                GuiHelper.drawTriangles(workspaceClient.getSelectedPlanErroneousCutout(), WORKSPACE_PLAN_ERRONEOUS_CUTOUT_COLOR);
            }

            if (workspaceClient.hasCurrentAction() && workspaceClient.isMouseInsideGui()) {
                GuiHelper.drawTriangles(workspaceClient.getCurrentActionOriginalCutout(), ACTION_CUTOUT_GHOST_COLOR);
                if (workspaceClient.canPerformCurrentAction()) {
                    GuiHelper.drawTriangles(workspaceClient.getCurrentActionEffectiveCutout(), ACTION_CUTOUT_PREVIEW_COLOR);
                } else {
                    GuiHelper.drawTriangles(workspaceClient.getCurrentActionBlockedClearance(), ACTION_BLOCKED_CLEARANCE_COLOR);
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        workspaceClient.selectPlan(button.id);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
        super.mouseClicked(mouseX, mouseY, mouseEvent);

        if (workspaceClient.isMouseInsideGui()) {
            if (mouseEvent == 0) {
                if (workspaceClient.canPerformCurrentAction()) {
                    if (workspaceClient.performCurrentAction()) {
                        performedActions.add(workspaceClient.getCurrentAction());
                        pushPerformedActions();

                        // Sided action can repeat
                        if (workspaceClient.getCurrentAction().getSide() != WoodworkingActionSide.NONE) {
                            repeatingActionPos = new Point(mouseX, mouseY);
                        }
                    }
                }
            } else if (mouseEvent == 1) {
                workspaceClient.selectNextAction();
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
        super.mouseMovedOrUp(mouseX, mouseY, which);

        if (which == 0) {
            if (repeatingActionPos != null) {
                // Stop repeating when mouse button is released
                repeatingActionPos = null;

                pushPerformedActions();
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseEvent, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, mouseEvent, timeSinceLastClick);

        if (workspaceClient.isMouseInsideGui()) {
            if (mouseEvent == 0) {
                if (repeatingActionPos != null) {
                    while (tryToCatchUpRepeatingAction(mouseX, mouseY)) {
                        workspaceClient.setMouseLocation(repeatingActionPos);
                        if (workspaceClient.canPerformCurrentAction()) {
                            if (workspaceClient.performCurrentAction()) {
                                performedActions.add(workspaceClient.getCurrentAction());
                            }
                        }
                    }
                }
            }
        } else {
            if (repeatingActionPos != null) {
                // Stop repeating when mouse leaves the GUI
                repeatingActionPos = null;

                pushPerformedActions();
            }
        }
    }

    private boolean tryToCatchUpRepeatingAction(int mouseX, int mouseY) {
        switch (workspaceClient.getCurrentAction().getSide()) {
            case TOP:
                if (repeatingActionPos.y < mouseY) {
                    repeatingActionPos.y++;
                    return true;
                } else {
                    return false;
                }
            case BOTTOM:
                if (repeatingActionPos.y > mouseY) {
                    repeatingActionPos.y--;
                    return true;
                } else {
                    return false;
                }
            case LEFT:
                if (repeatingActionPos.x < mouseX) {
                    repeatingActionPos.x++;
                    return true;
                } else {
                    return false;
                }
            case RIGHT:
                if (repeatingActionPos.x > mouseX) {
                    repeatingActionPos.x--;
                    return true;
                } else {
                    return false;
                }
        }

        return false;
    }

    public void pushPerformedActions() {
        if (performedActions.size() > 0) {
            WoodworkingPacket packet = new WoodworkingPacket();
            packet.setEvent(WoodworkingPacket.EVENT_PERFORM_ACTION);

            float accumulatedDamage = 0;
            for (WorkspaceAction workspaceAction : performedActions) {
                packet.addAction(new NetworkAction(workspaceAction.action.getName(), workspaceAction.x, workspaceAction.y));
                accumulatedDamage += WoodworkingHelper.getActionToolDamageByName(workspaceAction.action.getName());
            }

            int integralDamage = (int) Math.floor(accumulatedDamage);
            float partialDamage = accumulatedDamage - integralDamage;
            int totalDamage = integralDamage + (rand.nextFloat() < partialDamage ? 1 : 0);

            if (totalDamage > 0) {
                packet.setDamage(totalDamage);
                mc.thePlayer.inventory.getItemStack().damageItem(totalDamage, mc.thePlayer);
                if (mc.thePlayer.inventory.getItemStack().stackSize == 0) {
                    mc.thePlayer.inventory.setItemStack(null);
                }
            }

            Network.sendToContainer(packet, mc.thePlayer);

            performedActions.clear();
        }
    }

    public void resetWorkspace() {
        workspaceClient.reset();
    }

}


