
public class Record 
{
    private long rID;
    private double rKey;
    
    public Record(long id, double key)
    {
        rID = id;
        rKey = key;
    }
    
    public long getID()
    {
        return rID;
    }
    
    public double getKey()
    {
        return rKey;
    }

    public double compareTo(Record other) 
    {
        return rKey - other.getKey();
    }
    
}
