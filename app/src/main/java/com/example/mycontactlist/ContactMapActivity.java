package com.example.mycontactlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener gpsListener;
    final int PERMISSION_REQUEST_LOCATION = 101;
    LocationListener networkListener;
    Location currentBestLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_map);
        initGetLocationButton();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initListButton();
        initMapButton();
        initSettingsButton();
        //initGetLocationButton();
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactMapActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMapButton() {
        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setEnabled(false);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactMapActivity.this, ContactMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibList = findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactMapActivity.this, ContactSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initGetLocationButton(){
        Button locationButton = (Button) findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener(){

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v){
                EditText editAddress = (EditText) findViewById(R.id.editAddress);
                EditText editCity = (EditText) findViewById(R.id.editCity);
                EditText editState = (EditText) findViewById(R.id.editState);
                EditText editZipCode = (EditText) findViewById(R.id.editZipcode);

                String address = editAddress.getText().toString() + ", " +
                        editCity.getText().toString() + ", " +
                        editState.getText().toString() + ", " +
                        editZipCode.getText().toString();

                List<Address> addresses = null;
                Geocoder geo = new Geocoder(ContactMapActivity.this);
                try {
                    addresses = geo.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(ContactMapActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                ContactMapActivity.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                            Snackbar.make(findViewById(R.id.map),
                                            "MyContactList requires this permission to locate " +
                                                    "your contacts", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ActivityCompat.requestPermissions(
                                                    ContactMapActivity.this,
                                                    new String[]{
                                                            android.Manifest.permission.ACCESS_FINE_LOCATION
                                                    },
                                                    PERMISSION_REQUEST_LOCATION);
                                        }
                                    }).show();
                        } else {
                            ActivityCompat.requestPermissions(ContactMapActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSION_REQUEST_LOCATION);
                        }
                    } else {
                        startLocationUpdates();
                    }
                } else {
                    startLocationUpdates();
                }

                TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);

                if (addresses != null && !addresses.isEmpty()) {
                    txtLatitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                    txtLongitude.setText(String.valueOf(addresses.get(0).getLongitude()));
                } else {
                    Toast.makeText(ContactMapActivity.this, "Address not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try{
            locationManager.removeUpdates(gpsListener);
            locationManager.removeUpdates(networkListener);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void startLocationUpdates(){
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            locationManager = (LocationManager) getBaseContext()
                    .getSystemService(Context.LOCATION_SERVICE);

            gpsListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
                if(isBetterLocation(location)) { //IMPORTANT
                    currentBestLocation = location;
                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };
            networkListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
                if(isBetterLocation(location)) {  //IMPORTANT
                    currentBestLocation = location;
                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, gpsListener
            );
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,0,0,networkListener
            );
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error, Location not available",
                    Toast.LENGTH_LONG).show();
        }


    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationUpdates();
                } else {
                    Toast.makeText(ContactMapActivity.this,
                            "MyContactList will not locate your contacts.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private boolean isBetterLocation(Location location){
        boolean isBetter = false;
        if(currentBestLocation == null) {
            isBetter = true;
        }
        else if (location.getAccuracy() <= currentBestLocation.getAccuracy()){
            isBetter = true;
        }
        else if (location.getTime() - currentBestLocation.getTime() > 5*60*1000){
            isBetter = true;
        }
        return isBetter;
    }






}