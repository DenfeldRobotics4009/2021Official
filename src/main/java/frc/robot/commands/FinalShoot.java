/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.InnerIntake;

public class FinalShoot extends CommandBase {
  private InnerIntake s_InnerIntake;
  private boolean JustReleased = false;
  private Timer DelayTimer;
  private double Delay = .5;
  /**
   * Creates a new FinalShoot.
   */
  public FinalShoot(InnerIntake innerintake) {
    s_InnerIntake = innerintake;
    addRequirements(innerintake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    DelayTimer = new Timer();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_InnerIntake.Shoot();

  }
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_InnerIntake.SetStoredBall(false);
    s_InnerIntake.Stop();
    s_InnerIntake.InterruptShot(s_InnerIntake.TopSensorActive());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
