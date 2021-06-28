
package frc.robot.Libraries;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class tools {

    /** This private module is used to append a String[] to a String[][]
     * @param ToAppend The array of strings to be added to the array of arrays of strings (arrayception)
     * @param ToAppendTo The array that the array of strings will be added to 
    */
    public static String[][] ArrayAppend(String[] ToAppend, String[][] ToAppendTo) {

        String[][] arr = ToAppendTo;

        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = ToAppend; // Assign ToAppendTo to the last element

        return arr;
    }

    /** This private module is used to append a String to a String[]
     * @param ToAppend The string to be added to the array
     * @param ToAppendTo The array that the string will be added to 
    */
    public String[] StringAppend(String ToAppend, String[] ToAppendTo) {

        String[] arr = ToAppendTo;

        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = ToAppend; // Assign ToAppendTo to the last element

        return arr;
    }

    public int[] Intappend(int ToAppend, int[] ToAppendTo) {
        
        int[] arr = ToAppendTo;

        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = ToAppend; // Assign ToAppendTo to the last element

        return arr;
    }
    /**This module will only add things on top of the csv file and not edit existing values.
     * @param filePath the path to the file being selected
     * @param ToAddStringArray The string array that will be added in the csv file
     */
    public void csvAppend(String filePath, String[] ToAddStringArray){
        try {
            FileWriter writer = new FileWriter(filePath, true);

            for(String f : ToAddStringArray){writer.write(f);}
            writer.write("\r\n");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
