package learn.com.weatherapp;

import android.app.Activity;
import android.content.SharedPreferences;

public class SaveCityOfUser {

    SharedPreferences prefs;

    public SaveCityOfUser(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    // If the user has not chosen a city yet, return
    // Saint-Petersburg,RU as the default city
    String getCity(){
        return prefs.getString("city", "Saint-Petersburg,RU");
    }

    void setCity(String city){
        prefs.edit().putString("city", city).apply();
    }
}
