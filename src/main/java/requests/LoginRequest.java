package requests;

import models.User;

/**
 * The request object for the /user/login api call
 */
public class LoginRequest {
    /**
     * The username to login
     */
    private String username;
    /**
     * The password to login
     */
    private String password;

    public LoginRequest() {}

    public LoginRequest(User user) {
        username = user.getUsername();
        password = user.getPassword();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }
}
