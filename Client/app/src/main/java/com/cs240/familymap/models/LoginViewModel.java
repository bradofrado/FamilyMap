package com.cs240.familymap.models;

import androidx.lifecycle.ViewModel;

/**
 * A view model for the login fragment to store preset data
 */
public class LoginViewModel extends ViewModel {
    public String getServerHost() {
        return "10.0.2.2";
    }

    public String getServerPort() {
        return "8080";
    }

    public String getUsername() {
        return "sheila";
    }

    public String getPassword() {
        return "parker";
    }

    public String getFirstName() {
        return "Braydon";
    }

    public String getLastName() {
        return "Jones";
    }

    public String getEmail() {
        return "bradofrado@gmail.com";
    }

    public char getGender() {
        return 'm';
    }
}