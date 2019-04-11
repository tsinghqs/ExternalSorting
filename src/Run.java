public class Run {
    
    private long offset; // Offset to this run, in runfile
    private int count; // Number of records in the run
    
    public Run() {
        offset = 0;
        count = 0;
    }
    
    public int incrementCount() {
        return ++count;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getOffset() {
        return offset;
    }

    public int getCount() {
        return count;
    }
}