package com.cs240.familymap;

import static org.junit.jupiter.api.Assertions.*;

import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;
import com.cs240.familymapmodules.requests.LoginRequest;
import com.cs240.familymapmodules.results.EventsResult;
import com.cs240.familymapmodules.results.LoginResult;
import com.cs240.familymapmodules.results.PersonsResult;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CalculationsTest {
    DataCache cache;
    Person userPerson;

    private final int MALE_FILTER_KEY = 2131755106;
    private final int FEMALE_FILTER_KEY = 2131755079;
    private final int MOTHERS_SIDE_FILTER_KEY = 2131755129;
    private final int FATHERS_SIDE_FILTER_KEY = 2131755077;

    @BeforeEach
    public void setup() {
        cache = DataCache.getInstance();
        cache.resetData();
        ServerFacade facade = new ServerFacade("localhost", "8080");
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("sheila");
        loginRequest.setPassword("parker");

        LoginResult loginResult = facade.login(loginRequest);
        PersonsResult personsResult = facade.getPersons(loginResult.getAuthtoken());
        EventsResult eventsResult = facade.getEvents(loginResult.getAuthtoken());

        cache.setPersonID(loginResult.getPersonID());
        cache.setPersons(personsResult.getData());
        cache.setEvents(eventsResult.getData());

        userPerson = cache.getPerson(loginResult.getPersonID());
    }

    @Test
    public void CalculateFamilyRelationshipsFail() {
        List<Person> family = cache.getFamilyOfPerson("bobby jones");

        assertNull(family);
    }

    @Test
    public void CalculateFamilyRelationshipsUser() {
        List<Person> family = cache.getFamilyOfPerson(userPerson.getPersonID());

        assertEquals(3, family.size());
        assertNotNull(find(family, (t) -> { return t.getPersonID().equals(userPerson.getFatherID()); }));
        assertNotNull(find(family, (t) -> { return t.getPersonID().equals(userPerson.getMotherID()); }));
        assertNotNull(find(family, (t) -> { return t.getPersonID().equals(userPerson.getSpouseID()); }));
    }

    @Test
    public void FilterEvents() {
        Map<Integer, DataCache.Filter> filters = cache.getFilters();
        List<Event> noFilter = cache.getEvents();
        assertEquals(16, noFilter.size());

        filters.get(MALE_FILTER_KEY).toggleState();
        List<Event> noMales = cache.getEvents();
        assertEquals(10, noMales.size());
        assertNull(find(noMales, (event -> cache.getPerson(event.getPersonID()).getGender() == 'm')));

        filters.get(MALE_FILTER_KEY).toggleState();
        filters.get(FEMALE_FILTER_KEY).toggleState();
        List<Event> noFemales = cache.getEvents();
        assertEquals(6, noFemales.size());
        assertNull(find(noFemales, (event -> cache.getPerson(event.getPersonID()).getGender() == 'f')));

        filters.get(FEMALE_FILTER_KEY).toggleState();
        filters.get(FATHERS_SIDE_FILTER_KEY).toggleState();
        List<Event> noFathersSide = cache.getEvents();
        assertEquals(11, noFathersSide.size());
        assertNull(find(noFathersSide, event -> userPerson.getFatherID().equals(event.getPersonID())));

        filters.get(FATHERS_SIDE_FILTER_KEY).toggleState();
        filters.get(MOTHERS_SIDE_FILTER_KEY).toggleState();
        List<Event> noMothersSide = cache.getEvents();
        assertEquals(11, noMothersSide.size());
        assertNull(find(noMothersSide, event -> userPerson.getMotherID().equals(event.getPersonID())));


    }

    @Test
    public void filterEventsNothing() {
        Map<Integer, DataCache.Filter> filters = cache.getFilters();
        filters.get(MALE_FILTER_KEY).toggleState();
        filters.get(FEMALE_FILTER_KEY).toggleState();
        filters.get(FATHERS_SIDE_FILTER_KEY).toggleState();
        filters.get(MOTHERS_SIDE_FILTER_KEY).toggleState();

        assertEquals(0, cache.getEvents().size());
    }

    @Test
    public void sortEvents() {
        List<Event> personEvents = cache.getEventsOfPerson(userPerson.getPersonID());

        assertEquals(5, personEvents.size());
        assertEquals("birth", personEvents.get(0).getEventType().toLowerCase());
        assertEquals("marriage", personEvents.get(1).getEventType().toLowerCase());
        assertEquals("completed asteroids", personEvents.get(2).getEventType().toLowerCase());
        assertEquals("completed asteroids", personEvents.get(3).getEventType().toLowerCase());
        assertEquals("death", personEvents.get(4).getEventType().toLowerCase());
    }

    @Test
    public void sortEvents2() {
        List<Event> personEvents = cache.getEventsOfPerson(userPerson.getFatherID());

        assertEquals(1, personEvents.size());
        assertEquals("birth", personEvents.get(0).getEventType().toLowerCase());
    }

    @Test
    public void searchEvents() {
        final String QUERY = "a";
        List<Event> events = cache.getQueryEvents(QUERY);

        assertEquals(16, events.size());
        assertNotNull(find(events, event -> event.toString().contains(QUERY)));
    }

    @Test
    public void searchEventsNone() {
        final String QUERY = "garbage";
        List<Event> events = cache.getQueryEvents(QUERY);

        assertEquals(0, events.size());
    }

    @Test
    public void searchPeople() {
        final String QUERY = "a";
        List<Person> persons = cache.getQueryPersons(QUERY);

        assertEquals(6, persons.size());
        assertNotNull(find(persons, person -> person.toString().contains(QUERY)));
    }

    @Test
    public void searchPeopleNone() {
        final String QUERY = "garbage";
        List<Person> persons = cache.getQueryPersons(QUERY);

        assertEquals(0, persons.size());
    }

    private <T> T find(List<T> list, Predicate<T> predicate) {
        for (T item : list) {
            if (predicate.test(item)) {
                return item;
            }
        }

        return null;
    }
}
