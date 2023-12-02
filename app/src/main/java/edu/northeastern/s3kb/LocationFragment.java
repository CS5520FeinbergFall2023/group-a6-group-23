package edu.northeastern.s3kb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.Manifest;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationFragment extends Fragment {

    private ArrayList<ItemCard> itemList = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();
    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    private DatabaseReference databaseReference;
    private Map<String, Map<String, Object>> usersMap = new HashMap<>();
    private List<String> userData = new ArrayList<>();
    private Map<String, Integer> userImage = new HashMap<>();

    private FloatingActionButton getLocation;
    private EditText zipcode;
    private Button search;
    private Pattern pattern;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private Location lastKnownLocation;
    public LocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location, container, false);
        createRecyclerView(view);
        zipcode = view.findViewById(R.id.location_zip_edit);
        search = view.findViewById(R.id.location_search);
        pattern = Pattern.compile("\\b\\d{5}\\b");
        userImage.put("Ashur Restaurant", R.drawable.ashur);
        userImage.put("Chilacates", R.drawable.chilacates);
        userImage.put("Chutney's Cambridge", R.drawable.chutneys);
        userImage.put("Dumpling Daughter", R.drawable.dumpling);
        userImage.put("El Jefe's Taqueria", R.drawable.eljefe);
        userImage.put("Evergreen Cafe", R.drawable.evergreen);
        userImage.put("Fasika Cafe", R.drawable.fasika);
        userImage.put("Five Guys", R.drawable.fiveguys);
        userImage.put("Hojoko", R.drawable.hojoko);
        userImage.put("Lolita Back Bay", R.drawable.lolita);
        userImage.put("Mongu Dominican Bistro", R.drawable.mongu);
        userImage.put("Pho Le", R.drawable.phole);
        userImage.put("Saltie Girl", R.drawable.salte);
        userImage.put("Shabu Zen", R.drawable.shabuzen);
        userImage.put("Tamper Cafe", R.drawable.tamper);
        userImage.put("Tenderoni's Fenway", R.drawable.tenderoni);
        userImage.put("The Blarney Stonee", R.drawable.blarney);
        userImage.put("The Cheesecake Factory", R.drawable.cskf);
        userImage.put("Tikki Masala", R.drawable.tikkimasala);
        userImage.put("Tonino", R.drawable.tonino);
        userImage.put("Tuscan Kitchen Seaport", R.drawable.tusonkitchen);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matcher matcher = pattern.matcher(zipcode.getText().toString());
                if(!matcher.find()){
                    Toast.makeText(requireContext(), zipcode.getText().toString() + " is invalid zipcode!", Toast.LENGTH_SHORT).show();
                } else {
                    userData.clear();
                    for(String data: usersMap.keySet()) {
                        if(usersMap.get(data).getOrDefault("location", "").toString().contains(zipcode.getText().toString())){
                            userData.add(data);
                        }
                    }
                    if(userData.size() < 1) {
                        Toast.makeText(requireContext(), "No Restaurants in this location", Toast.LENGTH_SHORT).show();
                    } else {
                        updateRecycler();
                    }
                }

            }
        });
        getLocation = view.findViewById(R.id.location_locate);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationPermission();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                rviewAdapter.notifyItemChanged(position);
                double lat = (double)usersMap.get(userData.get(position)).get("latitude");
                double lon = (double)usersMap.get(userData.get(position)).get("longitude");
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" +
                        lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() +
                        "&destination=" + lat + "," + lon +
                        "&travelmode=driving");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                Context context = requireActivity();
                PackageManager packageManager = context.getPackageManager();

                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(requireContext(), "Google Maps app not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        initialItemData(savedInstanceState);
        return view;
    }

    private void initialItemData(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (itemList == null || itemList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
                try{

                for (int i = 0; i < size; i++) {
                    Integer imgId = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");
                    ItemCard itemCard = new ItemCard(R.drawable.apr, itemName, itemDesc);

                    Log.v("KAUSHIK", ""+itemName +" ,    " + itemDesc);
                    itemList.add(itemCard);
//                    rviewAdapter.notifyItemInserted(i);
                }
                    Log.v("KAUSHIK", ""+itemList);
                rviewAdapter.notifyDataSetChanged();
                }catch(Exception e){
                    Log.v("KAUSHIK", e.getMessage());
                }
            }
        }

    }
    private void createRecyclerView(View view) {
        rLayoutManger = new LinearLayoutManager(requireContext());
        recyclerView = view.findViewById(R.id.rcycle_nearby_restaurant);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new RviewAdapter(itemList);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    private void updateRecycler() {
        itemList.clear();
        for(String data: userData) {
            itemList.add(new ItemCard(
                    userImage.containsKey(data) ? userImage.get(data) : R.drawable.generic_restaurant,
                    data,
                    usersMap.get(data).getOrDefault("cuisine", "")
                            +String.format("   %s\n",
                                usersMap.get(data).containsKey("distance") ? String.format("-  %.2f mi",  ((float)usersMap.get(data).get("distance") * 0.000621371)):"")
                            +usersMap.get(data).getOrDefault("location", "")));
        }
        rviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Move the database retrieval code here
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("restaurants").get().addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                usersMap = (Map) task.getResult().getValue();
            } else {
                Log.e("KAUSHIK", "Error retrieving data from Firebase: " + task.getException());
            }
        });
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        int size = itemList == null ? 0 : itemList.size();
        outState.putInt(NUMBER_OF_ITEMS, size);
        for (int i = 0; i < size; i++) {
            outState.putInt(KEY_OF_INSTANCE + i + "0", itemList.get(i).getImageSource());
            outState.putString(KEY_OF_INSTANCE + i + "1", itemList.get(i).getItemName());
            outState.putString(KEY_OF_INSTANCE + i + "2", itemList.get(i).getItemDesc());
        }
        super.onSaveInstanceState(outState);
    }



    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getLocation() {
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastKnownLocation != null) {
                    double latitude = lastKnownLocation.getLatitude();
                    double longitude = lastKnownLocation.getLongitude();
                    userData.clear();
                    for(String data: usersMap.keySet()) {
                        Map<String, Object> temp = usersMap.get(data);
                        Location tempLoc = new Location("provider");
                        tempLoc.setLatitude((double)temp.getOrDefault("latitude", 0.0d));
                        tempLoc.setLongitude((double)temp.getOrDefault("longitude", 0.0d));
                        usersMap.get(data).put("distance", lastKnownLocation.distanceTo(tempLoc));
                        if(tempLoc.getLatitude() != 0 && tempLoc.getLongitude() != 0 && lastKnownLocation.distanceTo(tempLoc) < 3218.69) {
                            userData.add(data);
                        }
                    }
                    userData.sort((a, b) -> (int) ((float) usersMap.get(a).get("distance") - (float) usersMap.get(b).get("distance")));
                    updateRecycler();
                } else {
                    Toast.makeText(requireContext(), "Last known location not available", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}