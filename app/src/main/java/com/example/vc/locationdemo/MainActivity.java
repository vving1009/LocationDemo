package com.example.vc.locationdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.longitude)
    TextView longitudeTv;
    @BindView(R.id.latitude)
    TextView latitudeTv;
    @BindView(R.id.button)
    Button button;
    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private double latitude, longitude;
    private LocationManager mLocationManager;

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + location.getLatitude() + "," + location.getLongitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            getAddress();
            longitudeTv.setText("Longitude = " + longitude);
            latitudeTv.setText("Latitude = "+latitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: ");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: ");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLocationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 321);
        } else {
            //getLocation();
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getLocation();
                getLastLocation();
            }
        }
    }

    void getLastLocation() {
        @SuppressLint("MissingPermission")
        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d(TAG, "getLastLocation: " + location.getLongitude() + ", " + location.getLatitude());
        }
    }

    void getLocation() {

        List<String> list = mLocationManager.getProviders(true);
        for (String s : list) {
            Log.d(TAG, "getLocation: list = " + s);
        }
        /*if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            try {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, mLocationListener);
                Log.d(TAG, "getLocation: NETWORK_PROVIDER");
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else if (mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {

            try {
                mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 1, mLocationListener);
                Log.d(TAG, "getLocation: PASSIVE_PROVIDER");
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, mLocationListener);
                Log.d(TAG, "getLocation: GPS_PROVIDER");
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            mLocationManager.
        }*/
        try {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            Log.d(TAG, "getLocation: NETWORK_PROVIDER");
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    private void getAddress() {
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        List<Address> locationList = null;
        try {
            locationList = gc.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = locationList.get(0);//得到Address实例
//Log.i(TAG, "address =" + address);
        String countryName = address.getCountryName();//得到国家名称，比方：中国
        Log.i(TAG, "countryName = " + countryName);
        String locality = address.getLocality();//得到城市名称，比方：北京市
        Log.i(TAG, "locality = " + locality);
        for (int i = 0; address.getAddressLine(i) != null; i++) {
            String addressLine = address.getAddressLine(i);//得到周边信息。包含街道等。i=0，得到街道名称
            Log.i(TAG, "addressLine = " + addressLine);
        }
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        Log.d(TAG, "onViewClicked: button clicked");
        getLastLocation();
        longitudeTv.setText("Longitude = " + longitude);
        latitudeTv.setText("Latitude = "+latitude);
    }
}
