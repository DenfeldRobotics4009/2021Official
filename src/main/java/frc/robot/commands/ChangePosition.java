/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class ChangePosition extends CommandBase {
  private double startingleft, startingright, initiall, initialr;
  private double r, l;
  
  private DriveTrain train;
  
  /**
   * Creates a new ChangePosition.
   */
  public ChangePosition(DriveTrain drive, double rightchange, double leftchange, double startl, double startr) {
    train = drive;
    startingleft = startl;
    startingright = startr;
    r = rightchange;
    l = leftchange;
    addRequirements(drive);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initiall = startingleft;
    initialr = startingright;

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    train.SetSidePosition((initiall + l), false);
    train.SetSidePosition((initialr + r), true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    train.ArcadeDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
    //return train.SideOnTarget(initiall + l, false) && train.SideOnTarget(initialr + r, true);
  }
}
