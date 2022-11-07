package com.cs240.familymapmodules.results;

/**
 * The result of the /user/login api call
 */
public class LoginResult extends Result {
    /**
     * The auth token of the logged in user
     */
    private String authtoken;
    /**
     * The username of the logged in user
     */
    private String username;
    /**
     * The person id of the logged in user
     */
    private String personID;

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken=authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID=personID;
    }
}
