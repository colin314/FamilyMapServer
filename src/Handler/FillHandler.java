package Handler;

import Request.BadRequest;
import Result.FamilyMapException;
import Result.Response;
import Services.FillService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.HttpURLConnection;
import com.google.gson.*;
import java.io.IOException;

public class FillHandler extends Handler implements HttpHandler {
    public FillHandler() {}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                String query = exchange.getRequestURI().getPath();
                String userName = query.substring(query.indexOf('/', 5) + 1,query.lastIndexOf('/'));
                String genStr = query.substring(query.lastIndexOf('/') + 1);
                if (!userName.equals("")) {
                    //String userName = reqHeaders.getFirst("username");
                    int generations = 4;
                    if (!genStr.equals("")) {
                        generations = Integer.parseInt(genStr);
                    }

                    Response response = null;
                    var service = new FillService();
                    response = service.fillDatabase(userName, generations);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(new Gson().toJson(response).getBytes());
                }
                else {
                    throw new BadRequest("No username header was provided");
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
