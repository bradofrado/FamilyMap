package com.cs240.familymapserver.services;

import com.cs240.familymapmodules.results.ClearResult;
import com.cs240.familymapserver.util.ClearUtil;

import java.sql.SQLException;

/**
 * The service class for clearing the database
 */
public class ClearService {

    /**
     * Clears the database of data
     * @return The status of the clear
     */
    public static ClearResult Clear() {
        ClearResult result = new ClearResult();

        try {
            ClearUtil.ClearDatabase();
            result.setMessage("Clear succeeded");
        } catch (SQLException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        }

        return result;
    }
}
