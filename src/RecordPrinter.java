/*
* Prints Records to standard output; maximum of 5 recods on each line.
*/
public class RecordPrinter {
    
    private static int count = 0; // count of records written on current line
    
    /*
    * Prints the given record to the standard output.
    * Ensures that a maximum of 5 records are printed on each line.
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
    
    /*
    * Completes the the current line of print, if it is in progress.
    * Completing the line involves emitting a newline and resetting the counter.
    */
    public static void close() {
        if (count != 0) {
            System.out.println();
            count = 0;
        }
    }
}