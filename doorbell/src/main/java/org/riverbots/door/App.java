package org.riverbots.door;

import java.io.*;
import javazoom.jl.player.*;

/**
 *  HTTP Server to Provide a single /bell API that plays doorbell.mp3.
 *  When this is running, it will provide two APIs at http://localhost:8080/:
 *      /bell               - Play an MP3, return a JSON status message that's basically "ok" or "error".
 *      / (Everything else) - Return an "ok" status message.
 *  Main itself is pretty simple; it just instantiates a DoorbellServer (itself a subclass of HttpServlet) and starts it.
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
}
