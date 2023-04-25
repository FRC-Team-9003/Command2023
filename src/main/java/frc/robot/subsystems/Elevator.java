package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElevatorConsts.*;

public class Elevator extends SubsystemBase {

  private CANSparkMax elev;
  private RelativeEncoder elevEncoder;

  private SparkMaxLimitSwitch forwardSwitch;
  private SparkMaxLimitSwitch reverseSwitch;

  private SparkMaxPIDController controller;

  public Elevator() {
    elev = new CANSparkMax(ELEV.elevCan.id, MotorType.kBrushless);
    elevEncoder = elev.getEncoder();
    elevEncoder.setPosition(0);

    forwardSwitch = elev.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    reverseSwitch = elev.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);

    // controller = elev.getPIDController();
    // setup the controller
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run when in simulation

  }

  public boolean getfwdSwitch() {
    return forwardSwitch.isPressed();
  }

  public boolean getrevSwitch() {
    return reverseSwitch.isPressed();
  }

  public void resetEncoder() {
    elevEncoder.setPosition(0);
  }

  public double getEncoderValue() {
    return elevEncoder.getPosition();
  }

  public double getSpeed() {
    return elevEncoder.getVelocity();
  }

  public void setSpeed(double speed) {
    elev.set(speed);
  }

  public void stopElev() {
    elev.set(0);
  }
}
