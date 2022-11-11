package com.cs240.familymapserver.handlers;

import com.cs240.familymapmodules.requests.FillRequest;
import com.cs240.familymapmodules.results.FillResult;
import com.cs240.familymapserver.services.FillService;

import java.io.IOException;
import java.util.Map;

/**
 * Http handler for the /fill/{username}/{generations} endpoint
 */
public class FillHandler extends Handler {

    @Override
    protected void initRoutes() {
        post("/fill/{username}", this::fillDefault);
        post("/fill/{username}/{generations}", this::fill);
    }

    private void fillDefault(Request request) throws IOException {
        FillResult result = doFill(request.getParameters());

        sendResult(result);
    }

    private void fill(Request request) throws IOException {
        FillResult result = doFill(request.getParameters());

        sendResult(result);
    }

    private FillResult doFill(Map<String, String> parameters) {
        try {
            int generations=parameters.containsKey("generations") ? Integer.parseInt(parameters.get("generations")) : 4;
            String username=parameters.get("username");

            FillRequest request=new FillRequest();
            request.setUsername(username);
            request.setGenerations(generations);

            return FillService.Fill(request);
        } catch (NumberFormatException ex) {
            FillResult result = new FillResult();
            result.setSuccess(false);
            result.setMessage("Generation must be a number");

            return result;
        }
    }
}
