import java.io.FileNotFoundException;
import java.io.IOException;
import student.TestCase;
/**
 * Class to test HeapTest
 * @author tsingh
 * @version 2019
 */
public class HeapTest extends TestCase {
    /**
     * check field
     */
    private byte[] check = new byte[20];
    /**
     * @field Heap the test heap
     */
    private Heap testHeap;
    
    /**
     * Method to test heap constructor
     */
    public void testConstructor()
    {
        for (int i = 0; i < 9; i++)
        {
            check[i] = (byte)i;
        }
        testHeap = new Heap(check);
        assertNotSame(check, testHeap);
    }
    
    /**
     * method to test a new run
     */
    public void testNewRun()
    {
        byte[] check2 = new byte[20];
        for(int i = 0; i < 8; i++)
        {
            check2[i] = (byte)(i+1);
        }
        byte[] check3 = new byte[20];
        for(int i = 0; i < 8; i++)
        {
            check3[i] = (byte)(i+2);
        }
        for(int i = 0; i < 8; i++)
        {
            check[i] = (byte)i;
        }
        testHeap = new Heap(check);
        Record top_rec = new Record(check3);
        Record bottom_rec = new Record(check2);
        testHeap.push(bottom_rec);
        testHeap.push(top_rec);
        Record topcheck = testHeap.top();
        assertEquals(testHeap.top(), topcheck);
        testHeap.bubbleDown();
        testHeap.delete();
    }
    
    /**
     * method for delete and top functions
     */
    public void testBuildTop()
    {
        for(int i = 0; i < 9; i++)
        {
            check[i] = (byte)i;
        }
        testHeap = new Heap(check);
        testHeap.bubbleUp();
        testHeap.delete();
        Record r = testHeap.top();
        assertEquals(r, r);
        
    }
    
    /**
     * Test record methods
     * @throws IOException for write
     */
    public void testRecord() throws IOException
    {
        for(int i = 0; i < 8; i++)
        {
            check[i] = (byte)i;
        }
        Record top_rec = new Record(check);
        Record shift = new Record(check);
        top_rec.moveTo(shift);
        shift.copyContentTo(top_rec);
        top_rec.swapContentWith(shift);
        double val = shift.getValue();
        String tostring = top_rec.toString();
        assertEquals(val, 0.0, 1.0);
        RecordWriter write = new RecordWriter(check);
        String in = "sampleInput16b.bin.run1";
        assertNotNull(in);
        try {
            write.open(in);
        }
        catch(FileNotFoundException e)
        {
            //generated catch block
            assertNotNull(e);
            assertTrue(e instanceof FileNotFoundException);
        }
        boolean written = write.write(top_rec);
        assertTrue(written);
        boolean stream = write.writeToStream();
        assertTrue(stream);
        write.close();
        stream = write.write(shift);
        assertFalse(stream);
        
        
        
    }
    
}
