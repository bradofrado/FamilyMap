package dao;

import models.Person;
import models.User;

import java.util.List;

public class PersonDao {
    /**
     * Adds a person to the database
     * @param person The Person to add
     */
    public void AddPerson(Person person) {

    }

    /**
     * Adds a list of persons into the database
     * @param persons The list of person objects to add to the database
     */
    public void AddPersons(List<Person> persons) {

    }

    /**
     * Gets a Person object given a person id
     * @param personID The id of the person you want
     * @param username The current user associated with this person
     * @return The wanted person object
     */
    public Person GetPerson(String personID, String username) {
        return null;
    }

    /**
     * Returns all of the people associated with the current user
     * @param username The current user associated with these people
     * @return A list of Person objects associated with the given user
     */
    public List<Person> GetAllPersons(String username) {
        return null;
    }

    /**
     * Deletes all auth token data from the database
     */
    public void DeleteAll() {

    }

    /**
     * @param username The username of the person to delete
     * Deletes all person data for the current user
     */
    public void DeleteAll(String username) {

    }
}
