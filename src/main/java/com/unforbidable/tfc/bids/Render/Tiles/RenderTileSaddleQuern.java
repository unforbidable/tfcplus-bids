package com.unforbidable.tfc.bids.Render.Tiles;

import com.unforbidable.tfc.bids.Core.SaddleQuern.EnumWorkStoneType;
import org.lwjgl.opengl.GL11;

import com.dunk.tfc.Render.TESR.TESRBase;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySaddleQuern;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderTileSaddleQuern extends TESRBase {

    static final ResourceLocation baseModelLocation = new ResourceLocation(Tags.MOD_ID,
            "models/SaddleQuernBase.obj");
    static final ResourceLocation handstoneModelLocation = new ResourceLocation(Tags.MOD_ID,
            "models/SaddleQuernHandstone.obj");

    static final ResourceLocation pressingStoneModelLocation = new ResourceLocation(Tags.MOD_ID,
            "models/SaddleQuernPressingStone.obj");

    static IModelCustom baseModel = null;
    static IModelCustom handstoneModel = null;

    static IModelCustom pressingStoneModel = null;

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick) {
        TileEntitySaddleQuern saddleQuern = (TileEntitySaddleQuern) tileentity;

        try {
            if (saddleQuern.getBaseStone() != null) {
                renderBaseStone(saddleQuern, x, y, z);
            }

            if (saddleQuern.getWorkStoneType() == EnumWorkStoneType.SADDLE_QUERN_CRUSHING) {
                renderQuernHandstone(saddleQuern, x, y, z);
            } else if (saddleQuern.getWorkStoneType() == EnumWorkStoneType.SADDLE_QUERN_PRESSING) {
                renderQuernPressingStone(saddleQuern, x, y, z);
            }

            if (saddleQuern.hasInputStack()) {
                renderInputStack(saddleQuern, x, y, z);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public void renderBaseStone(TileEntitySaddleQuern saddleQuern, double x, double y, double z) {
        if (baseModel == null) {
            baseModel = AdvancedModelLoader.loadModel(baseModelLocation);
        }

        bindItemStackTexture(saddleQuern.getBaseStone());

        float scale = 1f;
        int angle = getOrientationAngle(saddleQuern.getOrientation());

        // Base stone model is off angle right now
        // so we need to adjust for that
        angle += 90;

        GL11.glPushMatrix(); // start

        GL11.glTranslated(x + 0.5, y, z + 0.5);
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(angle, 0, 1, 0);

        baseModel.renderAll();

        GL11.glPopMatrix(); // end
    }

    public void renderQuernHandstone(TileEntitySaddleQuern saddleQuern, double x, double y, double z) {
        if (handstoneModel == null) {
            handstoneModel = AdvancedModelLoader.loadModel(handstoneModelLocation);
        }

        bindItemStackTexture(saddleQuern.getWorkStone());

        float scale = 1f;
        Vec3 offset = getWorkStoneOffset(saddleQuern.getOrientation(), saddleQuern.getWorkStonePosition());
        int angle = getOrientationAngle(saddleQuern.getOrientation());

        GL11.glPushMatrix(); // start

        GL11.glTranslated(x + 0.5 + offset.xCoord, y + 0.75, z + 0.5 + offset.zCoord);
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(angle, 0, 1, 0);

        handstoneModel.renderAll();

        GL11.glPopMatrix(); // end
    }

    public void renderQuernPressingStone(TileEntitySaddleQuern saddleQuern, double x, double y, double z) {
        if (pressingStoneModel == null) {
            pressingStoneModel = AdvancedModelLoader.loadModel(pressingStoneModelLocation);
        }

        bindItemStackTexture(saddleQuern.getWorkStone());

        float scale = 1f;

        GL11.glPushMatrix(); // start

        GL11.glTranslated(x + 0.5, y + 0.75, z + 0.5);
        GL11.glScalef(scale, scale, scale);

        pressingStoneModel.renderAll();

        GL11.glPopMatrix(); // end
    }

    private Vec3 getWorkStoneOffset(int orientation, float workStonePosition) {
        double offset = workStonePosition * (1f / 16 * 5);

        switch (orientation) {
            case 0:
                return Vec3.createVectorHelper(0, 0, -offset);

            case 1:
                return Vec3.createVectorHelper(offset, 0, 0);

            case 2:
                return Vec3.createVectorHelper(0, 0, offset);

            case 3:
                return Vec3.createVectorHelper(-offset, 0, 0);
        }

        return Vec3.createVectorHelper(0, 0, 0);
    }

    private void renderInputStack(TileEntitySaddleQuern saddleQuern, double x, double y, double z) {
        EntityItem customitem = new EntityItem(field_147501_a.field_147550_f); // tileEntityRenderer.worldObj
        customitem.hoverStart = 0f;

        float scale = 1f;
        Vec3 offset = getInputStackOffset(saddleQuern.getOrientation());
        int angle = getOrientationAngle(saddleQuern.getOrientation());

        GL11.glPushMatrix(); // start

        GL11.glTranslated(x + offset.xCoord, y + 0.75, z + offset.zCoord);

        if (Minecraft.getMinecraft().gameSettings.fancyGraphics) {
            GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
        }

        GL11.glScalef(scale, scale, scale);

        customitem.setEntityItemStack(saddleQuern.getInputStack());
        itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);

        GL11.glPopMatrix(); // end
    }

    private Vec3 getInputStackOffset(int orientation) {
        switch (orientation) {
            case 0:
                return Vec3.createVectorHelper(0.5, 0, 0.75);

            case 1:
                return Vec3.createVectorHelper(0.25, 0, 0.5);

            case 2:
                return Vec3.createVectorHelper(0.5, 0, 0.25);

            case 3:
                return Vec3.createVectorHelper(0.75, 0, 0.5);
        }

        return Vec3.createVectorHelper(0, 0, 0);
    }

    private int getOrientationAngle(int orientation) {
        return (2 - orientation) * 90;
    }

    private void bindItemStackTexture(ItemStack itemStack) {
        Block block = Block.getBlockFromItem(itemStack.getItem());
        IIcon icon = block.getIcon(0, itemStack.getItemDamage());
        if (icon != null) {
            String iconName = icon.getIconName();
            String path = "textures/blocks/" + iconName.substring(iconName.indexOf(':') + 1) + ".png";
            ResourceLocation resource = new ResourceLocation(Tags.MOD_ID, path);
            bindTexture(resource);
        }
    }

}
