package frc.robot;

/**
 * Class for listing Frames
**/

import java.util.*;
public class FrameList
{
    Map<Frame, Integer> treeMap;
    Map.Entry<Frame, Integer>[] entryArray;
    Set<Map.Entry<Frame,Integer>> entrySet;
    public FrameList()
    {
        // Give it a frame and how long to hold it for.
        treeMap = new TreeMap<Frame, Integer>();
        entrySet = treeMap.entrySet();
        Map.Entry<Frame, Integer>[] entryArray= entrySet.toArray(new Map.Entry[entrySet.size()]);
    }
    public Frame getFrame(int frameNumber)
    {
        if(treeMap.isEmpty())
        {
            return null;
        }
        return entryArray[frameNumber].getKey();
        
    }
    public int getFrameNum(Frame frame)
    {
        if(!treeMap.containsKey(frame))
        {
            return 0;
        }
        return treeMap.get(frame);
    }
    public void addFrame(Frame frame, Integer frameNum)
    {
        treeMap.put(frame, frameNum);
        entryArray = entrySet.toArray(new Map.Entry[entrySet.size()]);
    }
    public int length()
    {
        return treeMap.size();
    }
    
}
