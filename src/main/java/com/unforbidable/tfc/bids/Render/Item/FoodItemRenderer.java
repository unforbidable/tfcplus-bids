package com.unforbidable.tfc.bids.Render.Item;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Food.ItemEgg;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.Interfaces.IFood;
import com.dunk.tfc.api.TFC_ItemHeat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class FoodItemRenderer implements IItemRenderer {
    // This is a clone of TFC FoodItemRenderer
    // that does not unnecessarily set the color.
    // This allows the item to be correctly rendered as grayed out in the Achievement tab

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack is, Object... data) {

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (is.getItem() instanceof IFood && is.hasTagCompound()) {
            if (is.getItem() instanceof ItemEgg) {
                renderIcon(0, 0, is.getItem().getIcon(is, 0), 16, 16);
            } else {
                renderIcon(0, 0, is.getItem().getIconIndex(is), 16, 16);
            }
            float decayPerc = Math.max(Food.getDecay(is) / Food.getWeight(is), 0);
            int tempIndex = 0;
            if (is != null && is.getItem() instanceof ItemFoodTFC) {
                tempIndex = ((ItemFoodTFC) is.getItem()).cookTempIndex;
            }
            int cookTemp = (int) Food.globalCookTemps[tempIndex];
            cookTemp = (int) Math.max(cookTemp, HeatRegistry.getInstance().getMeltingPoint(is));
            //We don't want to start changing the colour until it's way closer to being cooked
            //We'll start when it's within 5 degrees??

            float cookPerc = Math.max(Math.min(Math.max(Food.getCooked(is) + 3.5f - cookTemp, 0) / 5f, 1), 0);
            if (is.getItem() instanceof ItemFoodTFC) {
                int color = Food.getCookedColorMultiplier(is);
                GL11.glColor4f(((color & 0xFF0000) >> 16) / 255f, ((color & 0x00ff00) >> 8) / 255f, (color & 0x0000ff) / 255f, cookPerc);
                if (((ItemFoodTFC) is.getItem()).cookedIcon != null)
                    renderIcon(0, 0, ((ItemFoodTFC) is.getItem()).cookedIcon, 16, 16);
                else if (is.getItem() instanceof ItemEgg) {
                    renderIcon(0, 0, is.getItem().getIcon(is, 0), 16, 16);
                } else {
                    renderIcon(0, 0, is.getItem().getIconIndex(is), 16, 16);
                }

            }
            float decayTop = decayPerc * 13.0F;

            if (type == ItemRenderType.INVENTORY) {
                if (TFC_ItemHeat.hasTemp(is)) {
                    float meltTemp = TFC_ItemHeat.isCookable(is);
                    float temp = TFC_ItemHeat.getTemp(is);
                    float cooked = Food.getCooked(is);
                    float ambientTemp = TFC_Climate.getHeightAdjustedTemp(Minecraft.getMinecraft().theWorld, (int) Minecraft.getMinecraft().thePlayer.posX, (int) Minecraft.getMinecraft().thePlayer.posY, (int) Minecraft.getMinecraft().thePlayer.posZ);
                    float deltaTemp = temp - ambientTemp;
                    float deltaMeltTemp = meltTemp - ambientTemp;

                    float deltaCook = cooked;
                    float deltaCookTemp = meltTemp;

                    if (deltaCook > 0.5 /*&& deltaCook < deltaCookTemp*/) {
                        renderQuad(1, 2, 13, 1, 0);

                        float tempValue = (13 / (deltaCookTemp + cookTemp / 2)) * deltaCook;
                        if (tempValue < 0) tempValue = 0;
                        if (tempValue > 13) tempValue = 13;

                        if (deltaCook < deltaCookTemp * 1F)    // Cold
                            renderQuad(1, 2, tempValue, 1, 0xddddaa);
                        else    // VeryHot
                            renderQuad(1, 2, tempValue, 1, 0xff2000);
                    }

                    if (deltaTemp > 0.5 && deltaTemp < deltaMeltTemp + cookTemp) {
                        renderQuad(1, 1, 13, 1, 0);

                        float tempValue = (13 / (deltaMeltTemp + cookTemp)) * deltaTemp;
                        if (tempValue < 0) tempValue = 0;
                        if (tempValue > 13) tempValue = 13;

                        if (deltaTemp < deltaMeltTemp * 0.1F)    // Cold
                            renderQuad(1, 1, tempValue, 1, 0xffffff);
                        else if (deltaTemp < deltaMeltTemp * 0.4F)    // Warm
                            renderQuad(1, 1, tempValue, 1, 0xffdd00);
                        else if (deltaTemp < deltaMeltTemp * 1f)    // Hot
                            renderQuad(1, 1, tempValue, 1, 0xffaa00);
                        else    // VeryHot
                            renderQuad(1, 1, tempValue, 1, 0xff0000);
                    } else if (deltaTemp < -0.5) {
                        renderQuad(1, 1, 13, 1, 0);

                        float tempValue = (13 / -(20 + ambientTemp)) * deltaTemp;
                        if (tempValue < 0) tempValue = 0;
                        if (tempValue > 13) tempValue = 13;

                        if (deltaTemp > -(20 + ambientTemp) * 0.1F)    // Cold
                            renderQuad(1, 1, tempValue, 1, 0x55aaff);
                        else if (deltaTemp > -(20 + ambientTemp) * 0.4F)    // Warm
                            renderQuad(1, 1, tempValue, 1, 0x0080ff);
                        else if (deltaTemp > -(20 + ambientTemp) * 0.8F)    // Hot
                            renderQuad(1, 1, tempValue, 1, 0x0060ff);
                        else    // VeryHot
                            renderQuad(1, 1, tempValue, 1, 0x0000ff);
                    }
                }

                float weightPerc = Food.getWeight(is) / ((IFood) is.getItem()).getFoodMaxWeight(is);

                if (weightPerc <= 1 && weightPerc >= 0) // Only draw bars if the weight is within the max weight. Food created using the blank createTag (weight = 999) will not have the bars.
                {
                    if (((IFood) is.getItem()).renderDecay()) {
                        if (decayPerc < 0.10) {
                            decayTop = decayTop * 10;
                            renderQuad(1, 13, 13 - decayTop, 1, 0x00ff00);
                        } else
                            renderQuad(1, 13, 13 - decayTop, 1, 0xff0000);
                    }
                    if (((IFood) is.getItem()).renderWeight()) {
                        renderQuad(1, 14, 13, 1, 0);
                        float weightTop = weightPerc * 13.0F;

                        renderQuad(1, 14, weightTop, 1, 0xffffff);
                    }
                }
            }

        } else if (is.getItem() instanceof IFood) {
            if (is.getItem() instanceof ItemEgg) {
                renderIcon(0, 0, is.getItem().getIcon(is, 0), 16, 16);
            } else {
                renderIcon(0, 0, is.getItem().getIconIndex(is), 16, 16);
            }
        }

        GL11.glPopAttrib();
    }

    public static void renderIcon(int x, int y, IIcon icon, int sizeX, int sizeY) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + sizeY, 0, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + sizeX, y + sizeY, 0, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + sizeX, y + 0, 0, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(x + 0, y + 0, 0, icon.getMinU(), icon.getMinV());
        tessellator.draw();
    }

    private static void renderQuad(double x, double y, double sizeX, double sizeY, int color) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setColorOpaque_I(color);
        tess.addVertex(x + 0, y + 0, 0.0D);
        tess.addVertex(x + 0, y + sizeY, 0.0D);
        tess.addVertex(x + sizeX, y + sizeY, 0.0D);
        tess.addVertex(x + sizeX, y + 0, 0.0D);
        tess.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

}
