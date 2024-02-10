package frc.robot;

/**
 * Class describing an array of frames to be displayed.
**/

import java.util.*;

public class Animation {
    // The array of frame data.
    public Frame frames[];
    // The index of the next frame to be displayed.
    public int current;

    /**
     * Initializes an Animation object given a list of Frames.
    **/
    public Animation(Frame src[])
    {
        frames = src;
        reset();
    }

    /**
     * Initializes an Animation object given a Set of Frames.
    **/
    public Animation(ArrayList<Frame> src)
    {
        Frame dummy[] = new Frame[] {};
        // Java compiler is dumm; requires a dummy Frame object to coerce type correctly.
        frames = src.toArray(dummy);
        reset();
    }

    // Specify that we're using frame zero.
    public void reset()
    {
        current = 0;
    }

    /**
     * Execute a tick, returning one frame and advancing the counter to the
     * next frame for the next run.
    **/
    public Frame periodic()
    {
        Frame ret = frames[current];
        current++;
        if (current >= frames.length)
        {
            reset();
        }
        return ret;
    }
}
