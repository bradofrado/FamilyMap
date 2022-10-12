package results;

import models.Person;

/**
 * The persons result object for the /person api call
 */
public class PersonsResult extends Result {
    /**
     * The list of people to retrieve
     */
    private Person[] data;

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data=data;
    }
}
