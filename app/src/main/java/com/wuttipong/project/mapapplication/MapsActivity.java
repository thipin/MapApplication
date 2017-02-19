package com.wuttipong.project.mapapplication;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wuttipong.project.mapapplication.api.ApiUrl;
import com.wuttipong.project.mapapplication.model.Listname;

import java.util.List;

public class MapsActivity extends BaseActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private boolean isUpdateLocation;
    private List<Listname> hospitalList;
    private boolean isShowImage;
    private LatLng mLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        showProgress();
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(18.787943,98.985785)));
//        loadData();
    }


    private void loadData() {
        Ion.with(getApplicationContext())
                .load(ApiUrl.auto_map())
                .as(new TypeToken<List<Listname>>() {
                })
                .setCallback(new FutureCallback<List<Listname>>() {
                    @Override
                    public void onCompleted(Exception e, List<Listname> result) {
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "การเชื่อมต่อมีปัญหา", Toast.LENGTH_SHORT).show();
                            hideProgress();
                        } else {
                            hospitalList = result;
                            setMarker();
                            hideProgress();
                        }
                    }
                });
    }

    private void setMarker() {
        for (Listname listname : hospitalList) {
            String[] locations = listname.getHospitalLocaltion().split(",");
            if (locations.length == 2) {
                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(locations[0]), Double.parseDouble(locations[1])))
                                .title(listname.getHospitalName())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.nearby_marker))
//                        .anchor(0.5f, 0.5f)
                ).setTag(listname);
            }
        }
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
                intent.putExtra("hospitalID", ((Listname) marker.getTag()).getHospitalId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        /*Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
        intent.putExtra("hospitalID", ((Listname) marker.getTag()).getHospitalId());
        startActivity(intent);*/
        isShowImage = false;
        return false;
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        View view;

        CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.map_infowindow, null, false);
        }

        @Override
        public View getInfoContents(final Marker marker) {

            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
//            marker.setInfoWindowAnchor(0.1f, 0f);
            Listname listname = (Listname) marker.getTag();
            final ImageView img = (ImageView) view.findViewById(R.id.img);
            TextView name = (TextView) view.findViewById(R.id.txt_name);
            name.setText(listname.getHospitalName());
            if (!isShowImage) {
                Glide.with(getApplicationContext())
                        .load(listname.getHospitalImg())
                        .override(200, 200)
//                        .bitmapTransform(new CircleTransform(Contextor.getInstance().getContext()))
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                img.setImageDrawable(resource);
                                isShowImage = true;
                                marker.showInfoWindow();
                            }
                        });
            }
//            marker.showInfoWindow();
            return view;
        }

        @Override
        public View getInfoWindow(final Marker marker) {

            return null;

        }
    }

    /**
     * Location *******************************************************************************
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
        locationRequest();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");

        mLastLocation = location;
        mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//            if (!isUpdateLocation) {
        loadData();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
//            }

//            isUpdateLocation = true;
        stopLocationUpdates();
    }

    public void locationRequest() {
        /** get last location */
        Log.d(TAG, "locationRequest: ");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (locationAvailability.isLocationAvailable()) {

            createLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);   //        startLocationUpdates();
        } else {
            // TODO: 21/9/2559 location unavailable
        }

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        Log.d(TAG, "onResult LocationSettingsStatusCodes.SUCCESS");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        Log.d(TAG, "onResult LocationSettingsStatusCodes.RESOLUTION_REQUIRED");
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

}