package requests;

/**
 * The event request object for the /event/id api call
 */
public class EventRequest {
    /**
     * The event id
     */
    private String eventID;

    /**
     * This requests auth token
     */
    private String authToken;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID=eventID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken=authToken;
    }
}
