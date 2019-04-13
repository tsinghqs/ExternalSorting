/**
 * Run class.
 * @author vpratha
 * @version 2019
 *
 */
public class Run {
    
    /**
     * offset field
     */
    private long offset; // Offset to this run, in runfile
    /**
     * count field
     */
    private int count; // Number of records in the run
    
    /**
     * Run's constructor
     */
    public Run() {
        offset = 0;
        count = 0;
    }
    
    /**
     * Inc count
     * @return count
     */
    public int incrementCount() {
        return ++count;
    }

    /**
     * sets offset
     * @param offset the offset
     */
    public void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * gets offset
     * @return offset
     */
    public long getOffset() {
        return offset;
    }

    /**
     * gets count
     * @return count
     */
    public int getCount() {
        return count;
    }
}