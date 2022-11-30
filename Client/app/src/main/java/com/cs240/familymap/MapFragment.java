package com.cs240.familymap;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs240.familymapmodules.models.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * My map fragment
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private DataCache cache = DataCache.getInstance();

    private Map<String, Float> eventColors = new HashMap<>();
    private static final Queue<Float> allColors = new ArrayDeque() {
        {add(BitmapDescriptorFactory.HUE_AZURE);}
        {add(BitmapDescriptorFactory.HUE_BLUE);}
        {add(BitmapDescriptorFactory.HUE_ORANGE);}
        {add(BitmapDescriptorFactory.HUE_ROSE);}
        {add(BitmapDescriptorFactory.HUE_CYAN);}
        {add(BitmapDescriptorFactory.HUE_GREEN);}
        {add(BitmapDescriptorFactory.HUE_MAGENTA);}
        {add(BitmapDescriptorFactory.HUE_RED);}
        {add(BitmapDescriptorFactory.HUE_VIOLET);}
        {add(BitmapDescriptorFactory.HUE_YELLOW);}
    };

    private static final float LINE_WIDTH = 10f;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Event event = (Event)marker.getTag();

                selectEvent(event);
                return true;
            }
        });

        drawEvents(getEvents());
    }

    private void selectEvent(Event event) {
        map.clear();
        drawEvents(cache.getEvents());

        drawSpouseLine(event);
        drawPaternalLines(event);
        drawMaternalLines(event);
        drawLifeStoryLines(event);

        LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void drawSpouseLine(Event event) {
        drawLine(event, cache.getBirthOfSpouse(event.getPersonID()), allColors.peek(), LINE_WIDTH);
    }

    private void drawLifeStoryLines(Event event) {
        List<Event> events = cache.getEventsOfPerson(event.getPersonID());

        for (Event next : events) {
            drawLine(event, next, allColors.peek(), LINE_WIDTH);
        }
    }

    private void drawPaternalLines(Event event) {
        drawParentLine(event, true);
    }

    private void drawMaternalLines(Event event) {
        drawParentLine(event, false);
    }

    private void drawParentLine(Event event, boolean isFather) {
        Event currEvent = event;
        float color = allColors.peek();
        float width = LINE_WIDTH;
        final float WIDTH_CHANGE = .5f;
        do {
            Event parentEvent = isFather ? cache.getBirthOfFather(currEvent.getPersonID()) : cache.getBirthOfMother(currEvent.getPersonID());
            drawLine(currEvent, parentEvent, color, width);
            width *= WIDTH_CHANGE;
            currEvent = parentEvent;
        } while (currEvent != null);
    }

    private void drawLine(Event startEvent, Event endEvent, float color, float width) {
        int hex = ((int)color & 0xff) << 24;

        if (startEvent == null || endEvent == null) return;

        LatLng startPoint= new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint= new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        PolylineOptions options = new PolylineOptions().add(startPoint).add(endPoint).color(hex).width(width);
        Polyline line = map.addPolyline(options);
    }

    private List<Event> getEvents() {
        return cache.getEvents();
    }

    private void drawEvents(List<Event> events) {
        for (Event event : events) {
            drawEvent(event);
        }
    }

    private void drawEvent(Event event) {
        if (!eventColors.containsKey(event.getEventType())) {
            eventColors.put(event.getEventType(), nextColor());
        }

        float color = eventColors.get(event.getEventType());
        LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
        Marker marker = map.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(color)));

        marker.setTag(event);
    }

    private float nextColor() {
        float color = allColors.peek();

        allColors.remove();
        allColors.add(color);

        return color;
    }
}