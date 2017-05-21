package com.ribic.nejc.dragonhack.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ribic.nejc.dragonhack.R;
import com.ribic.nejc.dragonhack.adapters.ListAdapter;
import com.ribic.nejc.dragonhack.objects.Item;
import com.ribic.nejc.dragonhack.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ribic.nejc.dragonhack.ui.LoginActivity.EXTRA_LOGIN_EMAIL;
import static com.ribic.nejc.dragonhack.ui.LoginActivity.EXTRA_LOGIN_ID;
import static com.ribic.nejc.dragonhack.ui.LoginActivity.EXTRA_LOGIN_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListAdapter.MainAdapterOnClickHandler {

    public String mName;
    public String mEmail;
    public String mId;

    public RecyclerView mRecyclerView;
    public ListAdapter mAdapter;
    public GridLayoutManager layoutManager;
    public TextView mTextViewName;
    public TextView mTextViewEmail;
    public ArrayList<Item> mItems;
    public FloatingActionButton fab;
    public String orderId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextViewName = (TextView) findViewById(R.id.text_view_name);
        mTextViewEmail = (TextView) findViewById(R.id.text_view_email);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch();
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_list);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 2);
        } else
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
        if (intent.hasExtra(EXTRA_LOGIN_EMAIL)) {
            mEmail = intent.getStringExtra(EXTRA_LOGIN_EMAIL);
            mTextViewEmail.setText(mEmail);
        }
        if (intent.hasExtra(EXTRA_LOGIN_NAME)) {
            mName = intent.getStringExtra(EXTRA_LOGIN_NAME);
            mTextViewName.setText(mName);
        }
        if (intent.hasExtra(EXTRA_LOGIN_ID)) {
            mId = intent.getStringExtra(EXTRA_LOGIN_ID);
        }

        if (orderId.equals("")){
            handleOrderList(mId);
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

    public void showSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Find items");
        final EditText input = new EditText(this);
        input.setHint("Example");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String query = input.getText().toString();
                if (!query.equals("")) {
                    handleSearchResponse(query);
                }else{
                    input.setError("Please enter some query");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    public void handleSearchResponse(String search) {
        ArrayList<Item> tmp = new ArrayList<>();
        for (Item item : mItems) {
            if (item.name.contains(search)) tmp.add(item);
        }
        mAdapter = new ListAdapter(tmp, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void handleResponse() {
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
                                String id = jsonObject.getInt("id") + "";
                                String name = jsonObject.getString("name");
                                String price = jsonObject.getString("price_formatted");
                                String desc = jsonObject.getString("description");
                                Item item = new Item(id, name, price, desc, "je");
                                items.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.v("ERR:", "problem appeared when parsing JSON");
                        }
                        mItems = items;
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

    public void handleOrderList(String id) {
        final RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetworkUtils.orderList(id);
        mItems = new ArrayList<>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            orderId = response.getString("order_id");
                            JSONArray array = response.getJSONArray("items");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                String id = jsonObject.getInt("id") + "";
                                String name = jsonObject.getString("name");
                                String price = jsonObject.getString("price_formatted");
                                String desc = jsonObject.getString("description");
                                Item item = new Item(id, name, price, desc, "je");
                                mItems.add(item);
                            }
                            mAdapter = new ListAdapter(mItems, MainActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        } catch (Exception e) {}
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage() + " Internet connection is poor", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(jsonObjReq);
    }

    public void addItem(int position) {
        final String url = "http://dragonhack.zigastrgar.com/api/orders/" + orderId + "/items?item_id=" + mItems.get(position).id;
        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("user_id", mId);
            jobj.put("item_id", mItems.get(position).id);
        } catch (Exception e) {}
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, jobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Item added to order!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("Content-Type", "application/x-www-form-urlencoded");
                return pars;
            }
        };

        mRequestQueue.add(jor);
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

        int id = item.getItemId();

        if (id == R.id.nav_list) {
            setTitle("All items");
            handleResponse();

        } else if (id == R.id.nav_food) {

        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_orders) {
            setTitle("Order list");
            handleOrderList(mId);
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    Toast.makeText(MainActivity.this, "removed item", Toast.LENGTH_SHORT).show();//Remove swiped item from list and notify the RecyclerView
                    mAdapter.notifyDataSetChanged();
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_finish) {
            Intent intent = new Intent(MainActivity.this, FinishActivity.class);
            intent.putExtra(LoginActivity.EXTRA_LOGIN_ID, mId);
            intent.putExtra(LoginActivity.EXTRA_LOGIN_NAME, mName);
            intent.putExtra(LoginActivity.EXTRA_LOGIN_EMAIL, mEmail);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void partyOnClick(final int clickedItemIndex) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
        alertDialog.setTitle(mItems.get(clickedItemIndex).name);
        alertDialog.setMessage(mItems.get(clickedItemIndex).description);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add to order", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                addItem(clickedItemIndex);
            }
        });

        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                mId = data.getStringExtra(LoginActivity.EXTRA_LOGIN_ID);
            }
        }
    }
}
