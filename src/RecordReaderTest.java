import java.io.FileNotFoundException;
import student.TestCase;
/**
 * Class to test RecordPrinter
 * @author tsingh
 * @version 2019
 */
public class RecordReaderTest extends TestCase{
    private byte[] check = new byte[20];
    private String filename = "/Users/tsingh/eclipse-workspace/ExternalSorting/src/sampleinput16.bin";
    private RecordReader read;
    
    /**
     * Method to test read and set methods
     * @throws FileNotFoundException
     */
    public void testReadFile() throws FileNotFoundException
    {
        read = new RecordReader(filename);
        for(int i = 0; i < 8; i++)
        {
            check[i] = (byte)i;
        }
        read.set(check);
        int checker = read.read();
        assertEquals(checker, 1);
        Record r = read.getNext();
        Record c = new Record(check);
        assertEquals(r, read.getNext());
    }
}
