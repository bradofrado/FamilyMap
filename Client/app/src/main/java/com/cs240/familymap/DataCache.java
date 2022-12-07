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

    Map<String, Person> allPersons = new HashMap<>();
    Map<String, Event> allEvents = new HashMap<>();
    Map<String, SortedSet<Event>> personEvents = new HashMap<>();


    Set<String> paternalAncestors = new HashSet<>();
    Set<String> maternalAncestors = new HashSet<>();
    Set<String> malePeople = new HashSet<>();
    Set<String> femalePeople = new HashSet<>();

    Map<Integer, Filter> filters = new HashMap<>();
    private final List<Settings> settings = new ArrayList() {
        {add(new Settings("Life Story Lines", "Show Life Story Lines"));}
        {add(new Settings("Family Tree Lines", "Show Family Tree Lines"));}
        {add(new Settings("Spouse Lines", "Show Spouse Lines"));}
        {add(new Filter("Father's Side", "Filter by Father's Side of Family"));}
        {add(new Filter("Mother's Side", "Filter by Mother's Side of Family"));}
        {add(new Filter("Male Events", "Filter Events Based on Gender"));}
        {add(new Filter("Female Events", "Filter Events Based on Gender"));}
    };

    public void resetData() {
        authToken = null;
        rootPersonID = null;

        allPersons = new HashMap<>();
        allEvents = new HashMap<>();
        personEvents = new HashMap<>();


        paternalAncestors = new HashSet<>();
        maternalAncestors = new HashSet<>();
        malePeople = new HashSet<>();
        femalePeople = new HashSet<>();

        for (Filter filter : filters.values()) {
            if (!filter.getState()) {
                filter.toggleState();
            }
        }

        filters = new HashMap<>();
    }

    public List<Person> getPersons() {
        return new ArrayList<>(allPersons.values());
    }

    /**
     * Gets and filters all of the events based on configured settings
     * @return
     */
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

    /**
     * Sets all of the people and filter data
     * @param persons
     */
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

        filters = getFilters();
        filters.get(R.string.fathersSideName).setValues(paternalAncestors);
        filters.get(R.string.mothersSideName).setValues(maternalAncestors);
        filters.get(R.string.maleEventsName).setValues(malePeople);
        filters.get(R.string.femaleEventsName).setValues(femalePeople);
    }

    /**
     * Sets the events and personEvents
     * @param events
     */
    public void setEvents(Event[] events) {
        for (Event event : events) {
            allEvents.put(event.getEventID(), event);

            if (!personEvents.containsKey(event.getPersonID())) {
                personEvents.put(event.getPersonID(), new TreeSet<>(new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        String o1Type = o1.getEventType().toLowerCase();
                        String o2Type = o2.getEventType().toLowerCase();

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

                        return o1Type.compareTo(o2Type);
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

        return filterEvent(allEvents.get(eventID));
    }

    /**
     * Gets the birth event of the given person's spouse
     * @param personID
     * @return
     */
    public Event getBirthOfSpouse(String personID) {
        Person person = allPersons.get(personID);

        assert person != null;
        if (!allPersons.containsKey(person.getSpouseID())) return null;

        return filterEvent(personEvents.get(person.getSpouseID()).first());
    }

    /**
     * Gets the birth event of the given person's father
     * @param personID
     * @return
     */
    public Event getBirthOfFather(String personID) {
        Person person = allPersons.get(personID);

        assert person != null;
        if (person.getFatherID() == null) return null;

        return filterEvent(personEvents.get(person.getFatherID()).first());
    }

    /**
     * Gets the birth event of the given person's mother
     * @param personID
     * @return
     */
    public Event getBirthOfMother(String personID) {
        Person person = allPersons.get(personID);

        assert person != null;
        if (person.getMotherID() == null) return null;

        return filterEvent(personEvents.get(person.getMotherID()).first());
    }

    /**
     * Gets all of the filtered events for the given person
     * @param personID
     * @return
     */
    public List<Event> getEventsOfPerson(String personID) {
        return filterEvents(new ArrayList<>(personEvents.get(personID)));
    }

    /**
     * Gets the family (father, mother, spouse, children) of a person
     * @param personID The person for which we want the family
     * @return
     */
    public List<Person> getFamilyOfPerson(String personID) {
        Person person = getPerson(personID);

        if (person == null) return null;

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

    /**
     * Filters all of the events for the current settings
     * @param events
     * @return
     */
    public List<Event> filterEvents(List<Event> events) {
        List<Event> filteredEvents = new ArrayList<>(events);

        for (int i = filteredEvents.size() - 1; i >= 0; i--) {
            if (filterEvent(filteredEvents.get(i)) == null) {
                filteredEvents.remove(i);
            }
        }

        return filteredEvents;
    }

    /**
     * Returns the event if this event is ok with the filter settings, null otherwise
     * @param event
     * @return
     */
    public Event filterEvent(Event event) {
        for (Filter filter : filters.values()) {
            //Only look at the filters that are turned off to see if they are contained
            if (filter.getState()) continue;
            if (filter.isContained(event.getPersonID())) {
                return null;
            }
        }

        return event;
    }

    /**
     * Gets all the people whose name matches the query string
     * @param text The query string to match
     * @return
     */
    public List<Person> getQueryPersons(String text) {
        List<Person> filtered = new ArrayList<>();

        if (text == null || text.length() == 0) return filtered;

        List<Person> persons = getPersons();

        for (Person person : persons) {
            if (isContained(person.toString(), text)) {
                filtered.add(person);
            }
        }

        return filtered;
    }

    /**
     * Gets all of the events whose name or person match the query string
     * @param text The query string to match
     * @return
     */
    public List<Event> getQueryEvents(String text) {
        List<Event> filtered = new ArrayList<>();

        if (text == null || text.length() == 0) return filtered;

        List<Event> events = getEvents();

        for (Event event : events) {
            Person personEvent = getPerson(event.getPersonID());
            if (isContained(personEvent.toString(), text) || isContained(event.toString(), text)) {
                filtered.add(event);
            }
        }

        return filtered;
    }

    private boolean isContained(String fullText, String query) {
        return fullText.toLowerCase().contains(query.toLowerCase());
    }

    /**
     * Gets all of the filter settings as a map
     * @return
     */
    public Map<Integer, Filter> getFilters() {
        Map<Integer, Filter> map = new HashMap<>();
        map.put(R.string.fathersSideName, (Filter)settings.get(3));
        map.put(R.string.mothersSideName, (Filter)settings.get(4));
        map.put(R.string.maleEventsName, (Filter)settings.get(5));
        map.put(R.string.femaleEventsName, (Filter)settings.get(6));

        return map;
    }

    /**
     * Gets all of the line settings as a map
     * @return
     */
    public Map<Integer, Settings> getLines() {
        Map<Integer, Settings> map = new HashMap<>();
        map.put(R.string.lifeStoryName, settings.get(0));
        map.put(R.string.familyTreeName,settings.get(1));
        map.put(R.string.spouseName, settings.get(2));

        return map;
    }

    public List<Settings> getSettings() {
        return settings;
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

    public class Settings {
        private final String name;
        private final String description;

        private boolean state;

        public Settings(String name, String description) {
            this.name = name;
            this.description = description;
            state = true;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public boolean getState() {
            return state;
        }

        public void toggleState() {
            state = !state;
        }
    }

    public class Filter extends Settings {
        private Set<String> values;

        public Filter(String name, String description) {
            super(name, description);
        }

        public void setValues(Set<String> values) {
            this.values = values;
        }

        public boolean isContained(String value) {
            return values.contains(value);
        }
    }
}

// Clear datacache when logging out
// Display with original case, but color is case insensitive
// Name and date for completed astroid
// Sheila from austrialia to california to both greenland ones
// Sheila is missing a completed astroid event
// Sheila's death in china should not connect to birth
// Make the family tree lines the same color
// Check filter settings