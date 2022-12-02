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

        filters = getFilters();
        filters.get(R.string.fathersSideName).setValues(paternalAncestors);
        filters.get(R.string.mothersSideName).setValues(maternalAncestors);
        filters.get(R.string.maleEventsName).setValues(malePeople);
        filters.get(R.string.femaleEventsName).setValues(femalePeople);
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

        for (Filter filter : filters.values()) {
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

    public Map<Integer, Filter> getFilters() {
        Map<Integer, Filter> map = new HashMap<>();
        map.put(R.string.fathersSideName, (Filter)settings.get(3));
        map.put(R.string.mothersSideName, (Filter)settings.get(4));
        map.put(R.string.maleEventsName, (Filter)settings.get(5));
        map.put(R.string.femaleEventsName, (Filter)settings.get(6));

        return map;
    }

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
