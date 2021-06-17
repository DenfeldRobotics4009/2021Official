/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Libraries.DeadZoneTuner;
import frc.robot.subsystems.WheelSpinner;




public class SpinManual extends CommandBase {
  private final WheelSpinner WOFspin;

  private DoubleSupplier throttle;
  private DeadZoneTuner adjuster;
  /**
   * Creates a new SpinTheWheelYourself.
   */
  
  public SpinManual(WheelSpinner spinner, DoubleSupplier speed) {
    throttle = speed;
    WOFspin = spinner;
    addRequirements(spinner);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    adjuster = new DeadZoneTuner();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double output = adjuster.adjustForDeadzone(throttle.getAsDouble(), .25, false);
    WOFspin.ManualSpin(output);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    WOFspin.ManualSpin(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
