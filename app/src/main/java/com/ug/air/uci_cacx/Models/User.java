package com.ug.air.uci_cacx.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("currentProvider")
    Provider provider;

    @SerializedName("user")
    Another another_user;

    public User(Provider provider, Another another_user) {
        this.provider = provider;
        this.another_user = another_user;
    }

    public Provider getProvider() {
        return provider;
    }

    public Another getAnother_user() {
        return another_user;
    }
}
