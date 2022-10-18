package mytests;

import dao.AuthTokenDao;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import models.AuthToken;
import models.Event;
import models.Person;
import models.User;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DaoTest {
    @Nested
    @DisplayName("UserDao Operations")
    class user {
        User user;
        UserDao userDao;
        @BeforeEach
        public void startup() {
            user = new User("bradofrado", "mypassword", "bradofrado@gmail.com", "Braydon", "Jones", 'm', "asdf");

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
    }

    @Nested
    @DisplayName("Event Dao Operations")
    class event {
        Event event;
        EventDao eventDao;
        @BeforeEach
        public void setup() {
            event = new Event("hellothere", "bradofrado", "asdf", 0, 10, "USA", "Morgan", "math", 1980);

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
        public void TestEventClear() {
            try {
                eventDao.AddEvent(event);
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
        PersonDao personDao;

        @BeforeEach
        public void startup() {
            person = new Person("asdf", "bradofrado", "Braydon", "Jones", 'm');

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
        public void TestPersonClear() {
            try {
                personDao.AddPerson(person);
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
        AuthTokenDao tokenDao;
        @BeforeEach
        public void startup() {
            token = new AuthToken("asdf", "bradofrado");

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
                AuthToken findAuthToken=tokenDao.GetAuthToken(token.getUsername());

                assertEquals(token, findAuthToken);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestGetAuthTokenFail() {
            try {
                tokenDao.AddAuthToken(token);
                AuthToken findAuthToken=tokenDao.GetAuthToken(token.getAuthtoken());
                assertNotEquals(token, findAuthToken);
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }

        @Test
        public void TestAuthTokenClear() {
            try {
                tokenDao.AddAuthToken(token);
                tokenDao.DeleteAll();

                assertNull(tokenDao.GetAuthToken(token.getUsername()));
            } catch (SQLException ex) {
                fail(ex.getMessage());
            }
        }
    }
}
