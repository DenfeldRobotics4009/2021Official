/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;


public class CameraTarget extends CommandBase {
  private Turret turret;
  private Timer TimeoffTarget;

  private DoubleSupplier osy, osx;
  /**
   * Creates a new CameraTarget.
   */
  public CameraTarget(Turret t, DoubleSupplier Offsety, DoubleSupplier Offsetx) {
    osy = Offsety;
    osx = Offsetx;

    turret = t;
    addRequirements(turret);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turret.EnterVisionTargetingMode();
    TimeoffTarget = new Timer();
    TimeoffTarget.reset();
    TimeoffTarget.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turret.TurnOnLight();

   if(turret.IsOnTarget()){
     TimeoffTarget.reset();
   }
   else{}
   
    turret.UseCameraEyes(osy.getAsDouble(), osx.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.TurnOffLight();
  turret.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return TimeoffTarget.get() >= 2;
  }
}
