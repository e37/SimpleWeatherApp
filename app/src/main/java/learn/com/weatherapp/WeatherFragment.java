package learn.com.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {
    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    ImageView weatherImage;
    TextView provider;

    Handler handler;

    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment_app, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_field);
        updatedField = (TextView) rootView.findViewById(R.id.updated_field);
        detailsField = (TextView) rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) rootView.findViewById(R.id.current_temperature_field);
        weatherImage = (ImageView) rootView.findViewById(R.id.weather_icon);
        provider = (TextView) rootView.findViewById(R.id.provider_type);

//        TextView providerType = (TextView) rootView.findViewById(R.id.provider_type);  getFragmentManager().findFragmentById(R.id.container).getView().findViewById(R.id.provider_type);


        weatherIcon = (TextView) rootView.findViewById(R.id.weather_icon1);
        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        updateWeatherData(new SaveCityOfUser(getActivity()).getCity());
        updateWeatherData((int)MyLocationListener.imHere.getLatitude(),(int)MyLocationListener.imHere.getLongitude());
    }

    public void updateWeatherData(final int latitude , final int longitude) {
        new Thread() {
            public void run() {
                final JSONObject json;
                try {
                    json = FetchData.getJSON(getActivity(), latitude,longitude);
                    if (json == null) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(),
                                        getActivity().getString(R.string.place_not_found),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    renderWeather(json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) throws JSONException {

        cityField.setText(new StringBuilder().append(json.getString("name").toUpperCase(Locale.US)).append(", ").append(json.getJSONObject("sys").getString("country")).toString());

        JSONObject details = json.getJSONArray("weather").getJSONObject(0);
        JSONObject main = json.getJSONObject("main");
        detailsField.setText(
                new StringBuilder().append(details.getString("description").toUpperCase(Locale.US)).append("\n").append("Humidity: ").append(main.getString("humidity")).append("%").append("\n").append("Pressure: ").append(main.getString("pressure")).append(" hPa").toString());
        int temp = (int) (main.getDouble("temp") - 273.15);
        currentTemperatureField.setText(temp + " ℃");

        DateFormat df = DateFormat.getDateTimeInstance();
        String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
        updatedField.setText("Last update: " + updatedOn);

        setWeatherIcon(details.getInt("id"),
                json.getJSONObject("sys").getLong("sunrise") * 1000,
                json.getJSONObject("sys").getLong("sunset") * 1000);


        Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

//            Location location = MyLocationListener.SetUpLocationListener(getContext());
//            provider.setText((int) location.getAltitude());
        }
    }

    // http://openweathermap.org/weather-conditions
    // the weather codes in the 200 range are related to thunderstorms, which means we can use R.string.weather_thunder for these
    // in the 300 range we use R.string.weather_drizzle and so on

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                weatherImage.setImageResource(R.drawable.sunny);
                changeBackgroundWithHandler(R.color.sunny);

            } else {
                weatherImage.setImageResource(R.drawable.clear_night);
                changeBackgroundWithHandler(R.color.black);

            }
        } else {
            Log.e("myTags", "setWeatherIcon: " + actualId);
            switch (id) {
                case 2:
                    weatherImage.setImageResource(R.drawable.rainy);
                    changeBackgroundWithHandler(R.color.rainy);
                    break;
                case 3:
                    weatherImage.setImageResource(R.drawable.rainy_storm);
                    changeBackgroundWithHandler(R.color.stormy);
                    break;
                case 7:
                    weatherImage.setImageResource(R.drawable.sunny);
                    changeBackgroundWithHandler(R.color.sunny);
                    break;
                case 8:
                    weatherImage.setImageResource(R.drawable.cloudy);
                    changeBackgroundWithHandler(R.color.cloudy);
                    break;
//                case 6 : (R.string.weather_snowy);
//                    break;
                case 5:
                    weatherImage.setImageResource(R.drawable.rainy);
                    changeBackgroundWithHandler(R.color.rainy);
                    break;
            }
        }
    }

    private void changeBackgroundWithHandler(final int resId) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getFragmentManager().findFragmentById(R.id.container) != null)
                    getFragmentManager().findFragmentById(R.id.container).getView().setBackgroundColor(getResources().getColor(resId));
                else
                    Toast.makeText(getContext(), "fragment not found", Toast.LENGTH_LONG).show();
            }
        });
    }

//    public void changeCity(String city) {
//        updateWeatherData(city);
//    }
}
