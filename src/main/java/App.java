import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class App {
    private static Server server;
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws Exception {
        int maxThreads = 1000;
        int minThreads = 10;
        int idleTimeout = 120;
        QueuedThreadPool queuedThreadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
        server = new Server(queuedThreadPool);
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(8080);
        server.addConnector(serverConnector);

        ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);
        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/main/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(
                "jersey.config.server.provider.packages",
                "resources"
        );
        try {
            server.start();
            logger.info("App is up and running on port 8080");
            server.join();
        } catch (Exception ex) {
            logger.error("Error occurred while starting Jetty", ex);
            System.exit(1);
        }
        finally {
            server.destroy();
        }
    }
}
