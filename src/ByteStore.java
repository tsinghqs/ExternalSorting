/**
 * Represents a segment of a pre-allocated byte array.
 * @author vpratha
 * @version 4.11.2019
 */
public class ByteStore {
    
    /**
     * Stores the bytes of the buffer.
     */
    private byte[] buf;
    /**
     * Stores the offset amount.
     */
    private int offset;
    /**
     * Stores the size.
     */
    private int size;

    /**
     * ByteStore's constructor.
     * @param buf The bytes of the buffer
     * @param offset The offset amount
     * @param size The size
     */
    public ByteStore(byte[] buf, int offset, int size) {
        this.buf = buf;
        this.offset = offset;
        this.size = size;
    }

    /**
     * Returns the byte array.
     * @return buf
     */
    public byte[] getBuf() {
        return buf;
    }

    /**
     * Returns the offset amount.
     * @return offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Returns the size.
     * @return size
     */
    public int getSize() {
        return size;
    }
}