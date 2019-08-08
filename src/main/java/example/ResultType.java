package example;

public enum ResultType {

    SUCCESS((byte) 0),
    FAIL((byte) 1);

    private byte value;

    private ResultType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
