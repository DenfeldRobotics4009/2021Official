/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Libraries.DeadZoneTuner;
import frc.robot.subsystems.DriveTrain;

public class ManualDrive extends CommandBase {

  private final DriveTrain vroom;

  public DeadZoneTuner tuner;

  private final DoubleSupplier y, z;
  private final BooleanSupplier powered;
  //private final double tunedy, tunedz;

  /**
   * Creates a new ManualDrive.
   */
  public ManualDrive(DriveTrain drivee, DoubleSupplier rawforward, DoubleSupplier rawtwist, BooleanSupplier fullspeed) {
    powered = fullspeed;
    vroom = drivee;
    y = rawforward;
    z = rawtwist;
    addRequirements(vroom);
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
    double tunedy, tunedz;
    tunedy = tuner.adjustForDeadzone(y.getAsDouble(), .25, false);
    tunedz = tuner.adjustForDeadzone(z.getAsDouble(), .25, false);
    
    if(powered.getAsBoolean()){
      vroom.ArcadeDrive(tunedy, tunedz);
    }
    else {
      vroom.ArcadeDrive(tunedy*.75, tunedz*.75);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
