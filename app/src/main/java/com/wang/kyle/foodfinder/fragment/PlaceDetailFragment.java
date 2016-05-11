package com.wang.kyle.foodfinder.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Target;
import com.wang.kyle.foodfinder.R;
import com.wang.kyle.foodfinder.adapter.ReviewAdapter;
import com.wang.kyle.foodfinder.module.PlaceDetail;
import com.wang.kyle.foodfinder.module.PlaceDetailRequest;
import com.wang.kyle.foodfinder.module.PlaceDetailResponse;
import com.wang.kyle.foodfinder.module.Review;
import com.wang.kyle.foodfinder.receiver.HttpServiceReceiver;
import com.wang.kyle.foodfinder.service.HttpIntentService;
import com.wang.kyle.foodfinder.util.AnalyticsApplication;

import java.util.List;

/**
 * Created by Kyle on 5/5/2016.
 */
public class PlaceDetailFragment extends BaseFragment implements HttpServiceReceiver.Listener, OnMapReadyCallback {
    private final static String TAG = "PlaceDetailFragment";
    private PlaceDetail mPlaceDetail;
    private View mView;
    private String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&maxheight=300&key=AIzaSyCVfdWb5Df8k5lczfN-VvFfS7iUgw8qH14&photoreference=";
    private String mPlaceId;
    private List<Review> mReviews;
    private RecyclerView mRecyclerView;
    private LruCache<String, Bitmap> mMemoryCache;
    private Tracker mTracker;
    private final static String name="PlaceDetailFragment";

    public LruCache<String, Bitmap> getMemoryCache() {
        return mMemoryCache;
    }

    public void setMemoryCache(LruCache<String, Bitmap> memoryCache) {
        mMemoryCache = memoryCache;
    }

    public static PlaceDetailFragment findOrCreateRetainFragment(FragmentManager fm, String fragment_tag) {
        PlaceDetailFragment fragment = (PlaceDetailFragment) fm.findFragmentByTag(fragment_tag);
        if (fragment == null) {
            fragment = new PlaceDetailFragment();
            // fm.beginTransaction().add(fragment, fragment_tag).commit();
        }
        return fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_place_detail, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mPlaceId = (String) bundle.get("placeId");
        }

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.detail_listview);
        mRecyclerView.setFocusable(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        doSearchDetail();
        return mView;
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == HttpIntentService.RESULTCODE_SUCCESS) {
            PlaceDetailResponse response = (PlaceDetailResponse) resultData.getSerializable("response");
            mPlaceDetail = response.getResult();
            Log.d(TAG, response.getStatus());
            buildLayout();
            buildView();
            buildThumbnail(mView);
            mReviews = mPlaceDetail.getReviews();
            if (mReviews != null && mReviews.size() > 0) {

                ReviewAdapter ReviewAdapter = new ReviewAdapter(mReviews);
                mRecyclerView.setAdapter(ReviewAdapter);
            }


//            Log.d(TAG, PlaceSearchResponse.getUserName());
        } else {
            PlaceDetailResponse response = (PlaceDetailResponse) resultData.getSerializable("response");
            Log.d(TAG, "failed");
        }
    }

    private void buildThumbnail(View view) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.thumbnailLayout);
        LayoutInflater layoutInflater = LayoutInflater.from(mView.getContext());
        linearLayout.removeAllViews();


        if (mPlaceDetail.getPhotos() != null && mPlaceDetail.getPhotos().length > 0) {

            for (int i = 0; i < mPlaceDetail.getPhotos().length; i++) {
                String refer = (mPlaceDetail.getPhotos()[i]).getPhoto_reference();
                if (refer != null && refer != "") {
                    View thumbLayout = layoutInflater.inflate(R.layout.thumbnail_item, null);
                    ImageView imageView = (ImageView) thumbLayout.findViewById(R.id.thbImg);
                    StringBuffer imgKeyB = new StringBuffer(mPlaceDetail.getId());
                    imgKeyB.append("_");
                    imgKeyB.append(i);

                    loadBitmap(refer, imgKeyB.toString(), imageView);


                    imageView.setOnClickListener(new ImageOnClickListener());

                    linearLayout.addView(imageView);
                }

            }
        }


    }

    private void switchView(Bitmap bitmap) {
        if (bitmap != null){

            ImageView imageView = (ImageView) mView.findViewById(R.id.detail_image);
            imageView.setImageBitmap(bitmap);
        }
    }

    private void buildLayout(){
        if (mPlaceDetail != null) {
            TextView nameText = (TextView) mView.findViewById(R.id.detail_name);
            nameText.setText(mPlaceDetail.getName());
            TextView addressText = (TextView) mView.findViewById(R.id.detail_address);

            addressText.setText(mPlaceDetail.getFormatted_address());
        }

    }

    private void buildView() {
        if (mPlaceDetail != null) {

            FrameLayout frameLayout = (FrameLayout) mView.findViewById(R.id.detail_frame_image);
            LayoutInflater layoutInflater = LayoutInflater.from(mView.getContext());
            if (mPlaceDetail.getPhotos() != null && mPlaceDetail.getPhotos().length > 0) {
                View imgLView = layoutInflater.inflate(R.layout.image_detail_view, null);
                ImageView imgView = (ImageView) imgLView.findViewById(R.id.detail_image);
                ViewGroup vg = (ViewGroup) imgView.getParent();
                if (vg != null) {
                    vg.removeView(imgView);
                }
                frameLayout.addView(imgLView);

                StringBuffer sb_photo = new StringBuffer(imgUrl);
                StringBuffer imgKeyB = new StringBuffer(mPlaceDetail.getId());
                imgKeyB.append("_");
                imgKeyB.append(0);
                loadBitmap(mPlaceDetail.getPhotos()[0].getPhoto_reference(), imgKeyB.toString(), imgView);
            }else {
                if(getBitmapFromMemCache(mPlaceDetail.getId()) != null){
                    View imgLView = layoutInflater.inflate(R.layout.image_detail_view, null);
                    ImageView imgView = (ImageView) imgLView.findViewById(R.id.detail_image);
                    ViewGroup vg = (ViewGroup) imgView.getParent();
                    if (vg != null) {
                        vg.removeView(imgView);
                    }
                    imgView.setImageBitmap(getBitmapFromMemCache(mPlaceDetail.getId()));
                    frameLayout.addView(imgLView);

                }else {
                    View view = layoutInflater.inflate(R.layout.image_detail_mapview, null);
                    MapView mapView = (MapView) view.findViewById(R.id.mapDetailView);
                    mapView.onCreate(null);
                    mapView.getMapAsync(this);
                    mapView.setClickable(false);
                    ViewGroup vg = (ViewGroup) mapView.getParent();
                    if (vg != null) {
                        vg.removeView(mapView);
                    }
                    frameLayout.addView(mapView);
                }

            }
        }
    }

    private void doSearchDetail() {
        PlaceDetailRequest request = new PlaceDetailRequest();
        request.setPlaceid(mPlaceId);
        Intent intent = new Intent(this.getContext(), HttpIntentService.class);
        HttpServiceReceiver serviceReceiver = new HttpServiceReceiver(new Handler());
        serviceReceiver.setListener(this);
        intent.setAction(HttpIntentService.ACTION_SEARCH_DETAIL);
        intent.putExtra("request", request);
        intent.putExtra("receiver", serviceReceiver);
        this.mActivity.startService(intent);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng location = new LatLng(mPlaceDetail.getGeometry().getLocation().getLat(), mPlaceDetail.getGeometry().getLocation().getLng());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.addMarker(new MarkerOptions().position(location));
//        googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
//            @Override
//            public void onSnapshotReady(Bitmap bitmap) {
//                if (bitmap != null){
//
//                    addBitmapToMemoryCache(mPlaceDetail.getId(), bitmap);
//                }
//            }
//        });

    }

    private class ImageOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            ImageView imageView = (ImageView) mView.findViewById(R.id.detail_image);

            imageView.setImageBitmap(((BitmapDrawable) ((ImageView) v).getDrawable()).getBitmap());

        }
    }

    private class MyTarget implements Target {
        ImageView mImageView;
        String mImgId;
        public MyTarget(ImageView imageView, String imgId) {
            mImageView = imageView;
            mImgId = imgId;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if (bitmap != null){
                mImageView.setImageBitmap(bitmap);
                addBitmapToMemoryCache(mImgId, bitmap);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }


    public void loadBitmap( String imageKey,  String imgId, ImageView imageView) {
        Log.d(TAG, "loadBitmap");
        final Bitmap bitmap = getBitmapFromMemCache(imgId);
        if (bitmap != null) {//CoQBcwAAAB_S2g9Kc7nz， CoQBcwAAALFm6l3y_zGS
            imageView.setImageBitmap(bitmap);
        } else {
            final StringBuffer sb_photo = new StringBuffer(imgUrl);
            sb_photo.append(imageKey);
            MyTarget myTarget = new MyTarget(imageView, imgId);
            Picasso.with(getContext())
                    .load(sb_photo.toString())
                    .resize(200, 200)
                    .placeholder(R.drawable.progress_animation)
                    .centerCrop()
                    .into(imageView);
//                    .into(myTarget);
        }
    }



    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (mMemoryCache != null && getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        if (mMemoryCache != null) {
            return mMemoryCache.get(key);
        }
        return null;
    }
}
