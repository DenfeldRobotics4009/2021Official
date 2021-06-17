/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber2 extends SubsystemBase {
  private WPI_TalonSRX winch2;
  private double wInput = 0;
  /**
   * Creates a new Climber2.
   */
  public Climber2() {
    winch2 = new WPI_TalonSRX(Constants.RClimbMotorPort);
    winch2.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public  void Rclimb(double input){
    wInput = input;
    winch2.set(input);
  }
  public double grabClimber2Input(){return wInput;}
}
