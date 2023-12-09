package edu.northeastern.s3kb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class PropertySeekerActivity extends AppCompatActivity {
    private RecyclerView propertyRecyclerView;
    ArrayList<Property> propertiesList;
    LinearLayoutManager horizontalLayout;
    private ImageView  emptyView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference properties;
    Bundle bundle = null;
    String userKey;

    Set<String> myFavoritePropertiesList;
    Preference curUserPref;

    int propPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_seeker);

        propertiesList = new ArrayList<>();

        propertyRecyclerView = findViewById(R.id.property_list_recycler_view);

        emptyView = (ImageView) findViewById(R.id.empty_view);

        horizontalLayout = new LinearLayoutManager(
                PropertySeekerActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false);
        propertyRecyclerView.setLayoutManager(horizontalLayout);
        PropertyListAdapterSeeker propertyListAdapterSeeker =
                new PropertyListAdapterSeeker(this, propertiesList);
        propertyRecyclerView.setAdapter(propertyListAdapterSeeker);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL);


        propertyRecyclerView.addItemDecoration(dividerItemDecoration);

        bundle = getIntent().getExtras();
        if(bundle != null) {
            userKey = bundle.getString("userKey");
        }
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("");

        myFavoritePropertiesList = new HashSet<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("seekers").child(userKey).child("favorites").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    myFavoritePropertiesList.add(next.child("propID").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("KAUSHIK","The read failed: " + error.getCode());
                System.out.println("The read failed: " + error.getCode());
            }
        });

        databaseReference.child("seekers").child(userKey).child("myPreference").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curUserPref = snapshot.getValue(Preference.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("KAUSHIK","The read failed: " + error.getCode());
                System.out.println("The read failed: " + error.getCode());
            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("houses").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {

                    Iterable<DataSnapshot> snapshotIterator2 = iterator.next().getChildren();
                    Iterator<DataSnapshot> iterator2 = snapshotIterator2.iterator();

                    while (iterator2.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator2.next();

                        Property article = new Property(String.valueOf(next.child("houseId").getValue()),
                                String.valueOf(next.child("noOfRoom").getValue()),
                                String.valueOf(next.child("rentPerRoom").getValue()),
                                String.valueOf(next.child("houseDescription").getValue()),
                                String.valueOf(next.child("houseLocation").getValue()),
                                String.valueOf(next.child("houseImage").getValue()),
                                String.valueOf(next.child("userId").getValue()),
                                String.valueOf(next.child("country").getValue()),
                                String.valueOf(next.child("state").getValue()),
                                String.valueOf(next.child("type").getValue()),String.valueOf(next.child("baths").getValue()),
                                String.valueOf(next.child("address").getValue()));

                        Log.v("KAUSHIK", article.getAddress());

                        if(!myFavoritePropertiesList.contains(article.getHouseId())){

                            if(curUserPref == null){
                                propertiesList.add(article);

                                if (propertyRecyclerView != null && propertyRecyclerView.getAdapter() != null)
                                    propertyRecyclerView.getAdapter().notifyItemInserted(propertyRecyclerView.getAdapter().getItemCount());

                            } else {

                                if (curUserPref.getLocations() == null || (curUserPref.getLocations().contains(article.getHouseLocation()))) {

                                    if (curUserPref.getMinimumPrice() <= Integer.parseInt(article.getRentPerRoom())
                                            && curUserPref.getMaximumPrice() >= Integer.parseInt(article.getRentPerRoom())) {

                                        if (article.getType() == null || curUserPref.getTypeOfHouse() == null
                                                || (curUserPref.getTypeOfHouse().contains(article.getType()))) {

                                            boolean addProperty = false;
                                            //number of bedrooms
                                            if ("1".equalsIgnoreCase(curUserPref.getNumberOfBedrooms())) {
                                                if (article.getNoOfRoom().equalsIgnoreCase("1")) {
                                                    addProperty = true;
                                                }

                                            } else if ("2 - 3".equalsIgnoreCase(curUserPref.getNumberOfBedrooms())) {
                                                if (article.getNoOfRoom().equalsIgnoreCase("2")
                                                        || article.getNoOfRoom().equalsIgnoreCase("3")) {
                                                    addProperty = true;
                                                }

                                            } else if ("> 4".equalsIgnoreCase(curUserPref.getNumberOfBedrooms())) {
                                                if (Integer.parseInt(article.getNoOfRoom()) >= 4) {
                                                    addProperty = true;
                                                }
                                            } else {
                                                addProperty = true;
                                            }

                                            if (addProperty) {
                                                propertiesList.add(article);

                                                if (propertyRecyclerView != null && propertyRecyclerView.getAdapter() != null)
                                                    propertyRecyclerView.getAdapter().notifyItemInserted(propertyRecyclerView.getAdapter().getItemCount());
                                            }

                                        }

                                    }
                                }

                            }

                        }

                    }
                }
                if (propertiesList.isEmpty()) {
                    propertyRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    propertyRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("KAUSHIK","The read failed: " + databaseError.getCode());
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.DOWN){
                    Toast.makeText(PropertySeekerActivity.this, "Added to favorites list",
                            Toast.LENGTH_SHORT).show();

                    String propertyID = propertiesList.get(viewHolder.getBindingAdapterPosition()).getHouseId();
                    String ownerID = propertiesList.get(viewHolder.getBindingAdapterPosition()).getUserId();

                    HashMap<String, String> favMap = new HashMap<>();
                    favMap.put("propID",propertyID);
                    favMap.put("owner", ownerID);


                    DatabaseReference db =databaseReference.child("seekers").child(userKey).child("favorites").push();

                    db.setValue(favMap).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PropertySeekerActivity.this, "Could not add to favorites", Toast.LENGTH_SHORT).show();
                        }
                    });
                    databaseReference.child("seekers").child(userKey).child("propPoints").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            propPoints = snapshot.getValue(Integer.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    propPoints++;
                    databaseReference.child("seekers").child(userKey).child("propPoints").setValue(propPoints);

                }
                else if (direction == ItemTouchHelper.UP){
                    Toast.makeText(PropertySeekerActivity.this, "Property removed.",
                            Toast.LENGTH_SHORT).show();
                }
                //remove from list
                propertiesList.remove(viewHolder.getBindingAdapterPosition());
                if(propertyRecyclerView != null && propertyRecyclerView.getAdapter() != null) {
                    propertyRecyclerView.getAdapter().notifyItemRemoved(propertyRecyclerView.getAdapter().getItemCount());
                    propertyRecyclerView.getAdapter().notifyItemRangeChanged(viewHolder.getBindingAdapterPosition(), propertyRecyclerView.getAdapter().getItemCount());
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(propertyRecyclerView);

        properties = databaseReference.child("houses");
        properties.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try{
                if(item.getItemId() == R.id.page_home) {
                    return true;
                }

                if(item.getItemId() == R.id.page_favorites) {
                    Intent clickIntent = new Intent(PropertySeekerActivity.this, SeekerFavoriteActivity.class);
                    clickIntent.putExtra("userKey", userKey);
                    startActivity(clickIntent);
                    overridePendingTransition(0,0);
                    return true;
                }

                if(item.getItemId() == R.id.page_profile) {
                    Intent clickIntent4 = new Intent(PropertySeekerActivity.this, SeekerProfileActivity.class);
                    clickIntent4.putExtra("userKey", userKey);
                    startActivity(clickIntent4);
                    return true;
                }
            }catch(Exception ex) {
                Log.v("KAUSHIK", ex.getMessage() );
                }
                return false;
            }
        });
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Swipe down on property to add to favorites, swipe up to remove", Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(PropertySeekerActivity.this, ProjectStarterPageActivity.class));
        finish();
    }

}