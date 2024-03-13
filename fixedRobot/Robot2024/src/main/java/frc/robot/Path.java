package frc.robot;

//https://pathplanner.dev/pplib-named-commands.html what am I doing
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Path {
    public Path() {
        // Subsystem initialization
        DifferentialDrive tankDrive = new DifferentialDrive();
        exampleSubsystem = new ExampleSubsystem();

        // Register Named Commands
        NamedCommands.registerCommand("GrabRingAndShoot", FIREINTHEHOLE());
      //  NamedCommands.registerCommand("exampleCommand", exampleSubsystem.exampleCommand());
      //  NamedCommands.registerCommand("someOtherCommand", new SomeOtherCommand());

        // Do all other initialization
      //  configureButtonBindings();

        // ...
    }
}