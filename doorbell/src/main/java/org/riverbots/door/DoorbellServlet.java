package org.riverbots.door;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class DoorbellServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\"}");
    }
}
