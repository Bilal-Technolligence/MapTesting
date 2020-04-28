package com.example.maptesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class search extends AppCompatActivity {
    EditText searchtext;
    ArrayList<Location_Attr> location_attrs;
    ArrayList<Location_Attr> location_attrs1;
    SearchListAdapter adapter;
    RecyclerView recyclerView, recyclerView1;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchtext= findViewById(R.id.find);
        location_attrs = new ArrayList<Location_Attr>();
        recyclerView=findViewById(R.id.searchList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initTextListener();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView1 = findViewById(R.id.bookmarkList);
        location_attrs1 = new ArrayList<Location_Attr>();
        recyclerView1.setLayoutManager(layoutManager);
        String userId = "1";//FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        databaseReference.child("BookMark").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                location_attrs1.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Location_Attr p = dataSnapshot1.getValue(Location_Attr.class);
                    location_attrs1.add(p);
                }
                recyclerView1.setAdapter(new BookMarkAdapter(location_attrs1 , getApplicationContext() , search.this  ));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void initTextListener() {
        location_attrs.clear();
        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchtext.getText().toString().toUpperCase();
                searchForMatch(text);
            }

        });
    }

    private void searchForMatch(String text) {
        location_attrs.clear();
        updatePostList();
        if(text.length() ==0)
        {
            return;
        }

        else
        {

            Query query = FirebaseDatabase.getInstance().getReference("Location")
                    .orderByChild("name")
                    .startAt(text)
                    .endAt(text+"\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    location_attrs.clear();
                    for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren())
                    {
                        if(!location_attrs.contains(singleSnapshot.getValue(Location_Attr.class)))
                        {
                                location_attrs.add(singleSnapshot.getValue(Location_Attr.class));
                        }

                    }
                    try{
                        updatePostList();
                    }
                    catch (Exception ex)
                    {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(search.this,AllPlaces.class);
//        startActivity(intent);
        finish();
    }

    private void updatePostList() {
        recyclerView.setAdapter(new SearchListAdapter(location_attrs , getApplicationContext() , search.this  ));

    }
}
