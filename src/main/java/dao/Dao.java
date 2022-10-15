package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dao {
    private final String dbName = "db" + File.separator + "FamilyMapDB.db";
    private final String connectionURL = "jdbc:sqlite:" + dbName;

    protected void doTransaction(Transaction func) throws SQLException {
        Connection connection = null;
        try(Connection c = DriverManager.getConnection(connectionURL)) {
            connection = c;

            // Start a transaction
            connection.setAutoCommit(false);

            func.run(connection);

            // Commit the transaction
            connection.commit();
        } catch(SQLException ex) {
            if(connection != null) {
                connection.rollback();
            }
            throw ex;
        }
    }
}
