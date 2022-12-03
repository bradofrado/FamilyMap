package com.cs240.familymap;

import android.content.Intent;
import android.view.MenuItem;

public class UpActivity extends BaseActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            sendToMainActivity();
        }

        return true;
    }

}
