// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  private final Spark m_frontLeftMotor = new Spark(0);
  private final Spark m_frontRightMotor = new Spark(1);
  private final Spark m_rearLeftMotor = new Spark(2);
  private final Spark m_rearRightMotor = new Spark(3);
  private final Joystick m_stick = new Joystick(0);
  final DifferentialDrive m_robotDrive = new DifferentialDrive(m_frontLeftMotor::set, m_frontRightMotor::set);

 /*  public Robot() { //this is fine? 
    
  }*/

  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_frontRightMotor.setInverted(true);
    m_rearRightMotor.setInverted(true);

    m_frontLeftMotor.addFollower(m_rearLeftMotor);
    m_frontRightMotor.addFollower(m_rearRightMotor);

    
    SendableRegistry.addChild(m_robotDrive, m_frontLeftMotor);
    SendableRegistry.addChild(m_robotDrive, m_frontRightMotor);
  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(-m_stick.getY(), -m_stick.getX());
  }
}
