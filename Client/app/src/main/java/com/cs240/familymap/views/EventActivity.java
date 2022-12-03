package com.cs240.familymap.views;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.cs240.familymap.DataCache;
import com.cs240.familymap.R;
import com.cs240.familymapmodules.models.Event;

public class EventActivity extends UpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        Event event = DataCache.getInstance().getEvent(intent.getStringExtra(EVENT_ID_KEY));

        Fragment fragment = new MapFragment(event);
        fragmentManager.beginTransaction().add(R.id.eventFragmentContainer, fragment)
                .commit();
    }
}