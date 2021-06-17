/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class InnerIntake extends SubsystemBase {
  private NetworkTable InnerIntake = NetworkTableInstance.getDefault().getTable("InnerIntake");

  private AnalogInput FrontSensor, TopSensor;
  private Boolean P1 = false;
  private Boolean Storage = false;
  private Boolean ShotInterrupted = false;
  private Boolean FrontUpdated, TopUpdated;

  private int BallCount = 3;

  private static WPI_TalonSRX TopMotor;

  private static WPI_TalonSRX BottomMotor;
  private SpeedControllerGroup Conveyor;

  private double conveyorValue = 0;
  private double topConveyorValue = 0;
  private double bottomConveyorValue = 0;

  /**
   * Creates a new Sensors.
   */
  public InnerIntake() {
    TopMotor = new WPI_TalonSRX(Constants.Conveyor2MotorPort);
    BottomMotor = new WPI_TalonSRX(Constants.Conveyor1MotorPort);

    TopMotor.setInverted(true);
    BottomMotor.setInverted(true);

    Conveyor = new SpeedControllerGroup(TopMotor, BottomMotor);

    FrontSensor = new AnalogInput(Constants.FrontBallSensorAnalogPort);
    TopSensor = new AnalogInput(Constants.TopBallSensorAnalogPort);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Ball Count", GetBallCount());

    SmartDashboard.putBoolean("Sensor Top", TopSensorActive());
    SmartDashboard.putBoolean("InnerIntake Active", GetInnerIntake());
    SmartDashboard.putBoolean("Sensor Front", FrontSensorActive());

    if (FrontSensorActive() != FrontUpdated) {
      if (FrontSensorActive()) {
        AddBall();
      } else {
      }
      FrontUpdated = FrontSensorActive();
    }

    if (TopSensorActive() != TopUpdated) {
      if (!TopSensorActive()) {
        RemoveBall();
      } else {
      }
      TopUpdated = TopSensorActive();
    }
    // This method will be called once per scheduler run
  }

  /**
   * Will run the inside conveyor motors of the intake
   */
  public void Start() {

    topConveyorValue = .25;
    bottomConveyorValue = 0.50;

    TopMotor.set(0.25);
    BottomMotor.set(0.50);
  }

  public void Shoot() {

    topConveyorValue = 0.35;
    bottomConveyorValue = 0.4;

    TopMotor.set(.35);
    BottomMotor.set(.4);
  }

  /**
   * Will stop the inside conveyor motors of the intake
   */
  public void Stop() {

    topConveyorValue = 0;
    bottomConveyorValue = 0;

    Conveyor.set(0);
  }

  public void setToTop() {
    
  }

  /**
   * Will reverse the intake to properly set up spacing.
   */
  public void Respace() {
    topConveyorValue = -0.3;
    bottomConveyorValue = -0.3;

    Conveyor.set(-0.3);
  }

  public void innerOuttake() {
    topConveyorValue = -0.2;
    bottomConveyorValue = -0.7;

    TopMotor.set(-0.2);
    BottomMotor.set(-0.7);
  }

  public Boolean FrontSensorActive() {
    Boolean FrontSensorActive = FrontSensor.getVoltage() <= .63;
    return FrontSensorActive;
  }

  public Boolean TopSensorActive() {
    Boolean TopSensorActive = TopSensor.getVoltage() <= .63;
    return TopSensorActive;
  }

  public void SetStoredBall(Boolean Stored) {
    Storage = Stored;
  }

  public Boolean GetStoredBall() {

    if (Storage == null) {
      return FrontSensorActive();
    } else {
      return Storage;
    }
  }

  /**
   * Phase 1 of the intake system
   * 
   * @param phase1 the first phase
   */
  public void SetInnerIntake(Boolean Inner) {
    P1 = Inner;
  }

  public Boolean GetInnerIntake() {
    return P1;
  }

  public void InterruptShot(Boolean interruption) {
    ShotInterrupted = interruption;
  }

  public Boolean InterruptedShot() {
    return ShotInterrupted;
  }

  /**
   * Used to remove documented balls from the robot, these values will be
   * displayed on the shuffleboard.
   */
  public void RemoveBall() {
    BallCount -= 1;
  }

  /**
   * Used to add documented balls from the robot, these values will be displayed
   * on the shuffleboard.
   */
  public void AddBall() {
    BallCount += 1;
  }

  public void CalibrateBall(int ball) {
    BallCount = ball;
  }

  public int GetBallCount() {
    int FinalCount = BallCount;
    return FinalCount;
  }

  public static double grabTopConveyor(){return TopMotor.get();}
  public static double grabBottomConveyor(){return BottomMotor.get();}

  public static void setTopConveyor(Double val){TopMotor.set(val);}
  public static void setBottomConveyor(Double val){BottomMotor.set(val);}
}