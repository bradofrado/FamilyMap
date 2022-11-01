import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import handlers.*;

public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;

    private HttpServer server;

    // This method initializes and runs the server.
    // The "portNumber" parameter specifies the port number on which the
    // server should accept incoming client connections.
    private void run(String portNumber) {

        System.out.println("Initializing HTTP Server");

        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        // This line is necessary, but its function is unimportant for our purposes.
        server.setExecutor(null);

        // Log message indicating that the server is creating and installing
        // its HTTP handlers.
        // The HttpServer class listens for incoming HTTP requests.  When one
        // is received, it looks at the URL path inside the HTTP request, and
        // forwards the request to the handler for that URL path.
        System.out.println("Creating contexts");

        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/clear", new ClearHandler());

        // Create and install the "default" (or "file") HTTP handler.
        // All requests that do not match the other handler URLs
        // will be passed to this handle.
        // These are requests to download a file from the server
        // (e.g., web site files)
        server.createContext("/", new FileHandler());

        System.out.println("Starting server");

        server.start();

        System.out.println("Server started on port " + portNumber);
    }

    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
