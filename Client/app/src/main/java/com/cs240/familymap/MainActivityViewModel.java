package com.cs240.familymap;

import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    public String getServerHost() {
        return "10.0.2.2";
    }

    public String getServerPort() {
        return "8080";
    }

    public String getUsername() {
        return "bradofrado";
    }

    public String getPassword() {
        return "eBay";
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
