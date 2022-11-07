package com.cs240.familymapserver.util;

import com.cs240.familymapserver.dao.AuthTokenDao;
import com.cs240.familymapserver.dao.UserDao;
import com.cs240.familymapmodules.exceptions.InvalidAuthTokenException;
import com.cs240.familymapmodules.exceptions.InvalidCredentialsException;
import com.cs240.familymapmodules.models.AuthToken;
import com.cs240.familymapmodules.models.User;

import java.sql.SQLException;

/**
 * Util class for User methods
 */
public class UserUtil {
    /**
     * Login the user in and returns an auth token
     * @param username The user to log in
     * @return Auth Token
     * @throws SQLException
     */
    public static String LoginUser(String username, String password) throws SQLException, InvalidCredentialsException {
        User user = new UserDao().GetUser(username);
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        if (!user.getPassword().equals(password)){
            throw new InvalidCredentialsException();
        }

        String token = DataGenerator.getRandomId();

        new AuthTokenDao().AddAuthToken(new AuthToken(token, user.getUsername()));

        return token;
    }

    /**
     * Gets the user given an auth token
     * @param authToken
     * @return The user object for this auth token
     * @throws SQLException
     */
    public static User GetUser(String authToken) throws SQLException, InvalidAuthTokenException {
        AuthToken token = new AuthTokenDao().getAuthToken(authToken);
        if (token == null) {
            throw new InvalidAuthTokenException();
        }

        User user = new UserDao().GetUser(token.getUsername());

        if (user == null) {
            throw new InvalidAuthTokenException();
        }

        return user;
    }
}
