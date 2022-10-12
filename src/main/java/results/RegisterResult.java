package results;

/**
 * The result for the /user/register api call
 */
public class RegisterResult extends Result {
    /**
     * The auth token of the registered user
     */
    private String authtoken;
    /**
     * The username of the registered user
     */
    private String username;
    /**
     * The person id of the registered user
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
