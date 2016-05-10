package com.wang.kyle.foodfinder.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.wang.kyle.foodfinder.R;
import com.wang.kyle.foodfinder.module.MyItem;
import com.wang.kyle.foodfinder.module.Place;
import com.wang.kyle.foodfinder.util.AnalyticsApplication;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>{

    public GoogleMap getMap() {
        return mMap;
    }

    private GoogleMap mMap;
    private List<Place> mPlaces;
    private double curLan;
    private double curLon;
    private double radius;
    private ClusterManager<MyItem> mClusterManager;
    private Tracker mTracker;
    private final static String name="Map Activity";
    private static final String TAG = "MapsActivity";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        if (getIntent().getExtras() != null) {
            mPlaces = (List<Place>) getIntent().getExtras().getSerializable("places");
            curLan = getIntent().getDoubleExtra("lan", 0);
            curLon = getIntent().getDoubleExtra("lon", 0);
            radius = getIntent().getDoubleExtra("radius", 0);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 409);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static Intent newIntent(Context packageContext, List<Place> places, double lan, double lon, double radius) {
        Intent intent = new Intent(packageContext, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("places", (ArrayList) places);
        bundle.putDouble("lan", lan);
        bundle.putDouble("lon", lon);
        bundle.putDouble("radius", radius);
        intent.putExtras(bundle);
        return intent;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        final LatLng current = new LatLng(curLan, curLon);
        mMap.addMarker(new MarkerOptions().position(current).title("current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));


        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        mClusterManager.setRenderer(new MyRender(this, mMap,mClusterManager));
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add a marker in Sydney and move the camera
        for (Place place : mPlaces) {
            LatLng placeLL = new LatLng(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng());
            double distance = SphericalUtil.computeDistanceBetween(current, placeLL);
//            mMap.addMarker(new MarkerOptions().position(placeLL).title(place.getName())
//                    .snippet(formatNumber(distance))
//                    .icon(BitmapDescriptorFactory
//                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            MyItem myItem = new MyItem(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng());
            myItem.setTitle(place.getName());
            myItem.setDistance(formatNumber(distance));
            myItem.setPlaceId(place.getPlace_id());

            mClusterManager.addItem(myItem);

        }

        mClusterManager.cluster();


        final LatLngBounds latLngBounds = toBounds(current, radius);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), 1500, null);
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14.0f));


    }


    private String formatNumber(double distance) {
        String unit = "m";
        if (distance < 1) {
            distance *= 1000;
            unit = "mm";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }

        return String.format("%4.2f%s", distance, unit);
    }

    private LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
        List<String> list = new ArrayList<String>();
        list.add(myItem.getPlaceId());
        Intent intent = PlaceDetailActivity.newIntent(this, list, myItem.getPlaceId() );
        startActivity(intent);

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("mapInfoWin:" + myItem.getTitle())
                .build());
    }


    private class MyRender extends DefaultClusterRenderer<MyItem>{

        public MyRender(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);
            markerOptions.title(item.getTitle()).snippet(item.getDistance());
        }



    }



}
