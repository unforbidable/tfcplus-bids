package com.unforbidable.tfc.bids.core.network;

import com.unforbidable.tfc.bids.core.network.packet.PacketContext;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class NetworkUtil {

    public static PacketContext getPackerContextFromMessageContext(MessageContext ctx) {
        EntityPlayer player = getPlayerFromMessageContext(ctx);
        World world = player.worldObj;
        return new PacketContext(world, player);
    }

    public static EntityPlayer getPlayerFromMessageContext(MessageContext ctx) {
        return ctx.side == Side.SERVER ? getPlayerFromServerMessageContext(ctx) : getPlayerFromClientMessageContext(ctx);
    }

    private static EntityPlayer getPlayerFromServerMessageContext(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    @SideOnly(Side.CLIENT)
    private static EntityPlayer getPlayerFromClientMessageContext(MessageContext ctx) {
        return Minecraft.getMinecraft().thePlayer;
    }

    public static World getWorldFromMessageContext(MessageContext ctx) {
        return getPlayerFromMessageContext(ctx).worldObj;
    }

}
