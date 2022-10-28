package models;

import java.util.Objects;

/**
 * The event model for event data
 */
public class Event {
    /**
     * The id of this event object
     */
    private String eventID;

    /**
     * The user associated with this event
     */
    private String associatedUsername;
    /**
     * The person id for this event
     */
    private String personID;
    /**
     * The latitude location of this event
     */
    private float latitude;
    /**
     * The longitude location of the event
     */
    private float longitude;
    /**
     * The country of the event
     */
    private String country;
    /**
     * The city of the event
     */
    private String city;
    /**
     * The type of the event
     */
    private String eventType;
    /**
     * The year of the event
     */
    private int year;

    public Event(String eventID, String associatedUsername, String personID, float latitude, float longtude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longtude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public Event(Event copy, String eventID, String personID) {
        this.eventID = eventID;
        this.associatedUsername = copy.associatedUsername;
        this.personID = personID;
        this.latitude = copy.latitude;
        this.longitude = copy.longitude;
        this.country = copy.country;
        this.city = copy.city;
        this.eventType = copy.eventType;
        this.year = copy.year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID=eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername=associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID=personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude=latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude=longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country=country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city=city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType=eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year=year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event=(Event) o;
        return Float.compare(event.latitude, latitude) == 0 &&
                Float.compare(event.longitude, longitude) == 0 &&
                year == event.year &&
                Objects.equals(eventID, event.eventID) &&
                Objects.equals(associatedUsername, event.associatedUsername) &&
                Objects.equals(personID, event.personID) &&
                Objects.equals(country, event.country) &&
                Objects.equals(city, event.city) &&
                Objects.equals(eventType, event.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }
}
