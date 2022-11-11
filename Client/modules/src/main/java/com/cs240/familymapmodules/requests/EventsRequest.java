package com.cs240.familymapmodules.requests;

/**
 * The request object for the /event api call
 */
public class EventsRequest {
    /**
     * This requests auth token
     */
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken=authToken;
    }
}
