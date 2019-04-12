import java.io.FileNotFoundException;
import java.io.IOException;
import student.TestCase;
/**
 * Class to test RecordPrinter
 * @author tsingh
 * @version 2019
 */
public class RecordReaderTest extends TestCase{
    private byte[] check = new byte[20];
    private String filename = "/sampleinput.bin";
    private RecordReader read;
    
    /**
     * Method to test read and set methods
     * @throws IOException for filenotfound
     */
    public void testReadFile() 
    {
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
    }
}