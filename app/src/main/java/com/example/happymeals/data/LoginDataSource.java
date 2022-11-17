package com.example.happymeals.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.happymeals.DBHandler;
import com.example.happymeals.data.model.User;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    DBHandler db = new DBHandler();
    public Result<User> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            User fakeUser = new User("Jane", "Doe", username);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<User> register(String firstName, String lastName, String username, String password, Context context, ProgressBar bar) {
        //DBHandler will handle errors.
        User user = new User(firstName, lastName, username);
        db.checkAndAddUser(user, password, context, bar);
        return new Result.Success<>(user);

    }

    public void logout() {
        // TODO: revoke authentication
    }
}