package util;

import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import models.Event;
import models.Person;
import models.User;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Util class for populating generations
 */
public class PopulationGenerator {
    private static final int GENERATION_GAP = 25;


    /**
     * Populates the given amount of generations for the given user
     * @param user The user to populate
     * @param generations The number of generations to generate
     */
    public static void populateGenerations(User user, int generations) throws SQLException {
        Person person = generatePerson(user.getUsername(), user.getGender(), generations, user.getFirstName(), user.getLastName(), LocalDate.now().getYear() - GENERATION_GAP);
        user.setPersonID(person.getPersonID());
        new UserDao().UpdateUser(user);
    }

    /**
     * Generates a new person and saves it in the database
     * @param username The username associated with this person
     * @param gender The gender of this person
     * @param generations The number of generations to fill
     * @param firstname The person's firstname, or null if this person will have random firstname
     * @param lastname The person's lastname or null if this person will have random lastname
     * @return The generated Person
     * @throws SQLException
     */
    private static Person generatePerson(String username, char gender, int generations, String firstname, String lastname, int year) throws SQLException {
        Person mother = null;
        Person father = null;

        if (generations > 0) {
            mother = generatePerson(username, 'f', generations - 1, null, null, year - GENERATION_GAP);
            father = generatePerson(username, 'm', generations - 1, null, null, year - GENERATION_GAP);

            mother.setSpouseID(father.getPersonID());
            father.setSpouseID(mother.getPersonID());

            //Create and save marriage event
            Event fatherMarriage = DataGenerator.getRandomEvent(username, "marriage", father.getPersonID(), year - 5, year + 5);
            Event motherMarriage = new Event(fatherMarriage, DataGenerator.getRandomId(), mother.getPersonID());
            saveEvent(fatherMarriage);
            saveEvent(motherMarriage);

            //Create and save death event
            Event fatherDeath = DataGenerator.getRandomEvent(username, "death", father.getPersonID(), year + 5, year + 10);
            Event motherDeath = DataGenerator.getRandomEvent(username, "death", mother.getPersonID(), year + 5, year + 10);
            saveEvent(fatherDeath);
            saveEvent(motherDeath);
        }

        Person person;
        //If we have specified names, let's create a person with those names
        if (firstname != null && lastname != null) {
            person = new Person(DataGenerator.getRandomId(), username, firstname, lastname, gender);
        //Otherwise, create a random person
        } else {
            person = DataGenerator.getRandomPerson(username, gender);
        }

        if (mother != null) {
            person.setMotherID(mother.getPersonID());
        }

        if (father != null) {
            person.setFatherID(father.getPersonID());
            person.setLastName(father.getLastName());
        }

        savePerson(person);

        Event birth = DataGenerator.getRandomEvent(username, "birth", person.getPersonID(), year - 10, year);
        saveEvent(birth);

        return person;
    }

    private static void saveEvent(Event event) throws SQLException {
        new EventDao().AddEvent(event);
    }

    private static void savePerson(Person person) throws SQLException {
        new PersonDao().AddPerson(person);
    }
}
