package com.cs240.familymapmodules.requests;

/**
 * The request object for the /person api call
 */
public class PersonsRequest {
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
