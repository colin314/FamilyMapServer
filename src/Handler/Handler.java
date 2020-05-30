package Handler;

import Result.Response;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Handler {
    public Handler() {}

    protected void writeError(HttpExchange exchange, Throwable ex, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, 0);
        OutputStream outputStream = exchange.getResponseBody();
        Response res = new Response("error: " + ex.getMessage(), false);
        String output = new Gson().toJson(res);
        outputStream.write(output.getBytes());
        exchange.getResponseBody().close();
    }

}
