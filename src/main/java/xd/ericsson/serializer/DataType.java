package xd.ericsson.serializer;

enum DataType {
    NULL((byte) 0),
    STRING((byte) 1),
    INTEGER((byte) 2),
    BOOLEAN((byte) 3),
    LONG((byte) 4),
    FLOAT((byte) 5),
    DOUBLE((byte) 6),
    CHAR((byte) 7),
    BYTEARRAY((byte) 8),
    ARRAYLIST((byte) 9),
    PACKET((byte) 10);

    private final byte value;

    DataType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static DataType fromByte(byte value) {
        for (DataType type : DataType.values()) {
            if (type.getValue() == value)
                return type;
        }

        throw new IllegalArgumentException("Unsupported data type: " + value);
    }
}
