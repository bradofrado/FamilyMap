package handlers;

import results.ClearResult;
import services.ClearService;
import util.Encoder;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ClearHandler extends Handler {
    @Override
    protected void initRoutes() {
        post("/clear", this::clear);
    }

    private void clear(Request request) throws IOException {
        ClearResult result = ClearService.Clear();

        sendResult(result);
    }
}
