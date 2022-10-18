package dao;

import models.User;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends Dao {
    /**
     * Creates a new user in the database
     * @param user The user to create
     */
    public void AddUser(User user) throws SQLException {
        doTransaction((connection) -> {
            insertUsers(connection, new ArrayList<>() { { add(user); } });
        });
    }

    /**
     * Adds a list of users into the database
     * @param users The list of user objects to add to the database
     */
    public void AddUsers(List<User> users) throws SQLException {
        doTransaction((connection) -> {
            insertUsers(connection, users);
        });
    }

    /**
     * Gets a user model object given the user's Id
     * @param username The username of the user to get
     * @return The wanted User model object
     */
    public User GetUser(String username) throws SQLException {
        List<User> users = new ArrayList<>();
        doTransaction((connection) -> {
            users.add(getUser(connection, username));
        });

        return users.get(0);
    }

    /**
     * Deletes all auth token data from the database
     */
    public void DeleteAll() throws SQLException{
        doTransaction((connection) -> {
            deleteUsers(connection, null);
        });
    }

    private void insertUsers(Connection connection, List<User> users) throws SQLException {
        String sql="insert into user (username, password, email, firstname, lastname, gender, personID) values (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt=connection.prepareStatement(sql)) {
            for (User user : users) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getFirstName());
                stmt.setString(5, user.getLastName());
                stmt.setString(6, Character.toString(user.getGender()));
                stmt.setString(7, user.getPersonID());

                if (stmt.executeUpdate() == 1) {
                    System.out.println("Inserted user " + user);
                } else {
                    System.out.println("Failed to insert person");
                }
            }
        }
    }

    private User getUser(Connection connection, String userId) throws SQLException {
        String sql="select username, password, email, firstname, lastname, gender, personID from user where username = ?";

        try (PreparedStatement stmt=connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6).charAt(0), rs.getString(7));
            }

            return user;
        }
    }

    private void deleteUsers(Connection connection, String personID) throws SQLException {
        String sql = "delete from user";
        if (personID != null) sql += " where personID = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (personID != null) {
                stmt.setString(1, personID);
            }
            int count = stmt.executeUpdate();

            // Reset the auto-increment counter so new books start over with an id of 1
//            sql = "delete from sqlite_sequence where name = 'book'";
//            try(PreparedStatement resetSequenceStatement = connection.prepareStatement(sql)) {
//                resetSequenceStatement.executeUpdate();
//            }

            System.out.printf("Deleted %d users\n", count);
        }
    }
}
