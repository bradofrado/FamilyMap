package com.cs240.familymapserver.dao;

import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;
import com.cs240.familymapmodules.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDao extends Dao {
    /**
     * Adds an event to the database
     * @param event The Event to add
     */
    public void AddEvent(Event event) throws SQLException {
        doTransaction((connection) -> {
            insertEvents(connection, new ArrayList() {{add(event);}});
        });
    }

    /**
     * Adds a list of events into the database
     * @param events The list of event objects to add to the database
     */
    public void AddEvents(List<Event> events) throws SQLException {
        doTransaction((connection) -> {
            insertEvents(connection, events);
        });
    }

    /**
     * Gets a Event object given a person id
     * @param eventID The id of the event you want
     * @param username The current user associated with this person
     * @return The wanted event object
     */
    public Event GetEvent(String eventID, String username) throws SQLException {
        List<Event> events = new ArrayList<>();
        doTransaction((connection) -> {
            events.addAll(getEvents(connection, username, eventID));
        });

        return events.size() > 0 ? events.get(0) : null;
    }

    /**
     * Returns all of the events of all of the people for the associated user
     * @param username The current user associated with these events
     * @return A list of Event objects of this user
     */
    public List<Event> GetEvents(String username) throws SQLException {
        List<Event> events = new ArrayList<>();
        doTransaction((connection) -> {
            events.addAll(getEvents(connection, username, null));
        });

        return events;
    }

    /**
     * Deletes all of the event data in the database
     */
    public void DeleteAll() throws SQLException {
        doTransaction((connection) -> {
            deleteEvents(connection, null);
        });
    }

    /**
     * Deletes all of the event data for the user
     * @param username The username of the events to delete
     * Deletes all event data for the current user
     */
    public void DeleteAll(String username) throws SQLException {
        doTransaction((connection) -> {
            deleteEvents(connection, username);
        });
    }

    private void insertEvents(Connection connection, List<Event> events) throws SQLException {
        String sql="insert into event (eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt=connection.prepareStatement(sql)) {
            for (Event event : events) {
                stmt.setString(1, event.getEventID());
                stmt.setString(2, event.getAssociatedUsername());
                stmt.setString(3, event.getPersonID());
                stmt.setFloat(4, event.getLatitude());
                stmt.setFloat(5, event.getLongitude());
                stmt.setString(6, event.getCountry());
                stmt.setString(7, event.getCity());
                stmt.setString(8, event.getEventType());
                stmt.setInt(9, event.getYear());

                if (stmt.executeUpdate() == 1) {
                    System.out.println("Inserted event " + event);
                } else {
                    System.out.println("Failed to insert event");
                }
            }
        }
    }

    private List<Event> getEvents(Connection connection, String username, String eventID) throws SQLException {
        String sql="select eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year from event where associatedUsername = ?";
        if (eventID != null) sql += " and eventID = ?";

        try (PreparedStatement stmt=connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            if (eventID != null) {
                stmt.setString(2, eventID);
            }

            ResultSet rs = stmt.executeQuery();
            List<Event> events = new ArrayList<>();
            while (rs.next()) {
                Event event = new Event(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getFloat(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9));

                events.add(event);
            }

            return events;
        }
    }

    private void deleteEvents(Connection connection, String username) throws SQLException {
        String sql = "delete from event";
        if (username != null) sql += " where associatedUsername = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (username != null) {
                stmt.setString(1, username);
            }
            int count = stmt.executeUpdate();

            // Reset the auto-increment counter so new books start over with an id of 1
//            sql = "delete from sqlite_sequence where name = 'book'";
//            try(PreparedStatement resetSequenceStatement = connection.prepareStatement(sql)) {
//                resetSequenceStatement.executeUpdate();
//            }

            System.out.printf("Deleted %d events\n", count);
        }
    }
}
