package org.riverbots.door;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

/*
 * Simple servlet that outputs "ok" and the URL that we received for reference.
 */
public class StatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = HttpServletResponse.SC_OK;
	// This returns JSON that's basically status:ok and whatever query string we received.
        String ret = String.format("{ \"status\": \"ok\", \"query\": \"%s\" }", request.getRequestURI());
	// Send the status back to the client.
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().println(ret);
    }
}
