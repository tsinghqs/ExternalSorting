import student.TestCase;
/**
 * Test class for RunTest.
 * @author vpratha
 * @version 4.11.2019
 */
public class RunTest extends TestCase {
    
    /**
     * Test reference.
     */
    private Run run;
    
    /**
     * Sets up test cases.
     */
    public void setUp()
    {
        run = new Run();
    }
    
    /**
     * Tests incCount() and getCount().
     */
    public void testIncCountAndGetCount()
    {
        run.incrementCount();
        assertEquals(run.getCount(), 1);
    }
    
    /**
     * Tests get and setOffset().
     */
    public void testSetAndGetOffset()
    {
        run.setOffset(1);
        assertEquals(run.getOffset(), 1);
    }

}
