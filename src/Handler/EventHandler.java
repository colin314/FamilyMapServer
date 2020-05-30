package Handler;

import Request.BadRequest;
import Result.*;
import Services.EventService;
import Services.UnauthorizedException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.HttpURLConnection;
import com.google.gson.*;
import java.io.IOException;

public class EventHandler extends Handler implements HttpHandler {
    public EventHandler() {}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    String uri = exchange.getRequestURI().getPath();
                    String eventID = uri.substring(uri.lastIndexOf('/') + 1);
                    var service = new EventService();
                    if (!eventID.equalsIgnoreCase("event")) {
                        EventIDResponse response = null;
                        response = service.getEventByID(eventID, authToken);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(new Gson().toJson(response).getBytes());
                    } else {
                        EventResponse response = null;
                        response = service.getEventByUser(authToken);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(new Gson().toJson(response).getBytes());
                    }
                }
                else {
                    throw new BadRequest("No Authorization header was provided");
                }
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
            writeError(exchange, ex, HttpURLConnection.HTTP_BAD_REQUEST);
        }
        catch (UnauthorizedException ex) {
            writeError(exchange, ex, HttpURLConnection.HTTP_BAD_REQUEST);
        }
        catch (BadRequest ex) {
            writeError(exchange, ex, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }
}
