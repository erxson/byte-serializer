package xd.ericsson.serializer;

import org.junit.Test;
import xd.ericsson.packet.Packet;
import xd.ericsson.serializer.exception.BigPenisException;
import xd.ericsson.serializer.exception.UnsupportedObjectTypeException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SerializerTest {

    private final Deserializer deserializer = new Deserializer();

    @Test
    public void serializeObject() {
        String input = "bibibo";

        try {
            byte[] serialized = Serializer.serializeObject(input);

            Object deserialized = deserializer.deserializeObject(serialized);

            assertEquals("not equal", input, deserialized);
        } catch (UnsupportedObjectTypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void serializeMultiple() {
        List<Object> input = new ArrayList<>();

        input.add(null);
        input.add("test");
        input.add(1);
        input.add(true);
        input.add(1L);
        input.add(1F);
        input.add(1.0);
        input.add('a');
        input.add(new byte[]{1});

        try {
            byte[] serialized = Serializer.serialize(input);

            List<Object> deserialized = deserializer.deserialize(serialized);

            assertEquals("Null", input.get(0), deserialized.get(0));
            assertEquals("String", input.get(1), deserialized.get(1));
            assertEquals("Integer", input.get(2), deserialized.get(2));
            assertEquals("Boolean", input.get(3), deserialized.get(3));
            assertEquals("Long", input.get(4), deserialized.get(4));
            assertEquals("Float", input.get(5), deserialized.get(5));
            assertEquals("Double", input.get(6), deserialized.get(6));
            assertEquals("Char", input.get(7), deserialized.get(7));
            assertArrayEquals("Byte[]", (byte[]) input.get(8), (byte[]) deserialized.get(8));
        } catch (UnsupportedObjectTypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void arraySerialize() {
        List<String> array = new ArrayList<>();
        array.add("asd");
        array.add("bobo");

        List<Object> input = List.of(array);

        try {
            byte[] serialized = Serializer.serialize(input);

            List<Object> deserialized = deserializer.deserialize(serialized);

            assertEquals("array", array, deserialized.get(0));
        } catch (UnsupportedObjectTypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void packetSerialize() {
        Packet input = new Packet((byte) 0x1, "qwe", 1);

        try {
            byte[] serialized = Serializer.serialize(input);

            List<Object> deserialized = deserializer.deserialize(serialized);

            assertEquals("packet data", input, deserialized.get(0));
        } catch (UnsupportedObjectTypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void customPacketSerialize() {
        CustomPacket input = new CustomPacket("Cucumber", 1);

        try {
            byte[] serialized = Serializer.serialize(input);

            Deserializer customDeserializer = new Deserializer() {
                @Override
                public Packet getPacketById(byte packetId, List<Object> args) {
                    return packetId == (byte) 0x2
                            ? new CustomPacket(args.get(0).toString(), (int) args.get(1))
                            : null;
                }
            };

            List<Object> deserialized = customDeserializer.deserialize(serialized);

            assertTrue("instanceof", deserialized.get(0) instanceof CustomPacket);
        } catch (UnsupportedObjectTypeException e) {
            throw new RuntimeException(e);
        }
    }
}