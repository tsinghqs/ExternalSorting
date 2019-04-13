import student.TestCase;
/**
 * Class to test HeapTest
 * @author tsingh
 * @version 2019
 */
public class RunReaderTest extends TestCase {

    /**
     * tests runreader
     */
    public void testRunReader()
    {
        ByteStore bs;
        byte[] buf = new byte[10];
        for (int i = 0; i < 9; i++)
        {
            buf[i] = (byte)i;
        }
        bs = new ByteStore(buf, 9, 8);
        RunReader read = new RunReader(bs);
        //int check = read.read();
        //assertEquals(check, 1);
        read.getCurr();
        double val = read.getValue();
        assertEquals(0.0, val, 1.0);
        
        
    }
}