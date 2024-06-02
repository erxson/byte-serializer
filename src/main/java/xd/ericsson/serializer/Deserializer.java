package xd.ericsson.serializer;

import xd.ericsson.packet.Packet;
import xd.ericsson.serializer.exception.UnsupportedObjectTypeException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Deserializer {

    public List<Object> deserialize(byte[] bytes) throws UnsupportedObjectTypeException {
        List<Object> objects = new ArrayList<>();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        while (inputStream.available() > 0) {
            int objectLength = bytesToInt(readBytes(inputStream, 4));
            byte[] objectBytes = readBytes(inputStream, objectLength);
            Object obj = deserializeObject(objectBytes);
            objects.add(obj);
        }

        return objects;
    }

    private byte[] readBytes(ByteArrayInputStream inputStream, int length) {
        byte[] buffer = new byte[length];
        int bytesRead;
        try {
            bytesRead = inputStream.read(buffer);
        } catch (IOException e) {
            bytesRead = -1;
        }
        if (bytesRead != length) {
            throw new IllegalStateException("Unexpected end of input stream");
        }
        return buffer;
    }

    private int bytesToInt(byte[] bytes) {
        if (bytes.length != 4)
            throw new IllegalArgumentException();

        return ((bytes[3] & 0xFF) << 24) + ((bytes[2] & 0xFF) << 16) + ((bytes[1] & 0xFF) << 8) + (bytes[0] & 0xFF);
    }

    public Object deserializeObject(byte[] data) throws UnsupportedObjectTypeException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        DataInputStream dataStream = new DataInputStream(byteStream);

        try {
            byte dataType = dataStream.readByte();
            DataType type = DataType.fromByte(dataType);

            switch (type) {
                case NULL:
                    return null;
                case STRING:
                    int strLength = dataStream.readInt();
                    byte[] strBytes = new byte[strLength];
                    dataStream.readFully(strBytes);
                    return new String(strBytes);
                case INTEGER:
                    return dataStream.readInt();
                case BOOLEAN:
                    return dataStream.readBoolean();
                case LONG:
                    return dataStream.readLong();
                case FLOAT:
                    return dataStream.readFloat();
                case DOUBLE:
                    return dataStream.readDouble();
                case CHAR:
                    return dataStream.readChar();
                case BYTEARRAY:
                    int byteArrayLength = dataStream.readInt();
                    byte[] byteArrayBytes = new byte[byteArrayLength];
                    dataStream.readFully(byteArrayBytes);

                    return byteArrayBytes;
                case ARRAYLIST:
                    int arrayLength = dataStream.readInt();
                    byte[] arrayBytes = new byte[arrayLength];
                    dataStream.readFully(arrayBytes);

                    List<Object> deserialized = deserialize(arrayBytes);

                    return new ArrayList<>(deserialized);
                case PACKET:
                    byte packetId = dataStream.readByte();       // Packet ID
                    int packetLength = dataStream.readInt();     // Packet data length
                    byte[] packetBytes = new byte[packetLength];
                    dataStream.readFully(packetBytes);           // Packet data

                    List<Object> args = deserialize(packetBytes);

                    Packet packet = getPacketById(packetId, args);
                    if (packet == null)
                        return new Packet(packetId, args.toArray(new Object[0]));

                    return packet;
                default:
                    throw new UnsupportedObjectTypeException("Unsupported object type: " + type);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Packet getPacketById(byte packetId, List<Object> args) {
        return null;
    }
}
