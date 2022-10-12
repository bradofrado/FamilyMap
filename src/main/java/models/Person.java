package models;

/**
 * Model for the Person database table
 */
public class Person {
    /**
     * The id of this person
     */
    private String personID;
    /**
     * The user associated with this person
     */
    private String associatedUsername;
    /**
     * This person's first name
     */
    private String firstName;
    /**
     * This person's last name
     */
    private String lastName;
    /**
     * This person's gender 'f' or 'm'
     */
    private char gender;
    /**
     * This person's father id
     */
    private String fatherID;
    /**
     * This person's mother id
     */
    private String motherID;
    /**
     * This person's spouse id
     */
    private String spouseID;

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID=personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername=associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName=firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName=lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender=gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID=fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID=motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID=spouseID;
    }
}
