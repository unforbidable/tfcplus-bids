package com.unforbidable.tfc.bids.core.network.tileentity;

import com.unforbidable.tfc.bids.core.network.NetworkMessage;
import com.unforbidable.tfc.bids.core.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMessage extends NetworkMessage {

    int x;
    int y;
    int z;

    public TileEntityMessage() {
    }

    public TileEntityMessage(Packet packet, TileEntity tileEntity) {
        super(packet);

        this.x = tileEntity.xCoord;
        this.y = tileEntity.yCoord;
        this.z = tileEntity.zCoord;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        x = buf.readShort();
        y = buf.readShort();
        z = buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeShort(x);
        buf.writeShort(y);
        buf.writeShort(z);
    }

}
