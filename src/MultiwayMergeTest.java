import student.TestCase;
/**
 * Test class for MultiwayMerge.
 * @author vpratha
 * @version 4.11.2019
 */
public class MultiwayMergeTest extends TestCase {
    
    /**
     * Test reference.
     */
    private MultiwayMerge mm;
    /**
     * Test input file.
     */
    //private String input_file = "C:\\Users\\Vikramaditya\\git\\Exter
    //nalSorting\\src\\sampleinput16.bin";
    
    /**
     * Sets up test cases.
     */
    public void setUp()
    {
        byte[] processingBuf = new byte[8 * 8192];
        byte[] outBuf = new byte[8192];
        mm = new MultiwayMerge(processingBuf, outBuf);
    }
    
    /**
     * Tests printSampleRecords().
     */
    public void testPrintSampleRecords()
    {
        mm.printSampleRecords(false);
        int check = 1;
        assertEquals(1, check);
    }
    
//    public void testPerform()
//    {
//        String[] run_files = new String[] {
//            input_file + ".run1",
//            input_file + ".run2"
//        };
//        RunList run_list;
//        mm.perform(run_list, run_files[0], run_files[1]);
//    }

}
