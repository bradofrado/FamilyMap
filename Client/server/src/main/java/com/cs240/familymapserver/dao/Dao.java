package com.cs240.familymapserver.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dao {
    private final String dbName = "db" + File.separator + "FamilyMapDB.db";
    private final String connectionURL = "jdbc:sqlite:" + dbName;

    private Connection connection;
    private boolean isTransaction;

    public void startTransaction() {
        isTransaction = true;
    }

    public void endTransaction() {
        closeConnection();
        isTransaction = false;
    }

    protected void doTransaction(Transaction func) throws SQLException {
        try {
            if (!isTransaction || connection == null) {
                connection = DriverManager.getConnection(connectionURL);
            }

            // Start a transaction
            connection.setAutoCommit(false);

            func.run(connection);

            if (!isTransaction) {
                closeConnection();
            }
        } catch(SQLException ex) {
            if(connection != null) {
                connection.rollback();
            }
            throw ex;
        }
    }

    public void closeConnection() {
        if (connection == null) {
            return;
        }

        try {
            if (!isTransaction) {
                // This will commit the changes to the database
                connection.commit();
            } else {
                // If we find out something went wrong, pass a false into closeConnection and this
                // will rollback any changes we made during this connection
                connection.rollback();
            }
            connection.close();
            connection = null;
        } catch (SQLException e) {
            // If you get here there are probably issues with your code and/or a connection is being left open
            e.printStackTrace();
        }
    }
}
