package Handler;

import Request.BadRequest;
import Request.RegisterRequest;
import Result.Response;
import Result.FamilyMapException;
import Result.UserResponse;
import Services.RegisterService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import com.google.gson.*;
import java.io.IOException;

public class RegisterRequestHandler extends Handler implements HttpHandler {
    public RegisterRequestHandler() {}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // However, it does demonstrate the following:
        // 1. How to get the HTTP request type (or, "method")
        // 2. How to access HTTP request headers
        // 3. How to read JSON data from the HTTP request body
        // 4. How to return the desired status code (200, 404, etc.)
        //		in an HTTP response
        // 5. How to check an incoming HTTP request for an auth token

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    // Extract the auth token from the "Authorization" header
                    String authToken = reqHeaders.getFirst("Authorization");
                    // Verify that the auth token is the one we're looking for
                    if (authToken.equals("afj232hj2332")) {
                        // Extract the JSON string from the HTTP request body

                        // Get the request body input stream
                        InputStream reqBody = exchange.getRequestBody();

                        // Read JSON string from the input stream
                        String reqData = readString(reqBody);

                        //Deserialize the JSON into the request object
                        RegisterRequest request = new Gson().fromJson(reqData, RegisterRequest.class);
                        if (request == null) { throw new BadRequest("The request body was invalid"); }
                        request.validateRequest();
                        //Generate the response
                        UserResponse response = null;
                        RegisterService service = new RegisterService();
                        response = service.registerUser(request);


                        // Start sending the HTTP response to the client, starting with
                        // the status code and any defined headers.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(new Gson().toJson(response).getBytes());
                    } else {
                        // The auth token was invalid somehow, so we return a "not authorized"
                        // status code to the client.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    }
                } else {
                    // We did not get an auth token, so we return a "not authorized"
                    // status code to the client.
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }
            } else {
                // We expected a POST but got something else, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();
        } catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
        catch (FamilyMapException r) {
            writeError(exchange, r, HttpURLConnection.HTTP_INTERNAL_ERROR);
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
