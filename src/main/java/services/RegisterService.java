package services;

import dao.UserDao;
import exceptions.InvalidCredentialsException;
import models.User;
import requests.RegisterRequest;
import results.RegisterResult;
import util.UserUtil;
import util.PopulationGenerator;

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
            new UserDao().AddUser(newUser);
            PopulationGenerator.populateGenerations(newUser.getUsername(), 4);
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
        }

        return result;
    }
}
