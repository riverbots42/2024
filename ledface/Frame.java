package frc.robot;

/**
 * Class describing a single frame of previously-decoded RGB data.
**/
public class Frame {
    public int rgb[];
    public Frame(int src[])
    {
        rgb = src;
    }
}
