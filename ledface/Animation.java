/**
 * Class describing an array of frames to be displayed.
**/

public class Animation {
    // The array of frame data.
    public Frame faces[];
    // The index of the next frame to be displayed.
    synchronized public int current;

    /**
     * Initializes an Animation object given a list of Frames.
    **/
    public Animation(Frame src[])
    {
        faces = src;
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
        Frame ret = faces[current];
        current++;
        if (current >= faces.length)
        {
            reset();
        }
        return ret;
    }
}
