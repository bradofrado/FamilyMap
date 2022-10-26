package handlers;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * The default handler for all "/" or file end points
 */
public class FileHandler extends Handler {
    /**
     * Endpoint for / or any file. / defaults to /index.html. Any file will be send back
     * @param request
     * @throws IOException
     */
    private void get(Request request) throws IOException {
        String path = request.getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }

        String filePath = "web" + path;
        File file = new File(filePath);
        if (!file.exists()) {
            sendStatus(HttpURLConnection.HTTP_NOT_FOUND);
            return;
        }


        sendFile(HttpURLConnection.HTTP_OK, file);
    }

    @Override
    protected void initRoutes() {
        get("/", this::get);
    }
}
