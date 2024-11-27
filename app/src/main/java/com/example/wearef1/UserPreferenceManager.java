package com.example.wearef1;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class UserPreferenceManager {
    private static final String PREFERENCES_NAME = "user_pref";
    private static final String KEY_USER = "user";

    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the User object to JSON
        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        // Save the JSON string
        editor.putString(KEY_USER, userJson);
        editor.apply(); // Apply changes asynchronously
    }

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(KEY_USER, null);

        if (userJson != null) {
            Gson gson = new Gson();
            // Convert the JSON string back to a User object
            return gson.fromJson(userJson, User.class);
        }

        return null; // Return null if no user is found
    }

    public static void updateUser(Context context, User updatedUser) {
        // Get the current user from SharedPreferences
        User currentUser = getUser(context);

        if (currentUser != null) {
            // Update fields in the current user object
            if (updatedUser.getFavoriteDriverId() != null) {
                currentUser.setFavoriteDriverId(updatedUser.getFavoriteDriverId());
            }
            if (updatedUser.getFavoriteTeamId() != null) {
                currentUser.setFavoriteTeamId(updatedUser.getFavoriteTeamId());
            }
            // Add any other fields you want to update...

            // Save the updated user back to SharedPreferences
            saveUser(context, currentUser);
        } else {
            // If no user is found, save the new user as the current user
            saveUser(context, updatedUser);
        }
    }

}
