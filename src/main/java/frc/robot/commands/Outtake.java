/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.FrontIntake;
import frc.robot.subsystems.InnerIntake;

public class Outtake extends CommandBase {
  private FrontIntake frontIntake;
  private InnerIntake innerIntake;

  private boolean updated = false;
  /**
   * Creates a new Intake.
   */
  public Outtake(FrontIntake intake, InnerIntake inner) {
    frontIntake = intake;
    innerIntake = inner;
    addRequirements(intake);
    addRequirements(inner);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    frontIntake.frontOuttake();
    innerIntake.innerOuttake();

    if(innerIntake.FrontSensorActive() != updated){
      innerIntake.RemoveBall();
      updated = innerIntake.FrontSensorActive();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    frontIntake.stopIntake();
    innerIntake.Stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
