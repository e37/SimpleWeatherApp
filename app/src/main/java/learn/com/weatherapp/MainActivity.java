package learn.com.weatherapp;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         MyLocationListener.SetUpLocationListener(this);
       getFragmentManager().beginTransaction().add(R.id.container, new WeatherFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_city) {
            TextView tv = (TextView) findViewById(R.id.textView2);
            assert tv != null;

            if (MyLocationListener.imHere != null)
                tv.setText(String.format("LOL%s", String.valueOf(MyLocationListener.imHere.getLongitude())));
            else

            showInputDialog();
        }
        return false;
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city) {
        WeatherFragment fragment = (WeatherFragment)getFragmentManager()
                .findFragmentById(R.id.container);
        fragment.updateWeatherData(city);
        new SaveCityOfUser(this).setCity(city);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyLocationListener.turnItOff();
    }

}
