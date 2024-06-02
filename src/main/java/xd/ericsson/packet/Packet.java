package xd.ericsson.packet;

import xd.ericsson.serializer.Serializer;
import xd.ericsson.serializer.exception.BigPenisException;
import xd.ericsson.serializer.exception.UnsupportedObjectTypeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Packet {
    private final byte packetId;
    private final List<Object> packetData;

    public Packet(byte packetId, Object... args) {
        this.packetId = packetId;
        this.packetData = new ArrayList<>(Arrays.asList(args));
    }

    public byte getPacketId() {
        return this.packetId;
    }

    public List<Object> getPacketData() {
        return packetData;
    }

    public String getPacketName() {
        return this.getClass().getSimpleName();
    }

    public byte[] getBytes() {
        try {
            return Serializer.serialize(packetData);
        } catch (UnsupportedObjectTypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return this.getPacketName() + this.packetData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Packet other)) return false;
        return this.getPacketId() == other.getPacketId() &&
                this.getPacketName().equals(other.getPacketName()) &&
                this.getPacketData().equals(other.getPacketData());
    }


}
