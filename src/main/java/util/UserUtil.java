package util;

import dao.AuthTokenDao;
import dao.UserDao;
import exceptions.InvalidAuthTokenException;
import exceptions.InvalidCredentialsException;
import models.AuthToken;
import models.User;

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

        new AuthTokenDao().AddAuthToken(new AuthToken("abc", user.getUsername()));

        return "abc";
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
