package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PneumaticsConsts;
import frc.robot.Constants.PneumaticsConsts.PNEUMATICS;

public class Pneumatics extends SubsystemBase {

  private PneumaticHub compressor;
  private DoubleSolenoid elevator;
  private DoubleSolenoid driveFront;
  private DoubleSolenoid driveRear;

  public Pneumatics() {

    compressor = new PneumaticHub(PneumaticsConsts.pHubCANid);

    elevator = compressor.makeDoubleSolenoid(PNEUMATICS.elev.fwd, PNEUMATICS.elev.rev);

    driveFront = compressor.makeDoubleSolenoid(PNEUMATICS.secFront.fwd, PNEUMATICS.secFront.rev);

    driveRear = compressor.makeDoubleSolenoid(PNEUMATICS.secBack.fwd, PNEUMATICS.secBack.rev);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run when in simulation

  }

  public void enable() {
    compressor.enableCompressorDigital();
  }

  public boolean getStatus() {
    return compressor.getCompressor();
  }

  public void leanFwd() {
    elevator.set(Value.kForward);
  }

  public void leanBack() {
    elevator.set(Value.kReverse);
  }

  public void engageDrive() {
    driveFront.set(Value.kForward);
    driveRear.set(Value.kForward);
  }

  public void disengageDrive() {
    driveFront.set(Value.kReverse);
    driveRear.set(Value.kReverse);
  }
}
