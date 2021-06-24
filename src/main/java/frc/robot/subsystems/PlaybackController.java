// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Libraries.CSV_lib;

public class PlaybackController extends SubsystemBase {
  CSV_lib csv;
  int count;
  Boolean n = true;
  Double starttime;
  /** Creates a new PlaybackController. */
  public PlaybackController() {
    csv = new CSV_lib("currentMacro",null);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void record(Double... vals){csv.writeSet(vals);}

  public void play(){

    if(n){starttime = (double) System.currentTimeMillis(); n = false;}
    if ((System.currentTimeMillis() - starttime) > csv.countDouble(count, 0.0)){count+=1;
      DriveTrain.setLeft1(csv.countDouble(count, 0.0)); count += 1;
      DriveTrain.setLeft2(csv.countDouble(count, 0.0)); count += 1;
      DriveTrain.setRight1(csv.countDouble(count, 0.0)); count += 1;
      DriveTrain.setRight2(csv.countDouble(count, 0.0)); count += 1;
    }
  }
}
