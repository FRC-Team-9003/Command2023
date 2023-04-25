package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveTrain.*;

public class Drivetrain extends SubsystemBase {

  private CANSparkMax leftFront;
  private CANSparkMax rightFront;
  private CANSparkMax leftRear;
  private CANSparkMax rightRear;

  private RelativeEncoder lfEncoder;
  private RelativeEncoder rfEncoder;
  private RelativeEncoder rrEncoder;
  private RelativeEncoder lrEncoder;

  private MecanumDrive mecanumDrive;

  private CANSparkMax front;
  private CANSparkMax rear;

  private AHRS gyro;

  public Drivetrain() {
    leftFront = new CANSparkMax(mecDriveCanID.leftFront.id, MotorType.kBrushless);
    leftFront.restoreFactoryDefaults();
    leftFront.setInverted(false);
    lfEncoder = leftFront.getEncoder();
    lfEncoder.setPosition(0);

    rightFront = new CANSparkMax(mecDriveCanID.rightFront.id, MotorType.kBrushless);
    rightFront.restoreFactoryDefaults();
    rightFront.setInverted(true);
    rfEncoder = rightFront.getEncoder();
    rfEncoder.setPosition(0);

    leftRear = new CANSparkMax(mecDriveCanID.leftBack.id, MotorType.kBrushless);
    leftRear.restoreFactoryDefaults();
    leftRear.setInverted(false);
    lrEncoder = leftRear.getEncoder();
    lrEncoder.setPosition(0);

    rightRear = new CANSparkMax(mecDriveCanID.rightBack.id, MotorType.kBrushless);
    rightRear.restoreFactoryDefaults();
    rightRear.setInverted(true);
    rrEncoder = rightRear.getEncoder();
    rrEncoder.setPosition(0);

    mecanumDrive = new MecanumDrive(leftFront, leftRear, rightFront, rightRear);
    mecanumDrive.setSafetyEnabled(true);
    mecanumDrive.setExpiration(0.1);
    mecanumDrive.setMaxOutput(1.0);

    front = new CANSparkMax(secDriveTrain.front.id, MotorType.kBrushless);
    front.setInverted(false);

    rear = new CANSparkMax(secDriveTrain.back.id, MotorType.kBrushless);
    rear.setInverted(false);
    rear.follow(front);

    try {
      gyro = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException ex) {
      DriverStation.reportError("Error Instantiating NavX" + ex.getMessage(), true);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run when in simulation

  }

  public void drive(double xstick, double ystick, double rotate) {
    mecanumDrive.driveCartesian(
        xstick, ystick, Math.pow(rotate, 3), Rotation2d.fromDegrees(gyro.getAngle()));
  }

  public void autoDrive(double xstick, double ystick) {
    mecanumDrive.driveCartesian(ystick, xstick, 0);
  }

  public double getEncoderValue() {
    return lfEncoder.getPosition();
  }

  public double getSpeed() {
    return lfEncoder.getVelocity();
  }

  public void resetNavX() {
    gyro.reset();
  }

  public double getGyroAngle() {
    return gyro.getAngle();
  }

  public static double fromDistance(double dist) {
    return 84 * dist / Math.PI;
  }

  public static double toDistance(double numClicks) {
    return numClicks * Math.PI / 84;
  }
}
