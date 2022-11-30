package com.cs240.familymap;

import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
    Map<String, SortedSet<Event>> personEvents = new HashMap<>();


    Set<String> paternalAncestors = new HashSet<>();
    Set<String> maternalAncestors = new HashSet<>();


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

            if (!personEvents.containsKey(event.getPersonID())) {
                personEvents.put(event.getPersonID(), new TreeSet<>(new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        String o1Type = o1.getEventType();
                        String o2Type = o2.getEventType();

                        if (o1Type.equals(o2Type)) {
                            return o1.getEventID().compareTo(o2.getEventID());
                        }

                        if (o1Type.equals("birth")) {
                            return -1;
                        }

                        if (o2Type.equals("birth")) {
                            return 1;
                        }

                        return o1.getEventID().compareTo(o2.getEventID());
                    }
                }));
            }
            personEvents.get(event.getPersonID()).add(event);
        }
    }

    public Event getBirthOfSpouse(String personID) {
        Person person = allPersons.get(personID);

        assert person != null;
        if (!allPersons.containsKey(person.getSpouseID())) return null;

        return personEvents.get(person.getSpouseID()).first();
    }

    public Event getBirthOfFather(String personID) {
        Person person = allPersons.get(personID);

        assert person != null;
        if (person.getFatherID() == null) return null;

        return personEvents.get(person.getFatherID()).first();
    }

    public Event getBirthOfMother(String personID) {
        Person person = allPersons.get(personID);

        assert person != null;
        if (person.getMotherID() == null) return null;

        return personEvents.get(person.getMotherID()).first();
    }

    public List<Event> getEventsOfPerson(String personID) {
        return new ArrayList<>(personEvents.get(personID));
    }
}
