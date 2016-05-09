package com.wang.kyle.foodfinder.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wang.kyle.foodfinder.R;
import com.wang.kyle.foodfinder.adapter.NavDrawerListAdapter;
import com.wang.kyle.foodfinder.fragment.HomeFragment;
import com.wang.kyle.foodfinder.fragment.MyFragment;
import com.wang.kyle.foodfinder.module.NavDrawerItem;
import com.wang.kyle.foodfinder.module.PlaceSearchRequest;
import com.wang.kyle.foodfinder.receiver.HttpServiceReceiver;
import com.wang.kyle.foodfinder.service.HttpIntentService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener   {
    private final int mNumOfFragments = 2;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle actionBarDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string, R.string.close_string);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);



        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem());
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(0, -1)));
        // Mine
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2 ], navMenuIcons.getResourceId(1, -1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);


        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(1);
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setOnQueryTextListener(this);
//
//        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, MainActivity.class)));
//        searchView.setIconifiedByDefault(false);
//
//        return true;
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }


    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new MyFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
}
