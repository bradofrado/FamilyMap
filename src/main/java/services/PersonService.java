package services;

import dao.PersonDao;
import exceptions.InvalidAuthTokenException;
import models.Person;
import models.User;
import requests.PersonRequest;
import requests.PersonsRequest;
import results.PersonResult;
import results.PersonsResult;
import util.UserUtil;

import java.sql.SQLException;
import java.util.List;

public class PersonService {
    /**
     * Gets a person with the given id
     * @param request The request object of the wanted person
     * @return The result object holding the wanted person
     */
    public static PersonResult Person(PersonRequest request) {
        PersonResult result = new PersonResult();
        try {
            User user = UserUtil.GetUser(request.getAuthToken());
            Person person = new PersonDao().GetPerson(request.getPersonID(), user.getUsername());

            result.setPersonID(person.getPersonID());
            result.setAssociatedUsername(person.getAssociatedUsername());
            result.setFirstName(person.getFirstName());
            result.setLastName(person.getLastName());
            result.setGender(Character.toString(person.getGender()));
            result.setFatherID(person.getFatherID());
            result.setMotherID(person.getMotherID());
            result.setSpouseID(person.getSpouseID());
        } catch (SQLException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        } catch (InvalidAuthTokenException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        }

        return result;
    }

    /**
     * Gets all of the people for a given user
     * @param request The request object holding the current user
     * @return A result object holding a list of the wanted people
     */
    public static PersonsResult Persons(PersonsRequest request) {
        PersonsResult result = new PersonsResult();
        try {
            User user = UserUtil.GetUser(request.getAuthToken());
            List<Person> persons = new PersonDao().GetAllPersons(user.getUsername());

            result.setData(persons.toArray(new Person[0]));
        } catch (SQLException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        } catch (InvalidAuthTokenException ex) {
            result.setMessage(ex.getMessage());
            result.setSuccess(false);
        }

        return result;
    }
}
