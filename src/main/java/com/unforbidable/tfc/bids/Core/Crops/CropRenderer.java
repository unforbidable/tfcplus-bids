package com.unforbidable.tfc.bids.Core.Crops;

import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class CropRenderer {

    protected final EnumCropRenderType renderType;
    protected final double width;
    protected final double height;
    protected String blockIconName;

    protected IIcon[] blockIcons;

    public CropRenderer(EnumCropRenderType renderType, double width, double height, String blockIconName) {
        this.renderType = renderType;
        this.width = width;
        this.height = height;
        this.blockIconName = blockIconName;
    }

    public EnumCropRenderType getRenderType() {
        return renderType;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public String getBlockIconName() {
        return blockIconName;
    }

    public void registerBlockIcons(IIconRegister register, int numGrowthStages) {
        blockIcons = new IIcon[numGrowthStages + 1];
        for (int i = 0; i <= numGrowthStages; i++) {
            registerBlockIconsForStage(register, i);
        }
    }

    protected void registerBlockIconsForStage(IIconRegister register, int stage) {
        blockIcons[stage] = register.registerIcon(Tags.MOD_ID + ":" + "plants/crops/" + getBlockIconNameForStage(stage));
    }

    protected String getBlockIconNameForStage(int stage) {
        return getBlockIconName() + " (" + (stage + 1) + ")";
    }

    public IIcon getBlockIcon(int stage, boolean hasFarmland) {
        return blockIcons[stage];
    }

}
