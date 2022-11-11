package com.cs240.familymap;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String FIRSTNAME_KEY = "numPeople";
    private static final String LASTNAME_KEY = "numEvents";
    private static final String IS_SUCCESS_KEY = "isSucess";

    private MainActivityViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registerButton = findViewById(R.id.registerButton);
        Button signinButton = findViewById(R.id.signinButton);

        EditText serverHostField = findViewById(R.id.serverHostField);
        EditText serverPortField = findViewById(R.id.serverPortField);
        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);
        EditText emailField = findViewById(R.id.emailField);
        EditText firstNameField = findViewById(R.id.firstNameField);
        EditText lastNameField = findViewById(R.id.lastNameField);
        RadioGroup genderField = findViewById(R.id.genderField);

        serverHostField.setText(getViewModel().getServerHost());
        serverPortField.setText(getViewModel().getServerPort());
        usernameField.setText(getViewModel().getUsername());
        passwordField.setText(getViewModel().getPassword());
        firstNameField.setText(getViewModel().getFirstName());
        lastNameField.setText(getViewModel().getLastName());
        emailField.setText(getViewModel().getEmail());
        genderField.check(getViewModel().getGender() == 'm' ? R.id.maleRadio : R.id.famaleRadio);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler registerHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        boolean isSuccess = bundle.getBoolean(IS_SUCCESS_KEY);

                        if (!isSuccess) {
                            String str = "There was an error on the register";
                            Toast toast = Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            doGetDataTask(serverHostField.getText().toString(), serverPortField.getText().toString(), firstNameField.getText().toString(), lastNameField.getText().toString());
                        }
                    }
                };

                RegisterTask registerTask = new RegisterTask(registerHandler,
                        serverHostField.getText().toString(),
                        serverPortField.getText().toString(),
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
                        boolean isSuccess = bundle.getBoolean(IS_SUCCESS_KEY);

                        if (!isSuccess) {
                            String str = "There was an error on the login";
                            Toast toast = Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            doGetDataTask(serverHostField.getText().toString(), serverPortField.getText().toString(), firstNameField.getText().toString(), lastNameField.getText().toString());
                        }
                    }
                };

                LoginTask loginTask = new LoginTask(loginHandler,
                        serverHostField.getText().toString(),
                        serverPortField.getText().toString(),
                        usernameField.getText().toString(),
                        passwordField.getText().toString());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(loginTask);
            }
        });
    }

    private void doGetDataTask(String serverHost, String serverPort, String firstName, String lastName) {
        Handler getDataHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                String str = "Hello " + firstName + " " + lastName;
                Toast toast = Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG);
                toast.show();
            }
        };

        GetDataTask getDataTask = new GetDataTask(getDataHandler, serverHost, serverPort);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(getDataTask);
    }

    private static class LoginTask implements Runnable {
        String username;
        String password;
        String serverHost;
        String serverPort;

        Handler handler;

        public LoginTask(Handler handler, String serverHost, String serverPort, String username, String password) {
            this.handler = handler;
            this.username = username;
            this.password = password;
            this.serverHost = serverHost;
            this.serverPort = serverPort;
        }

        @Override
        public void run() {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);

            ServerFacade facade = new ServerFacade(serverHost, serverPort);
            LoginResult result = facade.login(loginRequest);

            if (result.isSuccess()) {
                DataCache.getInstance().setAuthToken(result.getAuthtoken());
            }

            sendMessage(result.isSuccess());
        }

        private void sendMessage(boolean isSuccess) {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putBoolean(IS_SUCCESS_KEY, isSuccess);

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
        String serverHost;
        String serverPort;
        char gender;

        public RegisterTask(Handler handler, String serverHost, String serverPort, String email, String firstName, String lastName, String username, String password, char gender) {
            this.handler = handler;
            this.serverHost = serverHost;
            this.serverPort = serverPort;
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

            ServerFacade facade = new ServerFacade(serverHost, serverPort);
            RegisterResult result = facade.register(registerRequest);

            if (result.isSuccess()) {
                DataCache.getInstance().setAuthToken(result.getAuthtoken());
            }

            sendMessage(result.isSuccess());
        }

        private void sendMessage(boolean isSuccess) {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putBoolean(IS_SUCCESS_KEY, isSuccess);

            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    private static class GetDataTask implements Runnable {
        Handler handler;
        String serverHost;
        String serverPort;

        public GetDataTask(Handler handler, String serverHost, String serverPort) {
            this.handler = handler;
            this.serverHost = serverHost;
            this.serverPort = serverPort;
        }

        @Override
        public void run() {
            ServerFacade facade = new ServerFacade(serverHost, serverPort);
            DataCache cache = DataCache.getInstance();
            PersonsResult result = facade.getPersons(cache.getAuthToken());
            EventsResult eventsResult = facade.getEvents(cache.getAuthToken());


            cache.setPersons(result.getData());
            cache.setEvents(eventsResult.getData());

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