package com.sidelance.weather.weather360.sensors.locationsensors;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sidelance.weather.weather360.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Class that handles geolocation
 *
 */
public class DeviceLocation extends Activity{

    private static final String TAG = DeviceLocation.class.getSimpleName();

    private LocationManager locationManager;

    private Geocoder geocoder;
    private LocationListener listener;
    public String city, country, provider;
    private List<Address> addressList;
    double x, y;
    boolean clicked;

    Button locationButton;
    TextView userLocation;

    MyLocationListener listen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_location);

        clicked = false;

        locationButton =  ( Button )findViewById(R.id.locationButton);
        userLocation = (TextView) findViewById(R.id.textView);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
                locationButtonClicked();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (city!=null && country != null) {
            userLocation.setText(getCity() + getCountry());
        }
    }

    private void locationButtonClicked(){


        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!enabled){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        LocationListener listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        Log.i(TAG, "==> Button CLick Event <==");
    }

    public class MyLocationListener implements LocationListener{

        @SuppressWarnings("static-access")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onLocationChanged(Location location) {

            x = location.getLatitude();
            y = location.getLongitude();

            try {
                geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                addressList = geocoder.getFromLocation(x, y, 1);
                StringBuilder stringBuilder = new StringBuilder();
                if(geocoder.isPresent()){

                    Log.e(TAG, "==> Geocoder Detected <==");

                    Address returnAddress = addressList.get(0);

                    city = returnAddress.getLocality();
                    country = returnAddress.getCountryName();

                    stringBuilder.append(city).append(", ");
                    stringBuilder.append(country);


                    userLocation.setText(stringBuilder);

                }else{
                    Log.e(TAG, "==> Geocoder Not Detected <==");
                }

            }catch(IOException e){
                Log.e(TAG, "" + e.getMessage());
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
