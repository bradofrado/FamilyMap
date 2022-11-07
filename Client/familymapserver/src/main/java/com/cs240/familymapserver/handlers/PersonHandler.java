package com.cs240.familymapserver.handlers;

import com.cs240.familymapmodules.requests.PersonRequest;
import com.cs240.familymapmodules.requests.PersonsRequest;
import com.cs240.familymapmodules.results.PersonResult;
import com.cs240.familymapmodules.results.PersonsResult;
import com.cs240.familymapserver.services.PersonService;

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
