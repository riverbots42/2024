package frc.robot;

import java.util.*;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class LED {
    public AddressableLED m_led;
    public  AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(512);
    int tick = 0;
    public AnimationList animations;
    private Animation cur_anim = null;

    //For translating our png output to our physically flipped LED arrays
    public int cur_x;
    public int cur_y;
    public int cur_index;

    // The maximum current we have at our disposal in milliamps
    public final int MAX_CURRENT = 2500;
    
    public LED() {
      //All LED Setup:

      // PWM port 0
      // Must be a PWM header, not MXP or DIO
      // AddressableLED m_led = new AddressableLED(8);
      //Why is this commented out?? ^

      m_led = new AddressableLED(0);
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
      
      animations = new AnimationList();
      System.out.printf("Number of animations: %d\n", animations.animations.size());
      Iterator<String> it = animations.animations.keySet().iterator();
      while(it.hasNext()) {
        String name = it.next();
        System.out.println(name);
      }

      setAnim("default_face");
      if(getAnim() == null) {
        System.out.printf("Default face is null\n");
      } else {
        System.out.printf("Default face has %d frames.\n", getAnim().frames.length);
      }
    }

  // Make sure our request for power doesn't exceed the 2.5A budget.
  // @param mapin the array of led values (all segments/colors as one string).
  // @return the normalized (to 2.5A max) array of led values.
  public int[] enforceCurrentBudget(int mapin[]) {
    int mapout = new int[mapin.length];
    int total = 0;
    for(int i=0; i<mapin.length; i++) {
      total += mapin[i];
    }
    double totalCurrent = 15.0 * total / 256;
    if( totalCurrent <= MAX_CURRENT) {
      return mapin;
    }
    // If we got here, then we're over the current budget.  Time to trim the fat.
    double adjustmentFactor = 1.0 * totalCurrent / total;
    for(int i=0; i<mapin.length; i++) {
      mapout[i] = (int) (adjustmentFactor * adjustmentFactor);
    }
    return mapout;
  }

  public void LEDPeriodic() {
    int red = 0;
    int green = 0;
    int blue = 0;
    final int NUM_ROWS = 16;
    final int NUM_COLS = 31;

    tick++;

    if(tick % 5 == 0) {
      Animation anim = getAnim();
      if(anim == null)
        return;
      Frame frame = anim.periodic();
      int pngMap[] = enforceCurrentBudget(frame.rgb);
      for(int x = 0; x<NUM_COLS; x++) {
        for(int y = 0; y<NUM_ROWS; y++) {
          int base = 3 * (x * NUM_ROWS + (15-y)); //flips upside down
          
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
      m_led.setData(m_ledBuffer);
    }
   // cur_index = 0;
  }

  public Animation getAnim() {
    return cur_anim;
  }

  public void setAnim(Animation anim) {
      
      cur_anim = anim;
      anim.reset();
  }

  public void setAnim(String name) {
    if(animations != null) {
      Animation cur = animations.get(name);
      if(cur != null) {
        setAnim(cur);
      }
    }
  }
}
