package com.cs240.familymapserver.handlers;

import com.cs240.familymapmodules.requests.LoginRequest;
import com.cs240.familymapmodules.results.LoginResult;
import com.cs240.familymapserver.services.LoginService;
import com.cs240.familymapmodules.util.Encoder;

import java.io.IOException;

public class LoginHandler extends Handler{
    @Override
    protected void initRoutes() {
        post("/user/login", this::login);
    }

    private void login(Request request) throws IOException {
        LoginRequest loginRequest =Encoder.Decode(request.getBody(), LoginRequest.class);
        LoginResult result =LoginService.Login(loginRequest);

        sendResult(result);
    }
}
