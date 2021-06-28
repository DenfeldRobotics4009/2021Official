/** Will record given values into a csv file located in home\lvuser
 *  Any type can be given and the file will record them.
 * 
 *  Able to be used for non-FRC reasons.
 * 
 *  6/17/2021
 * 
 */

package frc.robot.Libraries; // Change to desired workspace

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class CSV_lib {
    private FileWriter writer;
    private Scanner scanner;
    private Scanner tempScanner;
    private String file;

    /**
     * Initializes a new csv writer according to file name and directory
     * 
     * @param fileName specified filename (no .csv)
     * @param dir      path to add csv file to, if null is provided it will default
     *                 to "home\\lvuser\\"
     */
    public CSV_lib(String fileName, String dir) { // Constructs new RecCSV
        if (dir == null) {
            dir = "home\\lvuser\\";
        } // accepts null and defaults
        file = dir + fileName + ".csv"; // Path string
        try { // Init writer & reader
            writer = new FileWriter(file, true);
            scanner = new Scanner(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the given Object[] into the csv file, seperates 
     * each variable with an ','
     * 
     * @param dat String[] of
     */
    public void writeSet(Object[] dat) {
        for (Object i : dat) {
            try {
                writer.append(toString(i) + ",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the writer, be sure to run this before code ends to avoid memory leak
     */
    public void close() {
        try {
            writer.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Converts provided object to a string
     * 
     * @param obj object
     * @return object as the string
     */
    private String toString(Object obj) {
        String s = obj.toString();
        String s2 = String.valueOf(s);
        return s2;
    }

    /**
     * This private module is used to append a String to a String[]
     * 
     * @param ToAppend   The string to be added to the array
     * @param ToAppendTo The array that the string will be added to
     */
    public static String[] StringAppend(String ToAppend, String[] ToAppendTo) {

        String[] arr = ToAppendTo;

        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = ToAppend; // Assign ToAppendTo to the last element

        return arr;
    }

    /**
     * This private module is used to append a String[] to a String[][]
     * 
     * @param ToAppend   The array of strings to be added to the array of arrays of
     *                   strings (arrayception)
     * @param ToAppendTo The array that the array of strings will be added to
     */
    private static String[][] ArrayAppend(String[] ToAppend, String[][] ToAppendTo) {

        String[][] arr = ToAppendTo;

        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = ToAppend; // Assign ToAppendTo to the last element

        return arr;
    }

    /**
     * This module counts each data point from top left down the csv, use 
     * this module in a while loop to return each datapoint in order. 
     * This module will ignore all datapoints that are not doubles.
     * @param n the number given to the module that states what datapoint
     * to return
     * @param default_ the number to return if too high of a number is given
     * from n (Generally should be 0.0)
     * @return the datapoint in that location, ie; if the csv has 3 rows 
     * and 3 columns, giving a # 4 will give the datapoint at x=1 y=1
     * (keep in mind you count from 0)
     */
    public Double countDouble(int n, Double default_) {

        String[][] file = CSVContent();
        int m = 0;
        for (String[] i : file) {
            if (n > i.length - 1) { // -1 is used because .length doesnt count from 0
                m = m + 1;
                n = n - i.length;
            }
        }

        try {
            return Double.parseDouble(file[m][n]);
        }catch(ArrayIndexOutOfBoundsException e){
            return default_;
        }
    }

    /** 
     * Returns an array of all content from the csv file
     * stated when class began 
     * @return String[][] of content
    */
    public String[][] CSVContent() {
        
        try {tempScanner = new Scanner(new File(file));// resetting scanner
        } catch (FileNotFoundException e) {e.printStackTrace();} 

        String[][] lineAr = {};
        while (tempScanner.hasNextLine()){ // adds each line array to an empty array
            lineAr = ArrayAppend(
                tempScanner.nextLine().split(","), lineAr); // splits each line by comma to form line array
        }

        tempScanner.close();
        return lineAr;
        
    }
    
}