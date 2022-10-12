package dao;

import models.Event;
import models.User;

import java.util.List;

public class EventDao {
    /**
     * Adds an event to the database
     * @param event The Event to add
     */
    public void AddEvent(Event event) {

    }

    /**
     * Adds a list of events into the database
     * @param events The list of event objects to add to the database
     */
    public void AddEvents(List<Event> events) {

    }

    /**
     * Gets a Event object given a person id
     * @param eventID The id of the event you want
     * @param username The current user associated with this person
     * @return The wanted event object
     */
    public Event GetEvent(String eventID, String username) {
        return null;
    }

    /**
     * Returns all of the events of all of the people for the associated user
     * @param username The current user associated with these events
     * @return A list of Event objects of this user
     */
    public List<Event> GetEvents(String username) {
        return null;
    }

    /**
     * Deletes all of the event data in the database
     */
    public void DeleteAll() {

    }

    /**
     * Deletes all of the event data for the user
     * @param username The username of the events to delete
     * Deletes all event data for the current user
     */
    public void DeleteAll(String username) {

    }
}
