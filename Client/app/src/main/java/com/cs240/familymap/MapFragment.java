package com.cs240.familymap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.graphics.fonts.Font;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

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

    private Event selectedEvent;

    private TextView menuTextView;
    private ImageView menuImageView;
    private View menu;

    private Map<String, Integer> eventColors = new HashMap<>();
    private static final Queue<Integer> allColors = new ArrayDeque() {
        {add(R.color.marker_1);}
        {add(R.color.marker_2);}
        {add(R.color.marker_3);}
        {add(R.color.marker_4);}
        {add(R.color.marker_5);}
        {add(R.color.marker_6);}
    };

    private static final float LINE_WIDTH = 10f;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        menuTextView = view.findViewById(R.id.mapTextView);
        menuImageView = view.findViewById(R.id.menuIcon);
        menu = view.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEvent != null) {
                    Person person = cache.getPerson(selectedEvent.getPersonID());
                    Toast toast = Toast.makeText(getContext(), "You clikced " + person.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        menuImageView.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_android).sizeDp(40).colorRes(R.color.android_icon));

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
        selectedEvent = event;
        //Draw the markers and events
        map.clear();
        drawEvents(cache.getEvents());
        drawSpouseLine(event);
        drawPaternalLines(event);
        drawMaternalLines(event);
        drawLifeStoryLines(event);

        //Update the Menu
        updateMenu(event);

        //scroll to the marker
        LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void updateMenu(Event event) {
        Person person = cache.getPerson(event.getPersonID());
        String text = person.toString() + "\n";
        text += event.toString();
        menuTextView.setText(text);

        FontAwesomeIcons icon = FontAwesomeIcons.fa_male;
        int color = R.color.male_icon;
        if (person.getGender() == 'f') {
            icon = FontAwesomeIcons.fa_female;
            color = R.color.female_icon;
        }

        menuImageView.setImageDrawable(new IconDrawable(getContext(), icon).sizeDp(40).colorRes(color));
    }

    private void drawSpouseLine(Event event) {
        drawLine(event, cache.getBirthOfSpouse(event.getPersonID()), nextColor(), LINE_WIDTH);
    }

    private void drawLifeStoryLines(Event event) {
        List<Event> events = cache.getEventsOfPerson(event.getPersonID());


        for (Event next : events) {
            drawLine(event, next, nextColor(), LINE_WIDTH);
            event = next;
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
        int color = nextColor();
        float width = LINE_WIDTH;
        final float WIDTH_CHANGE = .5f;
        do {
            Event parentEvent = isFather ? cache.getBirthOfFather(currEvent.getPersonID()) : cache.getBirthOfMother(currEvent.getPersonID());
            drawLine(currEvent, parentEvent, color, width);
            width *= WIDTH_CHANGE;
            currEvent = parentEvent;
        } while (currEvent != null);
    }

    private void drawLine(Event startEvent, Event endEvent, int color, float width) {
        if (startEvent == null || endEvent == null) return;

        LatLng startPoint= new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint= new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        PolylineOptions options = new PolylineOptions().add(startPoint).add(endPoint).color(color).width(width);
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

        int color = eventColors.get(event.getEventType());
        LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
        Marker marker = map.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(new IconDrawable(getContext(), FontAwesomeIcons.fa_map_marker).sizeDp(40).colorRes(color))))); //BitmapDescriptorFactory.defaultMarker(color)));

        marker.setTag(event);
    }

    private Bitmap drawableToBitmap(IconDrawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private int nextColor() {
        int color = allColors.peek();

        allColors.remove();
        allColors.add(color);

        return color;
    }
}