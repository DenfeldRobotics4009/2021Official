/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {
  private WPI_TalonSRX winch1;
  private double wInput = 0;
  /**
   * Creates a new Climber.
   */
  public Climber() {
    winch1 = new WPI_TalonSRX(Constants.LClimbMotorPort);
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  /*
  public void climb(double input, double ProportionConstant, double offsetConstant) {
    winch.set(input);
    arm1.setNeutralMode(NeutralMode.Brake);
    winch.setNeutralMode(NeutralMode.Coast);
    if(input > 0){
    arm1.set(input*ProportionConstant + offsetConstant);
    }
    else {
      arm1.set(.2);
    }
  }
  */
  public void Lclimb(double input){
    wInput = input;
    winch1.set(input);
  }

 
  /*
  public void maintain(double speed) {
    winch.set(0);
    arm1.set(speed);
  }

  public void rewind(double input){
    arm1.set(0);
    arm1.setNeutralMode(NeutralMode.Brake);
    winch.setNeutralMode(NeutralMode.Brake);
    winch.set(input);
  }
*/

  public double grabClimber1Input(){return wInput;}

}
