package handlers;

import requests.FillRequest;
import results.FillResult;
import services.FillService;
import util.Encoder;

import java.io.IOException;
import java.security.spec.ECField;

public class FillHandler extends Handler {

    @Override
    protected void initRoutes() {
        post("/fill/{username}", this::fillDefault);
        post("/fill/{username}/{generations}", this::fill);
    }

    private void fillDefault(Request request) throws IOException {
        String username = request.getParameters().get("username");
        FillResult result = doFill(username, 4);

        sendResult(result);
    }

    private void fill(Request request) throws IOException {
        int generations = Integer.parseInt(request.getParameters().get("generations"));
        String username = request.getParameters().get("username");

        FillResult result = doFill(username, generations);

        sendResult(result);
    }

    private FillResult doFill(String username, int generations) {
        FillRequest request = new FillRequest();
        request.setUsername(username);
        request.setGenerations(generations);

        return FillService.Fill(request);
    }
}
