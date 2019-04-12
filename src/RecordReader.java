import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class RecordReader {
    
    private InputStream stream; // Stream to read records from
    private byte[] buf; // Buffer to store input records
    private int count;  // Number of records in the buffer
    private int index;  // Index of the latest record returned by getNext()
    private Record record; // Record to be returned by getNext()
    
    public RecordReader(String file) throws FileNotFoundException {
        stream = new BufferedInputStream(new FileInputStream(file));
    }
    
    public void close() throws IOException {
        stream.close();
        stream = null;
    }
    
    /*
    * Sets the buffer to be used for input records
    */
    public void set(byte[] buf) {
        this.buf = buf;
        this.count = 0;
        this.index = -1;
        this.record = new Record(buf);
    }
    
    /*
    * Reads bytes from the input stream and fills the record store.
    * Returns the number of records read.
    * Returns -1 if there is an error in reading.
    * NOTE: For performance reasons, supply a BufferedInputStream
    */
    public int read() {
        
        if (stream == null) {
            return -1;
        }
        
        // Reset buffered records
        count = 0;
        index = -1;
        
        int total_read = 0;
        while (total_read < buf.length) {
            int to_be_read = buf.length - total_read;
            int curr_read;
            try {
                curr_read = stream.read(buf, total_read, to_be_read);
            } catch (IOException ex) {
                return -1;
            }
            if (curr_read == -1) {
                // EOF reached; stop reading
                break;
            }
            total_read += curr_read;
        }
        
        count = total_read / Externalsort.RECORD_SIZE;
        return count;
    }
    
    /*
    * Serves the next record in the record store.
    * If there are no records to serve, then reads from the stream and
    * replenishes the record store before serving a record.
    *
    * Returns null if there are no records to serve
    */
    boolean no_more_records = false;
    public Record getNext() {
        
        if (stream == null || no_more_records) {
            return null;
        }
        
        // If no records to serve, read next block of records
        if (index >= count - 1) {
            
            // If there is an error in reading, return null
            if (read() == -1) {
                return null;
            }
            
            // If no more records in the run, return null
            if (count == 0) {
                no_more_records = true;
                return null;
            }
        }
        
        // Move to the next record
        index++;
        
        // Return the current record
        record.moveTo(index * Externalsort.RECORD_SIZE);
        return record;
    }
}
