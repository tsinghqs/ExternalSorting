import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The project's driver.
 * @author vpratha
 * @version 4.11.2019
 */
public class Externalsort {
    
    /**
     * Constant: bytes per record
     */
    public static final int RECORD_SIZE = 16;
    /**
     * Constant: bytes per block
     */
    public static final int BLOCK_SIZE = 8192;
    /**
     * Constant: records per block
     */
    public static final int RECORDS_PER_BLOCK = BLOCK_SIZE / RECORD_SIZE;

    /**
     * Project's main method; processes I/O.
     * @param args the input file name
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) 
        throws FileNotFoundException, IOException {
        
        // Used for processing; for heap and later for multi-way merge
        byte[] processingBuf = new byte[8 * BLOCK_SIZE];
        
        // Used for buffered reading of the input file and later the runfile(s)
        byte[] inBuf = new byte[BLOCK_SIZE];
        
        // Used for buffered writing of the runfile(s) and later the output file
        byte[] outBuf = new byte[BLOCK_SIZE];
        
        // Process command-line arguments; just one (the input file)
        String inFile = args[0];
        
        // Setup runfile names; two runfiles are sufficient
        String[] runFiles = new String[] {
            inFile + ".run1",
            inFile + ".run2"
        };
        
        // Open inputfile
        RecordReader reader = new RecordReader(inFile);
        
        // Initialize the reader with processing_buf, and fill it with records
        reader.set(processingBuf);
        reader.read();
        
        // Re-initialize the reader; now with in_buf
        reader.set(inBuf);
        
        // Initialize a min-heap with the filled processing_buf
        Heap heap = new Heap(processingBuf);
        
        // Create the the writer with out_buf, and open the output runfile
        RecordWriter writer = new RecordWriter(outBuf);
        writer.open(runFiles[0]);
        
        while (true) {
            
            // Get the top record from min-heap; break if the heap is empty
            Record rec = heap.top();
            if (rec == null) {
                break;
            }
            
            // Write the top record to the runfile
            writer.write(rec);
            
            // Read the next input record
            rec = reader.getNext();
            
            if (rec != null) {
                // Push the input record to the heap
                heap.push(rec);
                //count++;
            } 
            else {
                // No more input records; delete the top record from the heap
                heap.delete();
            }
        }
        
        // Close the output run file
        writer.close();
        
        // Close the input file
        reader.close();
        
        // Obtain the list of runs created by min-heap sorting
        RunList runList = heap.getRunList();
        
        //System.out.println("Record Count: " + count);
        //for (Run r : run_list) {
          //  System.out.println(r.getOffset() + ": " + r.getCount());
        //}
        
        // Setup in/out for the merge; run_files[0] is the input to start with
        String inFileStr = runFiles[0];
        String outFileStr = runFiles[1];
        
        // Perform multi-pass multi-way merge
        MultiwayMerge merge = new MultiwayMerge(processingBuf, outBuf);
        while (true) {
            
            // If this is the last pass:
            // (a) write the sorted output to inputfile itself, and
            // (b) turn on printing sample output records.
            if (runList.size() <= 8) {
                outFileStr = inFile;
                merge.printSampleRecords(true);
            }
            
            // Perform one complete pass of merging the runs
            runList = merge.perform(runList, inFileStr, outFileStr);
            
            // If the merged output contains just one run, then we are done.
            if (runList.size() == 1) {
                break;
            }
            
            // Continue to the next pass; swapping in_file and out_file names
            String tmp = inFileStr;
            inFileStr = outFileStr;
            outFileStr = tmp;
        }
    }
}