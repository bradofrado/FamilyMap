package services;

import dao.UserDao;
import exceptions.InvalidCredentialsException;
import models.User;
import results.LoginResult;
import requests.LoginRequest;
import util.UserUtil;

import java.sql.SQLException;

/**
 * The service class for loggin in the user
 */
public class LoginService {
    /**
     * Logs in the given user
     * @param request The request holding the user to log ing
     * @return The login result which has the auth token for the user
     */
    public static LoginResult Login(LoginRequest request) {
        LoginResult result = new LoginResult();

        try {
            String token = UserUtil.LoginUser(request.getUsername(), request.getPassword());
            User user = new UserDao().GetUser(request.getUsername());

            result.setAuthtoken(token);
            result.setUsername(user.getUsername());
            result.setPersonID(user.getPersonID());
        } catch(SQLException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        } catch (InvalidCredentialsException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        }

        return result;
    }
}
