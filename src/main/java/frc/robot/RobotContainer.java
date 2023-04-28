package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Command.InterruptionBehavior;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class RobotContainer {

  private static RobotContainer m_robotContainer = new RobotContainer();

  // The robot's subsystems
  public final Intake m_intake = new Intake();
  public final Pneumatics m_pneumatics = new Pneumatics();
  public final Arm m_arm = new Arm();
  public final Elevator m_elevator = new Elevator();
  public final Drivetrain m_drivetrain = new Drivetrain();

  // Joysticks
  private final CommandJoystick driveStick = new CommandJoystick(0);
  private final CommandXboxController xbox = new CommandXboxController(1);

  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  private RobotContainer() {

    // Configure the button bindings
    configureButtonBindings();

    // Sendable Chooser for Autonomous
    // m_chooser.setDefaultOption("Autonomous Command", new AutonomousCommand());
    m_chooser.setDefaultOption("Cube Drop", new CubeDrop(m_arm, m_drivetrain));

    SmartDashboard.putData("Auto Mode", m_chooser);

    m_drivetrain.setDefaultCommand(
        new RunCommand(
            () -> m_drivetrain.drive(-driveStick.getY(), driveStick.getX(), driveStick.getTwist()),
            m_drivetrain));

    m_arm.setDefaultCommand(new RunCommand(() -> m_arm.setArmSpeed(xbox.getRightY()), m_arm));

    m_elevator.setDefaultCommand(
        new RunCommand(() -> m_elevator.setSpeed(xbox.getLeftY()), m_elevator));
    // new SafetyEnabledElev(xbox.getLeftY(), m_elevator, m_arm));
  }

  public static RobotContainer getInstance() {
    return m_robotContainer;
  }

  /**
   * Use this method to define your button->command mappings. Uses Triggers found in CommandJoystick
   * and CommandXboxController Bindings using InstantCommand assume short durration and that there
   * will not be competition for resources.
   */
  private void configureButtonBindings() {

    // Drive Button Bindings
    final Trigger trigger = driveStick.trigger();
    trigger.onTrue(new InstantCommand(() -> m_pneumatics.engageDrive()));
    trigger.onFalse(new InstantCommand(() -> m_pneumatics.disengageDrive()));

    final Trigger lean = driveStick.button(7);
    lean.onTrue(new InstantCommand(() -> m_pneumatics.leanFwd()));
    lean.onFalse(new InstantCommand(() -> m_pneumatics.leanBack()));

    final Trigger resetGyro = driveStick.button(11);
    resetGyro.onTrue(new InstantCommand(() -> m_drivetrain.resetNavX()));

    // Mechanism Button Bindings
    final Trigger coneIntake = xbox.leftBumper();
    coneIntake
        .onTrue(new InstantCommand(() -> m_intake.setMotors(-0.5, -0.5)))
        .onFalse(new InstantCommand(() -> m_intake.stopMotors()));

    final Trigger coneOutake = xbox.rightBumper();
    coneOutake
        .onTrue(new InstantCommand(() -> m_intake.setMotors(0.5, 0.5)))
        .onFalse(new InstantCommand(() -> m_intake.stopMotors()));

    final Trigger cubeIntake = xbox.leftTrigger();
    cubeIntake
        .onTrue(new InstantCommand(() -> m_intake.setMotors(0.5, -0.5)))
        .onFalse(new InstantCommand(() -> m_intake.stopMotors()));

    final Trigger cubeOutake = xbox.rightTrigger();
    cubeOutake
        .onTrue(new InstantCommand(() -> m_intake.setMotors(-0.5, 0.5)))
        .onFalse(new InstantCommand(() -> m_intake.stopMotors()));

    final Trigger aButton = xbox.a();
    aButton.whileTrue(
        new HumanStation(m_arm, m_elevator)
            .withInterruptBehavior(InterruptionBehavior.kCancelSelf));

    final Trigger bButton = xbox.b();
    bButton.whileTrue(
        new FoldUp(m_arm, m_elevator).withInterruptBehavior(InterruptionBehavior.kCancelSelf));

    final Trigger yButton = xbox.y();
    yButton.whileTrue(
        new FloorPickup(m_arm, m_elevator).withInterruptBehavior(InterruptionBehavior.kCancelSelf));

    final Trigger xButton = xbox.x();
    xButton.whileTrue(
        new ElevMin(m_elevator)
            .andThen(new ElevMax(m_elevator))
            .withInterruptBehavior(InterruptionBehavior.kCancelSelf));

    final Trigger armLimit = new Trigger(m_arm::getfwdSwitch);
    armLimit.onTrue(new InstantCommand(() -> m_arm.resetEncoder()));
  }

  public CommandJoystick getDriveStick() {
    return driveStick;
  }

  public CommandXboxController getXbox() {
    return xbox;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // The selected command will be run in autonomous
    return m_chooser.getSelected();
  }
}
