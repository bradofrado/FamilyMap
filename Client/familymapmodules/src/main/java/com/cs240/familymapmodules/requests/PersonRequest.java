package com.cs240.familymapmodules.requests;

/**
 * The person request object of the person/id api call
 */
public class PersonRequest {
    /**
     * The person id to retrieve
     */
    private String personID;

    /**
     * This requests auth token
     */
    private String authToken;

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID=personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken=authToken;
    }
}
