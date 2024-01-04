package com.example.standu;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USERID = "userId";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return !sharedPreferences.getString(KEY_USERID, "").isEmpty();
    }

    public static void loginUser(Context context, String userId, String nama, String email, String username, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public static void logoutUser(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    public static User getUserDetails(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return new User(
                sharedPreferences.getString(KEY_USERID, ""),
                sharedPreferences.getString(KEY_NAMA, ""),
                sharedPreferences.getString(KEY_EMAIL, ""),
                sharedPreferences.getString(KEY_USERNAME, ""),
                sharedPreferences.getString(KEY_PASSWORD, "")
        );
    }

    public static class User {
        public final String userId;
        public final String nama;
        public final String email;
        public final String username;
        public final String password;

        public User(String userId, String nama, String email, String username, String password) {
            this.userId = userId;
            this.nama = nama;
            this.email = email;
            this.username = username;
            this.password = password;
        }
    }
}

