package com.wuttipong.project.mapapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wuttipong.project.mapapplication.api.ApiUrl;
import com.wuttipong.project.mapapplication.model.Listname;

import java.util.List;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Location mLocaiton;
    private List<Listname> hospitalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        showProgress();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkCurrentLocation();

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        loadData();
    }

    private void checkCurrentLocation() {

        /*mMap.setMyLocationEnabled(true);
        mLocaiton = mMap.getMyLocation();
        mMap.seton
        if (mLocaiton!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLocaiton.getLatitude(),mLocaiton.getLongitude())));
        }*/

    }

    private void loadData() {
        Ion.with(getApplicationContext())
                .load(ApiUrl.auto_map())
                .as(new TypeToken<List<Listname>>(){})
                .setCallback(new FutureCallback<List<Listname>>() {
                    @Override
                    public void onCompleted(Exception e, List<Listname> result) {
                        if (e!=null){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "การเชื่อมต่อมีปัญหา", Toast.LENGTH_SHORT).show();
                            hideProgress();
                        }else {
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
            if (locations.length == 2 ) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(locations[0]),Double.parseDouble(locations[1])))
                        .title(listname.getHospitalName())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.nearby_marker))
//                        .anchor(0.5f, 0.5f)
                ).setTag(listname);
            }
        }
//        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
                intent.putExtra("id", (int) marker.getTag());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
        intent.putExtra("id", (int) marker.getTag());
        startActivity(intent);
        return false;
    }

    /*class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        View view;

        CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.map_infowindow, null, false);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
            marker.setInfoWindowAnchor(0.1f, 0f);
            PlaceListViewModel placeListViewModel = new PlaceListViewModel((PlaceListItem) marker.getTag());

//            binding.setViewModel(new PlaceListViewModel(placeListItemList.get(Integer.parseInt(marker.getId().toString().substring(1)))));
            infoBinding.setViewModel(placeListViewModel);
            infoBinding.executePendingBindings();
            if (!isShowImage) {
                Glide.with(getActivity())
                        .load(placeListViewModel.image.get())
                        .override(200, 200)
//                        .bitmapTransform(new CircleTransform(Contextor.getInstance().getContext()))
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                infoBinding.img.setImageDrawable(resource);
                                isShowImage = true;
                                marker.showInfoWindow();
                            }
                        });
            }
            if (!isShowCategoryImage) {
                Glide.with(getActivity())
                        .load(placeListViewModel.category.image.get())
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                infoBinding.imgCategory.setImageDrawable(resource);
                                isShowCategoryImage = true;
                                marker.showInfoWindow();
                            }
                        });
            }
//            marker.showInfoWindow();
            return infoBinding.getRoot();
        }

        @Override
        public View getInfoWindow(final Marker marker) {

                return null;

        }


    }*/

}