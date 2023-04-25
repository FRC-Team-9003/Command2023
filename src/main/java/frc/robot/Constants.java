// RobotBuilder Version: 5.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public class Constants {
  public static final class DriveTrain {
    public enum mecDriveCanID {
      rightFront(4),
      leftFront(5),
      rightBack(6),
      leftBack(7);

      public final int id;

      mecDriveCanID(int value) {
        this.id = value;
      }
    }

    public enum secDriveTrain {
      front(8),
      back(9);

      public final int id;

      secDriveTrain(int value) {
        this.id = value;
      }
    }
  }

  public static final class ArmConsts {
    public enum ArmCAN {
      armRight(20),
      armLeft(21);

      public final int id;

      ArmCAN(int value) {
        this.id = value;
      }
    }
  }

  public static final class ElevatorConsts {
    public enum ELEV {
      elevCan(10);

      public final int id;

      ELEV(int value) {
        this.id = value;
      }
    }
    // Smart Motion Slot
    public static final int slot = 0;

    // PID Coefficients
    public static final double kP = 0;
    public static final double kI = 0;
    public static final double kD = 0;
    public static final double kIz = 0;
    public static final double kFF = 0;
    public static final double kMaxOutput = 1;
    public static final double kMinOutput = -1;

    // Smart Motion Coefficients
    public final double maxVelocity = 5000;
    public final double maxAccel = 2000;
  }

  public static final class Intake {
    public enum INTAKE {
      leftIN(31),
      rightIN(30);

      public final int id;

      INTAKE(int value) {
        this.id = value;
      }
    }
  }

  public static final class PneumaticsConsts {
    public enum PNEUMATICS {
      secFront(2, 3),
      secBack(4, 5),
      elev(1, 0);

      public final int fwd;
      public final int rev;

      PNEUMATICS(int fwd_value, int rev_value) {
        this.fwd = fwd_value;
        this.rev = rev_value;
      }
    }

    public static final int pHubCANid = 2;
  }
}
