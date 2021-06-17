/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Libraries.DeadZoneTuner;
import frc.robot.subsystems.Climber;

public class Ldown extends CommandBase {
  private DeadZoneTuner tuner;
  private Climber climber;
  private DoubleSupplier speed;
  private double finput, output;
  /**
   * Creates a new TestClimb.
   */
  public Ldown(Climber climb, DoubleSupplier input) {
    speed = input;
    climber = climb;
    addRequirements(climber);
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


    climber.Lclimb(output);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.Lclimb(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
