package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ArmConsts.*;

public class Arm extends SubsystemBase {
  // Variables for arm motors
  private CANSparkMax armLeft;
  private CANSparkMax armRight;
  private RelativeEncoder armEncoder;
  private AbsoluteEncoder armAbsolute;

  private SparkMaxLimitSwitch fwdLimitSwitch;

  public Arm() {

    armRight = new CANSparkMax(ArmCAN.armRight.id, MotorType.kBrushless);
    armRight.setInverted(false);

    armLeft = new CANSparkMax(ArmCAN.armLeft.id, MotorType.kBrushless);
    armLeft.follow(armRight, true);

    armEncoder = armRight.getEncoder();
    armEncoder.setPosition(0);

    armAbsolute = armRight.getAbsoluteEncoder(Type.kDutyCycle);

    fwdLimitSwitch = armRight.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run when in simulation

  }

  public void setArmSpeed(double speed) {
    armRight.set(speed);
  }

  public void stopArm() {
    armRight.set(0);
  }

  public double getEncoderValue() {
    return armEncoder.getPosition();
  }

  public double getAbsoluteValue() {
    return armAbsolute.getPosition();
  }

  public void resetEncoder() {
    armEncoder.setPosition(0);
  }

  public boolean getfwdSwitch() {
    return fwdLimitSwitch.isPressed();
  }
}
