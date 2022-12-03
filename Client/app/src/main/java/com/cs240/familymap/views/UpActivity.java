package com.cs240.familymap.views;

import android.view.MenuItem;

import com.cs240.familymap.views.BaseActivity;

/**
 * A base class for any activity that wants the up button to go to the main activity
 */
public class UpActivity extends BaseActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            sendToMainActivity();
        }

        return true;
    }

}
