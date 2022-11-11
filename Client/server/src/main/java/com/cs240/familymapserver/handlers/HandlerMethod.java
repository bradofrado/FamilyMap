package com.cs240.familymapserver.handlers;

import java.io.IOException;

/**
 * Interface for the methods that get called when a route is sent
 */
interface HandlerMethod {
    void run(Request request) throws IOException;
}
