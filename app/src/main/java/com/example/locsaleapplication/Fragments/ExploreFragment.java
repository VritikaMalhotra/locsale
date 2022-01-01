package com.example.locsaleapplication.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.Adapter.PhotoAdapter;
import com.example.locsaleapplication.EditProfileActivity;
import com.example.locsaleapplication.MainActivity;
import com.example.locsaleapplication.Model.Post;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.utils.AppGlobal;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("All")
public class ExploreFragment extends Fragment {
    private RecyclerView recyclerViewExplore;
    private PhotoAdapter photoAdapter;
    private List<Post> myPhotoList;
    private Location mCurrentLocation;
    private AppCompatTextView tvExploreLocation;

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), getResources().getString(R.string.places_api_key));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        //Back pressed Logic for fragment
        /*view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        *//*Fragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, fragment);
                        fragmentTransaction.commit();*//*
                        getActivity().finish();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });*/


        tvExploreLocation = view.findViewById(R.id.tvExploreLocation);
        tvExploreLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLocation();
            }
        });

        recyclerViewExplore = view.findViewById(R.id.recycler_view_pictures_search);
        //recyclerViewExplore.setHasFixedSize(true);
        recyclerViewExplore.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myPhotoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(), myPhotoList);
        recyclerViewExplore.setAdapter(photoAdapter);

        //myPhotos();
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                tvExploreLocation.setText(place.getName());

                mSelectedplace = place;
                Location mLocation = new Location("location");
                mLocation.setLatitude(mSelectedplace.getLatLng().latitude);
                mLocation.setLongitude(mSelectedplace.getLatLng().longitude);
                mCurrentLocation = mLocation;

                if (mCurrentLocation != null) {
                    myPhotos();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
    }


    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private Place mSelectedplace = null;
    private void selectLocation() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setBottomNavigationItem(R.id.nav_explore);

        if (mCurrentLocation == null && checkPermission()) {
            mCurrentLocation = getLocation();

            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (mCurrentLocation == null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }

            if (mCurrentLocation != null) {
                myPhotos();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppGlobal.permission_location && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onResume();
        }
    }

    //this method will check there is a storage permission given or not
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            try {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, AppGlobal.permission_location);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    private void myPhotos() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPhotoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getType() != null && post.getType().equals("1")) {
                        if (AppGlobal.checkStringValue(post.getStAudienceName()) && mCurrentLocation != null) {
                            //Post Location
                            Location mLocationPost = new Location("postLocation");
                            mLocationPost.setLatitude(Double.parseDouble(post.getStLocationLat()));
                            mLocationPost.setLongitude(Double.parseDouble(post.getStLocationLng()));

                            if ((mLocationPost.distanceTo(mCurrentLocation) / 1000) <= 10.0) {
                                myPhotoList.add(post);
                            }
                        }
                    }
                }
                Collections.sort(myPhotoList, new Comparator<Post>() {
                    @Override
                    public int compare(Post post, Post t1) {
                        return (post.getTimestamp() <= t1.getTimestamp() ? 1 : -1);
                    }
                });
                if (myPhotoList.isEmpty()) {
                    recyclerViewExplore.setVisibility(View.GONE);
                } else {
                    recyclerViewExplore.setVisibility(View.VISIBLE);
                    photoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    boolean canGetLocation = false;
    Location location = null;

    public Location getLocation() {
        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                canGetLocation = false;

            } else {

                canGetLocation = true;

                if (isGPSEnabled) {

                    Log.d("TAG" + "-->GPS", "GPS Enabled");

                    if (locationManager != null) {

                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                010,
                                0100, new LocationListener() {
                                    @Override
                                    public void onLocationChanged(@NonNull Location locationMain) {
                                        location = locationMain;
                                    }
                                });

                        return location;
                    }
                } else if (isNetworkEnabled) {

                    Log.d("TAG" + "-->Network", "Network Enabled");

                    if (locationManager != null) {

                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                010,
                                0100, new LocationListener() {
                                    @Override
                                    public void onLocationChanged(@NonNull Location locationMain) {
                                        location = locationMain;
                                    }
                                });
                        return location;
                    }
                }
            }

        } catch (SecurityException e) {
            Toast.makeText(getActivity(), "Exception " + e, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Exception " + e, Toast.LENGTH_LONG).show();
        }
        return location;
    }

}