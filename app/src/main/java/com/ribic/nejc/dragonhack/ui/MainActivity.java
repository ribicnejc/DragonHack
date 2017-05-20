package com.ribic.nejc.dragonhack.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ribic.nejc.dragonhack.R;
import com.ribic.nejc.dragonhack.adapters.ListAdapter;
import com.ribic.nejc.dragonhack.objects.Item;
import com.ribic.nejc.dragonhack.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
    ListAdapter.MainAdapterOnClickHandler{

    public RecyclerView mRecyclerView;
    public ListAdapter mAdapter;
    public GridLayoutManager layoutManager;
    public TextView mTextViewName;
    public TextView mTextViewEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextViewName = (TextView) findViewById(R.id.text_view_name);
        mTextViewEmail = (TextView) findViewById(R.id.text_view_email);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_list);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(this, 2);
        }else
            layoutManager = new GridLayoutManager(this, 1);

        mRecyclerView.setLayoutManager(layoutManager);


        handleResponse();

        mRecyclerView.setHasFixedSize(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        mTextViewName = (TextView) headerView.findViewById(R.id.text_view_name);
        mTextViewEmail = (TextView) headerView.findViewById(R.id.text_view_email);


        Intent intent = getIntent();
        if (intent.hasExtra(LoginActivity.EXTRA_LOGIN_EMAIL)){
            mTextViewEmail.setText(intent.getStringExtra(LoginActivity.EXTRA_LOGIN_EMAIL));
        }
        if(intent.hasExtra(LoginActivity.EXTRA_LOGIN_NAME)){
            mTextViewName.setText(intent.getStringExtra(LoginActivity.EXTRA_LOGIN_NAME));
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void handleResponse(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetworkUtils.items();
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Item> items = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String price = jsonObject.getString("price_formated");
                                String desc = jsonObject.getString("description");
                                Item item = new Item(name, price, desc, "je");
                                items.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.v("ERR:", "problem appeared when parsing JSON");
                        }
                        mAdapter = new ListAdapter(items, MainActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        Log.v("SUC:", "result get from internet");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            // Handle the camera action
        } else if (id == R.id.nav_food) {

        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void partyOnClick(int clickedItemIndex) {
        Toast.makeText(this, "works!", Toast.LENGTH_SHORT).show();
    }
}
