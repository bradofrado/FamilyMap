package com.cs240.familymapserver.handlers;

import com.cs240.familymapmodules.requests.LoadRequest;
import com.cs240.familymapmodules.results.LoadResult;
import com.cs240.familymapserver.services.LoadService;
import com.cs240.familymapserver.util.Encoder;

import java.io.IOException;

/**
 * Http handler for the /load endpoint
 */
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
