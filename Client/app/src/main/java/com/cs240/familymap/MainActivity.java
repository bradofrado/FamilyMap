package com.cs240.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cs240.familymapmodules.requests.LoginRequest;
import com.cs240.familymapmodules.requests.PersonsRequest;
import com.cs240.familymapmodules.requests.RegisterRequest;
import com.cs240.familymapmodules.results.EventsResult;
import com.cs240.familymapmodules.results.LoginResult;
import com.cs240.familymapmodules.results.PersonsResult;
import com.cs240.familymapmodules.results.RegisterResult;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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