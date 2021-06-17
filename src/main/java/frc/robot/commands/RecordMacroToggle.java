// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.io.IOException;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.PlaybackController;

public class RecordMacroToggle extends CommandBase {
  PlaybackController pController;
  Boolean doRecord = false;
  Double trigger = 0.0;
  public RecordMacroToggle(PlaybackController p) {
    pController = p;
    addRequirements(pController);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {} // lol i never use this

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    doRecord = !doRecord; // flips recording state
    if (doRecord) {
      try {
        if (RobotContainer.Joystickd1()){ trigger = 1.0;} // converting bool to double sucks for no reason at all
        else{trigger = 0.0;}
		    pController.record(
          RobotContainer.Joystickdy(),
          RobotContainer.Joystickdz(),
          RobotContainer.Joystickdx(),
          0.0, 0.0, 0.0, // operator joy stick is not needed
          trigger
		    );
	    } catch (IOException e) {
		    e.printStackTrace(); // heehee
	}
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {} // do nothing on end and this will never be interrupted

  // Returns true when the command should end.
  @Override
  public boolean isFinished() { // nop
    return false;
  }
}
