package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Intake.*;

public class Intake extends SubsystemBase {
  private CANSparkMax leftIntake;
  private CANSparkMax rightIntake;

  public Intake() {
    leftIntake = new CANSparkMax(INTAKE.leftIN.id, MotorType.kBrushless);
    leftIntake.setInverted(false);

    rightIntake = new CANSparkMax(INTAKE.rightIN.id, MotorType.kBrushless);
    rightIntake.setInverted(false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run when in simulation

  }

  public void setMotors(double left, double right) {
    leftIntake.set(left);
    rightIntake.set(right);
  }

  public void stopMotors() {
    leftIntake.set(0);
    rightIntake.set(0);
  }
}
