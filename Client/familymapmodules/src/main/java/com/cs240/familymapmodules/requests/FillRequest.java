package com.cs240.familymapmodules.requests;

/**
 * The request for the /fill/[username]/?generation api call
 */
public class FillRequest {
    /**
     * The username of who we want to fill
     */
    private String username;
    /**
     * The number of generations to fill
     */
    private int generations;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations=generations;
    }
}
