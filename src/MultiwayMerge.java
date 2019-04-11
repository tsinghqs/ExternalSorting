import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MultiwayMerge {
    
    private RunReader[] readers;
    private RecordWriter writer;
    private int merge_degree;
    private boolean print_sample_records = false;
    
    public MultiwayMerge(byte[] processing_buf, byte[] out_buf) {
        writer = new RecordWriter(out_buf);
        merge_degree = processing_buf.length / Externalsort.BLOCK_SIZE;
        readers = new RunReader[this.merge_degree];
        for (int i = 0; i < readers.length; i++) {
            readers[i] = new RunReader(
                new ByteStore(processing_buf, i * Externalsort.BLOCK_SIZE, Externalsort.BLOCK_SIZE)
            );
        }
    }
    
    public void printSampleRecords(boolean val) {
        print_sample_records = val;
    }
    
    public RunList perform(RunList in_list, String in_file, String out_file)
        throws FileNotFoundException, IOException {
        
        // Initialize the merged output run_list; to be returned
        RunList out_list = new RunList();
        
        // Open the input runfile
        RandomAccessFile source = new RandomAccessFile(in_file, "r");
        
        // Open the merged output runfile
        writer.open(out_file);
        
        int merged_runs = 0;
        while (true) {
            
            // Start a new merge phase for input runs that need to be merged yet
            int phase_length = in_list.size() - merged_runs;
            if (phase_length == 0) {
                break; // No more input runs to merge; we are done
            }
            
            // Limit phase_length to the work area capacity
            if (phase_length > merge_degree) {
                phase_length = merge_degree;
            }
            
            // Set up run readers for this phase, and bind them to input runs
            RunReader[] phase_readers = new RunReader[phase_length];
            for (int i = 0; i < phase_readers.length; i++) {
                phase_readers[i] = readers[i];
                phase_readers[i].bind(source, in_list.get(merged_runs++));
            }
            
            // Merge the input runs of this phase
            Run merged_run = mergeRuns(phase_readers);
            
            // Add the merged run to output run_list
            out_list.add(merged_run);
        }
        
        // Close the merged output runfile
        writer.close();
        if (print_sample_records) {
            RecordPrinter.close();
        }
        
        // Close input runfile
        source.close();
        
        return out_list;
    }
    
    private Run mergeRuns(RunReader[] phase_readers) {
        
        // Create the output run
        Run out_run = new Run();
        
        // Index of the last run in this phase
        int last = phase_readers.length - 1;
        
        // While there are still runs to be merged in this phase
        while (last >= 0) {
            
            // Select the index of the run with minimum front record
            int min = 0;
            double min_value = phase_readers[0].getValue();
            for (int i = 1; i <= last; i++) {
                if (phase_readers[i].getValue() < min_value) {
                    min = i;
                    min_value = phase_readers[i].getValue();
                }
            }
            
            // Write the minimum record to the output, and increment run count
            writer.write(phase_readers[min].getCurr());
            int n_outrec = out_run.incrementCount();
            
            // Print sample records, if needed
            if (print_sample_records) {
                if (n_outrec % Externalsort.RECORDS_PER_BLOCK == 1) {
                    RecordPrinter.print(phase_readers[min].getCurr());
                }
            }
            
            // Advance to the next record in this selected run
            if (phase_readers[min].getNext() == null) {
                
                // No more records in this input run; remove it from this phase:
                // Deletion is done by replacing it with the LAST and shrinking.
                if (min != last) {
                    phase_readers[min] = phase_readers[last];
                }
                last--; // Shrink
            }
        }
        
        return out_run;
    }
}

