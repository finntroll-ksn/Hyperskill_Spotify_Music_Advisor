package advisor;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerConfigs {

    private HttpServer httpServer;
    private final String context = "/";
    String query;
    int port = 8080;

    HttpServerConfigs() {
        initServer();
    }

    void initServer() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(port), 0);
            httpServer.createContext(context, exchange -> {
                query = exchange.getRequestURI().getQuery();

                String hello = "";

                if (query != null && query.startsWith("code=")) {
                    Config.AUTH_CODE = query.substring(5);
                    hello = "Got the code. Return back to your program.";
                } else {
                    hello = "Authorization code not found. Try again.";
                }

                exchange.sendResponseHeaders(200, hello.length());
                exchange.getResponseBody().write(hello.getBytes());
                exchange.getResponseBody().close();
                exchange.close();
            });

            httpServer.setExecutor(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void webStart() {
        this.httpServer.start();
    }

    public void webStop(int i) {
        this.httpServer.stop(i);
    }
}
