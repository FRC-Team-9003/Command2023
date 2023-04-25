package frc.robot;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.*;
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
import java.util.*;

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
  // private final CommandJoystick boxStick = new CommandJoystick(2);

  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  // Network table setup
  NetworkTableInstance inst;
  NetworkTable table;
  DoublePublisher angle;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  private RobotContainer() {
    // Shuffleboard Setup
    ShuffleboardTab sensorTab = Shuffleboard.getTab("Sensor Data");
    ShuffleboardTab commands = Shuffleboard.getTab("Commands");

    ShuffleboardLayout elevCmds =
        commands
            .getLayout("Elevator", BuiltInLayouts.kList)
            .withSize(2, 2)
            .withProperties(Map.of("Label Position", "HIDDEN"));

    ShuffleboardLayout armSensors =
        sensorTab
            .getLayout("Arm", BuiltInLayouts.kList)
            .withSize(2, 2)
            .withProperties(Map.of("Label Position", "HIDDEN"));

    ShuffleboardLayout elevSensors =
        sensorTab
            .getLayout("Elevator", BuiltInLayouts.kList)
            .withSize(2, 2)
            .withProperties(Map.of("Label Position", "HIDDEN"));
    ShuffleboardLayout driveSensors =
        sensorTab
            .getLayout("Motors & Encoders", BuiltInLayouts.kList)
            .withSize(2, 2)
            .withProperties(Map.of("Label Position", "LEFT"));

    // SmartDashboard Buttons
    elevCmds.add(new ElevMax(m_elevator));
    elevCmds.add(new ElevMin(m_elevator));

    armSensors.addBoolean("Forward Limit", m_arm::getfwdSwitch);
    armSensors.addDouble("Arm Encoder", m_arm::getEncoderValue);

    elevSensors.addBoolean("Forward Limit", m_elevator::getfwdSwitch);
    elevSensors.addBoolean("Reverse Limit", m_elevator::getrevSwitch);
    elevSensors.addDouble("Elevator Encoder", m_elevator::getEncoderValue);
    elevSensors.addDouble("Elevator Speed", m_elevator::getSpeed);

    driveSensors.addDouble("Left Front Speed", m_drivetrain::getSpeed);
    driveSensors.addDouble("Left Front Encoder", m_drivetrain::getEncoderValue);

    // Configure the button bindings
    configureButtonBindings();

    // Sendable Chooser for Autonomous
    // m_chooser.setDefaultOption("Autonomous Command", new AutonomousCommand());
    m_chooser.setDefaultOption("Cube Drop", new CubeDrop(m_arm, m_drivetrain));

    SmartDashboard.putData("Auto Mode", m_chooser);

    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("datatable");
    angle = table.getDoubleTopic("gyro").publish();

    m_drivetrain.setDefaultCommand(
        new RunCommand(
            () -> {
              m_drivetrain.drive(-driveStick.getY(), driveStick.getX(), driveStick.getTwist());
              angle.set(m_drivetrain.getGyroAngle());
            },
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

    /*final Trigger test = boxStick.button(3);
    test.whileTrue(
        new ElevMin(m_elevator)
            .andThen(new ElevMax(m_elevator))
            .withInterruptBehavior(InterruptionBehavior.kCancelSelf));*/
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
