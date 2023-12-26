package com.ug.air.uci_cacx.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    boolean authenticated;
    String session_id;
    @SerializedName("currentProvider")
    Provider provider;

    @SerializedName("user")
    Another another_user;

    public User(boolean authenticated, String session_id, Provider provider, Another another_user) {
        this.authenticated = authenticated;
        this.session_id = session_id;
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

    public String getSession_id() {
        return session_id;
    }
}
