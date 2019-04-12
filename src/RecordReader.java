import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents the process of reading
 * records.
 * @author vpratha
 * @version 2019
 */
public class RecordReader {
    
    /**
     * Stream to read records from
     */
    private InputStream stream;
    /**
     * Buffer to store input records
     */
    private byte[] buf; 
    /**
     * Number of records in the buffer
     */
    private int count;  
    /**
     * Index of the latest record returned by getNext()
     */
    private int index; 
    /**
     * Record to be returned by getNext()
     */
    private Record record;
    
    /**
     * RecordReader's constructor.
     * @param file the filename
     * @throws FileNotFoundException
     */
    public RecordReader(String file) throws FileNotFoundException {
        stream = new BufferedInputStream(new FileInputStream(file));
    }
    
    /**
     * Closes stream
     * @throws IOException
     */
    public void close() throws IOException {
        stream.close();
        stream = null;
    }
    
    /**
     * Sets the buffer to be used for input records. 
     * @param buf the buffer
     */
    public void set(byte[] buffer) {
        buf = buffer;
        count = 0;
        index = -1;
        record = new Record(buf);
    }
    
    /**
     * Reads bytes from the input stream and fills the record store.
     * Returns the number of records read.
     * Returns -1 if there is an error in reading.
     * @return num of records read
     */
    public int read() {
        
        if (stream == null) {
            return -1;
        }
        
        // Reset buffered records
        count = 0;
        index = -1;
        
        int totalRead = 0;
        while (totalRead < buf.length) {
            int toBeRead = buf.length - totalRead;
            int currRead;
            try {
                currRead = stream.read(buf, totalRead, toBeRead);
            } 
            catch (IOException ex) {
                return -1;
            }
            if (currRead == -1) {
                // EOF reached; stop reading
                break;
            }
            totalRead += currRead;
        }
        
        count = totalRead / Externalsort.RECORD_SIZE;
        return count;
    }
    
    /**
     * noMoreRecords field
     */
    private boolean noMoreRecords = false;
    
    /**
     * Serves the next record.
     * If there are no records, then reads from the stream and
     * replenishes the record store first.
     * @return the next record or null if there
     *          are no records
     */
    public Record getNext() {
        
        if (stream == null || noMoreRecords) {
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
                noMoreRecords = true;
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
