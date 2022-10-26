package handlers;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

public class FileHandler extends Handler {
    private void get(Request request) throws IOException {
        String path = request.getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }

        String filePath = "web" + path;
        File file = new File(filePath);
        if (!file.exists()) {
            sendStatus(HttpURLConnection.HTTP_NOT_FOUND);
        }


        sendFile(HttpURLConnection.HTTP_OK, file);
    }

    @Override
    protected void initRoutes() {
        get("/", this::get);
    }
}
