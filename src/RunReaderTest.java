import student.TestCase;
/**
 * Class to test HeapTest
 * @author tsingh
 * @version 2019
 */
public class RunReaderTest extends TestCase {

    /**
     * Test reference.
     */
    private ByteStore bs;
    private Run run;
    
    public void testRunReader()
    {
        byte[] buf = new byte[10];
        for(int i = 0; i < 9; i++)
        {
            buf[i] = (byte)i;
        }
        bs = new ByteStore(buf, 9, 8);
        RunReader read = new RunReader(bs);
        //int check = read.read();
        //assertEquals(check, 1);
        Record firstRec = new Record(buf);
        Record curr = read.getCurr();
        double val = read.getValue();
        assertEquals(0.0, val, 1.0);
        
        
    }
}