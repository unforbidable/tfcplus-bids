package com.unforbidable.tfc.bids.Core.Carving;

import com.unforbidable.tfc.bids.Core.Network.TileEntityMessageBase;

import io.netty.buffer.ByteBuf;

public class CarvingMessage extends TileEntityMessageBase {

    private int action;
    private int flag;
    private CarvingBit bit = CarvingBit.Empty;

    public CarvingMessage() {
        super(-1, -1, -1, -1);
    }

    public CarvingMessage(int x, int y, int z, int action) {
        super(x, y, z, action);
    }

    public CarvingMessage setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public int getFlag() {
        return flag;
    }

    public CarvingMessage setBit(CarvingBit bit) {
        this.bit = bit;
        return this;
    }

    public CarvingBit getBit() {
        return bit;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        action = buf.readByte();
        flag = buf.readByte();

        boolean isEmpty = buf.readBoolean();
        if (!isEmpty) {
            int bitX = buf.readByte();
            int bitY = buf.readByte();
            int bitZ = buf.readByte();
            bit = new CarvingBit(bitX, bitY, bitZ);
        } else {
            bit = CarvingBit.Empty;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeByte(action);
        buf.writeByte(flag);

        buf.writeBoolean(bit.isEmpty());
        if (!bit.isEmpty()) {
            buf.writeByte(bit.bitX);
            buf.writeByte(bit.bitY);
            buf.writeByte(bit.bitZ);
        }
    }

    public static class ClientHandler extends ClientHandlerBase<CarvingMessage> {
    }

    public static class ServerHandler extends ServerHandlerBase<CarvingMessage> {
    }

}
