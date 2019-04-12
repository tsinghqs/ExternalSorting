import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Represents the multi-merge process.
 * @author vpratha
 * @version 4.11.2019
 */
public class MultiwayMerge {
    
    /**
     * readers field
     */
    private RunReader[] readers;
    /**
     * writer field
     */
    private RecordWriter writer;
    /**
     * mergeDegree field
     */
    private int mergeDegree;
    /**
     * printRecords field
     */
    private boolean printRecords = false;
    
    /**
     * MultiwayMerge's constructor.
     * @param processingBuf the processing buffer
     * @param outBuf the output buffer
     */
    public MultiwayMerge(byte[] processingBuf, byte[] outBuf) {
        writer = new RecordWriter(outBuf);
        mergeDegree = processingBuf.length / Externalsort.BLOCK_SIZE;
        readers = new RunReader[this.mergeDegree];
        for (int i = 0; i < readers.length; i++) {
            readers[i] = new RunReader(
                new ByteStore(processingBuf, 
                    i * Externalsort.BLOCK_SIZE, 
                    Externalsort.BLOCK_SIZE)
            );
        }
    }
    
    /**
     * Sets the printRecords field
     * @param val the value to set
     */
    public void printSampleRecords(boolean val) {
        printRecords = val;
    }
    
    /**
     * Performs the multi-merge.
     * @param inList the input list
     * @param inFile the input file
     * @param outFile the output file
     * @return the edited run list
     * @throws FileNotFoundException
     * @throws IOException
     */
    public RunList perform(RunList inList, String inFile, String outFile)
        throws FileNotFoundException, IOException {
        
        // Initialize the merged output run_list; to be returned
        RunList outList = new RunList();
        
        // Open the input runfile
        RandomAccessFile source = new RandomAccessFile(inFile, "r");
        
        // Open the merged output runfile
        writer.open(outFile);
        
        int mergedRuns = 0;
        while (true) {
            
            // Start a new merge phase for input runs that need to be merged yet
            int phaseLength = inList.size() - mergedRuns;
            if (phaseLength == 0) {
                break; // No more input runs to merge; we are done
            }
            
            // Limit phase_length to the work area capacity
            if (phaseLength > mergeDegree) {
                phaseLength = mergeDegree;
            }
            
            // Set up run readers for this phase, and bind them to input runs
            RunReader[] phaseReaders = new RunReader[phaseLength];
            for (int i = 0; i < phaseReaders.length; i++) {
                phaseReaders[i] = readers[i];
                phaseReaders[i].bind(source, inList.get(mergedRuns++));
            }
            
            // Merge the input runs of this phase
            Run mergedRun = mergeRuns(phaseReaders);
            
            // Add the merged run to output run_list
            outList.add(mergedRun);
        }
        
        // Close the merged output runfile
        writer.close();
        if (printRecords) {
            RecordPrinter.close();
        }
        
        // Close input runfile
        source.close();
        
        return outList;
    }
    
    /**
     * Merges runs.
     * @param phaseReaders the run reader
     * @return the merged run
     */
    private Run mergeRuns(RunReader[] phaseReaders) {
        
        // Create the output run
        Run outRun = new Run();
        
        // Index of the last run in this phase
        int last = phaseReaders.length - 1;
        
        // While there are still runs to be merged in this phase
        while (last >= 0) {
            
            // Select the index of the run with minimum front record
            int min = 0;
            double minVal = phaseReaders[0].getValue();
            for (int i = 1; i <= last; i++) {
                if (phaseReaders[i].getValue() < minVal) {
                    min = i;
                    minVal = phaseReaders[i].getValue();
                }
            }
            
            // Write the minimum record to the output, and increment run count
            writer.write(phaseReaders[min].getCurr());
            int nOutRec = outRun.incrementCount();
            
            // Print sample records, if needed
            if (printRecords) {
                if (nOutRec % Externalsort.RECORDS_PER_BLOCK == 1) {
                    RecordPrinter.print(phaseReaders[min].getCurr());
                }
            }
            
            // Advance to the next record in this selected run
            if (phaseReaders[min].getNext() == null) {
                
                // No more records in this input run; remove it from this phase:
                // Deletion is done by replacing it with the LAST and shrinking.
                if (min != last) {
                    phaseReaders[min] = phaseReaders[last];
                }
                last--; // Shrink
            }
        }
        
        return outRun;
    }
}
