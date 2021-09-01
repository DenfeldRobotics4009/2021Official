// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Libraries.DeadZoneTuner;
import frc.robot.subsystems.Climber2;

public class Rdown extends CommandBase {
  private DeadZoneTuner tuner;
  private Climber2 climber2;
  private DoubleSupplier speed;
  private double finput, output;
  /**
   * Creates a new TestClimb.
   */
  public Rdown(Climber2 climb2, DoubleSupplier input) {
    speed = input;
    climber2 = climb2;
    addRequirements(climber2);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    tuner = new DeadZoneTuner();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    finput = tuner.adjustForDeadzone(speed.getAsDouble(), .25, false);
    output = (finput + 1) / -2;


    climber2.Rclimb(output);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber2.Rclimb(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
