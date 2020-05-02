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
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

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
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

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
        distance = findViewById(R.id.txtDistance);
        time = findViewById(R.id.txtTime);
        car = findViewById(R.id.txtCar);
        cycle = findViewById(R.id.txtCycle);
        bike = findViewById(R.id.txtBike);
        book = findViewById(R.id.imgBookMark);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        dref.child("BookMark").child(uid).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    book.setImageResource(R.drawable.bookmark);
                }
                else
                    book.setImageResource(R.drawable.addbook);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dref.child("BookMark").child(uid).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
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
                                        dref.child("BookMark").child(uid).child(name)
                                                .setValue(attr);
                                        Toast.makeText(getApplicationContext(), "Bookmark saved.", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            dref.child("BookMark").child(uid).child(id)
                                    .setValue(null);
                            Toast.makeText(getApplicationContext(), "Bookmark removed.", Toast.LENGTH_LONG).show();

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
//                    String d= String.valueOf(distance(Double.parseDouble(lat),latitude,Double.parseDouble(lon),longitude));
                    final int Radius = 6371;// radius of earth in Km
                    final double Latitude = Double.parseDouble(lat);
                    final double Longitude = Double.parseDouble(lon);
                    if (latitude == 0.0) {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(route.this);

                        Task<Location> task = fusedLocationProviderClient.getLastLocation();
                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    currentLocation = location;
                                    //Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                                    latitude = currentLocation.getLatitude();
                                    longitude = currentLocation.getLongitude();
                                }
                                double lat2 = latitude;
                                double lon2 = longitude;
                                // googleMap.addMarker(new MarkerOptions().position(new LatLng(lat2, lon2)).title("Destination"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.dp)));

                                double dLat = Math.toRadians(lat2 - Latitude);
                                double dLon = Math.toRadians(lon2 - Longitude);
                                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                                        + Math.cos(Math.toRadians(Latitude))
                                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                                        * Math.sin(dLon / 2);
                                double c = 2 * Math.asin(Math.sqrt(a));
                                double valueResult = Radius * c;
                                double km = valueResult / 1;
                                DecimalFormat newFormat = new DecimalFormat("####");
                                int kmInDec = Integer.valueOf(newFormat.format(km));
                                double meter = valueResult % 1000;
                                int meterInDec = Integer.valueOf(newFormat.format(meter));
                                Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                                        + " Meter   " + meterInDec);
                                if (kmInDec != 0) {
                                    distance.setText(kmInDec + " km");
                                    String w = String.valueOf(((kmInDec / 5)));
                                    String cy = String.valueOf(((kmInDec / 20)));
                                    String b = String.valueOf(((kmInDec / 40)));
                                    String ca = String.valueOf(((kmInDec / 60)));
                                    if ((kmInDec / 5) >= 1) {
                                        int d = ((kmInDec % 5));
                                        time.setText(w + " h " + String.valueOf(d) + " m");
                                    } else
                                        time.setText(String.valueOf((kmInDec / 5000)) + " m");
                                    if ((kmInDec / 20) >= 1) {
                                        int d = ((kmInDec % 20));
                                        cycle.setText(cy + " h " + String.valueOf(d) + " m");
                                    } else
                                        cycle.setText(String.valueOf((kmInDec / 20000)) + " m");
                                    if ((kmInDec / 40) >= 1) {
                                        int d = ((kmInDec % 40));
                                        bike.setText(b + " h " + String.valueOf(d) + " m");
                                    } else {
                                        int d = ((kmInDec % 40));
                                        bike.setText(String.valueOf(d) + " m");
                                    }
                                    if ((kmInDec / 60) >= 1) {
                                        int d = ((kmInDec % 60));
                                        car.setText(ca + " h " + String.valueOf(d) + " m");
                                    } else {
                                        int d = ((kmInDec % 60));
                                        car.setText(String.valueOf(d) + " m");
                                    }
                                } else {
                                    distance.setText(meterInDec + " m");
                                    Double w = Double.valueOf(((meterInDec / 1.38889)));
                                    Double cy = Double.valueOf(((meterInDec / 5.55556)));
                                    Double b = Double.valueOf(((meterInDec / 11.1111)));
                                    Double ca = Double.valueOf(((meterInDec / 16.6667)));
                                    time.setText(String.valueOf(w) + " m");
                                    cycle.setText(String.valueOf(cy) + " m");
                                    bike.setText(String.valueOf(b) + " m");
                                    car.setText(String.valueOf(ca) + " m");
                                }
                            }
                        });
                    } else {
                        double lat2 = latitude;
                        double lon2 = longitude;
                        // googleMap.addMarker(new MarkerOptions().position(new LatLng(lat2, lon2)).title("Destination"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.dp)));

                        double dLat = Math.toRadians(lat2 - Latitude);
                        double dLon = Math.toRadians(lon2 - Longitude);
                        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                                + Math.cos(Math.toRadians(Latitude))
                                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                                * Math.sin(dLon / 2);
                        double c = 2 * Math.asin(Math.sqrt(a));
                        double valueResult = Radius * c;
                        double km = valueResult / 1;
                        DecimalFormat newFormat = new DecimalFormat("####");
                        int kmInDec = Integer.valueOf(newFormat.format(km));
                        double meter = valueResult % 1000;
                        int meterInDec = Integer.valueOf(newFormat.format(meter));
                        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                                + " Meter   " + meterInDec);
                        if (kmInDec != 0) {
                            distance.setText(kmInDec + " km");
                            String w = String.valueOf(((kmInDec / 5)));
                            String cy = String.valueOf(((kmInDec / 20)));
                            String b = String.valueOf(((kmInDec / 40)));
                            String ca = String.valueOf(((kmInDec / 60)));
                            if ((kmInDec / 5) >= 1) {
                                int d = ((kmInDec % 5));
                                time.setText(w + " h " + String.valueOf(d) + " m");
                            } else
                                time.setText(String.valueOf((kmInDec / 5000)) + " m");
                            if ((kmInDec / 20) >= 1) {
                                int d = ((kmInDec % 20));
                                cycle.setText(cy + " h " + String.valueOf(d) + " m");
                            } else
                                cycle.setText(String.valueOf((kmInDec / 20000)) + " m");
                            if ((kmInDec / 40) >= 1) {
                                int d = ((kmInDec % 40));
                                bike.setText(b + " h " + String.valueOf(d) + " m");
                            } else {
                                int d = ((kmInDec % 40));
                                bike.setText(String.valueOf(d) + " m");
                            }
                            if ((kmInDec / 60) >= 1) {
                                int d = ((kmInDec % 60));
                                car.setText(ca + " h " + String.valueOf(d) + " m");
                            } else {
                                int d = ((kmInDec % 60));
                                car.setText(String.valueOf(d) + " m");
                            }
                        } else {
                            distance.setText(meterInDec + " m");
                            Double w = Double.valueOf(((meterInDec / 1.38889)));
                            Double cy = Double.valueOf(((meterInDec / 5.55556)));
                            Double b = Double.valueOf(((meterInDec / 11.1111)));
                            Double ca = Double.valueOf(((meterInDec / 16.6667)));
                            time.setText(String.valueOf(w) + " m");
                            cycle.setText(String.valueOf(cy) + " m");
                            bike.setText(String.valueOf(b) + " m");
                            car.setText(String.valueOf(ca) + " m");
                        }
                    }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(route.this, search.class));
        finish();
    }
}
