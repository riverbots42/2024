package org.riverbots.door;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class StatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = HttpServletResponse.SC_OK;
        String ret = String.format("{ \"status\": \"ok\", \"query\": \"%s\" }", request.getRequestURI());
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().println(ret);
    }
}
