package com.cs240.familymapserver.services;

import com.cs240.familymapmodules.exceptions.InvalidAuthTokenException;
import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.User;
import com.cs240.familymapmodules.requests.EventRequest;
import com.cs240.familymapmodules.requests.EventsRequest;
import com.cs240.familymapmodules.results.EventResult;
import com.cs240.familymapmodules.results.EventsResult;
import com.cs240.familymapserver.dao.EventDao;
import com.cs240.familymapserver.util.UserUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * The service class for the /event endpoints
 */
public class EventService {
    /**
     * Gets a event with the given id
     * @param request The request object of the wanted event
     * @return The result object holding the wanted event
     */
    public static EventResult Event(EventRequest request) {
        EventResult result = new EventResult();

        try {
            User user = UserUtil.GetUser(request.getAuthToken());
            Event event = new EventDao().GetEvent(request.getEventID(), user.getUsername());

            if (event == null) {
                result.setMessage("Cannot find event with id " + request.getEventID());
                result.setSuccess(false);
                return result;
            }

            result.setEventID(event.getEventID());
            result.setPersonID(event.getPersonID());
            result.setAssociatedUsername(event.getAssociatedUsername());
            result.setLatitude(event.getLatitude());
            result.setLongitude(event.getLongitude());
            result.setCountry(event.getCountry());
            result.setCity(event.getCity());
            result.setEventType(event.getEventType());
            result.setYear(event.getYear());
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
     * Gets all of the events for a given user
     * @param request The request object holding the current user
     * @return A result object holding a list of the wanted events
     */
    public static EventsResult Events(EventsRequest request) {
        EventsResult result = new EventsResult();

        try {
            User user = UserUtil.GetUser(request.getAuthToken());
            List<Event> events = new EventDao().GetEvents(user.getUsername());

            result.setData(events.toArray(new Event[0]));
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
