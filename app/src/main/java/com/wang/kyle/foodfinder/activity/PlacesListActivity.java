package com.wang.kyle.foodfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wang.kyle.foodfinder.R;
import com.wang.kyle.foodfinder.fragment.PlacesListFragment;
import com.wang.kyle.foodfinder.module.Place;
import com.wang.kyle.foodfinder.module.PlaceSearchRequest;
import com.wang.kyle.foodfinder.util.AnalyticsApplication;

import java.util.List;

public class PlacesListActivity extends AppCompatActivity {
    private PlaceSearchRequest mPlaceSearchRequest;
    private List<Place> mPlaces;
    private Tracker mTracker;
    private final static String name="PlacesListActivity";
    private static final String TAG = "PlacesListActivity";

    public static Intent newIntent(Context packageContext, PlaceSearchRequest request) {
        Intent intent = new Intent(packageContext, PlacesListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("request", request);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        setContentView(R.layout.activity_places_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.placelist_container);
        if (fragment == null) {
            fragment = new PlacesListFragment();
//            ((PlacesListFragment)fragment).setOnPlacesListener(this);
            fm.beginTransaction().add(R.id.placelist_container, fragment).commit();
        }

        if (getIntent().getExtras() != null) {
            mPlaceSearchRequest = (PlaceSearchRequest) getIntent().getExtras().getSerializable("request");
        }

    }

    public void setPlaces(List<Place> places) {
        mPlaces = places;
        startMapActivity(mPlaceSearchRequest.getLatitude(), mPlaceSearchRequest.getLongitude(), mPlaceSearchRequest.getRadius());
    }

//    @Override
//    public void setPlaces(List<Place> PLACES, double lan, double lon) {
//        mPlaces = PLACES;
//        startMapActivity(lan, lon, mPlaceSearchRequest.getRadius());
//    }

    private void startMapActivity(double lan, double lon, double radius){
        ImageView mapIView = (ImageView) findViewById(R.id.map_icon);
        final Intent mapIntent = MapsActivity.newIntent(this, mPlaces, lan, lon, radius);
        mapIView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("list start map")
                        .build());
                startActivity(mapIntent);
            }
        });

    }
}
