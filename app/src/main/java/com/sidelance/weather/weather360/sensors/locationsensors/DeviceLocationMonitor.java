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

/**
 * Class that handles geolocation
 *
 */
public class DeviceLocationMonitor extends Activity{

    private static final String TAG = DeviceLocationMonitor.class.getSimpleName();

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

        if (city != null && country != null) {

            userLocation.setText(getCity() + ", "+ getCountry());
        }
    }

    /**
     * Checks GPS settings and
     * */
    private void locationButtonClicked(){

        try {

            LocationListener listener = new MyLocationListener();

            boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gpsEnabled && !networkEnabled) {
                Log.i(TAG, "==> Enable GPS or Network to get Location <==");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);

            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

            }
            Log.i(TAG, "==> Button CLick Event <==");
        }catch (Throwable throwable){
            Log.e(TAG, "LocationMonitor: " + throwable.getMessage());
        }
    }

    public QualifiedCoordinates getLastKnowLocation(){

        QualifiedCoordinates coordinates = null;
        if (locationManager != null){

            Location lastLocation;
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if ((gpsLocation != null) && (networkLocation != null)){
                long gpsLocationTime = gpsLocation.getTime();
                long networkLocationTime = networkLocation.getTime();
                if (networkLocationTime > gpsLocationTime){
                    lastLocation = networkLocation;
                }else{
                    lastLocation = gpsLocation;
                }
            }else if (gpsLocation != null){
                lastLocation = gpsLocation;
            }else{
                lastLocation = gpsLocation;
            }
            if (lastLocation != null){
                coordinates = new QualifiedCoordinates(lastLocation.getLatitude(), lastLocation.getLongitude(), lastLocation.getAccuracy());

            }
        }
        return coordinates;
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

                    Log.i(TAG, "==> Geocoder Detected <==");

                    Address returnAddress = addressList.get(0);

                    city = returnAddress.getLocality();
                    country = returnAddress.getCountryName();
                    String address = returnAddress.getAddressLine(0);

                    stringBuilder.append(address).append(", ");
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

    public static class QualifiedCoordinates{

        private double latitude;

        private double longitude;

        private float accuracy;

        /**
         * Constructor
         */
        public QualifiedCoordinates(double latitude, double longitude, float accuracy) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.accuracy = accuracy;
        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public float getAccuracy() {
            return this.accuracy;
        }
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
