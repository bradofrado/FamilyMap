package com.cs240.familymapmodules.results;

/**
 * The result for the /event/id api call
 */
public class EventResult extends Result {
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

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername=associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID=eventID;
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
}
