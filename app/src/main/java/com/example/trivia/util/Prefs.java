package com.example.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

//sharedpreference class
public class Prefs {
    public static final String HIGHEST_SCORE = "highest_score";
    public static final String TRIVIA_STATE = "Trivia_state";
    private final SharedPreferences preferences;

    //constructor on sharedpreference
    public Prefs(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

     //method that will save highest score
    public void saveHighestScore(int score){
        int currentScore = score;
       int lastScore = preferences.getInt(HIGHEST_SCORE, 0);

       if (currentScore > lastScore){
           preferences.edit().putInt(HIGHEST_SCORE, currentScore).apply();
       }
    }

    public int getHighestScore(){
    return preferences.getInt(HIGHEST_SCORE, 0);
    }

    public void setState(int index){
        preferences.edit().putInt(TRIVIA_STATE, index).apply();
    }

    public int getState(){
        return preferences.getInt(TRIVIA_STATE, 0);
    }
}
