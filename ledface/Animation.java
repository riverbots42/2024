public class Animation {
    
    public Frame faces[];
    public int current;

    public Animation(Frame src[])
    {
        faces = src;
        reset();
    }
    public void reset()
    {
        current = 0;
    }
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
