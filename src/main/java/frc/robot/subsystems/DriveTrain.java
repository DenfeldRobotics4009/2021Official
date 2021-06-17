/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.EncoderType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Libraries.PIDController;

/**
 * Drives the robot. We have 2 NEOS as motors, and the encoders go on the NEOS.
 * @author Nikolai
 */
public class DriveTrain extends SubsystemBase {
  /**
   * Creates a new DriveTrain.
   */

  private NetworkTable DriveTrain = NetworkTableInstance.getDefault().getTable("DriveTrain");
  /*
  private NetworkTableEntry LeftGroup = DriveTrain.getEntry("l");
  private NetworkTableEntry RightGroup = DriveTrain.getEntry("r");
  */



  private static double[] addative = {0.0,0.0,0.0,0.0};
  public static CANSparkMax left1, right1, left2, right2;
  private static CANEncoder leftE, rightE, leftE2, rightE2;
  private SpeedControllerGroup l, r;
  private DifferentialDrive drive;
  private AHRS navx;
  private CANPIDController pid1, pid2, pid3, pid4;
  private PIDController locater;
  private static Double[] encoders = {0.0,0.0,0.0,0.0};

  private static boolean allow = true;

  private static Double joyStickForward, joyStickTwist, joyStickSide;

  private static boolean mec;

  private double p, i, d;
  public DriveTrain() {
    p = .1;
    i = 0;
    d = .075;
   locater = new PIDController(0.03, 0, 0.05, 0);
    
    left1 = new CANSparkMax(Constants.LeftDrive1MotorPort, MotorType.kBrushless);
    right1 = new CANSparkMax(Constants.RightDrive1MotorPort, MotorType.kBrushless);
    left2 = new CANSparkMax(Constants.leftDrive2MotorPort, MotorType.kBrushless);
    right2 = new CANSparkMax(Constants.RightDrive2MotorPort, MotorType.kBrushless);

    leftE = left1.getEncoder(EncoderType.kHallSensor, 42);
    leftE2 = left2.getEncoder(EncoderType.kHallSensor, 42);
    rightE = right1.getEncoder(EncoderType.kHallSensor, 42);
    rightE2 = right2.getEncoder(EncoderType.kHallSensor, 42);

    pid1 = left1.getPIDController();
    pid2 = left2.getPIDController();
    pid3 = right1.getPIDController();
    pid4 = right2.getPIDController();

    pid1.setP(p);
    pid1.setI(i);
    pid1.setD(d);
    pid1.setIZone(0);
    pid1.setFF(0);
    pid1.setOutputRange(-1, 1);

    pid2.setP(p);
    pid2.setI(i);
    pid2.setD(d);
    pid2.setIZone(0);
    pid2.setFF(0);
    pid2.setOutputRange(-1, 1);
    
    pid3.setP(p);
    pid3.setI(i);
    pid3.setD(d);
    pid3.setIZone(0);
    pid3.setFF(0);
    pid3.setOutputRange(-1, 1);   
    
    pid4.setP(p);
    pid4.setI(i);
    pid4.setD(d);
    pid4.setIZone(0);
    pid4.setFF(0);
    pid4.setOutputRange(-1, 1);

    left1.setOpenLoopRampRate(1);
    left2.setOpenLoopRampRate(1);
    right1.setOpenLoopRampRate(1);
    right2.setOpenLoopRampRate(1);


    l = new SpeedControllerGroup(left1, left2);
    r = new SpeedControllerGroup(right1, right2);

    leftE = left1.getEncoder(EncoderType.kHallSensor, Constants.DriveEncoderResolution);
    rightE = right1.getEncoder(EncoderType.kHallSensor, Constants.DriveEncoderResolution);
  

    drive = new DifferentialDrive(l, r);

    try {
      navx = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantating navX-MXP: " + ex.getMessage(), true);
    }

    navx.reset();

 
    
  }

  @Override
  public void periodic() {

  SmartDashboard.putNumber("Left1 Encoder", -leftE.getVelocity());
  SmartDashboard.putNumber("Right1 Encoder", rightE.getVelocity());
  SmartDashboard.putNumber("Left2 Encoder", -leftE2.getVelocity());
  SmartDashboard.putNumber("Right2 Encoder", rightE2.getVelocity());
  SmartDashboard.putBoolean("mecBool", mec);

    // This method will be called once per scheduler run
  }

  public static double joystickforward;
  public static double joysticktwist;
  /**
   * Use this method to drive the robot with the input parameters.
   * @param forward the amount of forward/backward motion you want
   * @param twist the amount of twisting left/right you put in.
   */
  public void ArcadeDrive(double forward, double twist) {
    joystickforward = forward;
    joysticktwist = twist;
    drive.arcadeDrive(forward, twist);

  }

  /** Drive the robot according to mecanum input parameters
   *  @param forward the forward angle of the joystick
   *  @param twist The twist of the joystick
   *  @param side the side to side angle of the joystick
   */
  public static void MecanumDrive(double side, double twist, double forward, boolean mecBool, double multiplyer) {
    
    if(allow){
    
    
    mec = mecBool;
    joystickforward = forward;
    joysticktwist = twist;
    joyStickSide = side;
    encoders[0] = (leftE.getVelocity()/Constants.RPMtoPowerVal);   encoders[1] = (leftE2.getVelocity()/Constants.RPMtoPowerVal);
    encoders[2] = (rightE.getVelocity()/Constants.RPMtoPowerVal);  encoders[3] = (rightE2.getVelocity()/Constants.RPMtoPowerVal);

    if(mecBool){ // checking wether to activate strafing
       // Compare values with respective parts
       //TODO this
      left1.set( strafingSystem(forward, multiplyer, twist,
        side,
         1,
          "L1", left1.get()));
      left2.set( strafingSystem(forward, multiplyer, twist,
        side,
         1,
          "L2", left2.get()));
      right1.set( strafingSystem(forward, multiplyer, twist,
        side,
         1,
          "R1", right1.get()));
      right2.set( strafingSystem(forward, multiplyer, twist,
        side,
         1,
          "R2", right2.get()));


    }else{
    // setting motors
      // double coffeeMaker = motor(forward, multiplyer, twist, side, 1, "Brewing Coffee"); hehe

      left1.set( motor(forward, multiplyer, twist, 
            // side,
              0.0,
                        1, "L1")     );
      left2.set( motor(forward, multiplyer, twist, 
            // sideLeft2,
              0.0, 
                        1, "L2")     );
      right1.set(motor(forward, multiplyer, twist, 
            // sideRight1,
              0.0,
                        1, "R1") * -1);
      right2.set(motor(forward, multiplyer, twist,
            // sideRight2, 
              0.0, 
                        1, "R2") * -1);
    }
  }
}
  public static double motor(double forward, double multiplyer, double twist, double side, double jank,String motor){
    if      (motor == "L1"){return ((((forward / 1.4) * multiplyer) + twist / 1.6 + side ) / 2) * jank;}
    else if (motor == "L2"){return ((((forward / 1.4) * multiplyer) + twist / 1.6 - side ) / 2) * jank;}
    else if (motor == "R1"){return ((((forward / 1.4) * multiplyer) - twist / 1.6 - side ) / 2) * jank;}
    else if (motor == "R2"){return ((((forward / 1.4) * multiplyer) - twist / 1.6 + side ) / 2) * jank;}
    else    {DriverStation.reportError("418 im a teapot", true); return 0;}
  }
  /** Uses strafing methods to controll the robot in that way.
   * This is horribly jank.
   * @return a Double[] of all values in the format <L1,R1,L2,R2>
   */
  public static double strafingSystem(
      double forward, 
      double multiplyer, 
      double twist, 
      double sideJoystick, 
      double jank, 
      String Select,
      double prevOutput){

    double encoder = 0.0;
    double factor = 1;

    if      (Select == "R1"){factor = -1;}
    else if (Select == "L2"){factor = -1;}
    else if (Select == "L1"){factor = 1;}
    else if (Select == "R2"){factor = 1;}
    double target = sideJoystick*Constants.RPMtoPowerVal*factor;

    if      (Select == "R1"){encoder = rightE.getVelocity();}
    else if (Select == "L2"){encoder = leftE2.getVelocity();}
    else if (Select == "L1"){encoder = leftE.getVelocity();}
    else if (Select == "R2"){encoder = rightE2.getVelocity();}
    
    if      (encoder > target){prevOutput = prevOutput - 0.001;}
    else if (encoder < target){prevOutput = prevOutput + 0.001;}
    else    {} // Do not change output
    return prevOutput;
  }
  



  /**
   * @return the angle the gyro is facing from -180 to 180 degrees.
   */
  public double ReportAngle() {return navx.getYaw();}
  public double ReportLeftPosition() {return leftE.getPosition();}
  public double ReportRightPosition() {return rightE.getPosition();}

  public void SetSidePosition(double target, boolean right){
    locater.setTarget(target);
    locater.setTolerance(0.5);

    if(right) {
      locater.setInput(rightE.getPosition());
      r.set(locater.calculate(1, -1));
    }
    else {
      locater.setInput(leftE.getPosition());
      l.set(locater.calculate(1, -1));
    }
  }

  public Boolean SideOnTarget(double target, Boolean right){
    if(right){
      return Math.abs(rightE.getPosition() - target) < 1;
    }
    else {
      return Math.abs(leftE.getPosition() - target) < 1;
    }
  }

  public Double grabDTJoyStickForward(){return joystickforward;}
  public Double grabDTJoyStickTwist(){return joysticktwist;}
  public Double grabDTJoyStickSide(){return joyStickSide;}

  public static Double grabLeft1(){return left1.get();}
  public static Double grabLeft2(){return left2.get();}
  public static Double grabRight1(){return right1.get();}
  public static Double grabRight2(){return right2.get();}

  public static void setLeft1(Double val){left1.set(val); allow = false;}
  public static void setLeft2(Double val){left2.set(val); allow = false;}
  public static void setRight1(Double val){right1.set(val); allow = false;}
  public static void setRight2(Double val){right2.set(val); allow = false;}

  public void resetPerms(){allow = true;}

}