/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final double BallMass = 0.136078; 

    public static final double DragCoefficient = 26.94;

    public static final double HorizontalFOV = Math.toRadians(54);

    public static final int TurretEncoderResolution = 4096;
    public static final int DriveEncoderResolution = 42;

    public static final int UTurretLimit = 3000;
    public static final int LTurretLimit = 1600;
    
    public static final double AnglePerPulse = 360/TurretEncoderResolution;

    public static final double scaleFactor = 1/(5./1024.);
    
    public static final int FrontBallSensorAnalogPort = 1;
    public static final int TopBallSensorAnalogPort = 3;

    public static final double xlength = 0.9906;

    public static final double GoodAngley = 0;
    public static final double GoodAnglex = 0;
    public static final double StartYAngle = 10.17;
    public static final double StartXAngle = 0;
    public static final double TrenchBallYAngle = 3.23;
    public static final double TrenchBallXAngle = -0.06;

    public static final double LLhFOV = Math.toRadians(54);

    public static final double smallLength = 0.762;


    public static final double InitialBallMagnitude = 15.0;
    
    public static final double res = 320;

    public static final int AbsoluteEncoderPort = 2;

    public static final int LeftDrive1MotorPort = 1;
    public static final int leftDrive2MotorPort = 2;

    public static final int RightDrive1MotorPort = 3;
    public static final int RightDrive2MotorPort = 4;
    
    public static final int IntakeMotorPort = 5;
    public static final int Conveyor1MotorPort = 6;
    public static final int Conveyor2MotorPort = 7;
    
    public static final int TurretMotorPort = 8;
    
    public static final int Shoot1MotorPort = 9;
    public static final int Shoot2MotorPort = 10;
    
    public static final int ShootAngleMotorPort = 11;
    
    public static final int WOFMotorPort = 12;
    
    public static final int LClimbMotorPort = 13;
    public static final int RClimbMotorPort = 14;

    public static final int kTimeoutMs = 30;
    public static final int kPIDLoopIdx = 0;

    public static final double DesiredFlyWheelSpeed = 11500;

    //Time from back sensor to the top/vice versa
    public static final double BallProccessTime = 4;

    public static final double RPMtoPowerVal = 5676;


}
