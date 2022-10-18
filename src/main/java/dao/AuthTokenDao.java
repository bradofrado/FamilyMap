package dao;

import models.AuthToken;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthTokenDao extends Dao {
    /**
     * Adds an auth token to the database
     * @param token The auth token object to add
     */
    public void AddAuthToken(AuthToken token) throws SQLException {
        doTransaction((connection) -> {
            addToken(connection, token);
        });
    }

    /**
     * Gets the auth token of the specified username
     * @param token The user of the wanted auth token
     * @return The auth token model object
     */
    public AuthToken getAuthToken(String token) throws SQLException {
        List<AuthToken> tokens = new ArrayList<>();
        doTransaction((connection) -> {
            tokens.add(getToken(connection, token));
        });

        return tokens.size() > 0 ? tokens.get(0) : null;
    }

    /**
     * Deletes all auth token data from the database
     */
    public void DeleteAll() throws SQLException {
        doTransaction((connection) -> {
            deleteToken(connection, null);
        });
    }

    /**
     * Deletes all of the auth tokens for a given user
     * @param username The username of the auth tokens to delete
     * Deletes all auth token data for the current user
     */
    public void DeleteAll(String username) throws SQLException {
        doTransaction((connection) -> {
            deleteToken(connection, username);
        });
    }

    private void addToken(Connection connection, AuthToken token) throws SQLException {
        String sql = "insert into AuthToken (authtoken, username) values(?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, token.getAuthtoken());
            stmt.setString(2, token.getUsername());

            if (stmt.executeUpdate() == 1) {
                System.out.println("Inserted token " + token);
            } else {
                System.out.println("Failed to insert token");
            }
        }
    }

    private AuthToken getToken(Connection connection, String authtoken) throws SQLException {
        String sql = "select authtoken, username from AuthToken where authtoken = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authtoken);

            ResultSet set = stmt.executeQuery();
            if (set.next()) {
                AuthToken token = new AuthToken(set.getString(1), set.getString(2));
                return token;
            }

            return null;
        }
    }

    private void deleteToken(Connection connection, String username) throws  SQLException {
        String sql = "delete from AuthToken";
        if (username != null) {
            sql += " where username = ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (username != null) {
                stmt.setString(1, username);
            }

            int count = stmt.executeUpdate();

            System.out.printf("Deleted %d tokens\n", count);
        }
    }
}
