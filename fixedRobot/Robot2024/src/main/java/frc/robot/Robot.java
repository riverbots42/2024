package frc.robot;


import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax; If we ever want to use PWM again
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;



/**
 * Tank drive and main robot code
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_robotDrive;
  private Joystick m_leftStick;
  private Joystick m_rightStick;

  //https://codedocs.revrobotics.com/java/com/revrobotics/package-summary.html
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

 //THIS line breaks our code:
 // private final Ultrasonic m_ultrasonic = new Ultrasonic(ultrasonicPingPort, ultrasonicEchoPort);
 //Maybe it'll work better after we actually have an ultrasonic sensor

  @Override
  public void robotInit() {
    //Turn on the face
    LED.LEDInit();
    System.out.println("RobotInit");


    // addFollower merges left motors and right motors.
    //temporarly gone to get to work? 
    //m_frontLeftMotor.addFollower(m_rearLeftMotor);
    //m_frontRightMotor.addFollower(m_rearRightMotor);
    
    SendableRegistry.addChild(m_robotDrive, m_rearLeftMotor);
    SendableRegistry.addChild(m_robotDrive, m_rearRightMotor);

    
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_frontRightMotor.setInverted(true);
    m_rearRightMotor.setInverted(true);    
    m_robotDrive = new DifferentialDrive(m_frontLeftMotor, m_frontRightMotor);
    m_leftStick = new Joystick(0);
    m_rightStick = new Joystick(0);
  }
  @Override
  public void autonomousInit() {
    //What's this?
    // TODO Auto-generated method stub 
    super.autonomousInit();

    m_frontRightMotor.setInverted(true);
    m_rearRightMotor.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {

    m_robotDrive.tankDrive(-m_leftStick.getY(), -m_rightStick.getY());
  }
  public void autonomousPeriodic() {
    // Do something
  }
}
