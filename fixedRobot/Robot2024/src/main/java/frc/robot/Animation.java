package frc.robot;

/**
 * Class describing an array of frames to be displayed.
**/

import java.util.*;

public class Animation {
    // The array of frame data.
    public FrameList frames;
    // The index of the next frame to be displayed.
    public int tick;
    private int currentFrame = 0;
    private Frame ret;
    /**
     * Initializes an Animation object given a list of Frames.
    **/
    public Animation(FrameList src)
    {
        frames = src;
        ret = frames.getFrame(0);
        reset();
    }

    /**
     * Initializes an Animation object given a Set of Frames.
    *
    public Animation(ArrayList<Frame> src)
    {
        Frame dummy[] = new Frame[] {};
        // Java compiler is dumm; requires a dummy Frame object to coerce type correctly.
        frames = src.toArray(dummy);
        reset();
    }*/

    // Specify that we're using frame zero.
    public void reset()
    {
        tick = 0;
    }

    /**
     * Execute a tick, returning one frame and advancing the counter to the
     * next frame for the next run.
    **/
    public Frame periodic()
    {
        tick++;
        if(tick > frames.getFrameNum(ret))
        {
            reset();
            currentFrame++;
            if(currentFrame > frames.length())
            {
                currentFrame = 0;
            }
            ret = frames.getFrame(currentFrame);
        }
        
        
        
        return ret;
    }
}
