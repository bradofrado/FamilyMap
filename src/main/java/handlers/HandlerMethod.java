package handlers;

import java.io.IOException;

interface HandlerMethod {
    void run(Request request) throws IOException;
}
