import student.TestCase;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * Class to test HeapTest
 * @author tsingh
 * @version 2019
 */
public class ExternalsortTest extends TestCase {
    
    /**
     * Method to test main file 
     * @throws IOException Ioexception
     */
    public void testMain() throws IOException {
        String[] in = {"sampleInput16b.bin"};
        assertNotNull(in);
        try {
            Externalsort.main(in);
        }
        catch(FileNotFoundException e)
        {
            //generated catch block
            assertNotNull(e);
            assertTrue(e instanceof FileNotFoundException);
        }
    }
    
    
}
