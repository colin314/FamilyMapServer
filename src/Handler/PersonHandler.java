package Handler;

import Request.BadRequest;
import Result.FamilyMapException;
import Result.PersonIDResponse;
import Result.PersonResponse;
import Services.PersonService;
import Services.UnauthorizedException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.HttpURLConnection;
import com.google.gson.*;
import java.io.IOException;

public class PersonHandler extends Handler implements HttpHandler {
    public PersonHandler() {}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                String url = exchange.toString();
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    String uri = exchange.getRequestURI().getPath();
                    String personID = uri.substring(uri.lastIndexOf('/') + 1);
                    if (!personID.equalsIgnoreCase("person")) {
                        PersonIDResponse response = null;
                        var service = new PersonService();
                        response = service.getPersonByID(personID, authToken);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(new Gson().toJson(response).getBytes());
                    } else {
                        PersonResponse response = null;
                        var service = new PersonService();
                        response = service.getPersonByUsername(authToken);

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
