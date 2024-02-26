package com.unforbidable.tfc.bids.Handlers.Client;

import com.unforbidable.tfc.bids.Core.Carving.CarvingHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.api.Enums.EnumAdzeMode;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import org.lwjgl.opengl.GL11;

public class AdzeHighlightHandler {

	@SubscribeEvent
	public void drawBlockHighlightEvent(DrawBlockHighlightEvent evt) {
        EntityPlayer player = evt.player;
        World world = player.worldObj;
        MovingObjectPosition target = evt.target;
        ItemStack item = evt.currentItem;

        TileEntity te = world.getTileEntity(target.blockX, target.blockY, target.blockZ);
        if (te instanceof TileEntityCarving) {
            TileEntityCarving teCarving = (TileEntityCarving) te;
            ICarvingTool tool = item != null && item.getItem() instanceof ICarvingTool ? (ICarvingTool) item.getItem()
                : null;

            if (!teCarving.isCarvingLocked() && !teCarving.getSelectedBit().isEmpty() && tool != null) {
                double var8 = player.lastTickPosX + (player.posX - player.lastTickPosX) * evt.partialTicks;
                double var10 = player.lastTickPosY + (player.posY - player.lastTickPosY) * evt.partialTicks;
                double var12 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * evt.partialTicks;

                int side = CarvingHelper.getPlayerCarvedSide(player);
                EnumAdzeMode carvingMode = CarvingHelper.getPlayerCarvingMode(player);

                AxisAlignedBB bb = carvingMode.getCarvingMode().getSelectedBitBounds(teCarving.getSelectedBit(), side)
                    .getOffsetBoundingBox(target.blockX, target.blockY, target.blockZ)
                    .expand(0.002F, 0.002F, 0.002F)
                    .getOffsetBoundingBox(-var8, -var10, -var12);

                boolean canCarveBit = teCarving.canCarveBit(teCarving.getSelectedBit(), side);

                // Setup GL for the depthbox
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);

                // Setup the GL stuff for the outline
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                if (canCarveBit) {
                    GL11.glColor4f(1.0F, 1.0F, 0.0F, 0.4F);
                } else {
                    GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.4F);
                }

                GL11.glLineWidth(4.0F);
                GL11.glDepthMask(true);

                // Draw the mini Box
                drawOutlinedBoundingBox(bb);

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
	}

	public void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB) {
		Tessellator var2 = Tessellator.instance;
		var2.startDrawing(3);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.draw();
		var2.startDrawing(3);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.draw();
		var2.startDrawing(1);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var2.draw();
	}

}
