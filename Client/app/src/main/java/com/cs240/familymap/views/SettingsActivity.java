package com.cs240.familymap.views;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cs240.familymap.util.DataCache;
import com.cs240.familymap.R;

import java.util.List;

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

    /**
     * Creates all of the given settings' UI
     * @param settings
     */
    private void createAllSettings(List<DataCache.Settings> settings) {
        for (DataCache.Settings setting : settings) {
            createSettings(setting);
        }
    }

    /**
     * Creates a single setting's UI
     * @param settings
     */
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