package services;

import requests.EventRequest;
import requests.EventsRequest;
import results.EventResult;
import results.EventsResult;

/**
 * The service class for the /event endpoints
 */
public class EventService {
    /**
     * Gets a event with the given id
     * @param request The request object of the wanted event
     * @return The result object holding the wanted event
     */
    public static EventResult Event(EventRequest request) {
        return null;
    }

    /**
     * Gets all of the events for a given user
     * @param request The request object holding the current user
     * @return A result object holding a list of the wanted events
     */
    public static EventsResult Events(EventsRequest request) {
        return null;
    }
}
