package mytests;

import dao.AuthTokenDao;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import exceptions.InvalidCredentialsException;
import models.Event;
import models.Person;
import models.User;
import org.junit.jupiter.api.*;
import requests.*;
import results.*;
import services.*;
import util.ClearUtil;
import util.DataGenerator;
import util.UserUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    private static final String EMPTY_STRING = "";
    private static final User john = new User("johnnyboi", "johntheman", "john@gmail.com", "john", "boi", 'm', "a");

    @Nested
    @DisplayName("Register Service Tests")
    class RegisterService {
        RegisterRequest request;

        @BeforeEach
        public void setup() {
            request = new RegisterRequest(john);
        }

        @AfterEach
        public void cleanup() {
            try {
                ClearUtil.ClearDatabase();
            } catch(SQLException ex) {

            }
        }

        @Test
        public void TestRegister() {
            RegisterResult result = services.RegisterService.Register(request);

            assertEquals(john.getUsername(), result.getUsername());
            assertNotNull(result.getAuthtoken());
            assertNotEquals(EMPTY_STRING, result.getAuthtoken());
            assertNotEquals(EMPTY_STRING, result.getPersonID());
            assertTrue(result.isSuccess());
        }

        @Test
        public void TestRegisterFail() {
            services.RegisterService.Register(request);
            RegisterResult result = services.RegisterService.Register(request);

            assertFalse(result.isSuccess());
            assertNotEquals(EMPTY_STRING, result.getMessage());
        }
    }

    @DisplayName("Person Service Tests")
    @Nested
    class PersonServiceTests {
        private PersonsRequest personsRequest;
        private PersonRequest personRequest;
        private List<Person> persons;

        private static final String BAD_TOKEN = "badboi";

        String authToken;
        @BeforeEach
        public void setup() {
            RegisterRequest registerRequest = new RegisterRequest(john);
            RegisterResult result = services.RegisterService.Register(registerRequest);
            authToken = result.getAuthtoken();
            persons = new ArrayList<>() {
                {add(DataGenerator.getRandomPerson(john.getUsername(), john.getGender()));}
                {add(DataGenerator.getRandomPerson(john.getUsername(), john.getGender()));}
            };

            personsRequest = new PersonsRequest();
            personsRequest.setAuthToken(authToken);

            personRequest = new PersonRequest();
            personRequest.setAuthToken(authToken);
            personRequest.setPersonID(persons.get(0).getPersonID());

            try {
                new PersonDao().AddPersons(persons);
            } catch (SQLException ex) {

            }
        }

        @AfterEach
        public void cleanup() {
            try {
                ClearUtil.ClearDatabase();
                authToken = null;
            } catch(SQLException ex) {

            }
        }

        @Test
        public void TestGetPersons() {
            PersonsResult result = PersonService.Persons(personsRequest);

            assertTrue(result.isSuccess());
            assertEquals(persons.size(), result.getData().length);
            assertArrayEquals(persons.toArray(), result.getData());
        }

        @Test
        public void TestGetPersonsFail() {
            PersonsRequest badRequest = new PersonsRequest();
            badRequest.setAuthToken(BAD_TOKEN);

            PersonsResult result = PersonService.Persons(badRequest);

            assertFalse(result.isSuccess());
            assertEquals(EMPTY_STRING, result.getMessage());
        }

        @Test
        public void TestGetPerson() {
            PersonResult result = PersonService.Person(personRequest);

            assertTrue(result.isSuccess());

            Person person = persons.get(0);
            assertEquals(person.getPersonID(), result.getPersonID());
            assertEquals(person.getAssociatedUsername(), result.getAssociatedUsername());
            assertEquals(person.getFirstName(), result.getFirstName());
            assertEquals(person.getLastName(), result.getLastName());
            assertEquals(person.getGender(), result.getGender());
            assertEquals(person.getFatherID(), result.getFatherID());
            assertEquals(person.getMotherID(), result.getMotherID());
            assertEquals(person.getSpouseID(), result.getSpouseID());
        }

        @Test
        public void TestGetPersonFail() {
            PersonRequest badRequest = new PersonRequest();
            badRequest.setAuthToken(BAD_TOKEN);

            PersonResult result = PersonService.Person(badRequest);

            assertFalse(result.isSuccess());
            assertEquals(EMPTY_STRING, result.getMessage());
        }
    }

    @DisplayName("Event Service Tests")
    @Nested
    class EventServiceTests {
        private EventsRequest eventsRequest;
        private EventRequest eventRequest;
        private List<Event> events;

        private static final String BAD_TOKEN = "badboi";

        String authToken;
        @BeforeEach
        public void setup() {
            RegisterRequest registerRequest = new RegisterRequest(john);
            RegisterResult result = services.RegisterService.Register(registerRequest);
            authToken = result.getAuthtoken();
            events = new ArrayList<>() {
                {add(DataGenerator.getRandomEvent(john.getUsername(), "marriage", john.getPersonID(), 0, 2020));}
                {add(DataGenerator.getRandomEvent(john.getUsername(), "baptism", john.getPersonID(), 0, 2020));}
            };

            eventsRequest = new EventsRequest();
            eventsRequest.setAuthToken(authToken);

            eventRequest = new EventRequest();
            eventRequest.setAuthToken(authToken);
            eventRequest.setEventID(events.get(0).getEventID());

            try {
                new EventDao().AddEvents(events);
            } catch (SQLException ex) {

            }
        }

        @AfterEach
        public void cleanup() {
            try {
                ClearUtil.ClearDatabase();
                authToken = null;
            } catch(SQLException ex) {

            }
        }

        @Test
        public void TestGetEvents() {
            EventsResult result = EventService.Events(eventsRequest);

            assertTrue(result.isSuccess());
            assertEquals(events.size(), result.getData().length);
            assertArrayEquals(events.toArray(), result.getData());
        }

        @Test
        public void TestGetEventsFail() {
            EventsRequest badRequest = new EventsRequest();
            badRequest.setAuthToken(BAD_TOKEN);

            EventsResult result = EventService.Events(badRequest);

            assertFalse(result.isSuccess());
            assertEquals(EMPTY_STRING, result.getMessage());
        }

        @Test
        public void TestGetEvent() {
            EventResult result = EventService.Event(eventRequest);

            assertTrue(result.isSuccess());

            Event event = events.get(0);
            assertEquals(event.getEventID(), result.getEventID());
            assertEquals(event.getAssociatedUsername(), result.getAssociatedUsername());
            assertEquals(event.getEventType(), result.getEventType());
            assertEquals(event.getPersonID(), result.getPersonID());
            assertEquals(event.getLatitude(), result.getLatitude());
            assertEquals(event.getLongitude(), result.getLongitude());
            assertEquals(event.getCity(), result.getCity());
            assertEquals(event.getCountry(), result.getCountry());
            assertEquals(event.getYear(), result.getYear());
        }

        @Test
        public void TestGetEventFail() {
            EventRequest badRequest = new EventRequest();
            badRequest.setAuthToken(BAD_TOKEN);

            EventResult result = EventService.Event(badRequest);

            assertFalse(result.isSuccess());
            assertEquals(EMPTY_STRING, result.getMessage());
        }
    }

    @DisplayName("Login Service Tests")
    @Nested
    class LoginServiceTests {
        LoginRequest loginRequest;
        private static final String BAD_PASSWORD = "badboi";

        @BeforeEach
        public void setup() {
            loginRequest = new LoginRequest(john);
        }

        @AfterEach
        public void cleanup() {
            try {
                new AuthTokenDao().DeleteAll();
            } catch (SQLException ex) {

            }
        }

        @Test
        public void TestLogin() {
            LoginResult result = LoginService.Login(loginRequest);

            assertNotEquals(EMPTY_STRING, result.getAuthtoken());
            assertEquals(john.getUsername(), result.getUsername());
            assertEquals(john.getPersonID(), result.getPersonID());
            assertTrue(result.isSuccess());
            assertEquals(EMPTY_STRING, result.getMessage());
        }

        @Test
        public void TestLoginBadCredientials() {
            loginRequest.setPassword(BAD_PASSWORD);
            LoginResult result = LoginService.Login(loginRequest);

            assertFalse(result.isSuccess());
            assertNotEquals(EMPTY_STRING, result.getMessage());
        }
    }

    @DisplayName("Load Service Tests")
    @Nested
    class LoadServiceTests {
        LoadRequest loadRequest;

        UserDao userDao;
        PersonDao personDao;
        EventDao eventDao;

        Event[] events;
        Person[] persons;
        User[] users;

        @BeforeEach
        public void setup() {
            userDao = new UserDao();
            personDao = new PersonDao();
            eventDao = new EventDao();

            events = new Event[] {
                    DataGenerator.getRandomEvent(john.getUsername(), "marriage", john.getPersonID(), 0, 2020),
                    DataGenerator.getRandomEvent(john.getUsername(), "birth", john.getPersonID(), 0, 2020),
                    DataGenerator.getRandomEvent(john.getUsername(), "baptism", john.getPersonID(), 0, 2020),
            };
            persons = new Person[] {
                    DataGenerator.getRandomPerson(john.getUsername(), 'm')
            };
            users = new User[] {
                    john
            };

            loadRequest = new LoadRequest();
            loadRequest.setEvents(events);
            loadRequest.setPersons(persons);
            loadRequest.setUsers(users);
        }

        @AfterEach
        public void cleanup() {
            try {
                ClearUtil.ClearDatabase();
            } catch (SQLException ex) {

            }
        }

        @Test
        public void TestLoad() {

            LoadResult result = LoadService.Load(loadRequest);
            assertTrue(result.isSuccess());

            try {
                for (User user : users) {
                    assertNotNull(userDao.GetUser(user.getUsername()));

                    List<Person> loadedPeople = personDao.GetAllPersons(user.getUsername());
                    List<Person> expectedPeople = filter(persons, person -> person.getAssociatedUsername().equals(user.getUsername()));
                    assertEquals(expectedPeople.size(), loadedPeople.size());

                    List<Event> loadedEvents = eventDao.GetEvents(user.getUsername());
                    List<Event> expectedEvents = filter(events, event -> event.getAssociatedUsername().equals(user.getUsername()));
                    assertEquals(expectedEvents.size(), loadedEvents.size());
                }
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestLoadFail() {
            loadRequest.setPersons(new Person[] {
                    new Person(null, null, null, null, 'm')
            });

            LoadResult result = LoadService.Load(loadRequest);

            assertFalse(result.isSuccess());
        }

        private <T> List<T> filter(T[] list, Predicate<T> predicate) {
            List<T> filterd = new ArrayList<>();
            for (T t : list) {
                if (predicate.test(t)) {
                    filterd.add(t);
                }
            }

            return filterd;
        }
    }

    @DisplayName("Fill Service Test")
    @Nested
    class FillServiceTests {
        FillRequest request;

        @BeforeEach
        public void setup() {
            try {
                new UserDao().AddUser(john);
            } catch (SQLException ex) {

            }

            request = new FillRequest();
            request.setUsername(john.getUsername());
        }

        @AfterEach
        public void cleanup() {
            try {
                ClearUtil.ClearDatabase();
            } catch (SQLException ex) {

            }
        }

        @Test
        public void TestFill() {
            int numGenerations = 4;
            String userId = testFillGenerations(numGenerations);

            testPopulationOfPersonsAndEvents(request.getUsername(), userId, numGenerations);

        }

        @Test
        public void TestFillFail() {
            request.setGenerations(-1);
            FillResult result = FillService.Fill(request);

            assertFalse(result.isSuccess());
            assertNotEquals(EMPTY_STRING, result.getMessage());
        }

        private boolean eventsHaveTypes(List<Event> events, String[] types) {
            int size = events.size();
            List<Event> copy =  new ArrayList<>(events);
            for (String type : types) {
                copy.removeIf((event) -> event.getEventType().equals(type));
                size--;

                if (copy.size() != size) {
                    return false;
                }
            }

            return true;
        }

        private String testFillGenerations(int numGenerations) {
            String message = "Successfully added " + getNumberOfPersonsForGeneration(numGenerations) + " persons and " + getNumberOfEventsForGeneration(numGenerations) +
                    " events to the database.";
            request.setGenerations(numGenerations);
            FillResult result = FillService.Fill(request);

            assertTrue(result.isSuccess());
            assertEquals(message, result.getMessage());

            try {
                User user = new UserDao().GetUser(request.getUsername());
                return user.getPersonID();
            } catch (SQLException ex) {

            }

            return null;
        }

        private void testPopulationOfPersonsAndEvents(String username, String userId, int numGenerations) {
            try {
                List<Person> persons=new PersonDao().GetAllPersons(username);
                assertTrue(persons.size() == getNumberOfPersonsForGeneration(numGenerations));

                for (Person person : persons) {
                    List<Event> events = new EventDao().GetEvents(person.getAssociatedUsername());
                    events.removeIf((event) -> !event.getPersonID().equals(person.getPersonID()));
                    if (person.getPersonID().equals(userId)) {
                        assertEquals(1, events.size());
                        assertEquals("birth", events.get(0).getEventType());
                    } else {
                        assertEquals(3, events.size());
                        assertTrue(eventsHaveTypes(events, new String[] {"birth", "marriage", "death"}));
                    }
                }
            } catch (SQLException ex) {

            }
        }

        private int getNumberOfPersonsForGeneration(int generations) {
            if (generations < 0) return 0;

            int num = 0;
            for (int i = 0; i <= generations; i++) {
                num += Math.pow(2, i);
            }

            return num;
        }

        private int getNumberOfEventsForGeneration(int generations) {
            if (generations < 0) return 0;

            int numPeople = getNumberOfPersonsForGeneration(generations);

            return (numPeople - 1) * 3 + 1;
        }
    }

    @DisplayName("Clear Service Tests")
    @Nested
    class ClearServiceTests {
        private String token;
        @BeforeEach
        public void setup() {
            RegisterRequest registerRequest = new RegisterRequest(john);
            RegisterResult result = services.RegisterService.Register(registerRequest);
            token = result.getAuthtoken();
        }

        @AfterEach
        public void cleanup() {
            try {
                ClearUtil.ClearDatabase();
            } catch (SQLException ex) {

            }
        }

        @Test
        public void TestClearService() {
            ClearResult result = ClearService.Clear();

            assertTrue(result.isSuccess());

            try {
                List<Person> persons = new PersonDao().GetAllPersons(john.getUsername());
                assertEquals(0, persons.size());

                List<Event> events = new EventDao().GetEvents(john.getUsername());
                assertEquals(0, persons.size());

                User user = new UserDao().GetUser(john.getUsername());
                assertNull(user);

                var authToken = new AuthTokenDao().getAuthToken(token);
                assertNull(authToken);
            } catch (SQLException ex) {

            }
        }

        @Test
        public void TestClearService2Clears() {
            ClearResult result = ClearService.Clear();
            assertTrue(result.isSuccess());

            try {
                new PersonDao().AddPerson(DataGenerator.getRandomPerson("happy", 'f'));
            } catch (SQLException ex) {

            }

            result = ClearService.Clear();
            assertTrue(result.isSuccess());
        }
    }
}
