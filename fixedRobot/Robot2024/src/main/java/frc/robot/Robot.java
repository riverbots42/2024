package frc.robot;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

//import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax; If we ever want to use PWM again
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;



/**
 * Tank drive and main robot code
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_robotDrive;
  private DifferentialDrive autonomousDrive;

  private Joystick stick;

  final int LEFT_BUMPER = 5;
  final int RIGHT_BUMPER = 6;
  final int Y_BUTTON = 4;
  //private final DoubleSolenoid m_doubleSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 1,   2);

//  VictorSPX winchAscender = new VictorSPX(5);


  //private final CANSparkMax m_frontLeftMotor = new CANSparkMax(1, MotorType.kBrushed);
  //private final CANSparkMax m_rearLeftMotor = new CANSparkMax(2, MotorType.kBrushed);
  //private final CANSparkMax m_frontRightMotor = new CANSparkMax(3, MotorType.kBrushed);
  //private final CANSparkMax m_rearRightMotor = new CANSparkMax(4, MotorType.kBrushed);

  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  PhotonCamera camera = new PhotonCamera(inst, "HD_USB_Camera 2"); // necesitamos una cámara
  
  final double ANGULAR_P = 0.1;
  final double ANGULAR_D = 0.0;
  PIDController turnController = new PIDController(ANGULAR_P, 0, ANGULAR_D);
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
    //led = new LED();
    

// start a NT4 client
    inst.startClient4("example client");

// connect to a roboRIO with team number TEAM
    inst.setServerTeam(6845);

// starting a DS client will try to get the roboRIO address from the DS application
    inst.startDSClient(); 

// connect to a specific host/port
    //inst.setServer("host", NetworkTableInstance.kDefaultPort4);
    
    /*m_frontLeftMotor.follow(m_rearLeftMotor);
    m_frontRightMotor.follow(m_rearRightMotor);
    
    SendableRegistry.addChild(m_robotDrive, m_rearLeftMotor);
    SendableRegistry.addChild(m_robotDrive, m_rearRightMotor);

    
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
      
    m_robotDrive = new DifferentialDrive(m_rearLeftMotor, m_rearRightMotor);*/
    stick = new Joystick(0);
    
  }
  @Override
  public void autonomousInit() {
    //What's this?
    // TODO Auto-generated method stub 

    //autonomousDrive = new DifferentialDrive(leftMotor, rightMotor);
    super.autonomousInit();
    stick.setXChannel(1);
    stick.setYChannel(5);
    /* 
    m_frontRightMotor.setInverted(true);
    m_rearRightMotor.setInverted(true);*/
  }

  @Override
  public void teleopPeriodic() {
    //led.LEDPeriodic();
    //m_robotDrive.tankDrive(stick.getRawAxis(1), stick.getRawAxis(5));

   // winchAscender.set(VictorSPXControlMode.PercentOutput,stick.getRawAxis(RIGHT_BUMPER-LEFT_BUMPER));
   // When Y button pressed toggle Solenoid.
   /*
   if (stick.getRawButtonPressed(4)) {
    System.out.println("Y button Pressed: Forward");
    m_doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    
 }
 if (stick.getRawButtonPressed(3  )) {
    System.out.println("X button Pressed: Reverse");
    m_doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    
 }*/
 
    var result = camera.getLatestResult();
    boolean hasTargets = result.hasTargets();
    if(hasTargets)
    {
       
      List<PhotonTrackedTarget> targets = result.getTargets();
      PhotonTrackedTarget target = result.getBestTarget();
      double yaw = target.getYaw();
      double pitch = target.getPitch();
      double area = target.getArea();
      double skew = target.getSkew();
      int targetID = target.getFiducialId();
     
      System.out.println("\nTargetID: " + targetID + "\n");
      System.out.println("Target: ");
      System.out.println("Yaw: " + yaw);
      System.out.println("Pitch: " + pitch);
      System.out.println("Area: " + area);
      System.out.println("Skew: " + skew);

    } 
    else{
      System.out.println("No Targets");
    }
    //Turn on the face
    // LED.LEDInit();
  }
  public void autonomousPeriodic() {
   
    double forwardSpeed = 0.1;
    double rotationSpeed = 0;
    double range = 0;
    var result = camera.getLatestResult();

    if (result.hasTargets()) {
      // Calculate angular turn power
      // -1.0 required to ensure positive PID controller effort _increases_ yaw
      //rotationSpeed = -turnController.calculate(result.getBestTarget().getYaw(), 0);
      range = PhotonUtils.calculateDistanceToTargetMeters(
          0.35,
          1.5,
          Units.degreesToRadians(2.0),
          Units.degreesToRadians(result.getBestTarget().getPitch()));
    } 
    
    else {
      // If we have no targets, stay still.
    rotationSpeed = 0;
    }
    System.out.println(range);
    //System.out.println("Rotation Speed: " + rotationSpeed);
    //autonomousDrive.arcadeDrive(forwardSpeed, rotationSpeed);
  }
}
