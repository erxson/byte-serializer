package xd.ericsson.serializer;

import xd.ericsson.packet.Packet;
import xd.ericsson.serializer.exception.BigPenisException;
import xd.ericsson.serializer.exception.UnsupportedObjectTypeException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Serializer {

    public static byte[] serialize(List<?> objects) throws UnsupportedObjectTypeException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            for (Object object : objects) {
                byte[] objectBytes = serializeObject(object);
                int objectLength = objectBytes.length;
                outputStream.write(intToBytes(objectLength));
                outputStream.write(objectBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputStream.toByteArray();
    }

    public static byte[] serialize(Packet packet) throws UnsupportedObjectTypeException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] objectBytes = serializeObject(packet);
            int objectLength = objectBytes.length;
            outputStream.write(intToBytes(objectLength));
            outputStream.write(objectBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputStream.toByteArray();
    }

    private static byte[] intToBytes(int value) {
        byte[] bytes = new byte[4];

        bytes[0] = (byte) value;
        bytes[1] = (byte) (value >> 8);
        bytes[2] = (byte) (value >> 16);
        bytes[3] = (byte) (value >> 24);

        return bytes;
    }

    public static byte[] serializeObject(Object obj) throws UnsupportedObjectTypeException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            switch (obj) {
                case null -> {
                    dataStream.writeByte(DataType.NULL.getValue());
                }
                case String s -> {
                    dataStream.writeByte(DataType.STRING.getValue());
                    byte[] strBytes = s.getBytes();
                    dataStream.writeInt(strBytes.length);
                    dataStream.write(strBytes);
                }
                case Integer i -> {
                    dataStream.writeByte(DataType.INTEGER.getValue());
                    dataStream.writeInt(i);
                }
                case Boolean b -> {
                    dataStream.writeByte(DataType.BOOLEAN.getValue());
                    dataStream.writeBoolean(b);
                }
                case Long l -> {
                    dataStream.writeByte(DataType.LONG.getValue());
                    dataStream.writeLong(l);
                }
                case Float f -> {
                    dataStream.writeByte(DataType.FLOAT.getValue());
                    dataStream.writeFloat(f);
                }
                case Double d -> {
                    dataStream.writeByte(DataType.DOUBLE.getValue());
                    dataStream.writeDouble(d);
                }
                case Character c -> {
                    dataStream.writeByte(DataType.CHAR.getValue());
                    dataStream.writeChar(c);
                }
                case byte[] b -> {
                    dataStream.writeByte(DataType.BYTEARRAY.getValue());
                    dataStream.writeInt(b.length);
                    for (byte b1 : b) {
                        dataStream.writeByte(b1);
                    }
                }
                case ArrayList<?> a -> {
                    dataStream.writeByte(DataType.ARRAYLIST.getValue());
                    byte[] itemsBytes = serialize(a);
                    dataStream.writeInt(itemsBytes.length);
                    dataStream.write(itemsBytes);
                }
                case Packet p -> {
                    dataStream.writeByte(DataType.PACKET.getValue()); // DataType
                    List<Object> packetData = p.getPacketData();
                    byte[] packetDataBytes = serialize(packetData);
                    dataStream.writeByte(p.getPacketId());            // Packet ID
                    dataStream.writeInt(packetDataBytes.length);      // Packet data length
                    dataStream.write(packetDataBytes);                // Packet data
                }
                default -> throw new UnsupportedObjectTypeException("Unsupported object type: " + obj.getClass().getSimpleName());
            }

            dataStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteStream.toByteArray();
    }

}