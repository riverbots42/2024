package org.riverbots.door;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javazoom.jl.player.*;

/*
 * Servlet class to handle the ringing of the doorbell.
 */
public class DoorbellServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Assume everything worked to plan unless we hit some exception.
        int status = HttpServletResponse.SC_OK;
        String ret = "{ \"status\": \"ok\" }";
        try {
            // Getting the file this way means that we can pull the MP3 from within the JAR (handy!).
            InputStream mp3stream = getClass().getResourceAsStream("/bell.mp3");
	    // Once we have the stream, just call jl.player to play it.
            Player p = new Player(mp3stream);
            p.play();
        } catch(Exception E) {
            // Whoops, something went wrong (missing file, can't open sound, etc).
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            ret = String.format(" \"status\": \"error\", \"error\": \"%s\" }", E.toString());
        }
	// Send back status to the client.
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().println(ret);
    }
}
