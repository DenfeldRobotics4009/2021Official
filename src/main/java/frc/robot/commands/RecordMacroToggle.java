// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.PlaybackController;

public class RecordMacroToggle extends CommandBase {
  PlaybackController pController;
  Boolean n = true;
  Double trigger = 0.0;
  Double starttime;
  public RecordMacroToggle(PlaybackController p) {
    pController = p;
    addRequirements(pController);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(n){starttime = (double) System.currentTimeMillis(); n = false;}

    pController.record(
      (double) System.currentTimeMillis() - starttime,
      DriveTrain.grabLeft1(),
      DriveTrain.grabLeft2(),
      DriveTrain.grabRight1(),
      DriveTrain.grabRight2()
      );
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
