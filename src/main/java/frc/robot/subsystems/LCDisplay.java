package frc.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.DisplayConsts;

public class LCDisplay extends SubsystemBase {
  private I2C display;

  public LCDisplay() {
    display = new I2C(I2C.Port.kOnboard, 0x27);
    /* writeCmd(0x03);
    writeCmd(0x03); 
    writeCmd(0x03);
    writeCmd(0x02); */

    writeCmd(DisplayConsts.LCD_FUNCTIONSET | DisplayConsts.LCD_2LINE | DisplayConsts.LCD_4BITMODE);
    writeCmd(DisplayConsts.LCD_DISPLAYCONTROL | DisplayConsts.LCD_DISPLAYON);
    writeCmd(DisplayConsts.LCD_CLEARDISPLAY);
    writeCmd(DisplayConsts.LCD_ENTRYMODESET | DisplayConsts.LCD_ENTRYLEFT);

    new WaitCommand(.01);
    System.out.println("LCD Setup Complete");
  }

  private void sleep(double time) {
    new WaitCommand(time);
  }

  private void rawWrite(int data) {
    display.write(0, data | DisplayConsts.LCD_BACKLIGHT);
  }

  private void writeChar(int data) {
    rawWrite(DisplayConsts.RS | (data & 0xF0));
    rawWrite(DisplayConsts.RS | (data << 4) & 0xF0);
  }

  private void writeCmd(int data) {
    rawWrite(data & 0xF0);
    rawWrite((data << 4) & 0xF0);
  }

  public void printLCD() {
    String greeting = "Hello 9003!";
    String greeting2 = "Are you Ready?";
    writeCmd(DisplayConsts.LCD_CLEARDISPLAY);

    writeCmd(0x80);
    sleep(0.02);

    for (char s : greeting.toCharArray()) {
      writeChar(s);
      sleep(0.01);
    }

    writeCmd(0xC0);
    sleep(0.02);

    for (char s : greeting2.toCharArray()) {
      writeChar(s);
      sleep(0.01);
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
}
