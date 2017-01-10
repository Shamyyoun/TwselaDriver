package com.twsela.driver.controllers;

import android.content.Context;

import com.twsela.driver.Const;
import com.twsela.driver.models.entities.User;
import com.twsela.driver.utils.SharedPrefs;


/**
 * Created by Shamyyoun on 8/27/16.
 */
public class ActiveUserController {
    private static User user;
    private Context context;
    private SharedPrefs<User> userPrefs;

    public ActiveUserController(Context context) {
        this.context = context;
        userPrefs = new SharedPrefs(context, User.class);
    }

    public void save() {
        userPrefs.save(user, Const.SP_USER);
    }

    public User getUser() {
        if (user == null) {
            user = userPrefs.load(Const.SP_USER);
        }

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean hasLoggedInUser() {
        return getUser() != null;
    }

    public void logout() {
        userPrefs.remove(Const.SP_USER);
        setUser(null);
    }
}
