package handlers;

import requests.LoginRequest;
import results.LoginResult;
import services.LoginService;
import util.Encoder;

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
