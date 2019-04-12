import student.TestCase;
/**
 * Class to test RecordPrinter
 * @author tsingh
 * @version 2019
 */
public class RecordPrinterTest extends TestCase {
    /**
     * check field
     */
    private byte[] check = new byte[20];
    /**
     * prints field
     */
    private RecordPrinter prints;
    
    /**
     * method to test a new run
     */
    public void testRecordPrinter()
    {
        prints = new RecordPrinter();
        for (int i = 0; i < 8; i++)
        {
            check[i] = (byte)i;
        }
        Record topRec = new Record(check);
        prints.print(topRec);
        prints.close();
        assertNotSame(topRec, check);
    }
    
}
