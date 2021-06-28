// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Libraries.CSV_lib;

public class PlaybackController extends SubsystemBase {
  CSV_lib csv;
  int count;
  Boolean n = true, m = true;
  Double pstarttime, rstarttime;
  /** PlaybackController controlls the macro system
   * of the robot. Is able to record and play macros.
   */
  public PlaybackController() {
    csv = new CSV_lib("currentMacro",null);
  }

  @Override
  public void periodic() {}

  /**
   * Writes the given set of doubles to the csv fle
   * @param vals All doubles to add to the csv in current line
   */
  public void record(Double... vals){
    if(m){rstarttime = (double) System.currentTimeMillis(); m = false;} // init

    // Joining arrays
    Double[] dat = {(System.currentTimeMillis() - rstarttime)};
    Double[] out = new Double[dat.length + vals.length];
    System.arraycopy(dat, 0, out, 0, dat.length);
    System.arraycopy(vals, 0, out, dat.length, out.length);

    csv.writeSet(out);
  }

  /**
   * Plays one line of the macro, use on repeat to play full macro
   */
  public void play(){
    if(n){pstarttime = (double) System.currentTimeMillis(); n = false;} // init
    if ((System.currentTimeMillis() - pstarttime) > csv.countDouble(count, .0)){count+=1; // if on time
      DriveTrain.setLeft1(csv.countDouble(count, 0.0)); count += 1; // make sure order is the same as it is recorded as
      DriveTrain.setLeft2(csv.countDouble(count, 0.0)); count += 1; 
      DriveTrain.setRight1(csv.countDouble(count, 0.0)); count += 1;
      DriveTrain.setRight2(csv.countDouble(count, 0.0)); count += 1;
    }
  }

  /**
   * Closes all usage of readers and writers, use to avoid memory leaks
   */
  public void close(){csv.close();}
}
