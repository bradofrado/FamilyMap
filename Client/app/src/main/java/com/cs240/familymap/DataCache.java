package com.cs240.familymap;

import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataCache {
    private static DataCache instance;

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }

        return instance;
    }

    private DataCache() {

    }

    String authToken;

    Map<String, Person> allPersons = new HashMap<>();
    Map<String, Event> allEvents = new HashMap<>();
    Map<String, List<Event>> personEvents;

    Set<String> paternalAncestors;
    Set<String> maternalAncestors;


    public List<Person> getPersons() {
        return new ArrayList<>(allPersons.values());
    }

    public List<Event> getEvents() {
        return new ArrayList<>(allEvents.values());
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setPersons(Person[] persons) {
        for (Person person : persons) {
            allPersons.put(person.getPersonID(), person);
        }
    }

    public void setEvents(Event[] events) {
        for (Event event : events) {
            allEvents.put(event.getEventID(), event);
        }
    }
}
