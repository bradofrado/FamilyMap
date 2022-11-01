package handlers;

import requests.PersonRequest;
import requests.PersonsRequest;
import results.PersonResult;
import results.PersonsResult;
import services.PersonService;

import java.io.IOException;

/**
 * Http handler for the /person and /person/{id} endpoints
 */
public class PersonHandler extends Handler {
    @Override
    protected void initRoutes() {
        get("/person", this::getPersons);
        get("/person/{id}", this::getPerson);
    }

    private void getPerson(Request request) throws IOException {
        String id = request.getParameters().get("id");
        PersonRequest personRequest = new PersonRequest();
        personRequest.setPersonID(id);
        personRequest.setAuthToken(request.getAuthToken());

        PersonResult result =PersonService.Person(personRequest);

        sendResult(result);
    }

    private void getPersons(Request request) throws IOException {
        PersonsRequest personRequest = new PersonsRequest();
        personRequest.setAuthToken(request.getAuthToken());

        PersonsResult result =PersonService.Persons(personRequest);

        sendResult(result);
    }
}
