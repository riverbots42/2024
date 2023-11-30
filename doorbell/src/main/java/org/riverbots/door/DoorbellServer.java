package org.riverbots.door;

import jakarta.servlet.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;

/**
 * Class to create/start the HTTP Servlet engine.  Basically connect it to :8081 and add the /bell and /* mappings.
 */
public class DoorbellServer {
    private Server server;
    public void start() throws Exception {
        // This "Server" is the one we get for free from jetty.server.Server.
        server = new Server();
	// A connector is required to bind the server to a port on the computer.
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8081);
        server.setConnectors(new Connector[] {connector});
	// The handler is basically the "OK, I got a request, who do I send it to?" answer.
	ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);
	// We bind the specific case of /bell, then the default handler for everything else.
	servletHandler.addServletWithMapping(DoorbellServlet.class, "/bell");
	servletHandler.addServletWithMapping(StatusServlet.class, "/*");
	// Finally, we just call Jetty's server.start() to run the server.
        server.start();
    }
}
