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
    public int cur_index;
    
    private int[] face = Faces.getFace();

    public LED() {
      //All LED Setup:

      // PWM port 9
      // Must be a PWM header, not MXP or DIO
      // AddressableLED m_led = new AddressableLED(8);
      //Why is this commented out?? ^


      cur_x = 0;
      cur_y = 0;
      cur_index = 0;

  
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
          m_ledBuffer.setRGB(i, 0, 0, 0);
        //  System.out.printf("%d of %d\n", i, LEDLength);
         }
         
      m_led.setData(m_ledBuffer);
      idx = 0;
    }

  public void LEDPeriodic() {
    int red = 0;
    int green = 0;
    int blue = 0;
    final int NUM_ROWS = 16;
    final int NUM_COLS = 32;

    for(int idx = 0; idx < face.length/3; idx++) {
      int col = (idx / 3) % NUM_ROWS;
      int row = (idx / 3) / NUM_COLS;

      red = row * 3;
      green = row * 3 + 1;
      blue = row * 3 + 2;
      
      if((row + 1) % 2 == 0){ //if it's odd
        int cur_index = NUM_ROWS * (NUM_COLS-1-row) + col;
        m_ledBuffer.setRGB(cur_index, red, green, blue);
      } else {
        int cur_index = NUM_ROWS * (NUM_COLS-1-row) + NUM_ROWS-1-col;
        m_ledBuffer.setRGB(cur_index, red, green, blue);
      }
    }
    m_led.setData(m_ledBuffer);
    cur_index = 0;
  }
}
