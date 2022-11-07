package com.cs240.familymapserver.handlers;

import com.cs240.familymapmodules.results.ClearResult;
import com.cs240.familymapserver.services.ClearService;

import java.io.IOException;

/**
 * Handler for the /clear endpoint
 */
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
