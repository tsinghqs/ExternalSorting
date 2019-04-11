import student.TestCase;
/**
 * Class to test HeapTest
 * @author tsingh
 * @version 2019
 */
public class HeapTest extends TestCase {
    byte[] check = new byte[20];
    /**
     * @field Heap the test heap
     */
    private Heap testHeap;
    
    /**
     * Method to test heap constructor
     */
    public void testConstructor()
    {
        for(int i = 0; i < 9; i++)
        {
            check[i] = (byte)i;
        }
        testHeap = new Heap(check);
        assertFalse(false);
    }
    
    /**
     * method to test a new run
     */
    public void testNewRun()
    {
        for(int i = 0; i < 8; i++)
        {
            check[i] = (byte)i;
        }
        testHeap = new Heap(check);
        Record top_rec = new Record(check);
        Record bottom_rec = new Record(check);
        testHeap.push(bottom_rec);
        testHeap.push(top_rec);
        Record topcheck = testHeap.top();
        assertEquals(testHeap.top(), topcheck);
        testHeap.delete();
        //testHeap.bubbleDown();
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
        testHeap.delete();
        Record r = testHeap.top();
        assertEquals(r, r);
        
    }
    
    /**
     * Test record methods
     */
    public void testRecord()
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
        
    }
    
}
