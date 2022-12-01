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
    String rootPersonID;

    List<Filter> filters = new ArrayList<>();
    Map<String, Person> allPersons = new HashMap<>();
    Map<String, Event> allEvents = new HashMap<>();
    Map<String, SortedSet<Event>> personEvents = new HashMap<>();


    Set<String> paternalAncestors = new HashSet<>();
    Set<String> maternalAncestors = new HashSet<>();
    Set<String> malePeople = new HashSet<>();
    Set<String> femalePeople = new HashSet<>();


    public List<Person> getPersons() {
        return new ArrayList<>(allPersons.values());
    }

    public List<Event> getEvents() {
        return filterEvents(new ArrayList<>(allEvents.values()));
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonID() {return rootPersonID;}
    public void setPersonID(String personID) {
        rootPersonID = personID;
    }

    public void setPersons(Person[] persons) {
        for (Person person : persons) {
            allPersons.put(person.getPersonID(), person);

            if (person.getGender() == 'm') {
                malePeople.add(person.getPersonID());
            }

            if (person.getGender() == 'f') {
                femalePeople.add(person.getPersonID());
            }
        }

        Person rootPerson = allPersons.get(rootPersonID);
        depthFirstAddToList(rootPerson.getFatherID(), paternalAncestors);
        depthFirstAddToList(rootPerson.getMotherID(), maternalAncestors);

        filters.add(new Filter("Father's Side", "Filter by Father's side of family", paternalAncestors));
        filters.add(new Filter("Mother's Side", "Filter by mother's side of family", maternalAncestors));
        filters.add(new Filter("Male Events", "Filter events based on gender", malePeople));
        filters.add(new Filter("Female Events", "Filter events based on gender", femalePeople));
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

                        if (o1Type.equals("birth") || o2Type.equals("death")) {
                            return -1;
                        }

                        if (o2Type.equals("birth") || o1Type.equals("death")) {
                            return 1;
                        }

                        if (o1.getYear() != o2.getYear()) {
                            return Integer.compare(o1.getYear(), o2.getYear());
                        }

                        return o1Type.toLowerCase().compareTo(o2Type.toLowerCase());
                    }
                }));
            }
            personEvents.get(event.getPersonID()).add(event);
        }
    }

    public Person getPerson(String personID) {
        if (personID == null) return null;

        return allPersons.get(personID);
    }

    public Event getEvent(String eventID) {
        if (eventID == null) return null;

        return allEvents.get(eventID);
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

    /**
     * Gets the family (father, mother, spouse, children) of a person
     * @param personID The person for which we want the family
     * @return
     */
    public List<Person> getFamilyOfPerson(String personID) {
        Person person = getPerson(personID);
        List<Person> family = new ArrayList<>();

        addIfNotNull(family, getPerson(person.getFatherID()));
        addIfNotNull(family, getPerson(person.getMotherID()));
        addIfNotNull(family, getPerson(person.getSpouseID()));

        for (Person child : getPersons()) {
            if (personID.equals(child.getFatherID()) || personID.equals(child.getMotherID())) {
                family.add(child);
            }
        }

        return family;
    }

    public List<Event> filterEvents(List<Event> events) {
        List<Event> filteredEvents = new ArrayList<>(events);

        for (Filter filter : filters) {
            if (!filter.getState()) {
                for (int i = filteredEvents.size() - 1; i >= 0; i--) {
                    if (filter.isContained(filteredEvents.get(i).getPersonID())) {
                        filteredEvents.remove(i);
                    }
                }
            }
        }

        return filteredEvents;
    }

    private void depthFirstAddToList(String person, Set<String> list) {
        if (person == null) return;

        list.add(person);
        depthFirstAddToList(allPersons.get(person).getFatherID(), list);
        depthFirstAddToList(allPersons.get(person).getMotherID(), list);
    }

    private <T> void addIfNotNull(List<T> list, T item) {
        if (item == null) {
            return;
        }

        list.add(item);
    }

    private class Filter {
        private String name;
        private String description;
        private boolean state;
        private final Set<String> values;

        public Filter(String name, String description, Set<String> values) {
            this.name = name;
            this.description = description;
            this.values = values;
            this.state = true;
        }

        public boolean getState() { return state;}
        public void setState(boolean state) {
            this.state = state;
        }

        public boolean isContained(String value) {
            return values.contains(value);
        }

        public String getDescription() {
            return description;
        }

        public String getName() {
            return name;
        }
    }
}
