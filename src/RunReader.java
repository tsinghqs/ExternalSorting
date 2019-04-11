import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RunReader {
    
    // Sprcific to the run
    private RandomAccessFile source; // Run source
    private Run run; // Run details (file offset and # of records)
    
    // Specific to the currently buffered records
    private ByteStore store; // Pre-allocated segment for buffered reading
    private int count;  // Number of records in the store
    private int index;  // Index of the latest record returned by getNext()
    private Record record;  // Currently fetched record
    private double value;   // Key-value of the current record
    
    // Records already read from the source
    private int records_read;
    
    /*
    * NOTE: ByteStore size must be a multiple of RECORD_SIZE.
    */
    public RunReader(ByteStore store) {
        this.store = store;
        this.record = new Record(store.getBuf());
    }
    
    /*
    * Associate the reader with the given record source and run.
    */
    public void bind(RandomAccessFile source, Run run) {
        this.source = source;
        this.run = run;
        count = 0;
        index = -1;
        records_read = 0;
        getNext();  // Pre-fetch a record
    }
    
    /*
    * Reads bytes from the source and fills the record store.
    * Returns the number of records read.
    * Returns -1 if there is an error in reading.
    */
    private int read() {
        
        // Remaining records to be read in this run
        int records_outstanding = run.getCount() - records_read;
        
        // Determine the number of bytes to read; limit to the byte store size
        int bytes_to_read = records_outstanding * Externalsort.RECORD_SIZE;
        if (bytes_to_read > store.getSize()) {
            bytes_to_read = store.getSize();
        }
        
        // Reset buffered records
        count = 0;
        index = -1;
        
        // If the run records are not exhausted, read the next batch of bytes
        if (bytes_to_read > 0) {
            try {
                // Position the stream, and read
                source.seek(run.getOffset() + records_read * Externalsort.RECORD_SIZE);
                source.readFully(store.getBuf(), store.getOffset(), bytes_to_read);
                count = bytes_to_read / Externalsort.RECORD_SIZE;
            } catch (EOFException ex) {
                return -1;
            } catch (IOException ex) {
                return -1;
            }
        }
        
        return count;
    }
    
    /*
    * Serves the next record in the record store.
    * If there are no records to serve, then reads from the source and
    * replenishes the record store before serving a record.
    *
    * Returns null if there are no records to serve
    */
    public Record getNext() {
        
        // If no records to serve, read next block of records
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
            records_read += count;
        }
        
        // Move to the next record
        index++;
        
        // Bind to the current record
        record.moveTo(store.getOffset() + index * Externalsort.RECORD_SIZE);
        value = record.getValue();
        
        // Return the current record
        return record;
    }
    
    public Record getCurr() {
        return record;
    }
    
    public double getValue() {
        return value;
    }
}