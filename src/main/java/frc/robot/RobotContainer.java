/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.BackMDrive;
import frc.robot.commands.CalibrateBalls;
import frc.robot.commands.CameraTarget;
import frc.robot.commands.CancelShot;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.FinalShoot;
import frc.robot.commands.GoToPosA;
import frc.robot.commands.InnerProccess;
import frc.robot.commands.Intake;
import frc.robot.commands.Ldown;
import frc.robot.commands.Lup;
import frc.robot.commands.ManualAim;
import frc.robot.commands.ManualDrive;
import frc.robot.commands.MecanumMDrive;
import frc.robot.commands.Outtake;
import frc.robot.commands.PrepareShoot;
import frc.robot.commands.Rdown;
import frc.robot.commands.RecordMacroToggle;
import frc.robot.commands.Rup;
import frc.robot.commands.SpinManual;
import frc.robot.commands.UpperProccess;
import frc.robot.commands.resetDriverPerms;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Climber2;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.FrontIntake;
import frc.robot.subsystems.InnerIntake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.WheelSpinner;
import frc.robot.subsystems.PlaybackController;


/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  SendableChooser<Integer> autoChooser = new SendableChooser<>();

  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final DriveTrain train = new DriveTrain();
  private final Turret turret = new Turret();
  private final Shooter s_shooter = new Shooter();
  private final Climber climber = new Climber();
  private final Climber2 climber2 = new Climber2();
  private final InnerIntake i_Intake = new InnerIntake();
  private final WheelSpinner wof = new WheelSpinner();
  private final FrontIntake f_Intake = new FrontIntake();
  private final PlaybackController pController = new PlaybackController();

  private final static Joystick driver = new Joystick(0);
  private final static Joystick operator = new Joystick(1);

  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  private final SequentialCommandGroup Shoot3Balls = new SequentialCommandGroup(
      new WaitUntilCommand(s_shooter::FastEnough).andThen(new FinalShoot(i_Intake).withInterrupt(s_shooter::TooSlow)
          .andThen(new WaitUntilCommand(s_shooter::FastEnough)).andThen(new FinalShoot(i_Intake))
          .andThen(new WaitUntilCommand(s_shooter::FastEnough)).andThen(new FinalShoot(i_Intake))
          .deadlineWith(new WaitUntilCommand(s_shooter::TooSlow))));

  private final ParallelCommandGroup SitAndShoot3Balls = new ParallelCommandGroup(
      new CameraTarget(turret, () -> turret.YGet(), () -> turret.XGet()).alongWith(new PrepareShoot(s_shooter),
          Shoot3Balls));

  private Trigger manual = new Trigger(this::Controlled);
  private Trigger ValidTarget = new Trigger(turret::IsOnTarget);

  private Trigger frontball = new Trigger(i_Intake::FrontSensorActive);
  private Trigger topball = new Trigger(i_Intake::TopSensorActive);

  private Trigger Phase1 = new Trigger(i_Intake::GetInnerIntake);
  private Trigger StoppedEarly = new Trigger(i_Intake::InterruptedShot);

  private Trigger UpToSpeed = new Trigger(s_shooter::FastEnough);



  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    autoChooser.addOption("Sit And Shoot", 1);
    autoChooser.setDefaultOption("Shoot, drive", 2);
    autoChooser.addOption("Wait 2 Secs, Shoot, then Drive", 3);
    // autoChooser.addOption("Test Turn", 4);
    SmartDashboard.putData("Autonomous", autoChooser);

    train.setDefaultCommand(
        new MecanumMDrive(
          train, () -> driver.getX(),
           () -> driver.getZ(),
            () -> driver.getY(),
             () -> driver.getTrigger()
    ));
    
      
    // Configure the button bindings
    configureButtonBindings();

  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    final JoystickButton d1 = new JoystickButton(driver, 1);
    final JoystickButton d2 = new JoystickButton(driver, 2);
    final JoystickButton d3 = new JoystickButton(driver, 3);
    final JoystickButton d4 = new JoystickButton(driver, 4);
    final JoystickButton d5 = new JoystickButton(driver, 5);
    final JoystickButton d6 = new JoystickButton(driver, 6);
    final JoystickButton d7 = new JoystickButton(driver, 7);
    final JoystickButton d8 = new JoystickButton(driver, 8);
    final JoystickButton d9 = new JoystickButton(driver, 9);
    final JoystickButton d10 = new JoystickButton(driver, 10);
    final JoystickButton d11 = new JoystickButton(driver, 11);
    final JoystickButton d12 = new JoystickButton(driver, 12);

    final JoystickButton o1 = new JoystickButton(operator, 1);
    final JoystickButton o2 = new JoystickButton(operator, 2);
    final JoystickButton o3 = new JoystickButton(operator, 3);
    final JoystickButton o4 = new JoystickButton(operator, 4);
    final JoystickButton o5 = new JoystickButton(operator, 5);
    final JoystickButton o6 = new JoystickButton(operator, 6);
    final JoystickButton o7 = new JoystickButton(operator, 7);
    final JoystickButton o8 = new JoystickButton(operator, 8);
    final JoystickButton o9 = new JoystickButton(operator, 9);
    final JoystickButton o10 = new JoystickButton(operator, 10);
    final JoystickButton o11 = new JoystickButton(operator, 11);
    final JoystickButton o12 = new JoystickButton(operator, 12);

      
    final POVButton up = new POVButton(operator, 0);
    final POVButton upR = new POVButton(operator, 45);
    final POVButton upRight = new POVButton(operator, 90);
    final POVButton upL = new POVButton(operator, 315);
    final POVButton upLeft = new POVButton(operator, 270);
    final POVButton down = new POVButton(operator, 180);
    final POVButton downLeft = new POVButton(operator, 225);
    final POVButton downRight = new POVButton(operator, 135);


    // Intake Button

    manual.whileActiveContinuous(new ManualAim(turret, () -> operator.getZ(), () -> operator.getY()));

    manual.negate().and(o3.negate()).and(o8).whileActiveContinuous(new ManualAim(turret, () -> operator.getZ()*.45, () -> operator.getY()*.60), true);
    manual.negate().and(o3).and(o8.negate()).whileActiveContinuous(new CameraTarget(turret, () -> 80,() -> 100).asProxy());



    o1.whileActiveContinuous(new PrepareShoot(s_shooter));
    o1.and(UpToSpeed).whileActiveContinuous(new FinalShoot(i_Intake));

    o3.whileActiveContinuous(new CameraTarget(turret, () -> Constants.GoodAngley, () -> Constants.GoodAnglex));

    o5.whileActiveContinuous(new CameraTarget(turret,  () -> turret.YGet(), () -> Constants.GoodAnglex));

    (o2.or(d2).and(o12.negate())).and(frontball).whileActiveOnce(new InnerProccess(i_Intake), true);
    d10.whileActiveOnce(new UpperProccess(i_Intake), true);

    o2.or(d2).or(d10).cancelWhenActive(new CancelShot(i_Intake));

    d2.or(d10).whileActiveOnce(new Intake(f_Intake));

    o2.whenHeld(new Outtake(f_Intake, i_Intake), false);

    
    o5.whileActiveContinuous(new Lup(climber, () -> operator.getThrottle()));
    o6.whileActiveContinuous(new Rup(climber2, () -> operator.getThrottle()));
    up.or(upR).or(upL).whileActiveContinuous(new Lup(climber, () -> operator.getThrottle()).alongWith(new Rup(climber2, () -> operator.getThrottle())));

    // o3.whileActiveContinuous(new Ldown(climber, () -> operator.getThrottle()));
    // o4.whileActiveContinuous(new Rdown(climber2, () -> operator.getThrottle()));
    down.or(downRight).or(downLeft).whileActiveContinuous(new Ldown(climber, () -> operator.getThrottle()).alongWith(new Rdown(climber2, () -> operator.getThrottle())));

    o11.whenHeld(new SpinManual(wof, () -> operator.getThrottle())); 
    
    //o12.whenPressed(new CalibrateBalls(i_Intake, 0)); //Will Calibarte the value of balls

    d11.whenPressed(new RecordMacroToggle(pController));

    
    d12.whenPressed(new resetDriverPerms(train));

    // o9.whenActive(new GoToPosA(turret));

    d12.toggleWhenActive(new BackMDrive(train,
    () -> driver.getX(),
      () -> driver.getZ(),
      () -> driver.getY(),
        () -> driver.getTrigger()));
    
    

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {

    final int select = autoChooser.getSelected();
    // An ExampleCommand will run in autonomous
    switch (select){
       
      case 1:  return SitAndShoot3Balls.withTimeout(4);
      case 2:  return SitAndShoot3Balls.withTimeout(4).andThen(new ManualDrive(train, () -> -.7, () -> 0, () -> false).withTimeout(0.5));
      case 3:  return new WaitCommand(2).andThen(SitAndShoot3Balls.withTimeout(4).andThen(new ManualDrive(train, () -> -.7, () -> 0, () -> false).withTimeout(0.5)));
     // case 4:  return new ChangePosition(train, 30, -30, 0, 0);
      default: return SitAndShoot3Balls.withTimeout(4).andThen(new ManualDrive(train, () -> -.7, () -> 0, () -> false).withTimeout(0.5));

    }
  }
  public Command CalibrateBall() {
    return new CalibrateBalls(i_Intake, 3);
  }
  public Boolean Controlled() {
     return (Math.abs(operator.getY()) >= .1) || Math.abs(operator.getZ()) >= .3;
  }

  public static double Joystickdy(){return driver.getY();}
  public static double Joystickdz(){return driver.getZ();}
  public static double Joystickdx(){return driver.getX();}
  public static double Joystickoy(){return operator.getY();}
  public static double Joystickoz(){return operator.getZ();}
  public static double Joystickox(){return operator.getX();}

  public static boolean Joystickd1(){return driver.getTrigger();}


}