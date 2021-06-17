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
import frc.robot.subsystems.Turret;

public class GyroTarget extends CommandBase {
  private DeadZoneTuner adjuster;
  private final Turret spinner;
  private final DoubleSupplier turretZ, angley, GAngle;
  
  /**
   * Creates a new GyroTarget.
   */
  public GyroTarget(Turret turner, DoubleSupplier GyroAngle, DoubleSupplier manualy, DoubleSupplier manualz) {
    spinner = turner;
    GAngle = GyroAngle;
    angley = manualy;
    turretZ = manualz;
    addRequirements(turner);

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

    double twist = turretZ.getAsDouble();
    double pitch = angley.getAsDouble();
    double adjustedtwist = adjuster.adjustForDeadzone(twist, .3, false);
    double adjustedpitch = adjuster.adjustForDeadzone(pitch, .15, false);
    // spinner.StayForward(GAngle.getAsDouble(), adjustedtwist, adjustedpitch);
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    spinner.stop();
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return spinner.IsOnTarget();
  }
}
