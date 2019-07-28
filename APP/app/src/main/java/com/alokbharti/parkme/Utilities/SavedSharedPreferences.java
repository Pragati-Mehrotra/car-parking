package com.alokbharti.parkme.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SavedSharedPreferences {

    public static void setUserId(Context context, int userId, boolean signIn){
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.alokbharti.parkme",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.putBoolean("signInStatus", signIn);
        editor.commit();
    }

    public static boolean getSignInStatus(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.alokbharti.parkme",Context.MODE_PRIVATE);
        boolean signInStatus = sharedPreferences.getBoolean("signInStatus",false);
        return signInStatus;
    }

    public static int getUserId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.alokbharti.parkme",Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",0);
        return userId;
    }
}