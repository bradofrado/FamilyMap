package dao;

import models.Person;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDao extends Dao {
    /**
     * Adds a person to the database
     * @param person The Person to add
     */
    public void AddPerson(Person person) throws SQLException {
        doTransaction((connection) -> {
            insertPersons(connection, new ArrayList<>(){{add(person);}});
        });
    }

    /**
     * Adds a list of persons into the database
     * @param persons The list of person objects to add to the database
     */
    public void AddPersons(List<Person> persons) throws SQLException {
        doTransaction((connection) -> {
            insertPersons(connection, persons);
        });
    }

    /**
     * Gets a Person object given a person id
     * @param personID The id of the person you want
     * @param username The current user associated with this person
     * @return The wanted person object
     */
    public Person GetPerson(String personID, String username) throws SQLException {
        List<Person> persons = new ArrayList<>();
        doTransaction((connection) -> {
            persons.addAll(getPersons(connection, username, personID));
        });

        return persons.size() > 0 ? persons.get(0) : null;
    }

    /**
     * Returns all of the people associated with the current user
     * @param username The current user associated with these people
     * @return A list of Person objects associated with the given user
     */
    public List<Person> GetAllPersons(String username) throws SQLException {
        List<Person> persons = new ArrayList<>();
        doTransaction((connection) -> {
            persons.addAll(getPersons(connection, username, null));
        });

        return persons;
    }

    public void UpdatePerson(Person person) throws SQLException {
        doTransaction((connection) -> {
            updatePerson(connection, person);
        });
    }

    /**
     * Deletes all person data from the database
     */
    public void DeleteAll() throws SQLException {
        doTransaction((connection) -> {
            deletePersons(connection, null);
        });
    }

    /**
     * @param username The username of the person to delete
     * Deletes all person data for the current user
     */
    public void DeleteAll(String username) throws SQLException {
        doTransaction((connection) -> {
            deletePersons(connection, username);
        });
    }

    private void insertPersons(Connection connection, List<Person> persons) throws SQLException {
        String sql="insert into person (personID, associatedUsername, firstname, lastname, gender, fatherID, motherID, spouseID) values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt=connection.prepareStatement(sql)) {
            for (Person person : persons) {
                stmt.setString(1, person.getPersonID());
                stmt.setString(2, person.getAssociatedUsername());
                stmt.setString(3, person.getFirstName());
                stmt.setString(4, person.getLastName());
                stmt.setString(5, Character.toString(person.getGender()));
                stmt.setString(6, person.getFatherID());
                stmt.setString(7, person.getMotherID());
                stmt.setString(8, person.getSpouseID());

                if (stmt.executeUpdate() == 1) {
                    System.out.println("Inserted person " + person);
                } else {
                    System.out.println("Failed to insert person");
                }
            }
        }
    }

    private List<Person> getPersons(Connection connection, String username, String personId) throws SQLException {
        String sql="select personID, associatedUsername, firstname, lastname, gender, fatherID, motherID, spouseID from person where associatedUsername = ?";
        if (personId != null) sql += " and personID = ?";

        try (PreparedStatement stmt=connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            if (personId != null) {
                stmt.setString(2, personId);
            }
            ResultSet rs = stmt.executeQuery();
            List<Person> persons = new ArrayList<>();
            while (rs.next()) {
                Person person = new Person(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getString(5).charAt(0));
                person.setFatherID(rs.getString(6));
                person.setMotherID(rs.getString(7));
                person.setSpouseID(rs.getString(8));

                persons.add(person);
            }

            return persons;
        }
    }

    private void updatePerson(Connection connection, Person person) throws SQLException {
        String sql = "update person set firstName = ?, lastName = ?, associatedUsername = ?, gender = ?, fatherID = ?, motherID = ?, spouseID = ? where personID = ?";

        try (PreparedStatement stmt= connection.prepareStatement(sql)) {
            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setString(3, person.getAssociatedUsername());
            stmt.setString(4, person.getGender() + "");
            stmt.setString(5, person.getFatherID());
            stmt.setString(6, person.getMotherID());
            stmt.setString(7, person.getSpouseID());
            stmt.setString(8, person.getPersonID());

            if (stmt.executeUpdate() == 1) {
                System.out.println("Updated person " + person);
            } else {
                System.out.println("Failed to update person");
            }
        }
    }

    private void deletePersons(Connection connection, String username) throws SQLException {
        String sql = "delete from person";
        if (username != null) sql += " where associatedUsername = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (username != null) {
                stmt.setString(1, username);
            }
            int count = stmt.executeUpdate();

            // Reset the auto-increment counter so new books start over with an id of 1
//            sql = "delete from sqlite_sequence where name = 'book'";
//            try(PreparedStatement resetSequenceStatement = connection.prepareStatement(sql)) {
//                resetSequenceStatement.executeUpdate();
//            }

            System.out.printf("Deleted %d persons\n", count);
        }
    }
}
