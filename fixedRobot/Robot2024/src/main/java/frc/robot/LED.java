package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class LED {
    public AddressableLED m_led = new AddressableLED(8);
    public  AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(512);
    int idx;

    //For translating our png output to our physically flipped LED arrays
    public int cur_x;
    public int cur_y;

    public LED() {
      //All LED Setup:

      // PWM port 9
      // Must be a PWM header, not MXP or DIO
      // AddressableLED m_led = new AddressableLED(8);
      //Why is this commented out?? ^


      cur_x = 0;
      cur_y = 0;

  
      // Reuse buffer
      // Length is expensive to set, so only set it once, then just update data
      // AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(512);
      m_led.setLength(m_ledBuffer.getLength());
  
      // Set the data
      m_led.setData(m_ledBuffer);
      m_led.start();

      //LED color setting
      int LEDLength =  m_ledBuffer.getLength();
      for (int i = 0; i < LEDLength; i++) {
          // Sets the specified LED to the RGB values for yellow
          m_ledBuffer.setRGB(i, i, 512-i, 0);
        //  System.out.printf("%d of %d\n", i, LEDLength);
         }
         
      m_led.setData(m_ledBuffer);
      idx = 0;
      System.out.println("Hello");
    }

    public void LEDPeriodic() {
      /*
      for(int i =0; i<m_ledBuffer.getLength(); i++) {
        m_ledBuffer.setRGB(i, 0,0,0);
      }

      m_ledBuffer.setRGB(idx, 0, 255, 0);
      idx++;
      if(idx>511) idx=0;
      m_led.setData(m_ledBuffer); */



      for( int x = 0; x < 32; x++) {
        for( int y = 0; y < 16; y++) {
            if( x == cur_x && y == cur_y ) {
                // turn on the LED
            } else {
                // turn off the LED
            }
        }
    }

      cur_x++; if(cur_x > 31) cur_x = 0;
      cur_y++; if(cur_y > 15) cur_y = 0;
    }
}
