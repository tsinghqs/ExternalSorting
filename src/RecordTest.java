import student.TestCase;
/**
 * Class to test HeapTest
 * @author tsingh
 * @version 2019
 */
public class RecordTest extends TestCase {
    
    /**
     * fields for testing
     */
    byte[] check = new byte[20];
    
    /**
     * method to check our record class
     */
    public void testRecord()
    {
        for(int i = 0; i < 8; i++)
        {
            check[i] = (byte)i;
        }
        Record top_rec = new Record(check);
        Record shift = new Record(check);
        top_rec.moveTo(shift);
        shift.copyContentTo(top_rec);
        top_rec.swapContentWith(shift);
        double val = shift.getValue();
        String tostring = top_rec.toString();
        assertEquals(val, 0.0, 1.0);
    }

}