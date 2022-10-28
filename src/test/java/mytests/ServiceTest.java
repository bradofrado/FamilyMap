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
import util.UserUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

        private static final String BAD_TOKEN = "badboi";

        String authToken;
        @BeforeEach
        public void setup() {
            RegisterRequest registerRequest = new RegisterRequest(john);
            RegisterResult result = services.RegisterService.Register(registerRequest);
            authToken = result.getAuthtoken();

            personsRequest = new PersonsRequest();
            personsRequest.setAuthToken(authToken);

            personRequest = new PersonRequest();
            personRequest.setAuthToken(authToken);
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

        @BeforeEach
        public void setup() {
            Event[] events = new Event[] {
                    new Event("123", john.getUsername(), john.getPersonID(), 100, 100, "USA", "Morgan", "Hat", 2020)
            };
            Person[] persons = new Person[] {
                    new Person(john.getPersonID(), john.getUsername(), "Bob", "Jones", 'f')
            };
            User[] users = new User[] {
                    john
            };

            loadRequest = new LoadRequest();
            loadRequest.setEvents(events);
            loadRequest.setPersons(persons);
            loadRequest.setUsers(users);
        }

        @Test
        public void TestLoad() {
            LoadResult result = LoadService.Load(loadRequest);
        }

        @Test
        public void TestLoadFail() {
            LoadResult result = LoadService.Load(loadRequest);
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
            request.setGenerations(numGenerations);
            FillResult result = FillService.Fill(request);

            assertTrue(result.isSuccess());
            assertEquals(EMPTY_STRING, result.getMessage());

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
            int num = 0;
            for (int i = 0; i <= generations; i++) {
                num += Math.pow(2, i);
            }

            return num;
        }
    }
}
