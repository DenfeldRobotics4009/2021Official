// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Libraries.CSVMacroLibrary;
import frc.robot.Libraries.DeadZoneTuner;

public class PlaybackController extends SubsystemBase {
  FileWriter writer;
  Scanner reader;
  CSVMacroLibrary csvLib;
  DeadZoneTuner tuner;

  static int
    autoNumber    =0, /** used to be final, but i will have a smart filer
                      to detect previous macros */
    startTime     =0;

  boolean 
    isRecording   =false, 
    rec           =false, 
    init          =false, 
    play          =false, 
    confirminit   =false, 
    PlayErr       =false, 
    onTime        =false, 
    attempt       =false;
  
  double 
    t_delta       =0.0,
    nextDouble    =0.0;

  String
    str           ="",
    fileName      ="",
    initMessage   ="[null]",
    errorMessage  ="";
  
  static String
    macroName     ="",
    dumbName      ="4009";

  static final Double
    factor        =1.0;
  
  static final String autoFile = new String("/home/lvuser/" + 
    (macroName != "" ? macroName : dumbName)
    + autoNumber // Filenames will be <Final Name><Version>
    + ".csv");

    String[] files = {};
  public PlaybackController() { // Initialize reader and writer

    tuner = new DeadZoneTuner();

    try{

      writer = new FileWriter(new File(autoFile), false); // To write to csv
      reader = new Scanner(new File(autoFile))     // To read to csv
        .useDelimiter(",|\\r"); // defines what chars to throw out
      
      // telling the system things went correctly
      confirminit = true; 
      initMessage = "Init reader & writer for autoFile [2]";

    }catch(IOException e)
    { errorMessage = "init IOException";
      e.printStackTrace();} // Throw out the error like the good programmer i am
  }

  @Override
  public void periodic() {

    fileName = macroName + autoNumber;

    // Shuffleboard lol
    SmartDashboard.putNumber("System Time", System.currentTimeMillis());
    SmartDashboard.putString("Initialized", initMessage);
    SmartDashboard.putString("Error Log", errorMessage);
    SmartDashboard.putString("FileName", getCurrentFileName());
    SmartDashboard.putStringArray("Files", files);
    // I have a habit of putting nothing in the periodic module




  }

  // TODO Smart Filer System

  public void SmartFiler() { Boolean checkWriter = false, checkReader = false;
    /** Should be able to read last active file and generate a new filename on its own accordingly,
     * may take inputs from shuffleboard
     */

    if (writer != null) {checkWriter = false;} else {checkWriter = true;}
    if (reader != null) {checkReader = false;} else {checkReader = true;}


    File folder = new File("home/lvuser");
    File[] listOfFiles = folder.listFiles();

    
    for (int i = 0; i < listOfFiles.length; i++) { // Generates a list of all files and directories within the robot
      if (listOfFiles[i].isFile()) {              files = CSVMacroLibrary.StringAppend((listOfFiles[i].getName()), files);
      } else if (listOfFiles[i].isDirectory()) {  files = CSVMacroLibrary.StringAppend(("D_" + listOfFiles[i].getName()), files);
    }}

    autoNumber = autoNumber + 1;
    createFile(""); // Change the name of this
    
  }


  /** Creates a new file and sets the reader ad writer to that file */
  private void createFile(String FileName){
    // flushing and closing previous reader and writer
    if (writer != null) {try{
      writer.flush(); writer.close();}                        // checks if it even needs too first
      catch(IOException e){e.printStackTrace();}}             // otherwise it may error
                                          else {errorMessage = "Unexpected Null - Non Fatal [2]";}
    if (reader != null) {reader.close();} else {errorMessage = "Unexpected Null - Non Fatal [3]";}

    File finalFile = new File("/home/lvuser/" + (macroName != "" ? macroName : dumbName) + autoNumber+ ".csv");

    try{

      writer = new FileWriter(finalFile); // To write to csv
      reader = new Scanner(finalFile)     // To read to csv
        .useDelimiter(",|\\r"); // defines what chars to throw out
      
      initMessage = "New Reader and Writer [0]";

    }catch(IOException e){e.printStackTrace();} // toss it out
  }

  /** Sets the curren file to whatever u make it
   * @param FileNameToSelect Do not include the .csv at the end, it is
   * not needed
   */
  private void setCurrentFile(String FileNameToSelect){
    // flushing and closing previous reader and writer
    if (writer != null) {try{writer.flush(); writer.close();} 
      catch(IOException e){e.printStackTrace();}}   else {errorMessage = "Unexpected Null - Non Fatal [0]";}
    if (reader != null) {reader.close();}           else {errorMessage = "Unexpected Null - Non Fatal [1]";}

    File finalFile = new File("/home/lvuser/" + FileNameToSelect + ".csv");
    try{

      writer = new FileWriter(finalFile, false); // To write to csv
      reader = new Scanner(finalFile)     // To read to csv
        .useDelimiter(",|\\r"); // defines what chars to throw out
      
      initMessage = "New Reader and Writer [3]";

    }catch(IOException e){e.printStackTrace();} // toss it out
  }

  /** Defines the startTime for all other modules to use, this module defines a new
   * startTime whenever it is provided a different InitMessage.
   * @param InitMessage The message to display to know this has run.
   */
  public void play_recordStart(String InitMessage) {
    if(initMessage != InitMessage){
      initMessage = InitMessage; // sets the new initMessage
      startTime = (int) System.currentTimeMillis();
      confirminit = true; // Flipping confirminit
      if (initMessage == "Started Recording [1]"){
        SmartFiler(); // runs the smartfiler when initializing recorder
      }
    }//else{}
  }
  /** Provides the time relative to the currently selected startTime.
  * @return startTime - System.currentTimeMillis();
  */
  public int relativeTime(){return (int) System.currentTimeMillis() - startTime;}
  /** Begins recording the given values of the robot. Im going to try to clearly
   * define where to provide values to track, as well as provide instructions to
   * provide modules for playback.
   * @param Joysticks Requires any number of doubles. The format must be shown as
   * js1y, js1z, js1x, js2y, js2z, js2x, then any number of buttons. Remember to match the
   * playback with these inputs. Button values must be either 0 or 1.
   * @throws IOException nae nae
   */
  public void record(Double... Joysticks) // Basically, joysticks is an array, and any numer of doubles can be given.
  throws IOException {isRecording = true;
    play_recordStart("Started Recording");
    int JoysticksLength = Joysticks.length;

    int[] buttons = {};
    double
      js1y = 0, js1z = 0, js1x = 0,

      js2y = 0, js2z = 0, js2x = 0;

      if (JoysticksLength > 0) {
        // gets all the joystick values
        js1y = Joysticks.length != 0 ? Joysticks[0] : 0;
        js1z = Joysticks.length != 0 ? Joysticks[1] : 0;
        js1x = Joysticks.length != 0 ? Joysticks[2] : 0;

        js2y = Joysticks.length != 0 ? Joysticks[3] : 0;
        js2z = Joysticks.length != 0 ? Joysticks[4] : 0;
        js2x = Joysticks.length != 0 ? Joysticks[5] : 0;

        // now for the fancy button reader
        if (Joysticks.length > 5) {int t = 6;
          while(t <= Joysticks.length) {
            buttons = csvLib.Intappend((int) 
              Math.round(Joysticks[t]), buttons);
            t=t+1;
          }
        }
      }

    writer.write("," + relativeTime()); // Writes the current timestamp

    /** To initialize your modules to record, create modules within each subsystem
     * for providing such values. After creating these values you must call them into
     * the writer with the code block:
     * 
     *  writer.write("," + <subsystemname>.<getvaluemodule>());
     */

      // Begin writing your values here

        // TODO input robot values

        /** just as an idea, input joystick controlls instead of giving motor values
         * this obviously would require a new recording when we change drive contols, but
         * it could lower the file size of the macros and thus increase speed.
         */

          writer.write(","+ js1y);
          writer.write(","+ js1z); // driver
          writer.write(","+ js1x);

          writer.write(","+ js2y);
          writer.write(","+ js2z); // operator
          writer.write(","+ js2x); // (will probably always be 0)

          if (buttons.length > 0){ // writing all buttons
            int t = 0;
            while (t >= buttons.length){writer.write("," + buttons[t]); 
              t=t+1;}}

        

      // End writing your values here
    writer.write("\r"); // creates a new line

  }
    /** Will perform all actions the recorder needs to perform when it ends
     * @throws IOException
     */
    public void recorderEnd() throws IOException { isRecording = false;
      writer.flush();
      writer.close();
      errorMessage = "Ended Recording";
    }
  /** @return The filename of the currently selected file */
  public String getCurrentFileName() {return fileName;}

  public void play(){
    if(onTime){nextDouble = reader.nextDouble();} // sets next timestamp when we pass 0
    t_delta = nextDouble - relativeTime();
    if (t_delta <= 0){ // set motors when ontime

      /** Make sure to play back in the same order as you recorded in, otherwis values will
       * not be put into the correct modules
       */


      // TODO Export csv values
      // Begin exporting your values here

        double dy= reader.nextDouble(); // creating temp
        double dz= reader.nextDouble(); // variables to
        double dx= reader.nextDouble(); // put into robot

        double oy= reader.nextDouble();
        double oz= reader.nextDouble(); // throwaway values
        double ox= reader.nextDouble();

        double factor;
        if (RobotContainer.Joystickd1()) {factor = 2;}
        else{factor = 1;}
        DriveTrain.MecanumDrive(
          dx, dz, dy,
          (tuner.adjustForDeadzone(dx, 0.01, false) > 0),
          factor
        );
        
      // End exporting your values here


      
      onTime = true;
    }else{onTime = false;} 
  }
    //stop motors and end playing the recorded file
    public void playEnd(){
  
      DriveTrain.setLeft1(0.0);
      DriveTrain.setLeft2(0.0);
      DriveTrain.setRight1(0.0);
      DriveTrain.setRight2(0.0);
      FrontIntake.setIntakeValue(0.0);
      InnerIntake.setTopConveyor(0.0);
      InnerIntake.setBottomConveyor(0.0);
  
      errorMessage = "Ending Player";
      reader.close();}
    public boolean Playing(){
      return play;}
}
