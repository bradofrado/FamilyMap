package com.cs240.familymap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;

public class SettingsActivity extends UpActivity {
    LinearLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        frameLayout = findViewById(R.id.settingsFrame);

        View logoutView = findViewById(R.id.logoutView);
        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        createAllSettings(DataCache.getInstance().getSettings());
    }

    private void createAllSettings(List<DataCache.Settings> settings) {
        for (DataCache.Settings setting : settings) {
            createSettings(setting);
        }
    }

    private void createSettings(DataCache.Settings settings) {
        //LayoutInflater inflater = getLayoutInflater();
        //View view = inflater.inflate(R.layout.setting_line_item, frameLayout, false);
        View view = View.inflate(this, R.layout.setting_line_item, null);

        TextView name = view.findViewById(R.id.settingLineItemName);
        TextView description = view.findViewById(R.id.settingLineItemDescription);
        Switch switcher = view.findViewById(R.id.settingLineItemSwitcher);

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.toggleState();
            }
        });

        name.setText(settings.getName());
        description.setText(settings.getDescription());
        switcher.setChecked(settings.getState());

        frameLayout.addView(view);
    }
}