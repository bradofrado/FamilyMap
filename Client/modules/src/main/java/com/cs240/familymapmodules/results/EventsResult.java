package com.cs240.familymapmodules.results;

import com.cs240.familymapmodules.models.Event;

public class EventsResult extends Result {
    /**
     * A list of the events
     */
    private Event[] data;

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data=data;
    }
}
