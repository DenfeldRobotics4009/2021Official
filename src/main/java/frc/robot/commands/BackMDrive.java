// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Libraries.DeadZoneTuner;
import frc.robot.subsystems.DriveTrain;

public class BackMDrive extends CommandBase {

  private final DriveTrain vroom;

  public DeadZoneTuner tuner;

  private final DoubleSupplier y, z, x;
  private final BooleanSupplier powered;
  private Boolean mecBool;
  
  //private final double tunedy, tunedz;

  /**
   * Creates a new ManualDrive.
   */
  public BackMDrive(
    DriveTrain drivee,
    DoubleSupplier rawforward,
    DoubleSupplier rawtwist,
    DoubleSupplier rawside,
    BooleanSupplier fullspeed){
    powered = fullspeed;
    vroom = drivee;
    y = rawforward;
    z = rawtwist;
    x = rawside;
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
  
    double tunedy, tunedz, tunedx;
    double MecDeadzone = 0.01;
    mecBool = tuner.adjustForDeadzone(x.getAsDouble(), MecDeadzone, false) > 0;


    if(mecBool = false) {
    
      // tunedy = tuner.adjustForDeadzone(y.getAsDouble(), .25, false) * -1;
      // tunedz = tuner.adjustForDeadzone(z.getAsDouble(), .07, false); // Old deadzone
      // tunedx = tuner.adjustForDeadzone(x.getAsDouble(), MecDeadzone, false);

    tunedy = tuner.adjustForDeadzone(y.getAsDouble(), .25, false) * -1;
    tunedz = tuner.adjustForDeadzone(z.getAsDouble(), .08, false);
    tunedx = tuner.adjustForDeadzone(x.getAsDouble(), MecDeadzone, false);


    }else{
      
      tunedy = tuner.adjustForDeadzone(y.getAsDouble(), .5, false) * -1;
      tunedz = tuner.adjustForDeadzone(z.getAsDouble(), .5, false);
      tunedx = tuner.adjustForDeadzone(x.getAsDouble(), MecDeadzone, false);

    }

    
    double multi = 1;
    if(powered.getAsBoolean()){multi = 2;}
    // if (reverse) {
    //   try{DriveTrain.MecanumDrive(-tunedy, -tunedz, -tunedx, mecBool, multi);}
    //   catch(NullPointerException e){DriveTrain.MecanumDrive(tunedy, tunedz, tunedx, mecBool, multi);};
    // }else{DriveTrain.MecanumDrive(tunedy, tunedz, tunedx, mecBool, multi);}

    DriveTrain.Drive(-tunedy, tunedz, -tunedx, mecBool, multi);
    
  }
  


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
