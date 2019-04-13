import student.TestCase;
/**
 * Class to test HeapTest
 * @author tsingh
 * @version 2019
 */
public class RecordTest extends TestCase {
    

    
    /**
     * method to check our record class
     */
    public void testRecord()
    {
        byte[] check = new byte[20];
        for (int i = 0; i < 8; i++)
        {
            check[i] = (byte)i;
        }
        Record topRec = new Record(check);
        Record shift = new Record(check);
        topRec.moveTo(shift);
        shift.copyContentTo(topRec);
        topRec.swapContentWith(shift);
        double val = shift.getValue();
        topRec.toString();
        assertEquals(val, 0.0, 1.0);
    }

}