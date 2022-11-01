package services;

import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import requests.LoadRequest;
import results.LoadResult;
import util.ClearUtil;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * The service class for the /load endpoints
 */
public class LoadService {
    /**
     * Loads the information from the request object into the database
     * @param request The request object that holds the user, person, and event data to load
     * @return The result of the load
     */
    public static LoadResult Load(LoadRequest request) {
        LoadResult result = new LoadResult();
        try {
            ClearUtil.ClearDatabase();

            new UserDao().AddUsers(Arrays.asList(request.getUsers()));
            new PersonDao().AddPersons(Arrays.asList(request.getPersons()));
            new EventDao().AddEvents(Arrays.asList(request.getEvents()));

            String message = "Successfully added " + request.getUsers().length + " users, " + request.getPersons().length +
                    " persons, and " + request.getEvents().length + " events to the database.";
            result.setMessage(message);
        } catch(SQLException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        }

        return result;
    }
}
