package com.cs240.familymapserver.mytests;

import com.cs240.familymapserver.dao.AuthTokenDao;
import com.cs240.familymapserver.dao.EventDao;
import com.cs240.familymapserver.dao.PersonDao;
import com.cs240.familymapserver.dao.UserDao;
import com.cs240.familymapmodules.models.AuthToken;
import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;
import com.cs240.familymapmodules.models.User;
import org.junit.jupiter.api.*;
import com.cs240.familymapserver.util.DataGenerator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DaoTest {
    @Nested
    @DisplayName("UserDao Operations")
    class user {
        User user;
        User user2;

        UserDao userDao;
        @BeforeEach
        public void startup() {
            user = new User("bradofrado", "mypassword", "bradofrado@gmail.com", "Braydon", "Jones", 'm', "asdf");
            user2 = new User("johnny", "man", "john@man.com", "John", "Man", 'm', "asdf");

            userDao = new UserDao();
            userDao.startTransaction();
        }

        @AfterEach
        public void cleanup() {
            userDao.endTransaction();
        }

        @Test
        public void TestAddUser() {
            try {
                userDao.AddUser(user);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAddUserFail() {
            try {
                userDao.AddUser(user);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
            assertThrows(SQLException.class, () -> {
                userDao.AddUser(user);
            });
        }

        @Test
        public void TestAddUsers() {
            try {
                List<User> users = new ArrayList() { { add(user); } { add(user2); }};
                userDao.AddUsers(users);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAddUsersFail() {
            List<User> users = new ArrayList() { { add(user); } { add(user); }};
            assertThrows(SQLException.class, () -> {
                userDao.AddUsers(users);
            });
        }

        @Test
        public void TestGetUser() {
            try {
                userDao.AddUser(user);
                User findUser=userDao.GetUser(user.getUsername());

                assertEquals(user, findUser);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetUserFail() {
            try {
                userDao.AddUser(user);
                User findUser=userDao.GetUser(user.getEmail());
                assertNotEquals(user, findUser);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestUserClear() {
            try {
                userDao.AddUser(user);
                userDao.DeleteAll();

                assertNull(userDao.GetUser(user.getEmail()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestUserClear2() {
            try {
                List<User> users = new ArrayList() { { add(user); } { add(user2); }};
                userDao = new UserDao();

                userDao.AddUsers(users);
                userDao.DeleteAll();

                assertNull(userDao.GetUser(user.getEmail()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestUserUpdate() {
            try {
                String differentPersonId = "This is a different person id";
                userDao.AddUser(user);
                user.setPersonID(differentPersonId);
                userDao.UpdateUser(user);

                User updatedUser = userDao.GetUser(user.getUsername());
                assertEquals(differentPersonId, updatedUser.getPersonID());
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestUserUpdateFail() {
            try {
                String differentPersonId = "This is a different person id";
                String username = user.getUsername();

                userDao.AddUser(user);
                user.setPersonID(differentPersonId);
                user.setUsername("garbade");
                userDao.UpdateUser(user);
                User updatedUser = userDao.GetUser(username);

                assertNotEquals(differentPersonId, updatedUser.getPersonID());
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Event Dao Operations")
    class event {
        Event event;
        Event event2;

        EventDao eventDao;
        @BeforeEach
        public void setup() {
            event =DataGenerator.getRandomEvent("john", "birth", "asdf", 0, 2020);
            event2 =DataGenerator.getRandomEvent("john", "marriage", "asdf", 0, 2020);

            eventDao = new EventDao();
            eventDao.startTransaction();
        }
        @AfterEach
        public void cleanup() {
            eventDao.endTransaction();
        }

        @Test
        public void TestAddEvent() {
            try {
                eventDao.AddEvent(event);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAddEventFail() {
            try {
                eventDao.AddEvent(event);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
            assertThrows(SQLException.class, () -> {
                eventDao.AddEvent(event);
            });
        }

        @Test
        public void TestAddEvents() {
            try {
                List<Event> events = new ArrayList() {{add(event);} {add(event2); }};
                eventDao.AddEvents(events);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAddEventsNoEvents() {
            try {
                List<Event> events = new ArrayList<>();
                eventDao.AddEvents(events);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetEvent() {
            try {
                eventDao.AddEvent(event);
                Event findEvent=eventDao.GetEvent(event.getEventID(), event.getAssociatedUsername());

                assertEquals(event, findEvent);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetEventFail() {
            try {
                eventDao.AddEvent(event);
                Event findEvent=eventDao.GetEvent(event.getAssociatedUsername(), event.getAssociatedUsername());
                assertNotEquals(event, findEvent);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetEvents() {
            try {
                List<Event> events = new ArrayList() {{add(event);} {add(event2);}};
                eventDao.AddEvents(events);
                List<Event> findEvents=eventDao.GetEvents(event.getAssociatedUsername());

                assertEquals(events.size(), findEvents.size());
                assertTrue(events.contains(event));
                assertTrue(events.contains(event2));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetEventsFail() {
            try {
                List<Event> events = new ArrayList() {{add(event);} {add(event2);}};
                eventDao.AddEvents(events);
                List<Event> findEvents=eventDao.GetEvents("garbageusername");
                assertEquals(0, findEvents.size());
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestEventClear() {
            try {
                event2.setAssociatedUsername("bob");

                List<Event> events = new ArrayList() {{add(event);} {add(event2);}};
                eventDao.AddEvents(events);
                eventDao.DeleteAll(event.getAssociatedUsername());
                assertNull(eventDao.GetEvent(event.getEventID(), event.getAssociatedUsername()));

                List<Event> findEvents = eventDao.GetEvents(event2.getAssociatedUsername());
                assertEquals(1, findEvents.size());
                assertEquals(event2, findEvents.get(0));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestEventClearFail() {
            try {
                eventDao.AddEvent(event);
                eventDao.DeleteAll("garbade");

                assertNotNull(eventDao.GetEvent(event.getEventID(), event.getAssociatedUsername()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestEventClearAll() {
            try {
                eventDao.AddEvent(event);
                eventDao.DeleteAll();

                assertNull(eventDao.GetEvent(event.getEventID(), event.getAssociatedUsername()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestEventClearAllWithoutAdd() {
            try {
                eventDao.DeleteAll();

                assertNull(eventDao.GetEvent(event.getEventID(), event.getAssociatedUsername()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Person Dao Operations")
    class person {
        Person person;
        Person person2;

        PersonDao personDao;

        @BeforeEach
        public void startup() {
            person = DataGenerator.getRandomPerson("bradofrado", 'm');
            person2 = DataGenerator.getRandomPerson("bradofrado", 'f');

            personDao = new PersonDao();
            personDao.startTransaction();
        }

        @AfterEach
        public void cleanup() {
            personDao.endTransaction();
        }

        @Test
        public void TestAddPerson() {
            try {
                personDao.AddPerson(person);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAddPersonFail() {
            try {
                personDao.AddPerson(person);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
            assertThrows(SQLException.class, () -> {
                personDao.AddPerson(person);
            });
        }

        @Test
        public void TestAddPersons() {
            try {
                List<Person> persons = new ArrayList() {{add(person);} {add(person2);}};
                personDao.AddPersons(persons);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAddPersonsFail() {
            List<Person> persons = new ArrayList() {{add(person);} {add(person);}};
            assertThrows(SQLException.class, () -> {
                personDao.AddPersons(persons);
            });
        }

        @Test
        public void TestGetPerson() {
            try {
                personDao.AddPerson(person);
                Person findPerson=personDao.GetPerson(person.getPersonID(), person.getAssociatedUsername());

                assertEquals(person, findPerson);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetPersonFail() {
            try {
                personDao.AddPerson(person);
                Person findPerson=personDao.GetPerson(person.getPersonID(), person.getPersonID());
                assertNotEquals(person, findPerson);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetPersons() {
            try {
                List<Person> persons = new ArrayList() {{add(person);} {add(person2);}};
                personDao.AddPersons(persons);

                List<Person> findPersons = personDao.GetAllPersons(person.getAssociatedUsername());

                assertEquals(persons.size(), findPersons.size());
                assertTrue(findPersons.contains(person));
                assertTrue(findPersons.contains(person2));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetPersonsFail() {
            try {
                List<Person> persons = new ArrayList() {{add(person);} {add(person2);}};
                personDao.AddPersons(persons);

                List<Person> findPersons=personDao.GetAllPersons("Garbade");

                assertEquals(0, findPersons.size());
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestUpdatePerson() {
            try {
                personDao = new PersonDao();
                personDao.AddPerson(person);
                person.setSpouseID("1234");
                personDao.UpdatePerson(person);

                Person changed = personDao.GetPerson(person.getPersonID(), person.getAssociatedUsername());
                assertTrue(person.equals(changed));

                personDao.DeleteAll();
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestUpdatePersonFail() {
            try {
                personDao.AddPerson(person);

                person.setAssociatedUsername("1234");

                Person changed = personDao.GetPerson(person.getPersonID(), person.getAssociatedUsername());
                assertNull(changed);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestPersonClear() {
            try {
                person.setAssociatedUsername("bob");

                List<Person> persons = new ArrayList() {{add(person);} {add(person2);}};
                personDao.AddPersons(persons);
                personDao.DeleteAll(person.getAssociatedUsername());
                assertNull(personDao.GetPerson(person.getPersonID(), person.getAssociatedUsername()));

                List<Person> findPersons = personDao.GetAllPersons(person2.getAssociatedUsername());
                assertEquals(1, findPersons.size());
                assertEquals(person2, findPersons.get(0));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestPersonClearFail() {
            try {
                personDao.AddPerson(person);
                personDao.DeleteAll("garbade");

                assertNotNull(personDao.GetPerson(person.getPersonID(), person.getAssociatedUsername()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestPersonClearAll() {
            try {
                personDao.AddPerson(person);
                personDao.DeleteAll();

                assertNull(personDao.GetPerson(person.getPersonID(), person.getAssociatedUsername()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestPersonClearAllWithoutAdd() {
            try {
                personDao.DeleteAll();

                assertNull(personDao.GetPerson(person.getPersonID(), person.getAssociatedUsername()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("AuthTokenDao Operations")
    class token {
        AuthToken token;
        AuthToken token2;
        AuthTokenDao tokenDao;
        @BeforeEach
        public void startup() {
            token = new AuthToken("asdf", "bradofrado");
            token2 = new AuthToken("bcde", "john");

            tokenDao = new AuthTokenDao();
            tokenDao.startTransaction();
        }

        @AfterEach
        public void cleanup() {
            tokenDao.endTransaction();
        }

        @Test
        public void TestAddAuthToken() {
            try {
                tokenDao.AddAuthToken(token);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAddAuthTokenFail() {
            try {
                tokenDao.AddAuthToken(token);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
            assertThrows(SQLException.class, () -> {
                tokenDao.AddAuthToken(token);
            });
        }

        @Test
        public void TestGetAuthToken() {
            try {
                tokenDao.AddAuthToken(token);
                AuthToken findAuthToken=tokenDao.getAuthToken(token.getAuthtoken());

                assertEquals(token, findAuthToken);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetAuthTokenFail() {
            try {
                tokenDao.AddAuthToken(token);
                AuthToken findAuthToken=tokenDao.getAuthToken(token.getUsername());
                assertNotEquals(token, findAuthToken);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAuthTokenClearAll() {
            try {
                tokenDao.AddAuthToken(token);
                tokenDao.DeleteAll();

                assertNull(tokenDao.getAuthToken(token.getUsername()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAuthTokenClearAll2() {
            try {
                tokenDao.AddAuthToken(token);
                tokenDao.AddAuthToken(token2);
                tokenDao.DeleteAll();

                assertNull(tokenDao.getAuthToken(token.getUsername()));
                assertNull(tokenDao.getAuthToken(token2.getUsername()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAuthTokenClear() {
            try {
                tokenDao = new AuthTokenDao();
                tokenDao.AddAuthToken(token);
                tokenDao.AddAuthToken(token2);

                tokenDao.DeleteAll(token.getUsername());

                assertNull(tokenDao.getAuthToken(token.getAuthtoken()));
                assertNotNull(tokenDao.getAuthToken(token2.getAuthtoken()));

                tokenDao.DeleteAll();
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAuthTokenClearFail() {
            try {
                tokenDao = new AuthTokenDao();
                tokenDao.AddAuthToken(token);
                tokenDao.DeleteAll("bargade");

                assertNotNull(tokenDao.getAuthToken(token.getAuthtoken()));

                tokenDao.DeleteAll();
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }
    }
}
