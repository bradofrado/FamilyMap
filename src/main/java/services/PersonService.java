package services;

import requests.PersonRequest;
import requests.PersonsRequest;
import results.PersonResult;
import results.PersonsResult;

public class PersonService {
    /**
     * Gets a person with the given id
     * @param request The request object of the wanted person
     * @return The result object holding the wanted person
     */
    public static PersonResult Person(PersonRequest request) {
        return null;
    }

    /**
     * Gets all of the people for a given user
     * @param request The request object holding the current user
     * @return A result object holding a list of the wanted people
     */
    public static PersonsResult Persons(PersonsRequest request) {
        return null;
    }
}
