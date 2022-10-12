package dao;

import models.User;

import java.util.List;

public class UserDao {
    /**
     * Creates a new user in the database
     * @param user The user to create
     */
    public void AddUser(User user) {

    }

    /**
     * Adds a list of users into the database
     * @param users The list of user objects to add to the database
     */
    public void AddUsers(List<User> users) {

    }


    /**
     * Gets a user model object given the user's Id
     * @param userId The id of the user to get
     * @return The wanted User model object
     */
    public User getUser(String userId) {
        return null;
    }

    /**
     * Deletes all auth token data from the database
     */
    public void DeleteAll() {

    }
}
