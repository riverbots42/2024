package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class LED {
  
    public void LEDInit() {
      //All LED Setup:

      // PWM port 9
      // Must be a PWM header, not MXP or DIO
      AddressableLED m_led = new AddressableLED(9);
  
      // Reuse buffer
      // Default to a length of 60, start empty output
      // Length is expensive to set, so only set it once, then just update data
      AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(60);
      m_led.setLength(m_ledBuffer.getLength());
  
      // Set the data
      m_led.setData(m_ledBuffer);
      m_led.start();

      //LED color setting
      int LEDLength =  m_ledBuffer.getLength();
      for (var i = 0; i < LEDLength; i++) {
          // Sets the specified LED to the RGB values for red
          m_ledBuffer.setRGB(i, 255, 255, 0);
         }
         
      m_led.setData(m_ledBuffer);
    }
}
