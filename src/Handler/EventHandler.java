package Handler;

import Request.BadRequest;
import Request.LoginRequest;
import Result.*;
import Services.EventService;
import Services.LoginService;
import Services.PersonService;
import Services.UnauthorizedException;
import com.sun.java.accessibility.util.EventID;
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
                    var service = new EventService();
                    if (reqHeaders.containsKey("eventID")) {
                        String personID = reqHeaders.getFirst("eventID");
                        EventIDResponse response = null;
                        response = service.getEventByID(personID, authToken);

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
            writeError(exchange, ex, HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
        catch (UnauthorizedException ex) {
            writeError(exchange, ex, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
        catch (BadRequest ex) {
            writeError(exchange, ex, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}