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

public class ManualTarget extends CommandBase {
  private Turret s_turret;
  private double tunedy, tunedz, mathedY;
  private Boolean isDone;

  public DeadZoneTuner turret_tuner;

  private final DoubleSupplier j_y, j_z;
  /**
   * Creates a new ManualTarget.
   */
  public ManualTarget(Turret su_turret, DoubleSupplier rawforward, DoubleSupplier rawtwist, Boolean cancelButton) {
    j_y = rawforward;
    j_z = rawtwist;
    isDone = cancelButton;
    s_turret = su_turret;
    addRequirements(s_turret);
    // Use addRequirements() here to declare subsystem dependencies.

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turret_tuner = new DeadZoneTuner();
    s_turret.TurnOffLight();
    s_turret.EnableRegularStream();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    tunedy = turret_tuner.scaleDeadzone(j_y.getAsDouble(), .1, false, 0.8); // updated to use better deadzone thing

    mathedY = (Math.abs(tunedy) /2) + .5;

    tunedz = turret_tuner.scaleDeadzone(j_z.getAsDouble(), .3, false, 0.8); // updated to use better deadzone thing

    if(tunedy > 0) {
    s_turret.ManuallyAim(tunedz, mathedY);
    }
    else {
      s_turret.ManuallyAim(tunedz, -mathedY);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    s_turret.ManuallyAim(0.0, 0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (tunedy == 0 && tunedz == 0) || isDone;
  }
}