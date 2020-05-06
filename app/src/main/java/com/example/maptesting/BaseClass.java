package com.example.maptesting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public abstract class BaseClass extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView drawerNavigationView;
    protected ActionBarDrawerToggle drawerToggle;
    TextView name,gmail;
    String eName,eImage;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
      

        //drawer navigation
        drawerLayout = (DrawerLayout) findViewById(R.id.drawarLayout);
        //adding drawar button
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //drawer navbar item click
        drawerNavigationView = (NavigationView) findViewById(R.id.drawerNavigationView);
        drawerNavigationView.setNavigationItemSelectedListener(this);

        //header click navbar
        View headerview = drawerNavigationView.getHeaderView(0);
      //  imageView = (ImageView) headerview.findViewById(R.id.profile_image);
        name=(TextView) headerview.findViewById(R.id.userName);

      //   FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
       //   final String uid=userName;

        databaseReference.child( "Users" ).child(userId).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    eName = dataSnapshot.child( "name" ).getValue().toString();
                    name.setText(String.valueOf( eName ));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );



    }


    //drawer open close click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    abstract int getContentViewId();
    abstract int getNavigationMenuItemId();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.backhome) {
            Intent intent=new Intent(this, AllPlaces.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.bookmarks) {
            Intent intent=new Intent(this, search.class);
            startActivity(intent);
            //finish();
        }


        else if (itemId == R.id.logout) {
//            SharedPreferences settings = getSharedPreferences("Log", MODE_PRIVATE);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.remove("isLoggedIn");
//            editor.remove("cat");
//            editor.commit();
            FirebaseAuth.getInstance().signOut();
            Save.save(getApplicationContext(),"session","false");

            Intent intent = new Intent(BaseClass.this , LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;    }


}
