package Handler;

import Request.BadRequest;
import Request.LoadRequest;
import Result.FamilyMapException;
import Result.Response;
import Services.LoadService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.HttpURLConnection;
import com.google.gson.*;
import java.io.IOException;

public class LoadHandler extends Handler implements HttpHandler {
    public LoadHandler() {}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                var request = new Gson().fromJson(reqData, LoadRequest.class);
                request.validate();

                Response response = null;
                var service = new LoadService();
                response = service.loadData(request);

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
        catch (BadRequest ex) {
            writeError(exchange, ex, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

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
