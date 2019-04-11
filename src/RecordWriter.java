import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RecordWriter {
    
    private byte[] buf; // Buffer to store output records
    private int size;   // Number of records that can be held in the store
    private Record record; // Record that points to the latest output record
    
    private int count;  // Number of output records buffered in the store
    private OutputStream stream; // Stream to write records to
    
    /*
    * Initializes the record store
    */
    public RecordWriter(byte[] buf) {
        this.buf = buf;
        size = buf.length / Externalsort.RECORD_SIZE;
        record = new Record(buf);
    }
    
    /*
    * Attaches this writer to the given output file destination.
    */
    public void open(String file) throws FileNotFoundException {
        stream = new BufferedOutputStream(new FileOutputStream(file));
        count = 0;
    }
    
    /*
    * Detaches this writer from the output file destination.
    * Flushes any buffered output records and closes the stream.
    */
    public void close() throws IOException {
        writeToStream();
        stream.close();
        stream = null;
    }
    
    /*
    * Appends the given record to the record store.
    * If the record store is already full, then the records in the record store
    * are written to the output stream and the record store is reset to make
    * room for the new record.
    *
    * Returns true if the record is successfully buffered, false otherwise.
    */
    public boolean write(Record r) {
        
        // If no output file is attached, return false.
        if (stream == null) {
            return false;
        }
        
        // If the record store is full, write them to the stream.
        if (count >= size ) {
            if (! writeToStream()) {
                return false; // Error in writing to stream
            }
        }
        
        record.moveTo(count * Externalsort.RECORD_SIZE);
        r.copyContentTo(record);
        
        count++;
        return true;
    }
    
    /*
    * Write bytes from buf to the output stream and resets the record store.
    * Returns true if successful.
    * Returns false if there is an error in writing.
    */
    private boolean writeToStream() {
        
        // Write bytes based on number of output records (count)
        try {
            stream.write(buf, 0, count * Externalsort.RECORD_SIZE);
        } catch (IOException ex) {
            return false;
        }
        
        // Reset buffered records
        count = 0;
        return true;
    }
}
