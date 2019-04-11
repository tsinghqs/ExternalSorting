import student.TestCase;

/**
 * Test class for ByteStore.
 * @author vpratha
 * @version 4.11.2019
 */
public class ByteStoreTest extends TestCase {
    
    /**
     * Test reference.
     */
    private ByteStore bs;
    
    /**
     * Sets up test cases.
     */
    public void setUp()
    {
        byte[] buf = new byte[10];
        bs = new ByteStore(buf, 9, 8);
    }
    
    /**
     * Tests getBuf().
     */
    public void testGetBuf()
    {
        byte[] buf2 = bs.getBuf();
        assertEquals(buf2.length, 10);
    }
    
    /**
     * Tests getOffset().
     */
    public void testGetOffset()
    {
        assertEquals(bs.getOffset(), 9);
    }
    
    /**
     * Tests getSize().
     */
    public void testGetSize()
    {
        assertEquals(bs.getSize(), 8);
    }

}
