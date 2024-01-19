// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
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

  //Channel one works now and IDK why but at least we got the motors working
  private final PWMSparkMax m_frontLeftMotor = new PWMSparkMax(1); //channels may change
  private final PWMSparkMax m_rearLeftMotor = new PWMSparkMax(0);
  private final PWMSparkMax m_frontRightMotor = new PWMSparkMax(2);
  private final PWMSparkMax m_rearRightMotor = new PWMSparkMax(3);
 // PhotonCamera camera = new PhotonCamera("null"); // necesitamos una cámara
 
  // distance the robot wants to stay from an object
  // (one meter)
  //Ultrasonic info: https://docs.wpilib.org/en/stable/docs/software/hardware-apis/sensors/ultrasonics-software.html#ultrasonics-software
  static final double kHoldDistanceMillimeters = 1.0e3;
  
  Ultrasonic m_rangeFinder = new Ultrasonic(1, 2);
  double distanceMillimeters = m_rangeFinder.getRangeMM();
  final int ultrasonicPingPort = 0;
  final int ultrasonicEchoPort = 1;
  // Ultrasonic sensors tend to be quite noisy and susceptible to sudden outliers,
  // so measurements are filtered with a 5-sample median filter
  private final MedianFilter m_filter = new MedianFilter(5);

  private final Ultrasonic m_ultrasonic = new Ultrasonic(ultrasonicPingPort, ultrasonicEchoPort);


  @Override
  public void robotInit() {
    // addFollower merges left motors and right motors.
    //temporarly gone to get to work? 
    m_frontLeftMotor.addFollower(m_rearLeftMotor);
    m_frontRightMotor.addFollower(m_rearRightMotor);

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
