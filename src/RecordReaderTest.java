import java.io.FileNotFoundException;
import java.io.IOException;
import student.TestCase;
/**
 * Class to test RecordPrinter
 * @author tsingh
 * @version 2019
 */
public class RecordReaderTest extends TestCase {
    
    
    /**
     * Method to test read and set methods
     * @throws IOException for filenotfound
     */
    public void testReadFile() 
    {
        RecordReader read = null;
        String filename = "/sampleinput.bin";
        String in = filename;
        assertNotNull(in);
        try {
            read = new RecordReader(filename);

        }
        catch (FileNotFoundException e)
        {
            //generated catch block
            assertNotNull(e);
            assertTrue(e instanceof FileNotFoundException);
        }
        assertTrue(read instanceof RecordReader);
    }
}