package Handler;

import Result.FamilyMapException;
import Result.Response;
import Services.ClearService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.HttpURLConnection;
import com.google.gson.*;
import java.io.IOException;

public class ClearHandler extends Handler implements HttpHandler {
    public ClearHandler() {}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                Response response = null;
                var service = new ClearService();
                response = service.clearDatabase();

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(new Gson().toJson(response).getBytes());
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            exchange.getResponseBody().close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        catch (FamilyMapException ex) {
            writeError(exchange, ex, HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }
}
