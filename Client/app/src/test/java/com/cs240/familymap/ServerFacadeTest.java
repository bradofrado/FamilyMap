package com.cs240.familymap;

import static org.junit.jupiter.api.Assertions.*;

import com.cs240.familymapmodules.requests.LoginRequest;
import com.cs240.familymapmodules.requests.RegisterRequest;
import com.cs240.familymapmodules.results.EventsResult;
import com.cs240.familymapmodules.results.LoginResult;
import com.cs240.familymapmodules.results.PersonsResult;
import com.cs240.familymapmodules.results.RegisterResult;

import org.junit.jupiter.api.*;

public class ServerFacadeTest {
    ServerFacade facade;
    @BeforeEach
    public void setup() {
        facade = new ServerFacade("localhost", "8080");
    }

    @Test
    public void TestLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("bradofrado");
        request.setPassword("eBay");

        LoginResult loginResult = facade.login(request);

        assertTrue(loginResult.isSuccess());
        assertNotNull(loginResult.getAuthtoken());
    }

    @Test
    public void TestLoginFail() {
        LoginRequest request = new LoginRequest();
        request.setUsername("bradofrado");
        request.setPassword("eBay123123123123123123123");

        LoginResult loginResult = facade.login(request);

        assertFalse(loginResult.isSuccess());
        assertNull(loginResult.getAuthtoken());
    }

    @Test
    public void TestRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setGender('m');
        request.setFirstName("Bob");
        request.setLastName("Dylan");
        request.setUsername("bobbyboii");
        request.setPassword("bob123");
        request.setEmail("bobbybobbyyes@gmail.com");

        RegisterResult result = facade.register(request);

        assertTrue(result.isSuccess());
        assertNotNull(result.getAuthtoken());
    }

    @Test
    public void TestRegisterFail() {
        RegisterRequest request = new RegisterRequest();
        request.setGender('m');
        request.setFirstName("Bob");
        request.setLastName("Dylan");
        request.setPassword("bob123");

        RegisterResult result = facade.register(request);

        assertFalse(result.isSuccess());
        assertNull(result.getAuthtoken());
    }

    @Test
    public void TestGetPersons() {
        LoginRequest request = new LoginRequest();
        request.setUsername("bradofrado");
        request.setPassword("eBay");

        LoginResult loginResult = facade.login(request);

        PersonsResult result = facade.getPersons(loginResult.getAuthtoken());

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertNotEquals(0, result.getData().length);
    }

    @Test
    public void TestGetPersonsFail() {
        PersonsResult result = facade.getPersons("asdfasdfasdfasdf");

        assertFalse(result.isSuccess());
        assertNull(result.getData());
    }

    @Test
    public void TestGetEvents() {
        LoginRequest request = new LoginRequest();
        request.setUsername("bradofrado");
        request.setPassword("eBay");

        LoginResult loginResult = facade.login(request);

        EventsResult result = facade.getEvents(loginResult.getAuthtoken());

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertNotEquals(0, result.getData().length);
    }

    @Test
    public void TestGetEventsFail() {
        EventsResult result = facade.getEvents("asdfasdfasdfasdf");

        assertFalse(result.isSuccess());
        assertNull(result.getData());
    }
}
