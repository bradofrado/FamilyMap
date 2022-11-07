package com.cs240.familymapserver.services;

import com.cs240.familymapserver.dao.UserDao;
import com.cs240.familymapmodules.exceptions.InvalidCredentialsException;
import com.cs240.familymapmodules.exceptions.InvalidNumberOfGenerations;
import com.cs240.familymapmodules.models.User;
import com.cs240.familymapmodules.requests.RegisterRequest;
import com.cs240.familymapmodules.results.RegisterResult;
import com.cs240.familymapserver.util.UserUtil;
import com.cs240.familymapserver.util.PopulationGenerator;

import java.sql.SQLException;

/**
 * A Service class for registering users
 */
public class RegisterService {
    /**
     * Registers a username
     * @param request The request object holding the username to register
     * @return
     */
    public static RegisterResult Register(RegisterRequest request) {
        RegisterResult result = new RegisterResult();

        User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getGender(), "");

        try {
            PopulationGenerator.populateGenerations(newUser, 4);
            new UserDao().AddUser(newUser);
            String token = UserUtil.LoginUser(newUser.getUsername(), newUser.getPassword());

            result.setAuthtoken(token);
            result.setUsername(newUser.getUsername());
            result.setPersonID(newUser.getPersonID());
        } catch (SQLException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        } catch (InvalidCredentialsException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        } catch (InvalidNumberOfGenerations ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        }

        return result;
    }
}
