package frc.robot;


import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

//VictorSPX import stopped working again and I don't know why this time
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;


/**
 * Tank drive and main robot code
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_robotDrive;
  private Joystick stick;

  final int LEFT_BUMPER = 5;
  final int RIGHT_BUMPER = 6;
  
  
  VictorSPX winchAscender = new VictorSPX(5);


  private final CANSparkMax m_frontLeftMotor = new CANSparkMax(1, MotorType.kBrushed);
  private final CANSparkMax m_rearLeftMotor = new CANSparkMax(2, MotorType.kBrushed);
  private final CANSparkMax m_frontRightMotor = new CANSparkMax(3, MotorType.kBrushed);
  private final CANSparkMax m_rearRightMotor = new CANSparkMax(4, MotorType.kBrushed);
 // PhotonCamera camera = new PhotonCamera("null"); // necesitamos una cámara
 
  // distance the robot wants to stay from an object
  // (one meter)
 // Ultrasonic info: https://docs.wpilib.org/en/stable/docs/software/hardware-apis/sensors/ultrasonics-software.html#ultrasonics-software
  static final double kHoldDistanceMillimeters = 1.0e3;
  
  Ultrasonic m_rangeFinder = new Ultrasonic(1, 2);
  double distanceMillimeters = m_rangeFinder.getRangeMM();
  final int ultrasonicPingPort = 0;
  final int ultrasonicEchoPort = 1;
  // Ultrasonic sensors tend to be quite noisy and susceptible to sudden outliers,
  // so measurements are filtered with a 5-sample median filter
  private final MedianFilter m_filter = new MedianFilter(5);

  public LED led;

  private final Encoder encoder = new Encoder(0, 1);

 //THIS line breaks our code:
 // private final Ultrasonic m_ultrasonic = new Ultrasonic(ultrasonicPingPort, ultrasonicEchoPort);
 //Maybe it'll work better after we actually have an ultrasonic sensor

  @Override
  public void robotInit() {
    led = new LED();

    m_frontLeftMotor.follow(m_rearLeftMotor);
    m_frontRightMotor.follow(m_rearRightMotor);
    
    SendableRegistry.addChild(m_robotDrive, m_rearLeftMotor);
    SendableRegistry.addChild(m_robotDrive, m_rearRightMotor);

    m_robotDrive = new DifferentialDrive(m_rearLeftMotor, m_rearRightMotor);
    stick = new Joystick(0);
  }
  @Override
  public void autonomousInit() {
    //What's this?
    // TODO Auto-generated method stub 
    super.autonomousInit();
    stick.setXChannel(1);
    stick.setYChannel(5);

    m_frontRightMotor.setInverted(true);
    m_rearRightMotor.setInverted(true);

    encoder.reset();
    encoder.setDistancePerPulse(1./256.); //change this value to match whatever distance we want (256 pulse/rotation in SECONDS currently)
  }

  @Override
  public void teleopPeriodic() {
    led.LEDPeriodic();
    m_robotDrive.tankDrive(stick.getRawAxis(1), stick.getRawAxis(5));

    winchAscender.set(VictorSPXControlMode.PercentOutput,stick.getRawAxis(RIGHT_BUMPER-LEFT_BUMPER));

    //Turn on the face
    // LED.LEDInit();
  }
  public void autonomousPeriodic() {
    //Example code.  We'll probably want while !aprilTagSeen spin left and then follow it
    // Drives forward at half speed until the robot has moved 5 feet, then stops:
    if(encoder.getDistance() < 5) {
      m_robotDrive.tankDrive(0.5, 0.5);
  } else {
      m_robotDrive.tankDrive(0, 0);
  }
  }
}
