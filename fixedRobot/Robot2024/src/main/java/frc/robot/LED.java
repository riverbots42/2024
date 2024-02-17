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
    
    private int[] pngMap = Faces.getFace();

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
    for(int x = 0; x<NUM_COLS; x++) {
      for(int y = 0; y<NUM_ROWS; y++) {
        int base = 3 * (x * NUM_ROWS + y);
        
        red = pngMap[base]/3;
        green = pngMap[base+1]/3;
        blue = pngMap[base+2]/3;
        int led_idx = x * NUM_ROWS + y;
        if( x % 2 == 1 ) {
          led_idx = (x * NUM_ROWS + (NUM_ROWS - y - 1));
        }
        m_ledBuffer.setRGB(led_idx, red/2, green/2, blue/2);
       /*  if(x == 16) {
          System.out.printf("%d, %d = %d\n", x, y, led_idx);
          m_ledBuffer.setRGB(led_idx, y*16, y*16, 255);
        }*/
        
       // m_ledBuffer.setRGB(base/3, 8*x/3, 4*y/3, 127);
      }
    }
    /*
    for(int idx = 0; idx < pngMap.length/3; idx+=3) {
      int col = (idx / 3) % NUM_ROWS;
      int row = (idx / 3) / NUM_ROWS;
      if(idx < 60)
        System.out.printf("%d = %d, %d\n", idx, col, row);

      red = pngMap[idx];
      green = pngMap[idx + 1];
      blue = pngMap[idx + 2];
      m_ledBuffer.setRGB(idx/3, red, green, blue);
      if((row + 1) % 2 == 0){ //if it's even
        int cur_index = 16 * (31-row) + col;
        System.out.println("Even Index: " + cur_index);
        m_ledBuffer.setRGB(cur_index, red, green , blue );
      } else {
        int cur_index = 16 * (31-row) + (15-col);
        System.out.println("Odd Index: " + cur_index);
        m_ledBuffer.setRGB(cur_index, red, green, blue );
      }
    }
    */
    m_led.setData(m_ledBuffer);
    cur_index = 0;
  }
}
