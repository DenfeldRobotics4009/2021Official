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

public class ManualAim extends CommandBase {
  private DeadZoneTuner tuner;
  private final Turret aimer;
  private final DoubleSupplier y, z;

  /**
   * Creates a new ManualAim.
   */
  public ManualAim(Turret turret, DoubleSupplier yaw, DoubleSupplier pitch) {
    aimer = turret;
    addRequirements(turret);
    y = pitch;
    z = yaw;
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
    double yf = tuner.adjustForDeadzone(y.getAsDouble(), .04, true);
    double zf = tuner.adjustForDeadzone(z.getAsDouble(), .04, true);

    aimer.ManuallyAim(.4 *zf, yf);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    aimer.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}