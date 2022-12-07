package com.cs240.familymap.views;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cs240.familymap.DataCache;
import com.cs240.familymap.R;
import com.cs240.familymap.views.EventActivity;
import com.cs240.familymap.views.MainActivity;
import com.cs240.familymap.views.PersonActivity;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A base class for an activity with some helper methods
 */
public class BaseActivity extends AppCompatActivity {
    public static final String PERSON_ID_KEY = "PersonIDKey";
    public static final String EVENT_ID_KEY = "EventIDKey";

    private static final Queue<Integer> allColors = new ArrayDeque() {
        {add(R.color.marker_1);}
        {add(R.color.marker_2);}
        {add(R.color.marker_4);}
        {add(R.color.marker_5);}
        {add(R.color.marker_6);}
        {add(R.color.marker_3);}
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

    /**
     * Goes to the person activity sending the given personID
     * @param personID
     */
    protected void sendToPersonActivity(String personID) {
        Intent intent = new Intent(this, PersonActivity.class);

        intent.putExtra(PERSON_ID_KEY, personID);
        startActivity(intent);
    }

    /**
     * Goes to the event activity sending the given eventID
     * @param eventID
     */
    protected void sendToEventActivity(String eventID) {
        Intent intent = new Intent(this, EventActivity.class);

        intent.putExtra(EVENT_ID_KEY, eventID);
        startActivity(intent);
    }

    /**
     * Logouts out the user by nulling the authtoken and going to the main activity
     */
    protected void logout() {
        DataCache cache = DataCache.getInstance();
        cache.resetData();
        sendToMainActivity();
    }

    /**
     * Goes to the main activity and clears the activity history
     */
    protected void sendToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
