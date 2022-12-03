package com.cs240.familymap.views;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cs240.familymap.DataCache;
import com.cs240.familymap.R;
import com.cs240.familymapmodules.models.Event;
import com.cs240.familymapmodules.models.Person;
import com.joanzapata.iconify.IconDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends UpActivity {
    private DataCache cache;
    private RecyclerView recyclerView;

    private Map<String, Integer> eventColors = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cache = DataCache.getInstance();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Person> persons = filterPeople(newText);
                List<Event> events = filterEvents(newText);

                updateAdapter(persons, events);

                return true;
            }
        });

        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Person> filterPeople(String text) {
        List<Person> filtered = new ArrayList<>();

        if (text == null || text.length() == 0) return filtered;

        List<Person> persons = cache.getPersons();

        for (Person person : persons) {
            if (isContained(person.toString(), text)) {
                filtered.add(person);
            }
        }

        return filtered;
    }

    private List<Event> filterEvents(String text) {
        List<Event> filtered = new ArrayList<>();

        if (text == null || text.length() == 0) return filtered;

        List<Event> events = cache.getEvents();

        for (Event event : events) {
            Person personEvent = cache.getPerson(event.getPersonID());
            if (isContained(personEvent.toString(), text) || isContained(event.toString(), text)) {
                filtered.add(event);
            }
        }

        return filtered;
    }

    private boolean isContained(String fullText, String query) {
        return fullText.toLowerCase().contains(query.toLowerCase());
    }

    private void updateAdapter(List<Person> persons, List<Event> events) {
        SearchViewAdapter adapter = new SearchViewAdapter(persons, events);
        recyclerView.setAdapter(adapter);
    }

    private class SearchViewAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<Person> personList;
        private final List<Event> eventList;

        public SearchViewAdapter(List<Person> personList, List<Event> eventList) {
            this.personList = personList;
            this.eventList = eventList;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.icon_line_item, parent, false);

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < personList.size()) {
                holder.bind(personList.get(position));
            } else {
                holder.bind(eventList.get(position - personList.size()));
            }
        }

        @Override
        public int getItemCount() {
            return personList.size() + eventList.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView text1View;
        private TextView text2View;
        private ImageView iconView;

        private Event event;
        private Person person;

        public SearchViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            itemView.setOnClickListener(this);

            text1View = itemView.findViewById(R.id.iconLineItemText1);
            text2View = itemView.findViewById(R.id.iconLineItemText2);
            iconView = itemView.findViewById(R.id.iconLineItemIcon);
        }

        public void bind(Person person) {
            this.person = person;
            event = null;
            bindView(person.toString(), "", getPersonDrawable(person.getGender()));
        }

        public void bind(Event event) {
            this.event = event;
            person = null;

            if (!eventColors.containsKey(event.getEventID())) {
                eventColors.put(event.getEventID(), nextColor());
            }

            Person eventPerson = DataCache.getInstance().getPerson(event.getPersonID());
            bindView(event.toString(), eventPerson.toString(), getEventIconDrawable(eventColors.get(event.getEventID())));
        }

        private void bindView(String text1, String text2, IconDrawable icon) {
            text1View.setText(text1);
            text2View.setText(text2);
            iconView.setImageDrawable(icon);
        }

        @Override
        public void onClick(View v) {
            if (person != null) {
                sendToPersonActivity(person.getPersonID());
            } else {
                sendToEventActivity(event.getEventID());
            }
        }
    }
}