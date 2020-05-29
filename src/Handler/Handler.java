package Handler;

import Result.Response;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public abstract class Handler {
    public Handler() {}

    protected void writeError(HttpExchange exchange, Throwable ex, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, 0);
        OutputStream outputStream = exchange.getResponseBody();
        String output = new Gson().toJson(new Response(ex.getMessage(), false));
        outputStream.write(output.getBytes());
        exchange.getResponseBody().close();
    }

}
