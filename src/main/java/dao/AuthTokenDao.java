package dao;

import models.AuthToken;

public class AuthTokenDao {
    /**
     * Adds an auth token to the database
     * @param token The auth token object to add
     */
    public void AddAuthToken(AuthToken token) {

    }

    /**
     * Gets the auth token of the specified username
     * @param username The user of the wanted auth token
     * @return The auth token model object
     */
    public AuthToken GetAuthToken(String username) {
        return null;
    }

    /**
     * Deletes all auth token data from the database
     */
    public void DeleteAll() {

    }

    /**
     * Deletes all of the auth tokens for a given user
     * @param username The username of the auth tokens to delete
     * Deletes all auth token data for the current user
     */
    public void DeleteAll(String username) {

    }
}
