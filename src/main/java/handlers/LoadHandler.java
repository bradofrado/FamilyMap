package handlers;

import jdk.jfr.Frequency;
import requests.LoadRequest;
import results.LoadResult;
import services.LoadService;
import util.Encoder;

import java.io.IOException;

public class LoadHandler extends Handler {

    @Override
    protected void initRoutes() {
        post("/load", this::load);
    }

    private void load(Request request) throws IOException {
        LoadRequest loadRequest = Encoder.Decode(request.getBody(), LoadRequest.class);
        LoadResult result =LoadService.Load(loadRequest);

        sendResult(result);
    }
}
