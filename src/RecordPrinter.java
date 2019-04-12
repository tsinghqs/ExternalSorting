
/**
 * Represents record printing process.
 * @author vpratha
 * @version 4.11.2019
 */
public class RecordPrinter {
    
    /**
     * count field
     */
    private static int count = 0; // count of records written on current line
    
    /**
     * Prints given record to stdout;
     * ensures a max of 5 records are printed
     * on each line
     * @param rec the record
     */
    public static void print(Record rec) {
        
        // If 5 records are written on current line, go to next line and reset
        if (count == 5) {
            System.out.println();
            count = 0;
        }
        
        // If this is not the first record on this line, print record separator
        if (count > 0) {
            System.out.print(" ");
        }
        
        // Print the given record
        System.out.print(rec);
        
        count++;
    }
    
    /**
     * Completes current line of print
     * if it is in progress.
     */
    public static void close() {
        if (count != 0) {
            System.out.println();
            count = 0;
        }
    }
}