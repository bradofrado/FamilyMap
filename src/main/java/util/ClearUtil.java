package util;

import dao.AuthTokenDao;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;

import java.sql.SQLException;

/**
 * Util class for clearing things from the database
 */
public class ClearUtil {
    /**
     * Clears all data from all the tables (user, person, event, authtoken)
     * @throws SQLException
     */
    public static void ClearDatabase() throws SQLException {
        new UserDao().DeleteAll();
        new PersonDao().DeleteAll();
        new EventDao().DeleteAll();
        new AuthTokenDao().DeleteAll();
    }
}
