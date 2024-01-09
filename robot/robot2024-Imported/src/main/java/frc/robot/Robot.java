// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
//import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;


/**
 * This is a demo program showing the use of the DifferentialDrive class, specifically it contains
 * the code necessary to operate a robot with tank drive. Super CChange 
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private Joystick m_leftStick;
  private Joystick m_rightStick;
  private final Joystick stick = new Joystick(0);

  //PWM channel 0 is broken on our current RoboRio.  Would not recommend trying to use it
  Spark leftMotor1 = new Spark(1);
  Spark leftMotor2 = new Spark(2);
  Spark rightMotor1 = new Spark(3);
  Spark rightMotor2 = new Spark(4);
 // MotorControllerGroup m_left = new MotorControllerGroup(leftMotor1, leftMotor2);
 // MotorControllerGroup m_right = new MotorControllerGroup(rightMotor1, rightMotor2);
 // DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    
    
  //  m_left.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {

  }
}
