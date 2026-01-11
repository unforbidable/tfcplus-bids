package com.unforbidable.tfc.bids.Core.Woodworking.Workspace;

import com.unforbidable.tfc.bids.Core.Woodworking.Actions.ActionGroupUsage;
import com.unforbidable.tfc.bids.Core.Woodworking.Plans.PlanInstance;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodworkingActionSide;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingAction;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingActionGroup;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingMaterial;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingTool;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceClient {

    private int guiX = 0;
    private int guiY = 0;
    private int guiWidth = 999;
    private int guiHeight = 999;
    private int guiScale = 1;

    private Point2D.Float cursor = new Point2D.Float();

    private IWoodworkingTool tool;
    private final IWoodworkingMaterial material;

    private final Workspace workspace;
    private Area border;

    private Rectangle workspaceRect;
    private Rectangle borderRect;

    private List<IWoodworkingAction> availableActions;
    private int selectedActionIndex = -1;
    private WorkspaceAction currentAction;

    private List<Polygon> currentCutout;
    private List<Polygon> selectedPlanCutout;
    private List<Polygon> selectedPlanErroneousCutout;
    private List<Polygon> currentActionOriginalCutout;
    private List<Polygon> currentActionEffectiveCutout;
    private List<Polygon> currentActionBlockedClearance;

    private Point mouseLocation;

    private final List<PlanInstance> plans;

    private int selectedPlanIndex = -1;

    public WorkspaceClient(IWoodworkingMaterial material, List<PlanInstance> plans) {
        this.material = material;
        this.plans = plans;

        workspace = new Workspace(0, 0, this.material.getWorkspaceWidth(), this.material.getWorkspaceHeight());
    }

    public void initGui(int guiX, int guiY, int guiWidth, int guiHeight) {
        if (this.guiX != guiX || this.guiY != guiY || this.guiWidth != guiWidth || this.guiHeight != guiHeight) {
            this.guiX = guiX;
            this.guiY = guiY;
            this.guiWidth = guiWidth;
            this.guiHeight = guiHeight;

            onGuiChanged();
        }
    }

    public void reset() {
        workspace.reset();

        onCutoutChanged();
    }

    public boolean hasCurrentAction() {
        return getCurrentAction() != null;
    }

    public void setTool(IWoodworkingTool tool) {
        if (this.tool != tool) {
            this.tool = tool;

            onToolChanged();
        }
    }

    public void setMouseLocation(Point mouseLocation) {
        this.mouseLocation = mouseLocation;

        float x = (mouseLocation.x - workspaceRect.x) / (float) guiScale;
        float y = (mouseLocation.y - workspaceRect.y) / (float) guiScale;
        setCursor(x, y);
    }

    public void setCursor(float x, float y) {
        if (cursor.x != x || cursor.y != y) {
            this.cursor = new Point2D.Float(x, y);

            onCursorChanged();
        }
    }

    public Point getEffectiveActionLocation(int actionIndex) {
        float originX = availableActions.get(actionIndex).getSpec().getOriginX();
        float originY = availableActions.get(actionIndex).getSpec().getOriginY();
        int x = Math.round(cursor.x - originX + tool.getOffsetX() / (float)guiScale);
        int y = Math.round(cursor.y - originY + tool.getOffsetY() / (float)guiScale);
        return new Point(x, y);
    }

    public Rectangle getWorkspaceRect() {
        return workspaceRect;
    }

    public Rectangle getBorder() {
        return borderRect;
    }

    public boolean isMouseInsideGui() {
        if (mouseLocation != null) {
            int x = mouseLocation.x + (tool != null ? tool.getOffsetX() : 0);
            int y = mouseLocation.y + (tool != null ? tool.getOffsetY() : 0);
            return x > guiX && x < guiX + guiWidth && y > guiY && y < guiY + guiHeight;
        }

        return false;
    }

    public List<Polygon> getCurrentCutout() {
        if (currentCutout != null) {
            return currentCutout;
        }

        if (workspace != null) {
            Area area = workspace.getCutout();
            return (currentCutout = WorkspaceHelper.getScaledAndTranslatedTriangles(area, workspaceRect.x, workspaceRect.y, guiScale));
        }

        return null;
    }

    public WorkspaceAction getCurrentAction() {
        if (currentAction != null) {
            return currentAction;
        }

        if (workspace != null && selectedActionIndex != -1) {
            Point pos = getEffectiveActionLocation(selectedActionIndex);
            return (currentAction = workspace.action(availableActions.get(selectedActionIndex)).at(pos.x, pos.y));
        }

        return null;
    }

    public List<Polygon> getSelectedPlanCutout() {
        if (selectedPlanCutout != null) {
            return selectedPlanCutout;
        }

        if (hasPlan()) {
            Area area = getSelectedPlan().getCutout();
            return (selectedPlanCutout = WorkspaceHelper.getScaledAndTranslatedTriangles(area, workspaceRect.x, workspaceRect.y, guiScale));
        }

        return null;
    }

    public List<Polygon> getSelectedPlanErroneousCutout() {
        if (selectedPlanErroneousCutout != null) {
            return selectedPlanErroneousCutout;
        }

        if (hasPlan()) {
            Area area = new Area(workspace.getCutout());
            area.subtract(getSelectedPlan().getCutout());
            return (selectedPlanErroneousCutout = WorkspaceHelper.getScaledAndTranslatedTriangles(area, workspaceRect.x, workspaceRect.y, guiScale));
        }

        return null;
    }

    public List<Polygon> getCurrentActionOriginalCutout() {
        if (currentActionOriginalCutout != null) {
            return currentActionOriginalCutout;
        }

        if (getCurrentAction() != null) {
            Area area = new Area(border);
            area.intersect(getCurrentAction().getCutout());
            return (currentActionOriginalCutout = WorkspaceHelper.getScaledAndTranslatedTriangles(area, workspaceRect.x, workspaceRect.y, guiScale));
        }

        return null;
    }

    public List<Polygon> getCurrentActionEffectiveCutout() {
        if (currentActionEffectiveCutout != null) {
            return currentActionEffectiveCutout;
        }

        if (getCurrentAction() != null) {
            Area area = getCurrentAction().getEffectiveCutoutArea();
            return (currentActionEffectiveCutout = WorkspaceHelper.getScaledAndTranslatedTriangles(area, workspaceRect.x, workspaceRect.y, guiScale));
        }

        return null;
    }

    public List<Polygon> getCurrentActionBlockedClearance() {
        if (currentActionBlockedClearance != null) {
            return currentActionBlockedClearance;
        }

        if (getCurrentAction() != null) {
            Area area = new Area(border);
            area.intersect(workspace.getInvertedCutout());
            area.intersect(getCurrentAction().getClearance());
            return (currentActionBlockedClearance = WorkspaceHelper.getScaledAndTranslatedTriangles(area, workspaceRect.x, workspaceRect.y, guiScale));
        }

        return null;
    }

    private void onGuiChanged() {
        workspaceRect = null;

        int borderX = 1;
        int borderY = 1;

        float guiScaleX = (guiWidth - 4) / (float)(material.getWorkspaceWidth() + borderX);
        float guiScaleY = (guiHeight - 4) / (float)(material.getWorkspaceHeight() + borderY);
        guiScale = (int)Math.floor(Math.min(guiScaleX, guiScaleY));

        int centerX = guiX + guiWidth / 2;
        int centerY = guiY + guiHeight / 2;
        int width = material.getWorkspaceWidth() * guiScale;
        int height = material.getWorkspaceHeight() * guiScale;
        int x = centerX - width / 2;
        int y = centerY - height / 2;
        workspaceRect = new Rectangle(x, y, width, height);

        border = new Area(new Rectangle(-borderX, -borderY, material.getWorkspaceWidth() + borderX * 2, material.getWorkspaceHeight() + borderY * 2));
        borderRect = new Rectangle(x - borderX * guiScale, y - borderY * guiScale, width + borderX * 2 * guiScale, height + borderY * 2 * guiScale);

        onWorkspaceChanged();
    }

    private void onWorkspaceChanged() {
        currentAction = null;
        currentCutout = null;

        onCurrentActionChanged();
    }

    private void onToolChanged() {
        availableActions = null;
        selectedActionIndex = -1;
        currentAction = null;

        if (tool != null) {
            availableActions = new ArrayList<IWoodworkingAction>();

            for (IWoodworkingActionGroup group : tool.getActionGroups()) {
                for (IWoodworkingAction action : group.getActions()) {
                    if (group.getUsage() != ActionGroupUsage.FLAT_MATERIAL_ONLY || material.isFlat()) {
                        availableActions.add(action);
                    }
                }
            }

            if (availableActions.size() > 0) {
                selectedActionIndex = 0;
            }
        }

        onCurrentActionChanged();
    }

    private void onCursorChanged() {
        // Moving the cursor may invalidate the current action
        if (currentAction != null) {
            Point pos = getEffectiveActionLocation(selectedActionIndex);
            if (currentAction.x != pos.x || currentAction.y != pos.y) {
                currentAction = null;

                onCurrentActionChanged();
            }
        }
    }

    private void onCurrentActionChanged() {
        currentActionOriginalCutout = null;
        currentActionEffectiveCutout = null;
        currentActionBlockedClearance = null;
    }

    public boolean canPerformCurrentAction() {
        return getCurrentAction() != null && getCurrentAction().canPerform();
    }

    public boolean performCurrentAction() {
        if (getCurrentAction() != null) {
            if (getCurrentAction().perform()) {
                onCutoutChanged();

                return true;
            }
        }

        return false;
    }

    private void onCutoutChanged() {
        currentCutout = null;
        currentActionEffectiveCutout = null;
        currentActionBlockedClearance = null;
        selectedPlanErroneousCutout = null;
    }

    public void selectNextAction() {
        if (availableActions != null && availableActions.size() > 0) {
            int prevSelectedActionIndex = selectedActionIndex;

            if (!findNextAllowedAction() && !findNextPreferredSideAction()) {
                selectedActionIndex = (selectedActionIndex + 1) % availableActions.size();
            }

            if (selectedActionIndex != prevSelectedActionIndex) {
                currentAction = null;

                onCurrentActionChanged();
            }
        }
    }

    private boolean findNextAllowedAction() {
        for (int i = 0; i < availableActions.size(); i++) {
            int j = (i + selectedActionIndex + 1) % availableActions.size();
            Point pos = getEffectiveActionLocation(j);
            WorkspaceAction action = workspace.action(availableActions.get(j)).at(pos.x, pos.y);
            if (action.canPerform()) {
                selectedActionIndex = j;
                return true;
            }
        }

        return false;
    }

    private boolean findNextPreferredSideAction() {
        EnumWoodworkingActionSide preferredSide = getCursorSide();
        if (preferredSide != EnumWoodworkingActionSide.NONE) {
            for (int i = 0; i < availableActions.size(); i++) {
                int j = (i + selectedActionIndex + 1) % availableActions.size();

                // find a preferred action, could be the same one of there is only one
                if (availableActions.get(j).getSide() == preferredSide) {
                    selectedActionIndex = j;
                    return true;
                }
            }
        }

        return false;
    }

    public EnumWoodworkingActionSide getCursorSide() {
        if (cursor != null && material != null) {
            float middleX = material.getWorkspaceWidth() / 4f;
            float middleY = material.getWorkspaceHeight() / 4f;
            float centerX = material.getWorkspaceWidth() / 2f;
            float centerY = material.getWorkspaceHeight() / 2f;
            float x = cursor.x - centerX;
            float y = cursor.y - centerY;
            if (Math.abs(x) > middleX || Math.abs(y) > middleY) {
                if (x > y) {
                    return (-x > y) ? EnumWoodworkingActionSide.TOP : EnumWoodworkingActionSide.RIGHT;
                } else {
                    return (-x > y) ? EnumWoodworkingActionSide.LEFT : EnumWoodworkingActionSide.BOTTOM;
                }
            } else {
                return (x > 0) ? EnumWoodworkingActionSide.RIGHT : EnumWoodworkingActionSide.LEFT;
            }
        }

        return EnumWoodworkingActionSide.NONE;
    }

    public List<PlanInstance> getPlans() {
        return plans;
    }

    public PlanInstance getSelectedPlan() {
        if (selectedPlanIndex != -1) {
            return plans.get(selectedPlanIndex);
        } else {
            return null;
        }
    }

    public void selectPlan(int selectedPlanIndex) {
        if (this.selectedPlanIndex != selectedPlanIndex && selectedPlanIndex < plans.size()) {
            this.selectedPlanIndex = selectedPlanIndex;

            onPlansChanged();
        }
    }

    public void clearPlanSelection() {
        if (this.selectedPlanIndex != -1) {
            this.selectedPlanIndex = -1;

            onPlansChanged();
        }
    }

    public boolean hasPlan() {
        return this.selectedPlanIndex != -1;
    }

    private void onPlansChanged() {
        selectedPlanCutout = null;
        selectedPlanErroneousCutout = null;
    }

}
