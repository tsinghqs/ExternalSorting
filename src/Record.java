import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Represents a Record.
 *
 * Bound to a pre-allocated byte array during construction. The bound array
 * cannot be changed.  Initially it points to the first record (at 0-offset) of
 * the array.  The record can be moved to any offset using moveTo(int). It can
 * also be moved to the same offset as another record using moveTo(Record).  
 *
 * The record's content is the segment of the byte array that starts at
 * the offset and has the length equal to the size of the record in bytes.
 * Record can copy its content to or swap content with another record.
 *
 * getValue() returns the key-value of the record.  
 * toString() returns the String
 * representation of both ID and key-value in the appropriate format.
 * 
 * @author vpratha
 * @version 4.11.2019
 */
public class Record {
    
    /**
     * Stores the bytes of the record.
     */
    private byte[] buf;
    /**
     * Stores the offset of the record.
     */
    private int offset;
    
    /**
     * Record's constructor; binds the given pre-allocated
     * byte array to this record.
     * @param buf the byte array to bind to
     */
    public Record(byte[] buffer) {
        buf = buffer;
    }
    
    /**
     * Moves the record to the specified offset.
     * @param offset the offset measured in bytes
     */
    public void moveTo(int off) {
        offset = off;
    }
    
    /**
     * Moves the record to the same offset as other.
     * @param other the other Record
     */
    public void moveTo(Record other) {
        this.offset = other.offset;
    }
    
    /**
     * Copies buffer content to other.
     * @param other the other Record
     */
    public void copyContentTo(Record other) {
        for  (int i = 0; i < Externalsort.RECORD_SIZE; i++) {
            other.buf[other.offset + i] = this.buf[this.offset + i];
        }
    }
    
    /**
     * Temporary Record object used for swapContentWith(Record other).
     */
    private static Record temp = new Record(new byte[Externalsort.RECORD_SIZE]);
    
    /**
     * Swaps buffer content between this and other.
     * @param other the other Record
     */
    public void swapContentWith(Record other) {
        // Perform a 3-way transfer among {this, other and temp}
        this.copyContentTo(temp);
        other.copyContentTo(this);
        temp.copyContentTo(other);
    }
    
    /**
     * Returns double value of this record's key.
     * @return the key, or NaN for errors
     */
    public double getValue() {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
            buf, offset + 8, 8));
        try {
            return dis.readDouble();
        } 
        catch (IOException ex) {
            return Double.NaN;
        }
    }
    
    /**
     * DecimalFormat used in toString().
     */
    private DecimalFormat formatter = 
        new DecimalFormat("0.################E00");
    
    /**
     * Returns a string containing the formatted id and key.
     * @return a string containing the formatted id and key
     */
    public String toString() {
        long id = -1;
        double value = Double.NaN;
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
            buf, offset, Externalsort.RECORD_SIZE));
        try {
            id = dis.readLong();
            value = dis.readDouble();
        } 
        catch (IOException ex) {
            /**
             * Empty on purpose
             */
        }
        
        // return String.format("%d %E", id, value);
        return String.format("%d %s", id, formatter.format(value));
    }
}
