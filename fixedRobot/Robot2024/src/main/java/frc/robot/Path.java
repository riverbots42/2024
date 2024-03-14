package frc.robot;

//https://pathplanner.dev/pplib-named-commands.html what am I doing
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.pathplanner.lib.auto.NamedCommands;
public class Path{
   // ExampleSubSystem exampleSubsystem;
    private int tick;
    private int startTick;
    
    public Path() {
        // Subsystem initialization
        DifferentialDrive tankDrive = Robot.m_robotDrive;
      //  exampleSubsystem = new ExampleSubsystem();
        // Register Named Commands
        tick = 0;
        startTick = 0;
        NamedCommands.registerCommand("GrabRingAndShoot", fireInTheHole());
      //  NamedCommands.registerCommand("exampleCommand", exampleSubsystem.exampleCommand());
      //  NamedCommands.registerCommand("someOtherCommand", new SomeOtherCommand());

        // Do all other initialization
      //  configureButtonBindings();

        // ...
    }
    public Command fireInTheHole()
    {
      Command thing = Command.startEnd(
      // Start a flywheel spinning at 50% power
      () -> m_shooter.shooterSpeed(0.5),
      // Stop the flywheel at the end of the command
      () -> m_shooter.shooterSpeed(0.0),
      // Requires the shooter subsystem
      m_shooter);
      return new Command();
    }
    public void FIREINTHEHOLE()
    {
      tick++;
      startTick = tick;
      // set victor shooter motors to high
      Robot.launcherLeft.set(TalonSRXControlMode.PercentOutput,-1);
      Robot.launcherRight.set(TalonSRXControlMode.PercentOutput,1);
      // this won't probably work but the idea is that after tick is 25 more than when first clicked it will unload the ring into firing mechanism
      if(startTick == tick - 25)
      {
        startTick = 0;
        //shoot unload the payload
        System.out.println("Shooting");
        Robot.intakeSucker.set(VictorSPXControlMode.PercentOutput, -0.5);
        // wait a bit then turn off shooters
        Robot.launcherLeft.set(TalonSRXControlMode.PercentOutput, 0);
        Robot.launcherRight.set(TalonSRXControlMode.PercentOutput, 0);
      }
      
      else{
        
      }
      if(startTick == 0 && tick > 1000)
      {
        tick = 0;
      }
    }

}
