package com.cs240.familymap.views;

import android.view.MenuItem;

import com.cs240.familymap.views.BaseActivity;

public class UpActivity extends BaseActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            sendToMainActivity();
        }

        return true;
    }

}
