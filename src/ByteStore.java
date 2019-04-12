/*
* Represents a segment of a pre-allocated byte array.
*/

public class ByteStore {
    
    private byte[] buf;
    private int offset;
    private int size;

    public ByteStore(byte[] buf, int offset, int size) {
        this.buf = buf;
        this.offset = offset;
        this.size = size;
    }

    public byte[] getBuf() {
        return buf;
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }
}