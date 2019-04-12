import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/*
* Represents a Record.
*
* Bound to a pre-allocated byte array during construction.  The bound array
* cannot be changed.  Initially it points to the first record (at 0-offset) of
* the array.  The record can be moved to any offset using moveTo(int). It can
* also be moved to the same offset as another record using moveTo(Record).  
*
* The record's content is the segment of the byte array that starts at
* the offset and has the length equal to the size of the record in bytes.
* Record can copy its content to or swap content with another record.
*
* getValue() returns the key-value of the record.  toString() returns the String
* representation of both ID and key-value in the appropriate format.  getValue()
* and toString() are computationally intensive; use them with care.
*/

public class Record {
    
    private byte[] buf;
    private int offset;
    
    /*
    * A record is permanently bound to a byte array; supply it in constructor.
    * buf - the byte array to bind to. The byte array must be pre-allocated.
    */
    public Record(byte[] buf) {
        this.buf = buf;
    }
    
    /*
    * Moves the record to the specified offset (measured in bytes)
    */
    public void moveTo(int offset) {
        this.offset = offset;
    }
    
    /*
    * Moves the record to the same offset as the other
    */
    public void moveTo(Record other) {
        this.offset = other.offset;
    }
    
    /*
    * Copies the content to the another record
    */
    public void copyContentTo(Record other) {
        for  (int i = 0; i < Externalsort.RECORD_SIZE; i++) {
            other.buf[other.offset + i] = this.buf[this.offset + i];
        }
    }
    
    /*
    * Swaps the content with another Record
    */
    private static Record temp = new Record(new byte[Externalsort.RECORD_SIZE]);
    public void swapContentWith(Record other) {
        // Perform a 3-way transfer among {this, other and temp}
        this.copyContentTo(temp);
        other.copyContentTo(this);
        temp.copyContentTo(other);
    }
    
    /*
    * Returns double value of the key
    * Returns NaN, for any error.
    */
    public double getValue() {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
            buf, offset + 8, 8));
        try {
            return dis.readDouble();
        } catch (IOException ex) {
            return Double.NaN;
        }
    }
    
    /*
    * Returns a stirng containing the formatted id and value
    */
    private DecimalFormat formatter = new DecimalFormat("0.################E00");
    public String toString() {
        long id = -1;
        double value = Double.NaN;
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
            buf, offset, Externalsort.RECORD_SIZE));
        try {
            id = dis.readLong();
            value = dis.readDouble();
        } catch (IOException ex) {
        }
        
        // return String.format("%d %E", id, value);
        return String.format("%d %s", id, formatter.format(value));
    }
}
