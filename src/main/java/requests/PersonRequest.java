package requests;

/**
 * The person request object of the person/id api call
 */
public class PersonRequest {
    /**
     * The person id to retrieve
     */
    private String personID;

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID=personID;
    }
}
