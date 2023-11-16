package org.riverbots.door;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javazoom.jl.player.*;

public class DoorbellServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = HttpServletResponse.SC_OK;
        String ret = "{ \"status\": \"ok\" }";
        try {
            InputStream mp3stream = getClass().getResourceAsStream("/bell.mp3");
            Player p = new Player(mp3stream);
            p.play();
        } catch(Exception E) {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            ret = String.format(" \"status\": \"error\", \"error\": \"%s\" }", E.toString());
        }
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().println(ret);
    }
}
