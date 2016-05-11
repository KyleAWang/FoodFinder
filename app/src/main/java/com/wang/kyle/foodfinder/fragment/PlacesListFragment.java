package com.wang.kyle.foodfinder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.wang.kyle.foodfinder.R;
import com.wang.kyle.foodfinder.activity.PlaceDetailActivity;
import com.wang.kyle.foodfinder.activity.PlacesListActivity;
import com.wang.kyle.foodfinder.module.Place;
import com.wang.kyle.foodfinder.module.PlaceSearchRequest;
import com.wang.kyle.foodfinder.module.PlaceSearchResponse;
import com.wang.kyle.foodfinder.receiver.HttpServiceReceiver;
import com.wang.kyle.foodfinder.service.HttpIntentService;
import com.wang.kyle.foodfinder.util.AnalyticsApplication;
import com.wang.kyle.foodfinder.webservice.ServiceConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 5/4/2016.
 */
public class PlacesListFragment extends BaseFragment implements HttpServiceReceiver.Listener {
    private final static String TAG = "PlacesListFragment";
    private PlaceSearchRequest mPlaceSearchRequest;
    private List<Place> mPlaces = null;
    private RecyclerView mRecyclerView;
    private List<String> placeIds;
    private OnPlacesListener mOnPlacesListener;
    private Tracker mTracker;
    private final static String name="PlacesListFragment";




    public PlacesListFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) mActivity.getApplication();
        mTracker = application.getDefaultTracker();
        setRetainInstance(true);
    }

    public List<Place> getPlaces() {
        return mPlaces;
    }

    public interface OnPlacesListener {
        void setPlaces(List<Place> PLACES, double lan, double lon);
    }

    public void setOnPlacesListener(OnPlacesListener onPlacesListener) {
        mOnPlacesListener = onPlacesListener;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == HttpIntentService.RESULTCODE_SUCCESS) {
            PlaceSearchResponse response = (PlaceSearchResponse) resultData.getSerializable("response");
            mPlaces = response.getResults();
            TestAdapter testAdapter = new TestAdapter(mPlaces);
            mRecyclerView.setAdapter(testAdapter);
            placeIds = new ArrayList<String>();
            for (Place place : mPlaces) {
                placeIds.add(place.getPlace_id());
            }
            ((PlacesListActivity) mActivity).setPlaces(mPlaces);
//            mOnPlacesListener.setPlaces(mPlaces, mPlaceSearchRequest.getLatitude(), mPlaceSearchRequest.getLongitude());

//            Log.d(TAG, PlaceSearchResponse.getUserName());
        } else {
            PlaceSearchResponse response = (PlaceSearchResponse) resultData.getSerializable("response");
            Log.d(TAG, "failed");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_list, container, false);
        Intent intent = mActivity.getIntent();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.pListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if (mPlaceSearchRequest == null && intent != null) {
            Bundle bundle = intent.getExtras();
            mPlaceSearchRequest = (PlaceSearchRequest) bundle.getSerializable("request");
        }
        if (mPlaceSearchRequest != null) {
            doSearchPlaces();
        }
        return view;
    }

    private void doSearchPlaces() {

        Intent intent = new Intent(this.getContext(), HttpIntentService.class);
        HttpServiceReceiver serviceReceiver = new HttpServiceReceiver(new Handler());
        serviceReceiver.setListener(this);
        intent.setAction(HttpIntentService.ACTION_SEARCH_PLACES);
        intent.putExtra("request", mPlaceSearchRequest);
        intent.putExtra("receiver", serviceReceiver);
        this.mActivity.startService(intent);

    }


    private class TestAdapter extends RecyclerView.Adapter<MyHolder> implements OnMapReadyCallback {
        private List<Place> mTestEntities;
        private Place testEntity;

        public TestAdapter(List<Place> testEntities) {
            mTestEntities = testEntities;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            View view = layoutInflater.inflate(R.layout.row_item_place, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            testEntity = null;
            testEntity = mTestEntities.get(position);
            holder.mName.setText(testEntity.getName());
            holder.mRatingBar.setRating(testEntity.getRating());
            holder.bindPlace(testEntity);
            LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            if (testEntity.getPhotos() != null) {
                View imgLView = layoutInflater.inflate(R.layout.image_view, null);
                ImageView imgView = (ImageView) imgLView.findViewById(R.id.imgId);
                ViewGroup vg = (ViewGroup) imgView.getParent();
                if (vg != null) {
                    vg.removeView(imgView);
                }
                holder.mFrameLayout.addView(imgLView);
                StringBuffer sb = new StringBuffer(ServiceConstants.SERVICE_GOOGLE_IMG);
                sb.append(testEntity.getPhotos()[0].getPhoto_reference());
                Picasso.with(getContext())
                        .load(sb.toString())
                        .resize(120, 120)
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation)
                        .into(imgView);
//                new DownloadImageTask(holder.mImageView)
//                        .execute(sb.toString());
            } else {
                View view = layoutInflater.inflate(R.layout.map_view, null);
                MapView mapView = (MapView) view.findViewById(R.id.mapView);
                mapView.onCreate(null);
                mapView.getMapAsync(this);
                mapView.setClickable(false);
                ViewGroup vg = (ViewGroup) mapView.getParent();
                if (vg != null) {
                    vg.removeView(mapView);
                }
                holder.mFrameLayout.addView(mapView);

            }

        }

        @Override
        public int getItemCount() {
            return mTestEntities.size();
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

            LatLng location = new LatLng(testEntity.getGeometry().getLocation().getLat(), testEntity.getGeometry().getLocation().getLng());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            googleMap.addMarker(new MarkerOptions().position(location));
        }
    }


    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mName;
        private RatingBar mRatingBar;
        private Place mPlace;
        private FrameLayout mFrameLayout;
//        private TextView mAddress;

        public MyHolder(View itemView) {
            super(itemView);
            mName = (TextView)itemView.findViewById(R.id.place_name);
            mRatingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            mFrameLayout = (FrameLayout) itemView.findViewById(R.id.pListPic);
            itemView.setOnClickListener(this);

//            mAddress = (TextView)itemView.findViewById(R.id.place_address);
        }

        public void bindPlace(Place event) {
            mPlace = event;
        }

        @Override
        public void onClick(View v) {
            Intent intent = PlaceDetailActivity.newIntent(getContext(), placeIds, mPlace.getPlace_id());
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("list:"+mPlace.getName())
                    .build());
            startActivity(intent);

        }
    }

}
