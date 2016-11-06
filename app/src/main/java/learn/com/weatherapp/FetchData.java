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


class FetchData {
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?lat=";
    private static final String OPEN_WEATHER_MAP_API_CITY = "http://api.openweathermap.org/data/2.5/weather?q=";

    @Nullable
    static JSONObject getJSON(Context context, int latitude, int longitude) throws IOException, JSONException {

        URL url = new URL(OPEN_WEATHER_MAP_API + latitude+"&lon=" +longitude+ "&APPID=324d4fd4cd536fc14529b54980d72371");

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
            return null;
        }else
        return data;
    }

    static JSONObject getJSON(Context context, String city) throws IOException, JSONException {

        URL url = new URL(OPEN_WEATHER_MAP_API_CITY +city + "&APPID=324d4fd4cd536fc14529b54980d72371");

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
            return null;
        }else
        return data;
    }

}
