package models;

/**
 * The model for the auth token for the user
 */
public class AuthToken {
    /**
     * The auth token string
     */
    private String authtoken;

    /**
     * The username associated with this auth token
     */
    private String username;

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
}
