package org.riverbots.door;

import jakarta.servlet.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;

public class DoorbellServer {
    private Server server;
    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[] {connector});
	ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);
	servletHandler.addServletWithMapping(DoorbellServlet.class, "/");
        server.start();
    }
}
