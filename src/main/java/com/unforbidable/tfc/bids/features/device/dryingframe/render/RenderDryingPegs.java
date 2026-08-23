package com.unforbidable.tfc.bids.features.device.dryingframe.render;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackTyingEquipment;
import com.unforbidable.tfc.bids.features.device.dryingframe.tileentity.TileEntityDryingPegs;
import com.unforbidable.tfc.bids.features.device.dryingrack.main.DryingRackHelper;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderDryingPegs implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (renderer.hasOverrideBlockTexture()) {
            renderer.renderStandardBlock(block, x, y, z);
        }

        TileEntityDryingPegs dryingPegs = (TileEntityDryingPegs)world.getTileEntity(x, y, z);

        if (dryingPegs.hasItem()) {
            int cordageMetadata = 12;
            Block cordageBlock = Blocks.wool;

            ItemStack cordage = dryingPegs.getItem().tyingItem;
            if (cordage != null) {
                DryingRackTyingEquipment tyingEquipment = DryingRackHelper.findTyingEquipment(cordage);
                if (tyingEquipment != null) {
                    cordageBlock = tyingEquipment.renderBlock;
                    cordageMetadata = tyingEquipment.renderBlockMetadata;
                }
            }

            RenderBlocksWithRotation rotationRenderer = new RenderBlocksWithRotation(renderer);
            rotationRenderer.staticTexture = true;

            int prevMeta = Minecraft.getMinecraft().theWorld.getBlockMetadata(x, y, z);
            Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, cordageMetadata, 0);

            double thick = 1 / 32d;
            double thickHalf = thick / 2;
            double minY = 0.326 - thickHalf;
            double maxY = 0.326 + thickHalf;

            for (int i = 0; i < 4; i++) {
                RenderBlocksWithRotation.yRotation += rotationRenderer.rot45 * 2;

                double minX = 0.5 - thickHalf;
                double minZ = 1.05;
                double maxX = 0.5 + thickHalf;
                double maxZ = 1.4;
                rotationRenderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
                rotationRenderer.renderStandardBlock(cordageBlock, x, y, z);
            }
            RenderBlocksWithRotation.yRotation = 0;

            for (int i = 2; i < 6; i++) {
                ForgeDirection dir = ForgeDirection.getOrientation(i);
                Block pegBlock = world.getBlock(x + dir.offsetX, y, z + dir.offsetZ);
                double pegHalf = pegBlock.getBlockBoundsMaxX() - 0.5;

                double minX = 0.5 - pegHalf - thick + dir.offsetX;
                double maxX = 0.5 + pegHalf + thick + dir.offsetX;
                double minZ = 0.5 - pegHalf - thick + dir.offsetZ;
                double maxZ = 0.5 + pegHalf + thick + dir.offsetZ;
                rotationRenderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
                rotationRenderer.renderStandardBlock(cordageBlock, x, y, z);
            }

            Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, prevMeta, 0);
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

}
