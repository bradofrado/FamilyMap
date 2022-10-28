package services;

import dao.UserDao;
import models.User;
import requests.FillRequest;
import results.FillResult;
import util.ClearUtil;
import util.PopulationGenerator;
import util.UserUtil;

import java.sql.SQLException;

/**
 * The service class for the /fill endpoints
 */
public class FillService {
    /**
     * Fills a number of generations for the current user
     * @param request The request object holding the username and the number of generations to fill
     * @return The result of the fill
     */
    public static FillResult Fill(FillRequest request) {
        FillResult result = new FillResult();
        try {
            User user = new UserDao().GetUser(request.getUsername());
            ClearUtil.ClearForUser(request.getUsername());

            PopulationGenerator.populateGenerations(user, request.getGenerations());
        } catch(SQLException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        }

        return result;
    }
}
