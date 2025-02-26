package com.example.mycontactlist;

import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class random {
//    private void initGetLocationButton() {
//        Button locationButton = findViewById(R.id.buttonGetLocation);
//        locationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText editAddress = findViewById(R.id.editAddress);
//                EditText editCity = findViewById(R.id.editCity);
//                EditText editState = findViewById(R.id.editState);
//                EditText editZipCode = findViewById(R.id.editZipcode);
//
//                String address = editAddress.getText().toString() + ", " +
//                        editCity.getText().toString() + ", " +
//                        editState.getText().toString() + ", " +
//                        editZipCode.getText().toString();
//
//                // Run the geocoding operation on a separate thread
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Geocoder geo = new Geocoder(ContactMapActivity.this);
//                        List<Address> addresses = null;
//                        try {
//                            addresses = geo.getFromLocationName(address, 1);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (addresses != null && !addresses.isEmpty()) {
//                            Address location = addresses.get(0);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    TextView txtLatitude = findViewById(R.id.textLatitude);
//                                    TextView txtLongitude = findViewById(R.id.textLongitude);
//                                    txtLatitude.setText(String.valueOf(location.getLatitude()));
//                                    txtLongitude.setText(String.valueOf(location.getLongitude()));
//                                }
//                            });
//                        } else {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(ContactMapActivity.this, "Unable to find location. Please check the address and try again.", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    }
//                }).start();
//            }
//        });
//    }
}
