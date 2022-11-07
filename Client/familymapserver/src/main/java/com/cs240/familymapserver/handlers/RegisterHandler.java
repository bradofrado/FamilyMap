package com.cs240.familymapserver.handlers;

import com.cs240.familymapmodules.requests.RegisterRequest;
import com.cs240.familymapmodules.results.RegisterResult;
import com.cs240.familymapserver.services.RegisterService;
import com.cs240.familymapmodules.util.Encoder;

import java.io.IOException;

/**
 * Handles the endpoint for /user/register
 */
public class RegisterHandler extends Handler {
    @Override
    protected void initRoutes() {
        post("/user/register", this::register);
    }

    /**
     * Endpoint for /user/register. Registers the user and sends back the auth token
     * @param request Contains the new user information
     * @throws IOException
     */
    private void register(Request request) throws IOException {
        RegisterRequest registerRequest = Encoder.Decode(request.getBody(), RegisterRequest.class);
        RegisterResult result = RegisterService.Register(registerRequest);

        sendResult(result);
    }
}
