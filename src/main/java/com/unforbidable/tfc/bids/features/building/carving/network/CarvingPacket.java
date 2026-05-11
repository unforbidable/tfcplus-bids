package com.unforbidable.tfc.bids.features.building.carving.network;

import com.unforbidable.tfc.bids.api._obsolete.Enums.EnumAdzeMode;
import com.unforbidable.tfc.bids.core.network.packet.Packet;
import com.unforbidable.tfc.bids.features.building.carving.main.CarvingBit;
import io.netty.buffer.ByteBuf;

public class CarvingPacket extends Packet {

    private int action;
    private int flag;
    private CarvingBit bit = CarvingBit.Empty;
    private EnumAdzeMode carvingMode = EnumAdzeMode.DEFAULT_MODE;
    private int side;
    private byte[] carvedData = null;

    public CarvingPacket() {
    }

    public CarvingPacket(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public CarvingPacket setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public int getFlag() {
        return flag;
    }

    public CarvingPacket setCarvedData(byte[] carvedData) {
        this.carvedData = carvedData;
        return this;
    }

    public byte[] getCarveData() {
        return carvedData;
    }

    public CarvingPacket setBit(CarvingBit bit) {
        this.bit = bit;
        return this;
    }

    public CarvingBit getBit() {
        return bit;
    }

    public CarvingPacket setCarvingMode(EnumAdzeMode carvingMode) {
        this.carvingMode = carvingMode;
        return this;
    }

    public EnumAdzeMode getCarvingMode() {
        return carvingMode;
    }

    public int getSide() {
        return side;
    }

    public CarvingPacket setSide(int side) {
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

}
