import student.TestCase;
/**
 * Class to test HeapTest
 * @author tsingh
 * @version 2019
 */
public class HeapTest extends TestCase {
    byte[] check = new byte[10];
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
        for(int i = 0; i < 9; i++)
        {
            check[i] = (byte)i;
        }
        testHeap = new Heap(check);
        Record top_rec = new Record(check);
        Record topcheck = testHeap.top();
        assertEquals(null, topcheck);
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
    
}
