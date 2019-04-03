import java.io.File;
import java.nio.ByteBuffer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;



public class Externalsort {
    
    /**
     * This is the main class and function that runs the project
     * 
     * @param args a string value to run the project
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException
    {
        //read file as an argument
      //read file as an argument
        String fileName = args[0];
        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        RandomAccessFile in = new RandomAccessFile(fileName, "r");
        byte[] doubles = new byte[8];
        byte[] id = new byte[8];
        for (int i = 0; i < 32; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                //System.out.println(in.readByte());
                doubles[j] = in.readByte();
                
            }
            for (int k = 0; k < 8; k++)
            {
                id[i] = in.readByte();
            }
            long vlong = ByteBuffer.wrap(doubles).getLong();
            double dub = ByteBuffer.wrap(id).getDouble();
            System.out.println(vlong);
            System.out.println(dub);
            
        }
        
        
        
        
    }

}
