package com.cs240.familymap;

import android.content.Intent;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A base class for an activity with some helper methods
 */
public class BaseActivity extends AppCompatActivity {
    protected static final String PERSON_ID_KEY = "PersonIDKey";
    protected static final String EVENT_ID_KEY = "EventIDKey";

    private static final Queue<Integer> allColors = new ArrayDeque() {
        {add(R.color.marker_1);}
        {add(R.color.marker_2);}
        {add(R.color.marker_3);}
        {add(R.color.marker_4);}
        {add(R.color.marker_5);}
        {add(R.color.marker_6);}
        {add(R.color.marker_7);}
        {add(R.color.marker_8);}
        {add(R.color.marker_9);}
        {add(R.color.marker_10);}
    };

    /**
     * @return The IconDrawable for an event marker with a random color
     */
    public IconDrawable getEventIconDrawable() {
        return getEventIconDrawable(nextColor());
    }


    /**
     * Gets the IconDrawable of an event marker for the given color
     * @param color The color of the marker
     * @return
     */
    public IconDrawable getEventIconDrawable(int color) {
        return new IconDrawable(this, FontAwesomeIcons.fa_map_marker).sizeDp(40).color(color);
    }

    /**
     * Gets a male or female IconDrawable, depending on the given gender
     * @param gender The gender of the icon
     * @return
     */
    public IconDrawable getPersonDrawable(char gender) {
        return gender == 'm' ?
                new IconDrawable(this, FontAwesomeIcons.fa_male).sizeDp(40).colorRes(R.color.male_icon) :
                new IconDrawable(this, FontAwesomeIcons.fa_female).sizeDp(40).colorRes(R.color.female_icon);
    }

    /**
     * Gets a random color
     * @return
     */
    public int nextColor() {
        int color = allColors.peek();

        allColors.remove();
        allColors.add(color);

        return ContextCompat.getColor(this, color);
    }

    protected void sendToPersonActivity(String personID) {
        Intent intent = new Intent(this, PersonActivity.class);

        intent.putExtra(PERSON_ID_KEY, personID);
        startActivity(intent);
    }

    protected void sendToEventActivity(String eventID) {
        Intent intent = new Intent(this, EventActivity.class);

        intent.putExtra(EVENT_ID_KEY, eventID);
        startActivity(intent);
    }

    protected void logout() {
        DataCache.getInstance().setAuthToken(null);
        sendToMainActivity();
    }

    protected void sendToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
