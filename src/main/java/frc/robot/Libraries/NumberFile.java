package frc.robot.Libraries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class NumberFile {
    Scanner reader; FileWriter writer, ender;
    File current;

    /** Creates new number filer
     * @param Filename The filename to name the txt file things will be
     * written in, you do not have to write the full file path.
     */
    public void Create(String Name){
        current = new File("home/lvuser/Name" +Name+ ".txt");
        try{ // Init reader and writer
            writer = new FileWriter(current, true);
            ender  = new FileWriter(current, false); ender.write("");
            reader = new Scanner(current);
        }catch(IOException e){e.printStackTrace();}
    }
    /** Will read out the txt file and paste all items as doubles
     * into a single array
     * @return Array of all doubles within the txt file
     */
    public double[] Read(){double[] return_ = {};
        while(reader.hasNextDouble()){
            return_ = doubleAppend(reader.nextDouble(), return_);}
        return return_;
    }

    /** Writes in the given double, when writing these values for specific
     * items be sure to save the format they were written in.
     * @param ToWrite the double that the writer will write inside of the txt.
     */
    public void Write(double ToWrite){
        try {writer.write(""+ToWrite);
		}catch(IOException e) {e.printStackTrace();}
    }


    private double[] doubleAppend(double ToAppend, double[] ToAppendTo) {double[] arr = ToAppendTo;
        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = ToAppend; // Assign ToAppendTo to the last element

        return arr;
    }
    /** Flishes both the reader and writer. Use this when you no longer need this
     * Library in that instance. May fail due to IOException or running this module
     * whilst the reader and writer are null.
     */
    public void end(){
        try{
            reader.close();
            writer.flush();
            writer.close();
        }catch(Exception e){e.printStackTrace();} // Throws out the error regardless of type
    }
}
