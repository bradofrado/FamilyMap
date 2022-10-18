package models;

import java.util.Objects;

/**
 * Model for the User database table
 */
public class User {
    /**
     * The username of this user
     */
    private String username;
    /**
     * This user's password
     */
    private String password;
    /**
     * This user's email
     */
    private String email;
    /**
     * This user's first name
     */
    private String firstName;
    /**
     * This user's lastName
     */
    private String lastName;
    /**
     * This user's gender 'f' or 'm'
     */
    private char gender;
    /**
     * This user's person id
     */
    private String personID;

    public User(String username, String password, String email, String firstName, String lastName, char gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
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

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender=gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID=personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user=(User) o;
        return gender == user.gender &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(personID, user.personID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, firstName, lastName, gender, personID);
    }
}
