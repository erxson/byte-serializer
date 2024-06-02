package xd.ericsson.serializer;

import xd.ericsson.packet.Packet;

public class CustomPacket extends Packet {
    private final String penisName;
    private final int penisSize;

    public CustomPacket(String penisName, int penisSize) {
        super((byte) 0x2, penisName, penisSize);

        this.penisName = penisName;
        this.penisSize = penisSize;
    }

    public String getPenisName() {
        return this.penisName;
    }

    public int getPenisSize() {
        return this.penisSize;
    }
}
