package frc.robot;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableInstance;

//import edu.wpi.first.math.filter.MedianFilter;
//import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode; //if these imports break, delete Phoenix5.json and add the same file back (copy it first)
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;


/**
 * Tank drive and main robot code
 */
public class Robot extends TimedRobot {
  public static DifferentialDrive m_robotDrive;
  private DifferentialDrive autonomousDrive;
  private static Joystick stick;

  //Controller must be set to "X" mode rather than "D" on the back
  final int A_BUTTON = 1;
  final static int B_BUTTON = 2;
  final int X_BUTTON = 3;
  final int Y_BUTTON = 4;
  final int LEFT_BUMPER = 5;
  final int RIGHT_BUMPER = 6;

  final int FIRE_BUTTON = 10; // currently firebutton set to X (3). Set to 10 for Right Stick down


  final int LEFT_TRIGGER = 2;
  final int RIGHT_TRIGGER = 3;

  boolean toggleTurbo = false;
  boolean endMethod = false;
  boolean sucking = false;

  VictorSPX winchAscender = new VictorSPX(5);
  VictorSPX arm2 = new VictorSPX(6);
  VictorSPX arm1 = new VictorSPX(8);
  public static VictorSPX intakeSucker = new VictorSPX(7);
  public static TalonSRX launcherLeft = new TalonSRX(9);
  public static TalonSRX launcherRight = new TalonSRX(10);

  static int tick=0;
  static int startTick=0;
  int autoTick=0;
  int autoTickEnd=0;


  private final CANSparkMax m_frontLeftMotor = new CANSparkMax(1, MotorType.kBrushed);
  private final CANSparkMax m_rearLeftMotor = new CANSparkMax(2, MotorType.kBrushed);
  private final CANSparkMax m_frontRightMotor = new CANSparkMax(3, MotorType.kBrushed);
  private final CANSparkMax m_rearRightMotor = new CANSparkMax(4, MotorType.kBrushed);

 
  private int targetSwitch;

  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  PhotonCamera camera = new PhotonCamera(inst, "HD_USB_Camera 2"); 
  public LED led;


  double rotationSpeed;
  final double ANGULAR_P = 1;
  final double ANGULAR_D = 1;
  PIDController turnController = new PIDController(ANGULAR_P, 0, ANGULAR_D);

  private final Encoder rightEncoder = new Encoder(4, 5);
  private final Encoder leftEncoder = new Encoder(6, 7);

  private final DigitalInput pathSetterOne = new DigitalInput(1);
  private final DigitalInput pathSetterTwo = new DigitalInput(2);
  final double DISTANCE_TO_AMP = 76.1; //inches
  final double FORWARD_POSITION = 240; //inches
  private int robotFieldPosition = 0;

  @Override
  public void robotInit() {

   //led = new LED();

   // start a NT4 client
   inst.startClient4("example client");

   // connect to a roboRIO with team number TEAM
   inst.setServerTeam(6845);
   
   // starting a DS client will try to get the roboRIO address from the DS application
   inst.startDSClient(); 
    m_frontLeftMotor.follow(m_rearLeftMotor);
    m_frontRightMotor.follow(m_rearRightMotor);
    
    SendableRegistry.addChild(m_robotDrive, m_rearLeftMotor);
    
    SendableRegistry.addChild(m_robotDrive, m_rearRightMotor);

    led = new LED();
    led.setAnim("default_face");
    
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
    
    endMethod = false;

    leftEncoder.reset();
    rightEncoder.reset();
    leftEncoder.setDistancePerPulse(1./256.); //change this value to match whatever distance we want (256 pulse/rotation in SECONDS currently)
    rightEncoder.setDistancePerPulse(1./256.);

    rotationSpeed = 0;
    autonomousDrive = new DifferentialDrive(m_rearLeftMotor, m_rearRightMotor);
    
    m_frontRightMotor.setInverted(true);
    m_rearRightMotor.setInverted(true);

    robotFieldPosition = pathChoice();
    
  }
  //Upload??
  @Override
  public void teleopPeriodic() {
    
    winchControl();
    FIREINTHEHOLE();
    armControl();
    intakeSuckerMethod();
    parabolicDrive();
    led.LEDPeriodic();
    
    
  }

  @Override
  public void disabledPeriodic() {    
    led.LEDPeriodic();
  }

  public void stopRobot()
  {
    m_robotDrive.tankDrive(0, 0);
  } 
  public void autonomousPeriodic() 
  {
    
     switch(robotFieldPosition){

       case 0: //do nothing
        stopRobot();
        break;
      case 1: //score in amp, then drive outside of starting position
        autonomousPathwayAmpPosition();
        break;
      case 2: //do something
        autonomousPathwayMiddlePosition();
        break; 
      case 3: //leave starting position
        autonomousPathwayFarFromAmpPosition();

        break;}
    autonomousDrive.feed();
    m_robotDrive.feed();

  }
  private void autonomousVisionControl()
  {
    var result = camera.getLatestResult();
    boolean hasTargets = result.hasTargets();
    boolean endMethod = false;
    if(hasTargets)
    {
      targetSwitch = 1;
      //System.out.println("ID: " + result.getBestTarget().getFiducialId());
      //System.out.println("YAW: " + result.getBestTarget().getYaw());
      rotationSpeed = -turnController.calculate(result.getBestTarget().getYaw(), 0.0); //has to be negative or we go backward
      if(rotationSpeed/100 < 0.3 && rotationSpeed/100 > -0.3)
      {
        autonomousDrive.arcadeDrive(-1 , rotationSpeed);
      }
      else{
        if(rotationSpeed > 0)
        {
          m_robotDrive.tankDrive(-0.25,0.25);
        }
        else
        {
          m_robotDrive.tankDrive(0.25, -0.25);
        }
        
      }
      double range = PhotonUtils.calculateDistanceToTargetMeters(
      Units.inchesToMeters(45.0), //cam height
      Units.inchesToMeters(54.38), //target height
      Units.degreesToRadians(0), //camera pitch needs change
      result.getBestTarget().getYaw()); // target yaw
      if(range < .5)
      {
        endMethod = true;
      }
      
      //autonomousDrive.feed();
      
    }

    else{
      m_robotDrive.tankDrive(-.25,.25);
      if(targetSwitch == 0)
        System.out.println("N/A");
        targetSwitch = 1;
      }     
    
    
  }

  private void intakeSuckerMethod()
  {
    if(stick.getRawButtonPressed(A_BUTTON))
    {
      sucking = !sucking;
    }

    if(stick.getRawButton(Y_BUTTON) && stick.getRawButton(A_BUTTON)) //If both pressed do nothing
    {
      intakeSucker.set(VictorSPXControlMode.PercentOutput, 0.0);
    }
    /*
    else if(stick.getRawButton(Y_BUTTON)) //Push out (edit if changed)
    {
      intakeSucker.set(VictorSPXControlMode.PercentOutput, -1);
    }*/
    
    else if(sucking) //Suck in (edit if changed)
    {
      intakeSucker.set(VictorSPXControlMode.PercentOutput, 1.0);
    }
    else //turn off
    {
      intakeSucker.set(VictorSPXControlMode.PercentOutput, 0.0);
    }
  }

  private void armControl()
  {
    double RightTriggerOut = stick.getRawAxis(RIGHT_TRIGGER) * .43;
    double LeftTriggerOut = stick.getRawAxis(LEFT_TRIGGER) * 0.7;
    
    if(RightTriggerOut > 0 && LeftTriggerOut > 0)
    {
      arm2.set(VictorSPXControlMode.PercentOutput, 0);
      arm1.set(VictorSPXControlMode.PercentOutput, 0);
    }
    else if(RightTriggerOut >0)
    {
      arm2.set(VictorSPXControlMode.PercentOutput, RightTriggerOut);
      arm1.set(VictorSPXControlMode.PercentOutput, -RightTriggerOut);
    }
    else if(LeftTriggerOut >0)
    {
      arm2.set(VictorSPXControlMode.PercentOutput, -LeftTriggerOut);
      arm1.set(VictorSPXControlMode.PercentOutput, LeftTriggerOut);
    }
    
  }

  private void winchControl()
  {
    if(stick.getRawButton(LEFT_BUMPER) && stick.getRawButton(RIGHT_BUMPER)) //If both pressed do nothing
    {
      winchAscender.set(VictorSPXControlMode.PercentOutput, 0.0);
    }
    else if(stick.getRawButton(LEFT_BUMPER)) //Left goes down (edit if changed)
    {
      winchAscender.set(VictorSPXControlMode.PercentOutput, -1.0);
    }
    else if(stick.getRawButton(RIGHT_BUMPER)) //Right goes up (edit if changed)
    {
      winchAscender.set(VictorSPXControlMode.PercentOutput, 1.0);
    }
    else //turn off
    {
      winchAscender.set(VictorSPXControlMode.PercentOutput, 0.0);
    }
  }
  
  private void parabolicDrive()
  {
    double leftStickSpeed = stick.getRawAxis(1);
    double rightStickSpeed = stick.getRawAxis(5);
    //Parabolic all going forwards
    toggleTurbo = false;
    
    //Parabolic left back 
    if(leftStickSpeed < 0)
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed * -1, rightStickSpeed * rightStickSpeed);
    }
    //Parabolic right back
    else if(rightStickSpeed < 0)
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed, rightStickSpeed * rightStickSpeed * -1);
    }
    //Parabolic all going backwards
    else if(leftStickSpeed < 0 && rightStickSpeed < 0)
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed * -1, rightStickSpeed * rightStickSpeed * -1);
    }
    else
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed, rightStickSpeed * rightStickSpeed);
    }
  }
  public static void FIREINTHEHOLE()
  {
    if(stick.getRawButton(B_BUTTON))
    {
      // set victor shooter motors to high
      launcherLeft.set(TalonSRXControlMode.PercentOutput,-1);
      launcherRight.set(TalonSRXControlMode.PercentOutput,1);
      intakeSucker.set(VictorSPXControlMode.PercentOutput, -1);
    }
    else{
      launcherLeft.set(TalonSRXControlMode.PercentOutput, 0);
      launcherRight.set(TalonSRXControlMode.PercentOutput, 0);
      intakeSucker.set(VictorSPXControlMode.PercentOutput, 0);
    }
  }

  private int pathChoice()
  {
    int pathSelection = 0;
    if(pathSetterOne.get() && pathSetterTwo.get())
    {
      pathSelection = 1;
    }
    else if(pathSetterOne.get() && !pathSetterTwo.get())
    {
      pathSelection = 2;
    }
    else if(!pathSetterOne.get() && pathSetterTwo.get())
    {
      pathSelection = 3;
    }
    return pathSelection;
  }

  private void autonomousPathwayAmpPosition()
  {
    //Example code.  We'll probably want while !aprilTagSeen spin left and then follow it
    // Drives forward at half speed until the robot has moved 1 foot, then stops:
    if(leftEncoder.getDistance() < DISTANCE_TO_AMP && rightEncoder.getDistance() < DISTANCE_TO_AMP) {
      m_robotDrive.tankDrive(0.5, 0.5);
    }
    //Trust??
    else
    {
      stopRobot();
    }
    //if(!endmethod)
    //autonomousVisionControl();
    //intakeSucker.set(VictorSPXControlMode.PercentOutput, -0.4);

  }

  private void autonomousPathwayMiddlePosition()
  {
    // look for april tags 5 (red) /6 (blue)
    
    //autonomousVisionControl(); //may remove for competition if we can't test
    stopRobot();
  }
  
  private void autonomousPathwayFarFromAmpPosition()
  {
    autoTick++;
    // Look for april tags 4 (red) & 8 (blue)
    if(/*leftEncoder.getDistance() < FORWARD_POSITION && rightEncoder.getDistance() < FORWARD_POSITION  && */autoTick < 80){
      System.out.println(autoTick);
      
      autonomousDrive.tankDrive(-1, -0.95);
    }
    else{

      stopRobot();
    }
    
  }
}