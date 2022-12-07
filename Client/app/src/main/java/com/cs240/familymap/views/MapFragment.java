package com.cs240.familymap.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs240.familymap.util.DataCache;
import com.cs240.familymap.R;
import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;
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
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * My map fragment
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final float LINE_WIDTH = 10f;
    private static final String PATERNAL_LINE_KEY = "PaternalLine";
    private static final String MATERNAL_LINE_KEY = "MaternalLine";
    private static final String SPOUSE_LINE_KEY = "SpouseLine";
    private static final String LIFE_STORY_LINE_KEY = "LifeStoryLine";

    private boolean isEventActivity = false;
    private GoogleMap map;
    private DataCache cache = DataCache.getInstance();

    private Event selectedEvent;

    private TextView menuTextView;
    private ImageView menuImageView;
    private View menu;
    BaseActivity activity;

    private Map<String, Integer> eventColors = new HashMap<>();
    private Map<String, Integer> lineColors = new HashMap<>();

    public MapFragment() {}

    public MapFragment(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
        isEventActivity = true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        activity = (BaseActivity) getContext();
        menuTextView = view.findViewById(R.id.mapTextView);
        menuImageView = view.findViewById(R.id.menuIcon);
        menu = view.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEvent != null) {
                    Intent intent = new Intent(getContext(), PersonActivity.class);
                    intent.putExtra(activity.PERSON_ID_KEY, selectedEvent.getPersonID());
                    startActivity(intent);
                }
            }
        });
        menuImageView.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_android).sizeDp(40).colorRes(R.color.android_icon));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setHasOptionsMenu(!isEventActivity);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (map == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            if (selectedEvent != null) {
                selectEvent(selectedEvent);
            } else {
                drawEvents(getEvents());
            }
        }
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

        if (selectedEvent != null) {
            selectEvent(selectedEvent);
        } else {
            drawEvents(getEvents());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.searchMenuIcon);
        MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenuIcon);

        setMenuItemIcon(searchMenuItem, FontAwesomeIcons.fa_search, R.color.white);
        setMenuItemIcon(settingsMenuItem, FontAwesomeIcons.fa_gear, R.color.white);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.searchMenuIcon:
                Intent searchIntent = new Intent(getContext(), SearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.settingsMenuIcon:
                Intent settingsIntent = new Intent(getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }

        return true;
    }

    private void setMenuItemIcon(MenuItem menuItem, FontAwesomeIcons icon, int color) {
        menuItem.setIcon(new IconDrawable(getContext(), icon).colorRes(color).actionBarSize());
    }

    /**
     * Selects the given event by centering it and drawing the appropriate lines
     * @param event
     */
    private void selectEvent(Event event) {
        //Draw the markers and events
        map.clear();
        drawEvents(cache.getEvents());

        //This makes sure that a setting wasn't set that filtered out the selected event
        if (cache.filterEvent(event) == null) {
            deselectEvent();
            return;
        }

        selectedEvent = event;

        Map<Integer, DataCache.Settings> lineSettings = cache.getLines();

        if (lineSettings.get(R.string.spouseName).getState()) {
            drawSpouseLine(event);
        }

        if (lineSettings.get(R.string.familyTreeName).getState()) {
            drawParentLine(event);
        }

        if (lineSettings.get(R.string.lifeStoryName).getState()) {
            drawLifeStoryLines(event);
        }

        //Update the Menu
        updateMenu(event);

        //scroll to the marker
        LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLng(location));
    }

    /**
     * Unselects the event and updates the menu accordingly
     */
    private void deselectEvent() {
        selectedEvent = null;
        updateMenu(null);
    }

    /**
     * Updates the menu to show the selected event
     * @param event
     */
    private void updateMenu(Event event) {
        if (event != null) {
            Person person = cache.getPerson(event.getPersonID());
            String text = person.toString() + "\n";
            text += event.toString();
            menuTextView.setText(text);

            menuImageView.setImageDrawable(activity.getPersonDrawable(person.getGender()));
        } else {
            menuTextView.setText(R.string.mapMenuDefaultText);
            menuImageView.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_android).sizeDp(40).colorRes(R.color.android_icon));
        }
    }

    private void drawSpouseLine(Event event) {
        if (!lineColors.containsKey(SPOUSE_LINE_KEY)) {
            lineColors.put(SPOUSE_LINE_KEY, activity.nextColor());
        }
        drawLine(event, cache.getBirthOfSpouse(event.getPersonID()), lineColors.get(SPOUSE_LINE_KEY), LINE_WIDTH);
    }

    private void drawLifeStoryLines(Event event) {
        List<Event> events = cache.getEventsOfPerson(event.getPersonID());
        if (!lineColors.containsKey(LIFE_STORY_LINE_KEY)) {
            lineColors.put(LIFE_STORY_LINE_KEY, activity.nextColor());
        }

        int color = lineColors.get(LIFE_STORY_LINE_KEY);

        Event curr = null;
        for (Event next : events) {
            if (curr != null) {
                drawLine(curr, next, color, LINE_WIDTH);
            }

            curr = next;
        }
    }

    private void drawParentLine(Event event) {
        drawParentLine(event, LINE_WIDTH);
    }

    private void drawParentLine(Event event, float width) {
        if (event == null) return;

        if (!lineColors.containsKey(PATERNAL_LINE_KEY)) {
            lineColors.put(PATERNAL_LINE_KEY, activity.nextColor());
        }
        if (!lineColors.containsKey(MATERNAL_LINE_KEY)) {
            lineColors.put(MATERNAL_LINE_KEY, lineColors.get(PATERNAL_LINE_KEY));
        }

        final float WIDTH_CHANGE = .5f;
        Event fatherEvent = cache.getBirthOfFather(event.getPersonID());
        Event motherEvent = cache.getBirthOfMother(event.getPersonID());

        drawLine(event, fatherEvent, lineColors.get(PATERNAL_LINE_KEY), width);
        drawLine(event, motherEvent, lineColors.get(MATERNAL_LINE_KEY), width);

        width *= WIDTH_CHANGE;

        drawParentLine(fatherEvent, width);
        drawParentLine(motherEvent, width);
    }

    /**
     * Draws a line in between two events
     * @param startEvent The start event of the line
     * @param endEvent The end event of the line
     * @param color The color of the line
     * @param width The width of the line
     */
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
        String eventType = event.getEventType().toLowerCase();
        if (!eventColors.containsKey(eventType)) {
            eventColors.put(eventType, activity.nextColor());
        }

        int color = eventColors.get(eventType);
        LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
        Marker marker = map.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(activity.getEventIconDrawable(color)))));

        marker.setTag(event);
    }

    /**
     * Converts an IconDrawable to a Bitmap
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(IconDrawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}