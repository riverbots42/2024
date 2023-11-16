package org.riverbots.door;

import java.io.*;
import javazoom.jl.player.*;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String args[]) {
	System.out.println("Hi there!");
    }

    public void play() {
        try {
            FileInputStream fis = new FileInputStream(new File("demo.mp3"));
            Player p = new Player(fis);
            p.play();
        } catch(Exception E) {
            System.out.println(E.toString());
        }
    }
}
