package com.example.test1;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.test1.upload_data.LOCATION;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button btnFind;
    private final  float DEFAULT_ZOOM =18;
    private RippleBackground rippleBackground ;
    public String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        rippleBackground = findViewById(R.id.ripple_bg);
        materialSearchBar = findViewById(R.id.searchBar);
        btnFind =findViewById(R.id.btnfindlocation );
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
        Places.initialize(MapActivity.this,"AIzaSyCl5MtEv3gAtzz-FL5YcmejOZUIEQHVhwA");
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) { }
            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(),true,null,true); }
            @Override
            public void onButtonClicked(int buttonCode) {
                if(buttonCode == materialSearchBar.BUTTON_NAVIGATION){ }
                else if(buttonCode == materialSearchBar.BUTTON_BACK){
                    materialSearchBar.disableSearch(); } }});
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setCountry("IN")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if(task.isSuccessful()){
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if(predictionsResponse !=null){
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for(int i=0;i<predictionList.size();i++){
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString()); }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                if(materialSearchBar.isSuggestionsVisible()){
                                    materialSearchBar.showSuggestionsList(); }
                            }
                        }
                        else{ Log.i("mytag","prediction fetching task unsuccessful"); }
                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if(position >= predictionList.size()){ return; }
                AutocompletePrediction selectPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { materialSearchBar.clearSuggestions();
                    }
                }, 1000);
                materialSearchBar.clearSuggestions();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm != null){ imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY); }
                String placeId = selectPrediction.getPlaceId();
                List<Place.Field> placeFiels = Arrays.asList(Place.Field.LAT_LNG);
                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId,placeFiels).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();
                        Log.i("mytag","Place found"+place.getName());
                        LatLng latLngOfPlace =place.getLatLng();
                        if (latLngOfPlace != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace,DEFAULT_ZOOM)); }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof ApiException){
                            ApiException apiException = (ApiException) e;
                            apiException.printStackTrace();
                            int statusCode =  apiException.getStatusCode();
                            Log.i("mytag","place not found"+e.getMessage());
                            Log.i("mytsg","status code"+statusCode); }
                    }
                });
            }
            @Override
            public void OnItemDeleteListener(int position, View v) { }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentMarkerLocation = mMap.getCameraPosition().target;
                rippleBackground.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBackground.stopRippleAnimation();
                        LOCATION=address;
                        onBackPressed();
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if(mapView!=null && mapView.findViewById(Integer.parseInt("1"))!=null){
            View locationButton = ((View)mapView.findViewById(Integer.parseInt("1")).getParent())
                    .findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
            layoutParams.setMargins(0,0,40,180);
        }
        //check if gps is enabled
        LocationRequest locationRequest =  LocationRequest.create();
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addAllLocationRequests(Collections.singleton(locationRequest));
        SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(MapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });
        task.addOnFailureListener(MapActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                    try { resolvableApiException.startResolutionForResult(MapActivity.this,51); }
                    catch (IntentSender.SendIntentException ex) { ex.printStackTrace(); } }
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if(materialSearchBar.isSuggestionsVisible()){ materialSearchBar.clearSuggestions(); }
                if(materialSearchBar.isSearchEnabled()){ materialSearchBar.disableSearch(); }
                return false;
            }
        });
    }
    private String getCityName(LatLng coordinates){
        String myCity = "";
        Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(coordinates.latitude,coordinates.longitude,1);
            address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            Log.d("mylog","Complete address"+addresses.toString());
            Log.d("mylog","Complete address"+address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 51){
            if(resultCode == RESULT_OK){
                getDeviceLocation();
            }
        }
    }



    private void getDeviceLocation(){
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()){
                            mLastKnownLocation = task.getResult();
                            if(mLastKnownLocation != null){
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude()
                                                ,mLastKnownLocation.getLongitude()),DEFAULT_ZOOM));
                                LatLng coordinates = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                                String address= getCityName(coordinates);
                                Toast.makeText(MapActivity.this,address,Toast.LENGTH_LONG).show(); }
                            else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);;
                                locationCallback = new LocationCallback(){
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if(locationResult == null){ return; }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude()
                                                ,mLastKnownLocation.getLongitude()),DEFAULT_ZOOM));
                                        LatLng coordinates = new LatLng(mLastKnownLocation.getLatitude()
                                                ,mLastKnownLocation.getLongitude());
                                        String cityName = getCityName(coordinates);
                                        Toast.makeText(MapActivity.this,cityName,Toast.LENGTH_LONG).show();
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
                            }
                        }
                        else {
                            Toast.makeText(MapActivity.this,"unable to get last location",Toast.LENGTH_SHORT);
                        }
                    }
                });
    }
}
