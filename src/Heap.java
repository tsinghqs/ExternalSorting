/*
* Implements a min-heap of records
*/

public class Heap {
    
    byte[] buf;// Buffer to store array of records for min-heap
    int size;  // Number of records the heap can hold
    int last;  // Last index; index of the last record
    
    int backup_size; // Number of records backedup after "last" for next run
    
    private Run run; // Details of the current run
    private RunList run_list = new RunList();  // Details of all runs
    
    // Temporary records used in min-heap processing
    private Record top_rec;     // Represents the top_record of the min-heap
    private Record curr_rec;    // Used in bubbleUp() and bubbleDown()
    private Record parent_rec;  // Used in bubbleUp()
    private Record smaller_rec; // Used in bubbleDown()
    private Record right_rec;   // Used in bubbleDown()
    private Record last_rec;    // Used in push() and delete()
    
    public Heap(byte[] buf) {
        
        this.buf = buf;
        this.size = buf.length / Externalsort.RECORD_SIZE;
        this.backup_size = 0;
        
        // Create the temporary records
        top_rec = new Record(buf); // NOTE: top_rec's offset must be fixed at 0
        curr_rec = new Record(buf);
        parent_rec = new Record(buf);
        smaller_rec = new Record(buf);
        right_rec = new Record(buf);
        last_rec = new Record(buf);
        
        // Build the min-heap
        build();
        
        // Start a new run
        newRun();
    }
    
    /**
     * Changed to public for easier testing
     * method to get a new run
     */
    public void newRun() {
        run = new Run();
        run_list.add(run);
    }
    
    /**
     * method to get the runList
     * @return RunList the runlist generated from new run
     */
    public RunList getRunList() {
        return run_list;
    }
    
    /*
    * Builds in-place min-heap of all records
    */
    public void build() {
        
        // Auto-include the first record
        // And, bubble-up records from position one to last
        for (last = 1; last <= size - 1; last++) {
            bubbleUp();
        }
        
        last = size - 1;
    }
    
    /*
    * Returns the Record on the top of the heap (the root record)
    * Returns null if the heap is empty.
    */
    public Record top() {
        
        // If the heap is empty, ...
        if (last < 0) {
            
            // If there are no backup records; then return null
            if (backup_size == 0) {
                return null;
            }
            
            // Else, start a new run and build min-heap with backup records:
            newRun();
            
            // If backup_size is less than size, then move them to offset 0
            if (backup_size < size) {
                int backup_offset = (size - backup_size) * Externalsort.RECORD_SIZE;
                int backup_bytes = backup_size * Externalsort.RECORD_SIZE;
                for (int i = 0; i < backup_bytes; i++) {
                    buf[i] = buf[backup_offset + i];
                }
                size = backup_size;
            }

            // Re-build min-heap with the backup records
            build();

            // Reset backup_size
            backup_size = 0;
        }
        
        run.incrementCount();
        return top_rec;
    }
    
    /*
    * Bubbles up the LAST record to maintain min-heap structure.
    */
    public void bubbleUp() {
        
        // Just return if the heap is empty or has just one record
        if (last <= 0) {
            return;
        }
        
        int index = last;
        curr_rec.moveTo(index * Externalsort.RECORD_SIZE);
        double val = curr_rec.getValue();
        
        while (index != 0) {
            
            int parent_index = (index - 1) / 2;  // Parent index
            parent_rec.moveTo(parent_index * Externalsort.RECORD_SIZE);
            double parent_val = parent_rec.getValue();
            
            // System.out.println(index + " " + val + " " + parent_index + " " + parent_val);
            
            if (val < parent_val) {
                index = parent_index;
                curr_rec.swapContentWith(parent_rec);
                curr_rec.moveTo(parent_rec);
            } else {
                break;
            }
        }
    }
    
    /*
    * Bubbles down the TOP record to maintain min-heap structure.
    */
    public void bubbleDown() {
        
        // Just return if the heap is empty or has just one record
        if (last <= 0) {
            return;
        }
        
        int index = 0;
        curr_rec.moveTo(index * Externalsort.RECORD_SIZE);
        double val = curr_rec.getValue();
        
        while (true) {
            
            // Assume the left child is the smaller of the children
            int smaller_index = index * 2 + 1;  // Left child index
            if (smaller_index > last) {
                // No children; we are done
                break;
            }
            
            smaller_rec.moveTo(smaller_index * Externalsort.RECORD_SIZE);
            double smaller_val = smaller_rec.getValue();
            
            int right_index = index * 2 + 2;  // Right child index
            if (right_index > last) {
                // No right child; keep the left child as the smaller child
            } else {
                right_rec.moveTo(right_index * Externalsort.RECORD_SIZE);
                double right_value = right_rec.getValue();
                if (right_value < smaller_val) {
                    
                    // Mark the right child as the smaller child
                    smaller_rec.moveTo(right_rec);
                    smaller_val = right_value;
                    smaller_index = right_index;
                }
            }
            
            // Compare with smaller child
            if (val < smaller_val) {
                // Smaller than children; we are done
                break;
            }
            
            // Else, swap with the smaller child and continue
            index = smaller_index;
            curr_rec.swapContentWith(smaller_rec);
            curr_rec.moveTo(smaller_rec);
        }
    }
    
    /*
    * Pushes a new record onto the min-heap.
    * NOTE: Call this ONLY after the root record is consumed via top().
    *
    * STEPS:
    * 1. Get the top_value (the value of the top_record)
    * 2. Place the new record on the top (root position).
    * 3. If the new record' value is smaller than the top_value:
    *    (a) swap the top record with the LAST record, and
    *    (b) shrink the heap.
    * 4. Bubble Down.
    */
    public void push(Record r) {
        
        // Get the top_value and place the new record on the top
        double top_value = top_rec.getValue();
        r.copyContentTo(top_rec);
        
        // Compare with the top_value, that was last consumed
        if (r.getValue() <  top_value) {
            
            // Swap the top and LAST records, if more than one record exist
            if (last > 0) {
                last_rec.moveTo(last * Externalsort.RECORD_SIZE);
                top_rec.swapContentWith(last_rec);
            }
            
            // Shrink the heap
            last--;
            
            // Increase the backup_size
            backup_size++;
        }
        
        // Bubble down to maintain the min-heap structure
        bubbleDown();
    }
    
    /*
    * Deletes the top record, and restructures the min-heap.
    * NOTE: Call this ONLY after the root record is consumed via top().
    *
    * STEPS:
    * 1. Move the LAST record to the top.
    * 2. Decrease the heap length.
    * 3. Bubble Down.
    */
    public void delete() {
        
        // Move the LAST record to the top, if more than one record exist
        if (last > 0) {
            last_rec.moveTo(last * Externalsort.RECORD_SIZE);
            last_rec.copyContentTo(top_rec);
        }
        
        // Shrink the heap
        last--;
        
        // Bubble down to maintain the min-heap structure
        bubbleDown();
    }
}
