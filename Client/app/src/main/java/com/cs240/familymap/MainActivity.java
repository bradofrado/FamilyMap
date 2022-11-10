package com.cs240.familymap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cs240.familymapmodules.requests.LoginRequest;
import com.cs240.familymapmodules.requests.RegisterRequest;
import com.cs240.familymapmodules.results.LoginResult;
import com.cs240.familymapmodules.results.RegisterResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String NUM_PEOPLE_KEY = "numPeople";
    private static final String NUM_EVENTS_KEY = "numEvents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registerButton = findViewById(R.id.registerButton);
        Button signinButton = findViewById(R.id.signinButton);

        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);
        EditText emailField = findViewById(R.id.emailField);
        EditText firstNameField = findViewById(R.id.firstNameField);
        EditText lastNameField = findViewById(R.id.lastNameField);
        RadioGroup genderField = findViewById(R.id.genderField);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler registerHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        int numPeople = bundle.getInt(NUM_PEOPLE_KEY, 0);
                        int numEvents = bundle.getInt(NUM_EVENTS_KEY, 0);
                        String str = "There were " + numPeople + " people created and " + numEvents + " events.";
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG);
                    }
                };

                RegisterTask registerTask = new RegisterTask(registerHandler,
                        emailField.getText().toString(),
                        firstNameField.getText().toString(),
                        lastNameField.getText().toString(),
                        usernameField.getText().toString(),
                        passwordField.getText().toString(),
                        genderField.getCheckedRadioButtonId() == R.id.maleRadio ? 'm' : 'f');
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(registerTask);
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler loginHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        int numPeople = bundle.getInt(NUM_PEOPLE_KEY, 0);
                        int numEvents = bundle.getInt(NUM_EVENTS_KEY, 0);
                        String str = "There were " + numPeople + " people created and " + numEvents + " events.";
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG);
                    }
                };

                LoginTask loginTask = new LoginTask(loginHandler, usernameField.getText().toString(), passwordField.getText().toString());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(loginTask);
            }
        });
    }

    private static class LoginTask implements Runnable {
        String username;
        String password;
        Handler handler;

        public LoginTask(Handler handler, String username, String password) {
            this.handler = handler;
            this.username = username;
            this.password = password;
        }

        @Override
        public void run() {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);

            LoginResult result = ServerFacade.login(loginRequest);

            sendMessage();
        }

        private void sendMessage() {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            int numPeople = DataCache.getInstance().getPersons(username).size();
            int numEvents = DataCache.getInstance().getEvents(username).size();

            bundle.putInt(NUM_PEOPLE_KEY, numPeople);
            bundle.putInt(NUM_EVENTS_KEY, numEvents);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    private static class RegisterTask implements Runnable {
        Handler handler;
        String email;
        String firstName;
        String lastName;
        String username;
        String password;
        char gender;

        public RegisterTask(Handler handler, String email, String firstName, String lastName, String username, String password, char gender) {
            this.handler = handler;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.password = password;
            this.gender = gender;
        }
        @Override
        public void run() {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(email);
            registerRequest.setFirstName(firstName);
            registerRequest.setLastName(lastName);
            registerRequest.setUsername(username);
            registerRequest.setPassword(password);
            registerRequest.setGender(gender);

            RegisterResult result = ServerFacade.register(registerRequest);

            sendMessage();
        }

        private void sendMessage() {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();

            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}