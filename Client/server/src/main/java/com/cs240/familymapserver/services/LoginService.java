package com.cs240.familymapserver.services;

import com.cs240.familymapmodules.exceptions.InvalidCredentialsException;
import com.cs240.familymapmodules.models.User;
import com.cs240.familymapmodules.requests.LoginRequest;
import com.cs240.familymapmodules.results.LoginResult;
import com.cs240.familymapserver.dao.UserDao;
import com.cs240.familymapserver.util.UserUtil;

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
