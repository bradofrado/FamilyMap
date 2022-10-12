package requests;

/**
 * The event request object for the /event/id api call
 */
public class EventRequest {
    /**
     * The event id
     */
    private String eventID;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID=eventID;
    }
}
