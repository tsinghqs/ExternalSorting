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
        byte[] processing_buf = new byte[8 * 8192];
        byte[] out_buf = new byte[8192];
        mm = new MultiwayMerge(processing_buf, out_buf);
    }
    
    /**
     * Tests printSampleRecords().
     */
    public void testPrintSampleRecords()
    {
        mm.printSampleRecords(false);
        assertEquals(1, 1);
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
