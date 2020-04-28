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
import com.google.firebase.auth.FirebaseAuth;
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
    TextView location, distance, time, car, bike, cycle;
    ImageView book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        Intent i = getIntent();
        id = i.getStringExtra("id");
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        supportMapFragment.getMapAsync(route.this);
        location = findViewById(R.id.txtName);
        distance =findViewById(R.id.txtDistance);
        time = findViewById(R.id.txtTime);
        car = findViewById(R.id.txtCar);
        cycle = findViewById(R.id.txtCycle);
        bike = findViewById(R.id.txtBike);
        book = findViewById(R.id.imgBookMark);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                dref.child("Location").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String lat = dataSnapshot.child("latitude").getValue().toString();
                            String lon = dataSnapshot.child("longitude").getValue().toString();
                            String type = dataSnapshot.child("type").getValue().toString();
                            String name = dataSnapshot.child("name").getValue().toString();
                            String id = dataSnapshot.child("id").getValue().toString();

                            Location_Attr attr = new Location_Attr();
                            attr.setId(id);
                            attr.setName(name);
                            attr.setLongitude(lon);
                            attr.setLatitude(lat);
                            attr.setType(type);
                            dref.child("Bookmark").child(uid).child(name)
                                    .setValue(attr);
                            Toast.makeText(getApplicationContext(), "Bookmark saved.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
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
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 20));
                        } else {

                            Toast.makeText(getApplicationContext(), "Please allow location to this app", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
        dref.child("Location").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String lat = dataSnapshot.child("latitude").getValue().toString();
                    String lon = dataSnapshot.child("longitude").getValue().toString();
                    String type = dataSnapshot.child("type").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    location.setText(name);
                    String d= String.valueOf(distance(Double.parseDouble(lat),latitude,Double.parseDouble(lon),longitude));
                    distance.setText(d.substring(0,6)+" km");
                    String w = String.valueOf((( Double.parseDouble(d.substring(0,6))/5)  ));
                    String c = String.valueOf(((Double.parseDouble(d.substring(0,6))/20) ));
                    String b = String.valueOf(((Double.parseDouble(d.substring(0,6))/40) ));
                    String ca = String.valueOf(((Double.parseDouble(d.substring(0,6))/60) ));
                    time.setText(w.substring(0,6) +" h");
                    cycle.setText(c.substring(0,6) +" h");
                    bike.setText(b.substring(0,6) +" h");
                    car.setText(ca.substring(0,6) +" h");
                    LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                    if (type.equals("Food")) {
                        icon = R.drawable.khana;
                    } else if (type.equals("Atm")) {
                        icon = R.drawable.atm;
                    } else if (type.equals("Bank")) {
                        icon = R.drawable.bank;
                    } else if (type.equals("Parking")) {
                        icon = R.drawable.parking;
                    } else if (type.equals("Fuel")) {
                        icon = R.drawable.fuel;
                    } else if (type.equals("Hospital")) {
                        icon = R.drawable.hospit;
                    } else if (type.equals("Hotel")) {
                        icon = R.drawable.hotel;
                    } else if (type.equals("Police Station")) {
                        icon = R.drawable.polic;
                    } else if (type.equals("Park")) {
                        icon = R.drawable.park;
                    } else if (type.equals("Pharmacy")) {
                        icon = R.drawable.pharm;
                    } else if (type.equals("Toilet")) {
                        icon = R.drawable.toilett;
                    } else if (type.equals("Train")) {
                        icon = R.drawable.train;
                    } else if (type.equals("Education")) {
                        icon = R.drawable.university;
                    }
                    googleMap.addMarker(new MarkerOptions().position(latLng).
                            icon(BitmapDescriptorFactory.fromBitmap(
                                    createCustomMarker(route.this, icon, name)))).setTitle(name);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                }
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
        TextView txt_name = (TextView) marker.findViewById(R.id.name);
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
    public static double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2)
    {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }
}
