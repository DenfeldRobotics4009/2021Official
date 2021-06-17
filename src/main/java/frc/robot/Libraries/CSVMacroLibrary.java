
// package okimgoinginsane; // COMMENT THIS
package frc.robot.Libraries; //UNCOMMENT THIS

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;

public class CSVMacroLibrary {



    /**Grabs the amount of rows of literally any file type. this thing is awsome
     * ( REMEMBER To subtract one to get it to count from 0)
     * @param fileName the filepath for the searcher to grab rows of
     */
    public static long getRows(String fileName) {

        Path path = Paths.get(fileName);
  
        long lines = 0;
        try {
  
            // much slower, this task better with sequence access
            //lines = Files.lines(path).parallel().count();
  
            lines = Files.lines(path).count();
  
        } catch (IOException e) {
            e.printStackTrace();
        }
  
        return lines;
  
    }

    /** This private module is used to append a String[] to a String[][]
     * @param ToAppend The array of strings to be added to the array of arrays of strings (arrayception)
     * @param ToAppendTo The array that the array of strings will be added to 
    */
    private static String[][] ArrayAppend(String[] ToAppend, String[][] ToAppendTo) {

        String[][] arr = ToAppendTo;

        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = ToAppend; // Assign ToAppendTo to the last element

        return arr;
    }

    /** This private module is used to append a String to a String[]
     * @param ToAppend The string to be added to the array
     * @param ToAppendTo The array that the string will be added to 
    */
    public static String[] StringAppend(String ToAppend, String[] ToAppendTo) {

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

    /**
     * This will read the csv and probably return the selected value determined by x
     * and y By supplying x and y the code will know what value to select from the
     * .csv and should return them in the form of most likely a string (just for
     * compatability).
     * 
     * @param filepath The file path that should be called in order for the csv file
     *                 to be read
     * @param y        The row that will be used
     * @param x        The column that will be used
     */
    public static String read(String filepath, int x, int y) {
        String[] file = {};
        String[][] arrfile = {};
        String[] row = {};
        // checking if the file actually exists
        File csvFile = new File(filepath);
        if (csvFile.isFile()) {
            try {
                File myObj = new File(filepath);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    file = StringAppend(data, file);
                }
                myReader.close();
              } catch (FileNotFoundException e) {
                System.out.println("File Not Found - 2");
                e.printStackTrace();
              }

              for (String f : file){
                  row = f.split(",");
                  arrfile = ArrayAppend(row, arrfile);
              }

              return arrfile[x][y];
        }else{return "File Not Found";}
        
    }

    /** A terrible method of searching a fill row, but it will work for now
     * im starting to worry about speed though
     * @param row The row to be grabbed
     * @param filePath the csv file that is being selected
     */
    public static String[] readRow(int row, String filepath){
        String[] file = {};
        String[][] arrfile = {};
        String[] rowb = {};

        String[] err = {"File Not Found"};
        // checking if the file actually exists
        File csvFile = new File(filepath);
        if (csvFile.isFile()) {
            try {
                File myObj = new File("filename.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    file = StringAppend(data, file);
                }
                myReader.close();
              } catch (FileNotFoundException e) {
                System.out.println("File Not Found - 2");
                e.printStackTrace();
              }

              for (String f : file){
                  rowb = f.split(",");
                  arrfile = ArrayAppend(rowb, arrfile);
              }

              return arrfile[row];
        }else{return err;}
        
    }

    /**This module will only add things on top of the csv file and not edit existing values.
     * @param filePath the path to the file being selected
     * @param ToAddStringArray The string array that will be added in the csv file
     */
    public void csvAppender(String filePath, String[] ToAddStringArray){
        try {
            FileWriter writer = new FileWriter(filePath, true);

            for(String f : ToAddStringArray){writer.write(f);}
            writer.write("\r\n");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /** Will create a new csv file and put it in CompetitionBot\\src\\main\\java\\frc\\robot\\Recordings\\
     *  @param Name The name of the file you wish to add, you do not need to put .csv at the end
      */
    public static String createCSV(String Name) {

        try{
        File newFile = new File("CompetitionBot\\src\\main\\java\\frc\\robot\\Recordings\\"+Name+".csv");

        BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

        writer.flush();
        writer.close();
        
        return newFile.getCanonicalPath();
        }catch(IOException e){e.printStackTrace();
        return "Error";}
        
    }

} // end of class
