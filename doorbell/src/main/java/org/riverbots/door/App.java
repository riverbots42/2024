package org.riverbots.door;

import java.io.*;
import javazoom.jl.player.*;

/**
 * Hello world!
 *
 */
public class App {
    public DoorbellServer server;

    public App() {
        super();
        this.server = new DoorbellServer();
    }

    public static void main(String args[]) {
        App app = new App();
	try {
	    app.server.start();
	} catch(Exception E) {
            System.out.println(E.toString());
	}
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
