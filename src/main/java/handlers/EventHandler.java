package handlers;

import requests.EventRequest;
import requests.EventsRequest;
import results.EventResult;
import results.EventsResult;
import services.EventService;
import util.Encoder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * The http handler for the /event and /event/{id} paths
 */
public class EventHandler extends Handler {
    @Override
    protected void initRoutes() {
        get("/event", this::getEvents);
        get("/event/{id}", this::getEvent);
    }

    /**
     * The end point for /event. Gets all of the events for the user
     * @param request The request object
     * @throws IOException
     */
    private void getEvents(Request request) throws IOException {
        EventsRequest eventRequest = new EventsRequest();
        eventRequest.setAuthToken(request.getAuthToken());

        EventsResult result = EventService.Events(eventRequest);

        sendResult(result);
    }

    /**
     * The endpoint for the /event/[id]. Gets the event for the given id
     */
    private void getEvent(Request request) throws IOException {
        EventRequest eventRequest = new EventRequest();
        eventRequest.setAuthToken(request.getAuthToken());

        Map<String, String> params = request.getParameters();
        eventRequest.setEventID(params.get("id"));

        EventResult result = EventService.Event(eventRequest);

        sendResult(result);
    }
}
