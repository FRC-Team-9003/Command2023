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
  private int slot = 0;

  public Elevator() {
    elev = new CANSparkMax(ELEV.elevCan.id, MotorType.kBrushless);
    elevEncoder = elev.getEncoder();
    elevEncoder.setPosition(0);

    forwardSwitch = elev.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    reverseSwitch = elev.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);

    /*     controller = elev.getPIDController();
    controller.setP(ElevatorConsts.kP);
    controller.setD(ElevatorConsts.kD);
    controller.setI(ElevatorConsts.kI);
    controller.setIZone(ElevatorConsts.kIz);
    controller.setFF(ElevatorConsts.kFF);
    controller.setOutputRange(ElevatorConsts.kMinOutput, ElevatorConsts.kMaxOutput);

    controller.setSmartMotionMaxVelocity(5000, slot);
    controller.setSmartMotionMaxAccel(2000, slot); */
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
    // controller.setReference(speed, CANSparkMax.ControlType.kSmartVelocity);
    elev.set(speed);
  }

  public void stopElev() {
    // controller.setReference(0, CANSparkMax.ControlType.kSmartVelocity);
    elev.stopMotor();
  }
}
