package frc.robot;

import java.util.*;

public class Main
{
    public static void main(String args[])
    {
        AnimationList anim = new AnimationList();
        System.out.printf("Loaded %d animations successfully:\n", anim.animations.size());
        Iterator<String> keys = anim.animations.keySet().iterator();
        while(keys.hasNext())
        {
            String key = keys.next();
            Animation val = anim.animations.get(key);
            System.out.printf("    %s: %d frames.\n", key, val.frames.length);
        }
    }
}
