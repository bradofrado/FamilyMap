package mytests;

import dao.UserDao;
import models.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class ServerTest {
    @Test
    public void TestAddUser() {
        User user = new User();
        user.setUsername("bradofrado");
        user.setEmail("bradofrado@gmail.com");
        user.setFirstName("Braydon");
        user.setLastName("Jones");
        user.setGender('m');
        user.setPassword("mypassword");
        user.setPersonID("asdf");

        UserDao userDao = new UserDao();

        try {
            userDao.AddUser(user);
        } catch (SQLException ex) {

        }
    }
}
