package com.unforbidable.tfc.bids.Handlers.Client;

import com.dunk.tfc.Blocks.BlockWoodSupport;
import com.dunk.tfc.Blocks.Devices.BlockAxleBearing;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class PlacementHighlightHandler {

    @SubscribeEvent
    public void drawBlockHighlightEvent(DrawBlockHighlightEvent evt)
    {
        World world = evt.player.worldObj;
        double var8 = evt.player.lastTickPosX + (evt.player.posX - evt.player.lastTickPosX) * evt.partialTicks;
        double var10 = evt.player.lastTickPosY + (evt.player.posY - evt.player.lastTickPosY) * evt.partialTicks;
        double var12 = evt.player.lastTickPosZ + (evt.player.posZ - evt.player.lastTickPosZ) * evt.partialTicks;
        Block b = null;

        if (evt.currentItem != null && evt.currentItem.getItem() instanceof ItemBlock)
        {
            int x, y, z;
            x = evt.target.blockX;
            y = evt.target.blockY;
            z = evt.target.blockZ;
            if (evt.target.sideHit == 0)
            {
                y--;
            }
            else if (evt.target.sideHit == 1)
            {
                y++;
            }
            else if (evt.target.sideHit == 2)
            {
                z--;
            }
            else if (evt.target.sideHit == 3)
            {
                z++;
            }
            else if (evt.target.sideHit == 4)
            {
                x--;
            }
            else if (evt.target.sideHit == 5)
            {
                x++;
            }
            b = world.getBlock(evt.target.blockX, evt.target.blockY, evt.target.blockZ);

            if (evt.currentItem.getItem() == Item.getItemFromBlock(BidsBlocks.woodAxleWallBearing)
                && (b.isSideSolid(world, evt.target.blockX, evt.target.blockY, evt.target.blockZ, ForgeDirection.getOrientation(evt.target.sideHit)) || b instanceof BlockWoodSupport)
                && world.getBlock(x, y, z).isReplaceable(world, x, y, z))
            {

                int meta = BlockAxleBearing.getMetaForBlockPlacement(world, x, y, z, evt.player, evt.currentItem, evt.player.isSneaking());
                if (meta >= 0)
                {
                    //System.out.println(meta);
                    int dir = BlockAxleBearing.getDirectionFromMetadata(meta);
                    GL11.glPushMatrix();
                    //Draw a highlighter for it
                    GL11.glDisable(GL11.GL_TEXTURE_2D);

                    //Setup GL for the depthbox
                    GL11.glEnable(GL11.GL_BLEND);
                    //Setup the GL stuff for the outline
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    GL11.glDepthMask(false);
                    //Draw the mini Box

                    double px, py, pz;
                    px = evt.player.prevPosX + (evt.player.posX - evt.player.prevPosX) * evt.partialTicks;
                    py = evt.player.prevPosY + (evt.player.posY - evt.player.prevPosY) * evt.partialTicks;
                    pz = evt.player.prevPosZ + (evt.player.posZ - evt.player.prevPosZ) * evt.partialTicks;

                    if (dir == 0)
                    {
                        GL11.glColor4f(1.0F, 0.5F, 1.5F, 0.4F);
                        //Bottom chunk
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.125, y + 0.01, z + 0.01, x + 0.875, y + 0.3, z + 0.99).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, false, true, true, true, true });

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
                        //Top chunk
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.125, y + 0.7, z + 0.01, x + 0.875, y + 0.99, z + 0.99).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { false, true, true, true, true, true });

                        //Left wall
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.125, y + 0.3, z + 0.01, x + 0.875, y + 0.7, z + 0.3).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { false, false, true, true, true, false });

                        //Right wall
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.125, y + 0.3, z + 0.7, x + 0.875, y + 0.7, z + 0.99).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { false, false, true, true, false, true });

                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 0.4F);
                        //hole
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.125, y + 0.3, z + 0.3, x + 0.875, y + 0.7, z + 0.7).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, false, false, true, true });
                    }
                    else if (dir == 1)
                    {
                        GL11.glColor4f(1.0F, 0.5F, 1.5F, 0.4F);
                        //Bottom chunk
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.01, y + 0.01, z + 0.125, x + 0.99, y + 0.3, z + 0.875).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, false, true, true, true, true });

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
                        //Top chunk
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.01, y + 0.7, z + 0.125, x + 0.99, y + 0.99, z + 0.875).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { false, true, true, true, true, true });

                        //Left wall
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.01, y + 0.3, z + 0.125, x + 0.3, y + 0.7, z + 0.875).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { false, false, true, false, true, true });

                        //Right wall
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.7, y + 0.3, z + 0.125, x + 0.99, y + 0.7, z + 0.875).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { false, false, false, true, true, true });

                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 0.4F);
                        //hole
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.3, y + 0.3, z + 0.125, x + 0.7, y + 0.7, z + 0.875).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, true, true, false, false });
                    }
                    else if (dir == 2)
                    {
                        GL11.glColor4f(1.0F, 0.5F, 1.5F, 0.4F);
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.01, y + 0.125, z + 0.01, x + 0.99, y + 0.875, z + 0.3).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, true, true, true, false });

                        //Top chunk
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.01, y + 0.125, z + 0.75, x + 0.99, y + 0.875, z + 0.99).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, true, true, false, true });

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
                        //Left wall
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.01, y + 0.125, z + 0.3, x + 0.3, y + 0.875, z + 0.75).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, true, false, false, false });

                        //Right wall
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.7, y + 0.125, z + 0.3, x + 0.99, y + 0.875, z + 0.75).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, false, true, false, false });

                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 0.4F);
                        //hole
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.3, y + 0.125, z + 0.3, x + 0.7, y + 0.875, z + 0.75).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { false, false, true, true, true, true });
                    }
                    else if (dir == 3)
                    {
                        GL11.glColor4f(1.0F, 0.5F, 1.5F, 0.4F);
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.01, y + 0.125, z + 0.01, x + 0.3, y + 0.875, z + 0.99).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, true, false, true, true });

                        //Top chunk
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.75, y + 0.125, z + 0.01, x + 0.99, y + 0.875, z + 0.99).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, false, true, true, true });

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
                        //Left wall
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.3, y + 0.125, z + 0.01, x + 0.75, y + 0.875, z + 0.3).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, false, false, true, false });

                        //Right wall
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.3, y + 0.125, z + 0.7, x + 0.75, y + 0.875, z + 0.99).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { true, true, false, false, false, true });

                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 0.4F);
                        //hole
                        drawBox(AxisAlignedBB.getBoundingBox(x + 0.3, y + 0.125, z + 0.3, x + 0.75, y + 0.875, z + 0.7).getOffsetBoundingBox(-var8, -var10, -var12),
                            new boolean[] { false, false, true, true, true, true });
                    }
                    GL11.glDepthMask(true);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    public void drawBox(AxisAlignedBB par1AxisAlignedBB, boolean[] drawableFaces)
    {
        Tessellator var2 = Tessellator.instance;

        if (drawableFaces[1])
        {
            //Top
            var2.startDrawing(GL11.GL_QUADS);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            var2.draw();
        }

        if (drawableFaces[0])
        {
            //Bottom
            var2.startDrawing(GL11.GL_QUADS);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            var2.draw();
        }

        if (drawableFaces[2])
        {
            //-x
            var2.startDrawing(GL11.GL_QUADS);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            var2.draw();
        }

        if (drawableFaces[3])
        {
            //+x
            var2.startDrawing(GL11.GL_QUADS);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            var2.draw();
        }
        if (drawableFaces[4])
        {
            //-z
            var2.startDrawing(GL11.GL_QUADS);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            var2.draw();
        }
        if (drawableFaces[5])
        {
            //+z
            var2.startDrawing(GL11.GL_QUADS);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            var2.draw();
        }
    }

}
