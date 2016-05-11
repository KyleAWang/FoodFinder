package com.wang.kyle.foodfinder.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.wang.kyle.foodfinder.R;
import com.wang.kyle.foodfinder.activity.PlacesListActivity;
import com.wang.kyle.foodfinder.adapter.SuggestionAdapter;
import com.wang.kyle.foodfinder.module.PlaceSearchRequest;
import com.wang.kyle.foodfinder.module.Prediction;
import com.wang.kyle.foodfinder.util.AnalyticsApplication;

/**
 * Created by Kyle on 5/2/2016.
 */
public class HomeFragment extends BaseFragment
        implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "HomeFragment";
    private Context mContext;
    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Tracker mTracker;
    private final static String name = "Home Fragment";

    public void setLastLocation(Location lastLocation) {
        mLastLocation = lastLocation;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnalyticsApplication application = (AnalyticsApplication) mActivity.getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.autoComView);
//        String[] countries = getResources().getStringArray(R.array.countries_array);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, countries);
        autoCompleteTextView.setAdapter(new SuggestionAdapter(mContext, android.R.layout.simple_expandable_list_item_1));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prediction prediction = (Prediction) parent.getItemAtPosition(position);
                autoCompleteTextView.setText(prediction.getDescription());
                PlaceSearchRequest placeSearchRequest = new PlaceSearchRequest();
                if (mLastLocation == null) {
                    mLastLocation = new Location("");
                    mLastLocation.setLatitude(-41.288214);
                    mLastLocation.setLongitude(174.776857);
                }
                placeSearchRequest.setLatitude(mLastLocation.getLatitude());
                placeSearchRequest.setLongitude(mLastLocation.getLongitude());
                placeSearchRequest.setKeyword(prediction.getDescription());
                placeSearchRequest.setType("restaurant");
                Intent i = PlacesListActivity.newIntent(mContext, placeSearchRequest);
                startActivity(i);
            }
        });

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startPlaceList(v.getText().toString());
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Search_keyboard")
                            .build());
                    return true;
                }
                return false;
            }
        });

        buildTextView(view);

        buildGoogleApiClient();

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 408);
        }

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        return view;
    }

    private void buildTextView(View v) {
        TextView view0 = (TextView) v.findViewById(R.id.txt0);
        view0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaceList("restaurant");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("restaurant")
                        .build());
            }
        });
        TextView view1 = (TextView) v.findViewById(R.id.txt1);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaceList("pizza");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("pizza")
                        .build());
            }
        });
        TextView view2 = (TextView) v.findViewById(R.id.txt2);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaceList("cafe");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("cafe")
                        .build());
            }
        });
        TextView view3 = (TextView) v.findViewById(R.id.txt3);
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaceList("chinese food");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("chinese")
                        .build());
            }
        });
        TextView view4 = (TextView) v.findViewById(R.id.txt4);
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaceList("kebabs");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("kebabs")
                        .build());
            }
        });
        TextView view5 = (TextView) v.findViewById(R.id.txt5);
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaceList("steak");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("steak")
                        .build());
            }
        });
        TextView view6 = (TextView) v.findViewById(R.id.txt6);
        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaceList("pasta");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("pasta")
                        .build());
            }
        });
        TextView view7 = (TextView) v.findViewById(R.id.txt7);
        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaceList("chicken");
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("chicken")
                        .build());
            }
        });

    }

    private void startPlaceList(String keyword) {
        PlaceSearchRequest request = new PlaceSearchRequest();
        request.setKeyword(keyword);
        if (mLastLocation == null) {
            mLastLocation = new Location("");
            mLastLocation.setLatitude(-41.288214);
            mLastLocation.setLongitude(174.776857);
        }
        request.setLatitude(mLastLocation.getLatitude());
        request.setLongitude(mLastLocation.getLongitude());
        request.setType("restaurant");
        Intent intent = PlacesListActivity.newIntent(this.mActivity, request);
        startActivity(intent);

    }

    private void getPlaces(Prediction prediction) {
        if (prediction.getId() != null && !prediction.getId().equals("")) {
            //address
        } else {
            //keyword
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            Log.d(TAG, "Latitude: " + String.valueOf(mLastLocation.getLatitude()) + "Longitude: " +
                    String.valueOf(mLastLocation.getLongitude()));
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "location failed");

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Log.d(TAG, location.getLatitude() + "," + location.getLongitude());
    }


}
