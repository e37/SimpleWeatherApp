package learn.com.weatherapp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

public class FetchData {
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=";

    @Nullable
    public static JSONObject getJSON(Context context, String city) throws IOException, JSONException {

        URL url = new URL(OPEN_WEATHER_MAP_API + city + "&APPID=324d4fd4cd536fc14529b54980d72371");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        StringBuilder json = new StringBuilder(1024);
        String tmp = "";
        while ((tmp = reader.readLine()) != null)
            json.append(tmp).append("\n");

        JSONObject data = new JSONObject(json.toString());

        // This value will be 404 if the request was not
        // successful
        if (data.getInt("cod") != 200) {
            Log.e("myLogs", "getJSON: " + data.getInt("cod"));
            return null;
        }
        return data;

//        } catch (Exception e) {
//            return null;
    }
}
