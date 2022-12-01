package com.cs240.familymap;

import android.content.Intent;
import android.view.MenuItem;

public class UpActivity extends BaseActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
    }

}
