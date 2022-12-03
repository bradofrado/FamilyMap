package com.cs240.familymap.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs240.familymap.DataCache;
import com.cs240.familymap.R;
import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;
import com.joanzapata.iconify.IconDrawable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonActivity extends UpActivity {
    private DataCache cache;

    private Person person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        TextView firstName = findViewById(R.id.personFirstName);
        TextView lastName = findViewById(R.id.personLastName);
        TextView gender = findViewById(R.id.personGender);

        Intent intent = getIntent();
        cache = DataCache.getInstance();
        person = cache.getPerson(intent.getStringExtra(PERSON_ID_KEY));

        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        gender.setText(person.getGender() == 'm' ? "Male" : "Female");

        List<Event> events = cache.getEventsOfPerson(person.getPersonID());
        List<Person> family = cache.getFamilyOfPerson(person.getPersonID());
        Map<String, String> familyRelationship = new HashMap<>();
        for (Person member : family) {
            String relationship = "Child";
            if (member.getPersonID().equals(person.getSpouseID())) {
                relationship = "Spouse";
            } else if (member.getPersonID().equals(person.getMotherID())) {
                relationship = "Mother";
            } else if (member.getPersonID().equals(person.getFatherID())) {
                relationship = "Father";
            }

            familyRelationship.put(member.getPersonID(), relationship);
        }

        ExpandableListView expandableListView = findViewById(R.id.personList);
        expandableListView.setAdapter(new ExpandableListViewAdapter(family, events, familyRelationship));
    }

    private class ExpandableListViewAdapter extends BaseExpandableListAdapter {
        private final int EVENTS_KEY = 0;
        private final int FAMILY_KEY = 1;

        private List<Person> family;
        private List<Event> events;
        private Map<String, String> familyRelationships;

        private Map<Integer, Integer> eventColors = new HashMap<>();

        public ExpandableListViewAdapter(List<Person> family, List<Event> events, Map<String, String> familyRelationships) {
            this.family = family;
            this.events = events;
            this.familyRelationships = familyRelationships;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENTS_KEY:
                    return events.size();
                case FAMILY_KEY:
                    return family.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch(groupPosition) {
                case EVENTS_KEY:
                    return getString(R.string.lifeEvents);
                case FAMILY_KEY:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch(groupPosition) {
                case EVENTS_KEY:
                    return events.get(childPosition);
                case FAMILY_KEY:
                    return family.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
           return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_view_group, parent, false);
            }

            TextView text = convertView.findViewById(R.id.listViewText);

            switch(groupPosition) {
                case EVENTS_KEY:
                    text.setText(R.string.lifeEvents);
                    break;
                case FAMILY_KEY:
                    text.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.icon_line_item, parent, false);
            }

            String text1Text;
            String text2Text;
            IconDrawable icon;

            switch(groupPosition) {
                case EVENTS_KEY:
                    Event event = events.get(childPosition);
                    Person person = cache.getPerson(event.getPersonID());
                    text1Text = event.toString();
                    text2Text = person.toString();

                    if (!eventColors.containsKey(childPosition)) {
                        eventColors.put(childPosition, nextColor());
                    }
                    icon = getEventIconDrawable(eventColors.get(childPosition));
                    break;
                case FAMILY_KEY:
                    Person familyPerson = family.get(childPosition);
                    text1Text = familyPerson.toString();
                    text2Text = familyRelationships.get(familyPerson.getPersonID());
                    icon = getPersonDrawable(familyPerson.getGender());
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }

            ImageView iconView = convertView.findViewById(R.id.iconLineItemIcon);
            TextView text1 = convertView.findViewById(R.id.iconLineItemText1);
            TextView text2 = convertView.findViewById(R.id.iconLineItemText2);

            iconView.setImageDrawable(icon);
            text1.setText(text1Text);
            text2.setText(text2Text);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (groupPosition) {
                        case EVENTS_KEY:
                            sendToEventActivity(events.get(childPosition).getEventID());
                            break;
                        case FAMILY_KEY:
                            sendToPersonActivity(family.get(childPosition).getPersonID());
                            break;
                        default:
                            throw new IllegalArgumentException("Unrecognized group position");
                    }
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}