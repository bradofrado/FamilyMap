package requests;

/**
 * The request for the /user/register api call
 */
public class RegisterRequest {
    /**
     * The user name of the new user
     */
    private String username;
    /**
     * The password of the new user
     */
    private String password;
    /**
     * The first name of the new user
     */
    private String firstName;
    /**
     * The last name of the new user
     */
    private String lastName;
    /**
     * The gender of the new user 'f' or 'm'
     */
    private String gender;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName=firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName=lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender=gender;
    }
}
