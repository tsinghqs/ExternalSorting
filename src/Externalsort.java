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
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        // Used for processing; for heap and later for multi-way merge
        byte[] processing_buf = new byte[8 * BLOCK_SIZE];
        
        // Used for buffered reading of the input file and later the runfile(s)
        byte[] in_buf = new byte[BLOCK_SIZE];
        
        // Used for buffered writing of the runfile(s) and later the output file
        byte[] out_buf = new byte[BLOCK_SIZE];
        
        // Process command-line arguments; just one (the input file)
        String input_file = args[0];
        
        // Setup runfile names; two runfiles are sufficient
        String[] run_files = new String[] {
            input_file + ".run1",
            input_file + ".run2"
        };
        
        // Open inputfile
        RecordReader reader = new RecordReader(input_file);
        
        // Initialize the reader with processing_buf, and fill it with records
        reader.set(processing_buf);
        reader.read();
        
        // Re-initialize the reader; now with in_buf
        reader.set(in_buf);
        
        // Initialize a min-heap with the filled processing_buf
        Heap heap = new Heap(processing_buf);
        
        // Create the the writer with out_buf, and open the output runfile
        RecordWriter writer = new RecordWriter(out_buf);
        writer.open(run_files[0]);
        
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
            } else {
                // No more input records; delete the top record from the heap
                heap.delete();
            }
        }
        
        // Close the output run file
        writer.close();
        
        // Close the input file
        reader.close();
        
        // Obtain the list of runs created by min-heap sorting
        RunList run_list = heap.getRunList();
        
        //System.out.println("Record Count: " + count);
        //for (Run r : run_list) {
          //  System.out.println(r.getOffset() + ": " + r.getCount());
        //}
        
        // Setup in/out for the merge; run_files[0] is the input to start with
        String in_file = run_files[0];
        String out_file = run_files[1];
        
        // Perform multi-pass multi-way merge
        MultiwayMerge merge = new MultiwayMerge(processing_buf, out_buf);
        while (true) {
            
            // If this is the last pass:
            // (a) write the sorted output to inputfile itself, and
            // (b) turn on printing sample output records.
            if (run_list.size() <= 8) {
                out_file = input_file;
                merge.printSampleRecords(true);
            }
            
            // Perform one complete pass of merging the runs
            run_list = merge.perform(run_list, in_file, out_file);
            
            // If the merged output contains just one run, then we are done.
            if (run_list.size() == 1) {
                break;
            }
            
            // Continue to the next pass; swapping in_file and out_file names
            String tmp = in_file;
            in_file = out_file;
            out_file = tmp;
        }
    }
}