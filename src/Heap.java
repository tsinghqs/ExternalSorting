/**
 * Represents the min heap.
 * @author vpratha
 * @version 2019
 */
public class Heap {
    
    /**
     * Buffer to store array of records for min-heap
     */
    private byte[] buf;
    /**
     * Number of records the heap can hold
     */
    private int size;
    /**
     * index of the last record
     */
    private int last;
    
    /**
     * Number of records backed up 
     * after "last" for next run
     */
    private int backupSize;
    
    /**
     * the current run
     */
    private Run run;
    /**
     * all runs
     */
    private RunList runList = new RunList();  // Details of all runs
    
    // Temporary records used in min-heap processing
    /**
     * Represents the top_record of the min-heap
     */
    private Record topRec;  
    /**
     * Used in bubbleUp() and bubbleDown()
     */
    private Record currRec;   
    /**
     * Used in bubbleUp()
     */
    private Record parentRec;
    /**
     * Used in bubbleDown()
     */
    private Record smallerRec; 
    /**
     * Used in bubbleDown()
     */
    private Record rightRec; 
    /**
     * Used in push() and delete()
     */
    private Record lastRec;    
    
    /**
     * Constructor for heap
     * @param buf byte array given
     */
    public Heap(byte[] buf) {
        
        this.buf = buf;
        this.size = buf.length / Externalsort.RECORD_SIZE;
        this.backupSize = 0;
        
        // Create the temporary records
        topRec = new Record(buf); // NOTE: top_rec's offset must be fixed at 0
        currRec = new Record(buf);
        parentRec = new Record(buf);
        smallerRec = new Record(buf);
        rightRec = new Record(buf);
        lastRec = new Record(buf);
        
        // Build the min-heap
        build();
        
        // Start a new run
        newRun();
    }
    
    /**
     * New run method
     */
    public void newRun() {
        run = new Run();
        runList.add(run);
    }
    
    /**
     * method to create a runList
     * @return RunList the list of runs from adding new runs
     */
    public RunList getRunList() {
        return runList;
    }
    
    /**
     * Builds in-place min-heap of all records
     * made public for testing
     */
    public void build() {
        
        // Auto-include the first record
        // And, bubble-up records from position one to last
        for (last = 1; last <= size - 1; last++) {
            bubbleUp();
        }
        
        last = size - 1;
    }
    
    /**
     * Returns the Record on the top of the heap (the root record)
     * Returns null if the heap is empty.
     * @return the root record or null
     */
    public Record top() {
        
        // If the heap is empty, ...
        if (last < 0) {
            
            // If there are no backup records; then return null
            if (backupSize == 0) {
                return null;
            }
            
            // Else, start a new run and build min-heap with backup records:
            newRun();
            
            // If backup_size is less than size, then move them to offset 0
            if (backupSize < size) {
                int backupOffset = (size - backupSize) 
                    * Externalsort.RECORD_SIZE;
                int backupBytes = backupSize 
                    * Externalsort.RECORD_SIZE;
                for (int i = 0; i < backupBytes; i++) {
                    buf[i] = buf[backupOffset + i];
                }
                size = backupSize;
            }

            // Re-build min-heap with the backup records
            build();

            // Reset backup_size
            backupSize = 0;
        }
        
        run.incrementCount();
        return topRec;
    }
    
    /**
     * Bubbles up the last record to maintain min-heap structure.
     * made public for testing
     */
    public void bubbleUp() {
        
        // Just return if the heap is empty or has just one record
        if (last <= 0) {
            return;
        }
        
        int index = last;
        currRec.moveTo(index * Externalsort.RECORD_SIZE);
        double val = currRec.getValue();
        
        while (index != 0) {
            
            int parentIndex = (index - 1) / 2;  // Parent index
            parentRec.moveTo(parentIndex * Externalsort.RECORD_SIZE);
            double parentVal = parentRec.getValue();
            
            if (val < parentVal) {
                index = parentIndex;
                currRec.swapContentWith(parentRec);
                currRec.moveTo(parentRec);
            } 
            else {
                break;
            }
        }
    }
    
    /**
     * Bubbles down the top record to maintain min-heap structure.
     * Made public for testing purposes
     */
    public void bubbleDown() {
        
        // Just return if the heap is empty or has just one record
        if (last <= 0) {
            return;
        }
        
        int index = 0;
        currRec.moveTo(index * Externalsort.RECORD_SIZE);
        double val = currRec.getValue();
        
        while (true) {
            
            // Assume the left child is the smaller of the children
            int smallerIndex = index * 2 + 1;  // Left child index
            if (smallerIndex > last) {
                // No children; we are done
                break;
            }
            
            smallerRec.moveTo(smallerIndex * Externalsort.RECORD_SIZE);
            double smallerVal = smallerRec.getValue();
            
            int rightIndex = index * 2 + 2;  // Right child index
//            if (rightIndex > last) {
//                // No right child; keep the left child as the smaller child
//            } 
//            else {
//                rightRec.moveTo(rightIndex * Externalsort.RECORD_SIZE);
//                double rightValue = rightRec.getValue();
//                if (rightValue < smallerVal) {
//                    
//                    // Mark the right child as the smaller child
//                    smallerRec.moveTo(rightRec);
//                    smallerVal = rightValue;
//                    smallerIndex = rightIndex;
//                }
//            }
            if (rightIndex <= last) {
                rightRec.moveTo(rightIndex * Externalsort.RECORD_SIZE);
                double rightValue = rightRec.getValue();
                if (rightValue < smallerVal) {
                  
                    // Mark the right child as the smaller child
                    smallerRec.moveTo(rightRec);
                    smallerVal = rightValue;
                    smallerIndex = rightIndex;
                }
            }
            
            // Compare with smaller child
            if (val < smallerVal) {
                // Smaller than children; we are done
                break;
            }
            
            // Else, swap with the smaller child and continue
            index = smallerIndex;
            currRec.swapContentWith(smallerRec);
            currRec.moveTo(smallerRec);
        }
    }
    
    /**
     * Pushes a new record onto the min-heap.
     * Should be called only after 
     * the root record is consumed via top().
     *
     * STEPS:
     * 1. Get the top_value (the value of the top_record)
     * 2. Place the new record on the top (root position).
     * 3. If the new record' value is smaller than the top_value:
     *    (a) swap the top record with the LAST record, and
     *    (b) shrink the heap.
     * 4. Bubble Down.
     * @param r the record
     */
    public void push(Record r) {
        
        // Get the top_value and place the new record on the top
        double topVal = topRec.getValue();
        r.copyContentTo(topRec);
        
        // Compare with the top_value, that was last consumed
        if (r.getValue() <  topVal) {
            
            // Swap the top and LAST records, if more than one record exist
            if (last > 0) {
                lastRec.moveTo(last * Externalsort.RECORD_SIZE);
                topRec.swapContentWith(lastRec);
            }
            
            // Shrink the heap
            last--;
            
            // Increase the backup_size
            backupSize++;
        }
        
        // Bubble down to maintain the min-heap structure
        bubbleDown();
    }
    
    /**
     * Deletes the top record, and restructures the min-heap.
     * Should be called ONLY 
     * after the root record is consumed via top().
     *
     * STEPS:
     * 1. Move the LAST record to the top.
     * 2. Decrease the heap length.
     * 3. Bubble Down
     */
    public void delete() {
        
        // Move the LAST record to the top, if more than one record exist
        if (last > 0) {
            lastRec.moveTo(last * Externalsort.RECORD_SIZE);
            lastRec.copyContentTo(topRec);
        }
        
        // Shrink the heap
        last--;
        
        // Bubble down to maintain the min-heap structure
        bubbleDown();
    }
}
