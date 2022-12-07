package com.cs240.familymap.views;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.cs240.familymap.util.DataCache;
import com.cs240.familymap.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends BaseActivity implements LoginFragment.Listener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createLoginFragment();

            manager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        } else {
            if (fragment instanceof LoginFragment) {
                ((LoginFragment)fragment).registerListener(this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        FragmentManager manager = this.getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        //If we are logged out, show the login fragment
        if (DataCache.getInstance().getAuthToken() == null) {
            //If it isn't already a login fragment
            if (!(fragment instanceof LoginFragment)) {
                fragment = createLoginFragment();

                manager.beginTransaction().replace(R.id.fragmentContainer, fragment)
                        .commit();
            }
        } else {
            if (!(fragment instanceof MapFragment)) {
                fragment = new MapFragment();

                manager.beginTransaction().replace(R.id.fragmentContainer, fragment)
                        .commit();
            }
        }
    }

    private Fragment createLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.registerListener(this);
        return loginFragment;
    }

    @Override
    public void notifyDone() {
        FragmentManager manager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        manager.beginTransaction().replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}