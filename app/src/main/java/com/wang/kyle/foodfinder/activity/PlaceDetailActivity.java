package com.wang.kyle.foodfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wang.kyle.foodfinder.R;
import com.wang.kyle.foodfinder.adapter.DetailPageAdapter;
import com.wang.kyle.foodfinder.fragment.PlaceDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {
    private List<String> placeIds;
    private String mPlaceId;
    private LruCache<String, Bitmap> mMemoryCache;
    private  List<Fragment> fragments;
    public LruCache<String, Bitmap> getMemoryCache() {
        return mMemoryCache;
    }

    public static Intent newIntent(Context packageContext, List<String> placeIds, String placeId){
        Intent intent = new Intent(packageContext, PlaceDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("placeId", placeId);
        bundle.putSerializable("placeIds", (ArrayList)placeIds);
        intent.putExtras(bundle);
        return intent;
    }

//    public static Bundle newBundle(List<String> placeIds, String placeId){
//        Bundle bundle = new Bundle();
//        bundle.putString("placeId", placeId);
//        bundle.putSerializable("placeIds", (ArrayList)placeIds);
//        return bundle;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;


        Intent intent  = getIntent();
        Bundle bundle= intent.getExtras();
        if(bundle != null){
            mPlaceId = (String)bundle.get("placeId");
            placeIds = (ArrayList)bundle.getSerializable("placeIds");
        }

        List<Fragment> fragments = buildFragments();
        ViewPager viewPager = (ViewPager)findViewById(R.id.pageId);
        DetailPageAdapter detailPageAdapter = new DetailPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(detailPageAdapter);
        int index = placeIds.indexOf(mPlaceId);
        viewPager.setCurrentItem(index);

        int currentIndex = viewPager.getCurrentItem();
        String curTag = placeIds.get(currentIndex);
        PlaceDetailFragment fragment = PlaceDetailFragment.findOrCreateRetainFragment(getSupportFragmentManager(), curTag);
        mMemoryCache = fragment.getMemoryCache();
        if (mMemoryCache == null){
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
            fragment.setMemoryCache(mMemoryCache);
        }


    }

    private List<Fragment> buildFragments(){
        fragments = new ArrayList<Fragment>();
        for(String s : placeIds){
            Bundle bundle = new Bundle();
            bundle.putString("placeId", s);
//            fragments.add(Fragment.instantiate(this, PlaceDetailFragment.class.getName(), bundle));
            Fragment fragment = PlaceDetailFragment.findOrCreateRetainFragment(getSupportFragmentManager(), s);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        return  fragments;
    }



}
