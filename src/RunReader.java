import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RunReader class.
 * @author vpratha
 * @version 2019
 */
public class RunReader {
    
    /**
     * run source
     */
    private RandomAccessFile source; 
    /**
     * the run
     */
    private Run run; 
    
    // Specific to the currently buffered records
    /**
     * store field
     */
    private ByteStore store; // Pre-allocated segment for buffered reading
    /**
     * count field
     */
    private int count;  // Number of records in the store
    /**
     * index field
     */
    private int index;  // Index of the latest record returned by getNext()
    /**
     * record field
     */
    private Record record;  // Currently fetched record
    /**
     * value field
     */
    private double value;   // Key-value of the current record
    
    // Records already read from the source
    /**
     * recordsRead field
     */
    private int recordsRead;
    
    /**
     * RunReader's constructor.
     * @param st the bytestore
     */
    public RunReader(ByteStore st) {
        store = st;
        record = new Record(st.getBuf());
    }
    
    /**
     * bind method
     * @src the file
     * @r the run
     */
    public void bind(RandomAccessFile src, Run r) {
        this.source = src;
        this.run = r;
        count = 0;
        index = -1;
        recordsRead = 0;
        getNext();  // Pre-fetch a record
    }
    
    /*
    * Reads bytes from the source and fills the record store.
    * Returns the number of records read.
    * Returns -1 if there is an error in reading.
    */
    /**
     * read method
     * @return # of records read or -1
     */
    public int read() {
        
        // Remaining records to be read in this run
        int recordsOutstanding = run.getCount() - recordsRead;
        
        // Determine the number of bytes to read; limit to the byte store size
        int bytesToRead = recordsOutstanding 
            * Externalsort.RECORD_SIZE;
        if (bytesToRead > store.getSize()) {
            bytesToRead = store.getSize();
        }
        
        // Reset buffered records
        count = 0;
        index = -1;
        
        // If the run records are not exhausted,
        //read the next batch of bytes
        if (bytesToRead > 0) {
            try {
                // Position the stream, and read
                source.seek(run.getOffset() + 
                    recordsRead * Externalsort.RECORD_SIZE);
                source.readFully(store.getBuf(), 
                    store.getOffset(), bytesToRead);
                count = bytesToRead / Externalsort.RECORD_SIZE;
            } 
            catch (EOFException ex) {
                return -1;
            } 
            catch (IOException ex) {
                return -1;
            }
        }
        
        return count;
    }

    /**
     * Serves the next record in the record store.
     * If there are no records to serve, then reads from the source and
     * replenishes the record store before serving a record.
     * @return the next record
     */
    public Record getNext() {
        
        // If no records to serve, 
        // read next block of records
        if (index >= count - 1) {
            
            // If there is error in reading, return null
            if (read() == -1) {
                return null;
            }
            
            // If no more records in the run, return null
            if (count == 0) {
                return null;
            }
            
            // Increment the records read from the source
            recordsRead += count;
        }
        
        // Move to the next record
        index++;
        
        // Bind to the current record
        record.moveTo(store.getOffset() 
            + index * Externalsort.RECORD_SIZE);
        value = record.getValue();
        
        // Return the current record
        return record;
    }
    
    /**
     * getCurr method
     * @return the curr record
     */
    public Record getCurr() {
        return record;
    }
    
    /**
     * getValue method
     * @return the value
     */
    public double getValue() {
        return value;
    }
}