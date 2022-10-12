package requests;

import models.Event;
import models.Person;
import models.User;

/**
 * The request object for the /load api call
 */
public class LoadRequest {
    /**
     * A list of the users to load
     */
    private User[] users;
    /**
     * A list of the persons to load
     */
    private Person[] persons;
    /**
     * A list of the events to load
     */
    private Event[] events;

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users=users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons=persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events=events;
    }
}
