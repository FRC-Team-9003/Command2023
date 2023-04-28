package frc.robot.commands;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;
import java.util.*;

public class ShuffleboardSetup extends CommandBase {
  // Subsystem Variables
  Arm m_arm;
  Elevator m_elevator;
  Drivetrain m_drivetrain;

  // Network table setup
  NetworkTableInstance inst;
  NetworkTable table;
  DoublePublisher angle;

  public ShuffleboardSetup(Arm a_subsystem, Elevator e_subsystem, Drivetrain d_subsystem) {

    m_arm = a_subsystem;
    m_elevator = e_subsystem;
    m_drivetrain = d_subsystem;

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
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("datatable");
    angle = table.getDoubleTopic("gyro").publish();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    angle.set(m_drivetrain.getGyroAngle());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }
}
