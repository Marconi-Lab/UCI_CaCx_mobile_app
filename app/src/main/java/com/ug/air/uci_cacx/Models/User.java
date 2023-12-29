package com.ug.air.uci_cacx.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    boolean authenticated;
    String sessionId;
    @SerializedName("currentProvider")
    Provider provider;

    @SerializedName("user")
    Another another_user;

    public User(boolean authenticated, String sessionId, Provider provider, Another another_user) {
        this.authenticated = authenticated;
        this.sessionId = sessionId;
        this.provider = provider;
        this.another_user = another_user;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public Provider getProvider() {
        return provider;
    }

    public Another getAnother_user() {
        return another_user;
    }

    public String getSessionId() {
        return sessionId;
    }
}
