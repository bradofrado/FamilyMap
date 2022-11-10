package com.cs240.familymap;

import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;

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

    Map<String, Person> persons;
    Map<String, Event> events;
    Map<String, List<Event>> personEvents;

    Set<String> paternalAncestors;
    Set<String> maternalAncestors;


    public String getAuthToken() {
        return "abc";
    }
    public List<Person> getPersons(String username) {
        return null;
    }

    public List<Event> getEvents(String username) {
        return null;
    }
}
