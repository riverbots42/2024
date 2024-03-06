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

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;


/**
 * Tank drive and main robot code
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_robotDrive;
  private DifferentialDrive autonomousDrive;
  private Joystick stick;

  //Controller must be set to "X" mode rather than "D" on the back
  final int B_BUTTON = 2;
  final int X_BUTTON = 3;
  final int LEFT_BUMPER = 5;
  final int RIGHT_BUMPER = 6;
  final int FIRE_BUTTON = 1; // currently A, switch to 4 for Y
  final int LEFT_TRIGGER = 2;
  final int RIGHT_TRIGGER = 3;

  VictorSPX winchAscender = new VictorSPX(5);
  VictorSPX arm2 = new VictorSPX(6);
  VictorSPX arm1 = new VictorSPX(8);
  VictorSPX loader = new VictorSPX(7);
  
  //VictorSPX shooter = new VictorSPX(9);

  int tick=0;
  int startTick=0;

  private final CANSparkMax m_frontLeftMotor = new CANSparkMax(1, MotorType.kBrushed);
  private final CANSparkMax m_rearLeftMotor = new CANSparkMax(2, MotorType.kBrushed);
  private final CANSparkMax m_frontRightMotor = new CANSparkMax(3, MotorType.kBrushed);
  private final CANSparkMax m_rearRightMotor = new CANSparkMax(4, MotorType.kBrushed);
 
  private int targetSwitch;

  //public LED led;

  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  PhotonCamera camera = new PhotonCamera(inst, "HD_USB_Camera 2"); 

  double rotationSpeed;
  final double ANGULAR_P = 1;
  final double ANGULAR_D = 1;
  PIDController turnController = new PIDController(ANGULAR_P, 0, ANGULAR_D);

  
  private final Encoder rightEncoder = new Encoder(4, 5);
  private final Encoder leftEncoder = new Encoder(6, 7);

  private final DigitalInput pathSetterOne = new DigitalInput(1);
  private final DigitalInput pathSetterTwo = new DigitalInput(2);
  final double DISTANCE_TO_AMP = 43.8; //inches
  private int robotFieldPosition = 0;

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

  @Override
  public void teleopPeriodic() {
    double leftStickSpeed = stick.getRawAxis(1);
    double rightStickSpeed = stick.getRawAxis(5);
    //Parabolic all going forwards
    m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed, rightStickSpeed * rightStickSpeed);
    //Parabolic left back
    if(leftStickSpeed < 0)
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed * -1, rightStickSpeed * rightStickSpeed);
    }
    //Parabolic right back
    if(rightStickSpeed < 0)
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed, rightStickSpeed * rightStickSpeed * -1);
    }
    //Parabolic all going backwards
    if(leftStickSpeed < 0 && rightStickSpeed < 0)
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed * -1, rightStickSpeed * rightStickSpeed * -1);
    }
    winchControl();
    FIREINTHEHOLE();
    armControl();
    //led.LEDPeriodic();
    // LED.LEDInit(); //Turn on the face
    
  }
  public void stopRobot()
  {
    m_robotDrive.tankDrive(0, 0);
  }
  public void autonomousPeriodic() 
  {
    /*switch(robotFieldPosition){
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
        break;}*/
    var result = camera.getLatestResult();
    boolean hasTargets = result.hasTargets();
    if(hasTargets)
    {
      targetSwitch = 1;
      System.out.println("ID: " + result.getBestTarget().getFiducialId());
      System.out.println("YAW: " + result.getBestTarget().getYaw());
      rotationSpeed = -turnController.calculate(result.getBestTarget().getYaw(), 0.0); //has to be negative or we go backward
      if(rotationSpeed/100 < 0.3 && rotationSpeed/100 > -0.3)
      {
        autonomousDrive.arcadeDrive(-1, rotationSpeed * .50);
      }
      else{
        autonomousDrive.arcadeDrive(0, rotationSpeed);
      }
      double range = PhotonUtils.calculateDistanceToTargetMeters(
      Units.inchesToMeters(45.0), //cam height
      Units.inchesToMeters(54.38), //target height
      Units.degreesToRadians(0), //camera pitch needs change
      result.getBestTarget().getYaw()); // target yaw
      
      
      //autonomousDrive.feed();
      
    }
    else{
      autonomousDrive.arcadeDrive(0, 0);
      if(targetSwitch == 0)
        System.out.println("N/A");
        targetSwitch = 1;
      }        //m_robotDrive.feed();
        //autonomousDrive.feed();
    
  }
  private void armControl()
  {
    double RightTriggerOut = stick.getRawAxis(RIGHT_TRIGGER) * .50;
    double LeftTriggerOut = stick.getRawAxis(LEFT_TRIGGER) * .50;
    
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
    m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed, rightStickSpeed * rightStickSpeed);
    //Parabolic left back
    if(leftStickSpeed < 0)
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed * -1, rightStickSpeed * rightStickSpeed);
    }
    //Parabolic right back
    if(rightStickSpeed < 0)
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed, rightStickSpeed * rightStickSpeed * -1);
    }
    //Parabolic all going backwards
    if(leftStickSpeed < 0 && rightStickSpeed < 0)
    {
      m_robotDrive.tankDrive(leftStickSpeed * leftStickSpeed * -1, rightStickSpeed * rightStickSpeed * -1);
    }
  }
    public void FIREINTHEHOLE()
    {
      /* tick++;
      if(stick.getRawButton(FIRE_BUTTON))
      {
        startTick = tick;
        // set victor shooter motors to high
        // shooter.set(VictorSPXControlMode.PercentOutput,1);
        // this won't probably work but the idea is that after tick is 25 more than when first clicked it will unload the ring into firing mechanism
        if(startTick + 25 == tick)
        {
          startTick = 0;
          //shoot unload the payload
        }
      }
      if(startTick == 0 && tick > 1000)
      {
        tick = 0;
      }*/
      if(stick.getRawButton(FIRE_BUTTON))
      {
        loader.set(VictorSPXControlMode.PercentOutput, 1);


      }
      else{
        loader.set(VictorSPXControlMode.PercentOutput, 0);
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
    /* if(leftEncoder.getDistance() < DISTANCE_TO_AMP && rightEncoder.getDistance() < DISTANCE_TO_AMP) {
      m_robotDrive.tankDrive(0.5, 0.5);
      System.out.println("HI");
    } else {
      stopRobot();
    }*/

    // testing April tag stuff
    

    /*while(aprilTagSeen == false)
      {
        m_robotDrive.tankDrive(-0.25, 0.25);
      }*/
  }

  private void autonomousPathwayMiddlePosition()
  {
    // look for april tags 5 (red) /6 (blue)
    
  }
  
  private void autonomousPathwayFarFromAmpPosition()
  {
    // Look for april tags 4 (red) & 8 (blue)
    stopRobot();
  }
}