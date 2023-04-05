package com.unforbidable.tfc.bids.Core.Carving;

import com.unforbidable.tfc.bids.Core.Network.TileEntityMessageBase;

import com.unforbidable.tfc.bids.api.Enums.EnumAdzeMode;
import io.netty.buffer.ByteBuf;

public class CarvingMessage extends TileEntityMessageBase {

    private int action;
    private int flag;
    private CarvingBit bit = CarvingBit.Empty;
    private EnumAdzeMode carvingMode = EnumAdzeMode.DEFAULT_MODE;
    private int side;
    private byte[] carvedData = null;

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

    public CarvingMessage setCarvedData(byte[] carvedData) {
        this.carvedData = carvedData;
        return this;
    }

    public byte[] getCarveData() {
        return carvedData;
    }

    public CarvingMessage setBit(CarvingBit bit) {
        this.bit = bit;
        return this;
    }

    public CarvingBit getBit() {
        return bit;
    }

    public CarvingMessage setCarvingMode(EnumAdzeMode carvingMode) {
        this.carvingMode = carvingMode;
        return this;
    }

    public EnumAdzeMode getCarvingMode() {
        return carvingMode;
    }

    public int getSide() {
        return side;
    }

    public CarvingMessage setSide(int side) {
        this.side = side;
        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        action = buf.readByte();
        flag = buf.readByte();
        carvingMode = EnumAdzeMode.valueOf(buf.readByte());
        side = buf.readByte();

        boolean isEmpty = buf.readBoolean();
        if (!isEmpty) {
            int bitX = buf.readByte();
            int bitY = buf.readByte();
            int bitZ = buf.readByte();
            bit = new CarvingBit(bitX, bitY, bitZ);
        } else {
            bit = CarvingBit.Empty;
        }

        int carvedDataLength = buf.readByte();
        if (carvedDataLength > 0) {
            carvedData = buf.readBytes(carvedDataLength).array();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeByte(action);
        buf.writeByte(flag);
        buf.writeByte(carvingMode.ordinal());
        buf.writeByte(side);

        buf.writeBoolean(bit.isEmpty());
        if (!bit.isEmpty()) {
            buf.writeByte(bit.bitX);
            buf.writeByte(bit.bitY);
            buf.writeByte(bit.bitZ);
        }

        if (carvedData != null) {
            buf.writeByte(carvedData.length);
            buf.writeBytes(carvedData);
        } else {
            buf.writeByte(0);
        }
    }

    public static class ClientHandler extends ClientHandlerBase<CarvingMessage> {
    }

    public static class ServerHandler extends ServerHandlerBase<CarvingMessage> {
    }

}
