// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

/**
 * This is a demo program showing the use of the DifferentialDrive class, specifically it contains
 * the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_robotDrive;
  private Joystick m_leftStick;
  private Joystick m_rightStick;

  private final PWMSparkMax m_frontLeftMotor = new PWMSparkMax(0); //channels may change
  private final PWMSparkMax m_rearLeftMotor = new PWMSparkMax(1);
  private final PWMSparkMax m_frontRightMotor = new PWMSparkMax(2);
  private final PWMSparkMax m_rearRightMotor = new PWMSparkMax(3);

  @Override
  public void robotInit() {
    // addFollower merges left motors and right motors.
    //temporarly gone to get to work? 
   // m_frontLeftMotor.addFollower(m_rearLeftMotor);
   // m_frontRightMotor.addFollower(m_rearRightMotor);

   //temp gone to get to worK?
   // SendableRegistry.addChild(m_robotDrive, m_rearLeftMotor);
    //SendableRegistry.addChild(m_robotDrive, m_rearRightMotor);
    
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_frontRightMotor.setInverted(true);
    m_rearRightMotor.setInverted(true);    
    m_robotDrive = new DifferentialDrive(m_frontLeftMotor, m_frontRightMotor);
    m_leftStick = new Joystick(1);
    m_rightStick = new Joystick(1);
  }

  @Override
  public void teleopPeriodic() {
    m_robotDrive.tankDrive(-m_leftStick.getY(), -m_rightStick.getY());
  }
}
