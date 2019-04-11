import java.util.ArrayList;

/*
* Maintains a list of run objects and automatically sets their offsets.
*/
@SuppressWarnings("serial")
public class RunList extends ArrayList<Run> {
    
    private long offset = 0;
    
    public boolean add(Run run) {
        
        // Update offset based on the count of records in the previous run
        if (! isEmpty()) {
            offset += (get(size() - 1).getCount() * Externalsort.RECORD_SIZE);
        }
        
        // Set the offset of the run and add it to the list
        run.setOffset(offset);
        super.add(run);
        
        return true;
    }
}

