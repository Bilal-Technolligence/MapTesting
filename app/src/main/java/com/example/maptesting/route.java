package com.example.maptesting;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class route extends AppCompatActivity implements OnMapReadyCallback {
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    LocationManager locationManager;
    String lati, loni;
    int icon;
    Double latitude = 0.0, longitude = 0.0;
    FusedLocationProviderClient mFusedLocationClient;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        Intent i = getIntent();
        id= i.getStringExtra("id");
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        supportMapFragment.getMapAsync(route.this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location1) {
                        // Got last known location. In some rare situations this can be null.
                        if (location1 != null) {
                            // Logic to handle location object

                            longitude = location1.getLongitude();
                            latitude = location1.getLatitude();
                            lati = (String.valueOf(latitude));
                            loni = (String.valueOf(longitude));

                            //showing on map
                            LatLng latLng1 = new LatLng(latitude, longitude);
                            googleMap.addMarker(new MarkerOptions().position(latLng1).title("Your location"));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 20));
                        } else {

                            Toast.makeText(getApplicationContext(), "Please allow location to this app", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
        dref.child("Location").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lat = dataSnapshot.child("latitude").getValue().toString();
                String lon = dataSnapshot.child("longitude").getValue().toString();
                String type = dataSnapshot.child("type").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                if(type.equals("Food")){
                    icon = R.drawable.khana;
                }
                else if(type.equals("Atm")){
                    icon = R.drawable.atm;
                }
                else if(type.equals("Bank")){
                    icon = R.drawable.bank;
                }
                else if(type.equals("Parking")){
                    icon = R.drawable.parking;
                }
                else if(type.equals("Fuel")){
                    icon = R.drawable.fuel;
                }
                else if(type.equals("Hospital")){
                    icon = R.drawable.hospit;
                }
                else if(type.equals("Hotel")){
                    icon = R.drawable.hotel;
                }
                else if(type.equals("Police Station")){
                    icon = R.drawable.polic;
                }
                else if(type.equals("Park")){
                    icon = R.drawable.park;
                }
                else if(type.equals("Pharmacy")){
                    icon = R.drawable.pharm;
                }
                else if(type.equals("Toilet")){
                    icon = R.drawable.toilett;
                }
                else if(type.equals("Train")){
                    icon = R.drawable.train;
                }
                else if(type.equals("Education")){
                    icon = R.drawable.university;
                }
                googleMap.addMarker(new MarkerOptions().position(latLng).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(route.this,icon,name)))).setTitle(name);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_layout, null);

        ImageView markerImage = (ImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView)marker.findViewById(R.id.name);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }
}
