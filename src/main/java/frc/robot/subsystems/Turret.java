/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Libraries.PIDController;
import edu.wpi.first.wpilibj.SPI;


public class Turret extends SubsystemBase {


  private PIDController horizontal, vertical, visual, goToH, goToV;
  private WPI_TalonSRX turretmotor, shotangler;
  private double absolutePosition, absolutePositionY, output,outputV, wantedpulses, ballexity, P;
  private double lastknownpixels = 1;

  private Boolean Direct = false;

  private NetworkTable Limelight = NetworkTableInstance.getDefault().getTable("limelight");
  private NetworkTableEntry tx = Limelight.getEntry("tx");
  private NetworkTableEntry ty = Limelight.getEntry("ty");
  private NetworkTableEntry tv = Limelight.getEntry("tv");
  private NetworkTableEntry ts = Limelight.getEntry("ts");
  private NetworkTableEntry Long = Limelight.getEntry("tlong");
  private NetworkTableEntry LedMode = Limelight.getEntry("ledMode");
  private NetworkTableEntry CamMode = Limelight.getEntry("camMode");

  private double turretMotorX = 0;
  private double turretMotorY = 0;

  // horizontal = new PIDController(0.13, 0.5, 0, 0);

  // visual = new PIDController(0.03, 0, 0.008, 0);
  // //.1, 0, .0075 is the current
  // vertical = new PIDController(0.11, 0.04, .01, 0);
  private ShuffleboardTab PIDtuner = Shuffleboard.getTab("PIDTuning");

  private NetworkTableEntry hP = PIDtuner.add("HP", 0.13).getEntry();
  private NetworkTableEntry hI = PIDtuner.add("HI", 0.50).getEntry();
  private NetworkTableEntry hD = PIDtuner.add("HD", 0.00).getEntry();

  private NetworkTableEntry vP = PIDtuner.add("VP", 0.11).getEntry();
  private NetworkTableEntry vI = PIDtuner.add("VI", 0.04).getEntry();
  private NetworkTableEntry vD = PIDtuner.add("VD", 0.01).getEntry();

  ShuffleboardTab PIDTuning = Shuffleboard.getTab("PIDTuning");
  AHRS navx;
  double outputGOTO;
  

  /**
   * Creates a new Turret.
   */
  public Turret() {

    
    try {
      navx = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantating navX-MXP: " + ex.getMessage(), true);
    }

    navx.reset();

    turretmotor = new WPI_TalonSRX(Constants.TurretMotorPort);

    // configure encoder
    turretmotor.configFactoryDefault();

    turretmotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx,
        Constants.kTimeoutMs);
    turretmotor.setSensorPhase(true);
    turretmotor.configFeedbackNotContinuous(true, Constants.kTimeoutMs);
    turretmotor.setInverted(true);

    /* Config the peak and nominal outputs, 12V means full */
    turretmotor.configNominalOutputForward(0, Constants.kTimeoutMs);
    turretmotor.configNominalOutputReverse(0, Constants.kTimeoutMs);
    turretmotor.configPeakOutputForward(1, Constants.kTimeoutMs);
    turretmotor.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    /**
     * Config the allowable closed-loop error, Closed-Loop output will be neutral
     * within this range. See Table in Section 17.2.1 for native units per rotation.
     */
    turretmotor.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    /* Config Position Closed Loop gains in slot0, tsypically kF stays zero. */
    // filler arc
    turretmotor.config_kF(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
    turretmotor.config_kP(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
    turretmotor.config_kI(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
    turretmotor.config_kD(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);

    goToH = new PIDController(0.13, 0.5, 0.3, 0);
    goToV = new PIDController(0.11, 0.4, 0.01, 0);

    /**
     * Grab the 360 degree position of the MagEncoder's absolute position, and
     * intitally set the relative sensor to match.
     */

    shotangler = new WPI_TalonSRX(Constants.ShootAngleMotorPort);

    shotangler.setInverted(true);

    horizontal = new PIDController(0.4, 0, 0.1, 0);

    visual = new PIDController(0.09, 0.0, 0.05, 0);
    // .1, 0, .0075 is the current
    vertical = new PIDController(0.3, 0.05, .03, 0);

  }

  @Override
  public void periodic() {

    /**
     * Tuning PID through the shuffleboard
     */

    SmartDashboard.putNumber("X Offset", tx.getDouble(0.0));
    SmartDashboard.putNumber("Y Offset", YGet());
    SmartDashboard.putBoolean("Valid Target", IsOnTarget());
    SmartDashboard.putNumber("Target Area", Math.abs(TargetArea()));
    SmartDashboard.putNumber("skew", ts.getDouble(0));
    SmartDashboard.putNumber("Horizontal PID Output", visual.calculate(1, -1));
    SmartDashboard.putNumber("Virtical PID Output", vertical.calculate(1, -1));
    SmartDashboard.putNumber("Distance", Distance() * 3.28084);

    SmartDashboard.putNumber("TurretX", TurretPosition());
    SmartDashboard.putNumber("TurretY", TurretPositionY());

    SmartDashboard.putNumber("outputT", outputGOTO);
    // private ShuffleboardTab PIDtuner = Shuffleboard.getTab("PIDTuning");
    // private NetworkTableEntry hP = PIDtuner.add("HP", 0.13).getEntry();
    // private NetworkTableEntry hI = PIDtuner.add("HI", 0.5).getEntry();
    // private NetworkTableEntry hD = PIDtuner.add("HD", 0).getEntry();

    // private NetworkTableEntry vP = PIDtuner.add("VP", 0.11).getEntry();
    // private NetworkTableEntry vI = PIDtuner.add("VI", 0.04).getEntry();
    // private NetworkTableEntry vD = PIDtuner.add("VD", 0.01).getEntry();

    // Now to set values to the PID system
    // horizontal.editPIDVals(hP.getDouble(0.13), hI.getDouble(0.5), hD.getDouble(0.0), 0.0);
    // vertical.editPIDVals(vP.getDouble(0.11), vI.getDouble(0.04), vD.getDouble(0.01), 0.0);

    // PIDTuning.SimpleWidget("hP-get", hP.getDouble(0.13));

    // Printing encoder values
    absolutePosition = turretmotor.getSensorCollection().getPulseWidthPosition();
    absolutePositionY= shotangler.getSensorCollection().getPulseWidthPosition();

    SmartDashboard.putNumber("AbsoluteUnit", TurretPosition());
    SmartDashboard.putNumber("Wanted Pulses", wantedpulses);

    SmartDashboard.putBoolean("Close",

        Math.abs(tx.getDouble(0.0)) < 2.5 && tv.getDouble(0) == 1 && Math.abs(TargetArea()) > .254);

    SmartDashboard.putBoolean("Direct",

        Math.abs(tx.getDouble(0.0)) < .5 && tv.getDouble(0) == 1 && Math.abs(TargetArea()) > .635);

    // This method will be called once per scheduler run
  }

  /**
   * we needed to make sure the light turns off cause it's illegal
   */
  public void TurnOffLight() {
    LedMode.setDouble(1);
  }

  /**
   * if you turn it off, you gotta turn it on again.
   */
  public void TurnOnLight() {
    LedMode.setDouble(3);
  }

  public void EnableRegularStream() {
    CamMode.setDouble(1);
  }

  public void EnterVisionTargetingMode() {
    CamMode.setDouble(0);
  }

  /**
   * @return the angle offset of the limelight's y angle.
   */
  public double YGet() {
    return ty.getDouble(0.0);
  }

  public double XGet() {
    return tx.getDouble(0.0);
  }

  /**
   * Triggers the turret to aim using the camera values
   */
  public void UseCameraEyes(double NeededOffsety, double NeededOffsetx) {

    if (NeededOffsetx < 90 && NeededOffsetx > -90) {
      visual.setTarget(NeededOffsetx);
    } else {
      visual.setTarget(0);
    }
    visual.setInput(tx.getDouble(0.0));
    visual.setTolerance(0);

    if ((visual.calculate(1, -1) <= 0 && TurretPosition() >= Constants.UTurretLimit)
        || (visual.calculate(1, -1) >= 0 && TurretPosition() <= Constants.LTurretLimit)) {

      turretMotorX = 0;
      turretmotor.set(0);
    } else {
      turretmotor.set(visual.calculate(1, -1));
    }
    if (NeededOffsety < 90 && NeededOffsety > -90) {
      vertical.setTarget(NeededOffsety); // Offset for turret Y
    } else {
      vertical.setTarget(0);
    }
    vertical.setInput(ty.getDouble(vertical.getTarget()));
    vertical.setTolerance(0.05);

    turretMotorY = vertical.calculate(1, -1);
    shotangler.set(vertical.calculate(1, -1));
  }

  /**
   * This method will make the robot try its hardest to stay looking at the darn
   * wall
   * 
   * @param GyroAngle          the angle in degrees of the gyro from -180 to 180.
   * @param ManualControlYaw   the controller input to control the turretmotor
   *                           manually
   * @param ManualControlPitch the controller input to control the hood angle
   *                           manually
   */
  public void StayForward(double GyroAngle, Double ManualControlYaw, double ManualControlPitch) {

    wantedpulses = ((navx.getAngle()) / Constants.AnglePerPulse);

    if (wantedpulses <= Constants.LTurretLimit) {
      horizontal.setTarget(Constants.LTurretLimit);
    } else if (wantedpulses >= Constants.UTurretLimit) {
      horizontal.setTarget(Constants.UTurretLimit);
    } else {
      horizontal.setTarget(wantedpulses);
    }

    horizontal.setInput(TurretPosition());
    horizontal.setTolerance(30);

    output = horizontal.calculate(1, -1);

    turretMotorX = output;
    // turretMotorY = ManualControlPitch;
    turretmotor.set(output);
    // shotangler.set(ManualControlPitch);
  }

  /**
   * Sets the motors to use the joystick values
   */
  public void ManuallyAim(Double j_z, Double j_y) {
    if ((TurretPosition() >= Constants.UTurretLimit && j_z <= 0)
        || (TurretPosition() <= Constants.LTurretLimit && j_z >= 0)) {

      turretmotor.set(0);
    } else {
      turretMotorX = j_z / 1.5;
      turretmotor.set(j_z / 1.5);
    }
    turretMotorY = j_y;
    shotangler.set(j_y);

  }

  /**
   * Stops all turret motors
   */
  public void stop() {
    turretMotorX = 0;
    turretMotorY = 0;
    turretmotor.set(0);
    shotangler.set(0);
  }

  public double TurretPosition() {
    return absolutePosition % 4096;
  }
  public double TurretPositionY(){
    return absolutePositionY% 4096;
  }

  /**
   * 
   * @param offset the angle offset of the ball angle in radians
   * 
   */
  public void SetBallVelocityComponents(double offset) {
    ballexity = Constants.InitialBallMagnitude * Math.sin(Math.atan(54.25/Distance()) + offset);
  }

  public boolean IsOnTarget() {
    return tv.getDouble(0) == 1;
  }

  public boolean Direct() {
    Direct = Math.abs(tx.getDouble(0.0)) < .5 
   //   && Math.abs(ty.getDouble(0.0)) < 2 
      && tv.getDouble(0) == 1 
      && Math.abs(TargetArea()) > .635;
    return Direct;
  }

  public double TargetArea() {
    double radiansoff = Math.toRadians(ts.getDouble(0.0));
    return Constants.xlength * Math.cos(radiansoff);
  }
  public double VisibleToRealRatio() {
    return TargetArea()/Constants.xlength; 
  }

   public double VelocityDown(double time) {
     double fear = (.603442*Math.pow(Math.E, time/.0307565) - 1)/( 1 +  Math.pow(Math.E , time/0303565));
     return fear;
   }
   public double VelocityUp(double time, double angle) {
     double C = -.061513*Math.atan(1.65716 * ballexity);
     double anger = Math.tan(time + C / .061513) / 1.65766;
     return anger;
   }

   public double VelocityX(double time, double angle) {
     P = Constants.BallMass/(Constants.InitialBallMagnitude*Math.cos(angle));
     double hate = Constants.BallMass/(26.94*time + P);
     return hate;
   }


   public double Distance() {
     double suffering = .78 *(((Constants.xlength * Constants.res/2)/(Math.tan(.25*Constants.LLhFOV) * Long.getDouble(1))));
     double horizontalSuffering = Math.sqrt(Math.pow(suffering, 2) - 1.8987462025);
     double ConvertedSuffering = -.008663*Math.pow(horizontalSuffering, 2) + (1.447* horizontalSuffering) - 5.886;
     
     return ConvertedSuffering;
  
    }

   public double SolveForTime(double angle) {
    double time = (Math.pow(Math.E, 26.94*Distance()/Constants.BallMass) - (Constants.BallMass/(Constants.InitialBallMagnitude*Math.cos(angle))))/26.94;
    return time;
   }

   /**
    * 
    * @return the last known pixel count for tlong instead of returning a default value.
    */
   public double memorizepixels() {
     if (Long.getDouble(0.0) != 0){
       lastknownpixels = Long.getDouble(0.0);
     }
     return lastknownpixels;
   }

   public void goTo(Double GyroAngle, Double xEnc,int yEnc){ // TODO this

    if (TurretPosition() > xEnc){ // set -
      outputGOTO = -0.3;
    }else if(TurretPosition() < xEnc){ // set +
      outputGOTO = 0.3;}
    else{outputGOTO = 0;}

    // No encoder is present on virtical axis
    if ((TurretPosition() >= Constants.UTurretLimit && output <= 0)
    || (TurretPosition() <= Constants.LTurretLimit && output >= 0)) {

      turretmotor.set(0);} else{turretmotor.set(outputGOTO);}

    // turretMotorY = ManualControlPitch;
    // turretmotor.set(output);
   }
   

   public Double grabTurretX(){return turretMotorX;}
   public Double grabTurretY(){return turretMotorY;}
}