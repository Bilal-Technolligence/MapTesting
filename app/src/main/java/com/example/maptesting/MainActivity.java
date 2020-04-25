package com.example.maptesting;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private MarkerOptions place1,place2;
    Button getDirection;
    private Polyline currentPolyline;
    List<MarkerOptions> markerOptionsList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchURL(MainActivity.this)
                        .execute(getUrl(place1.getPosition(),place2.getPosition(),"driving"),"driving");

            }
        });


    }

    private String getUrl(LatLng origin,LatLng destination,String directionMode) {

        return String.valueOf(0);
    }

}
