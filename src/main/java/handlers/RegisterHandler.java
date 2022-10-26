package handlers;

import requests.RegisterRequest;
import results.RegisterResult;
import services.RegisterService;
import util.Encoder;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RegisterHandler extends Handler {
    @Override
    protected void initRoutes() {
        post("/user/register", this::register);
    }

    private void register(Request request) throws IOException {
        RegisterRequest registerRequest = Encoder.Decode(request.getBody(), RegisterRequest.class);
        RegisterResult result = RegisterService.Register(registerRequest);

        send(HttpURLConnection.HTTP_OK, Encoder.Encode(result));
    }
}
