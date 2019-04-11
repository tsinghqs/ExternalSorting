import student.TestCase;
/**
 * Class to test RecordPrinter
 * @author tsingh
 * @version 2019
 */
public class RecordPrinterTest extends TestCase{
    byte[] check = new byte[20];
    RecordPrinter prints;
    /**
     * method to test a new run
     */
    public void testRecordPrinter()
    {
        prints = new RecordPrinter();
        for(int i = 0; i < 8; i++)
        {
            check[i] = (byte)i;
        }
        Record top_rec = new Record(check);
        prints.print(top_rec);
        prints.close();
        assertTrue(true);
    }
    
}
