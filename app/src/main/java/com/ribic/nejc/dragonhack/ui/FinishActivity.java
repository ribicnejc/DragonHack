package com.ribic.nejc.dragonhack.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ribic.nejc.dragonhack.R;
import com.ribic.nejc.dragonhack.utils.NetworkUtils;

import org.json.JSONObject;
import org.w3c.dom.Text;

import static com.ribic.nejc.dragonhack.ui.LoginActivity.EXTRA_LOGIN_EMAIL;
import static com.ribic.nejc.dragonhack.ui.LoginActivity.EXTRA_LOGIN_ID;
import static com.ribic.nejc.dragonhack.ui.LoginActivity.EXTRA_LOGIN_NAME;

public class FinishActivity extends AppCompatActivity {

    public TextView mTextViewName;
    public TextView mTextViewPrice;
    public String id;
    public String email;
    public String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        setTitle("Checkout");
        mTextViewName = (TextView) findViewById(R.id.text_name);
        mTextViewPrice = (TextView) findViewById(R.id.text_price);
        Intent intent = getIntent();
        if (intent.hasExtra(LoginActivity.EXTRA_LOGIN_ID)){
            id = intent.getStringExtra(LoginActivity.EXTRA_LOGIN_ID);
            mTextViewName.setText(intent.getStringExtra(LoginActivity.EXTRA_LOGIN_NAME));
            email = intent.getStringExtra(LoginActivity.EXTRA_LOGIN_EMAIL);
            name = intent.getStringExtra(LoginActivity.EXTRA_LOGIN_NAME);
            response(id);
        }
    }

    public void response(String id){
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetworkUtils.orderList(id);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String name = "";
                        String price = "";
                        try{
                            //name = response.getString("name");
                            price = response.getString("total_formatted");
                            //mTextViewName.setText(name);
                            mTextViewPrice.setText(price);
                        } catch (Exception e){
                            Toast.makeText(FinishActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FinishActivity.this, "Some field were incorrect filled", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(jsonObjReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(FinishActivity.this, MainActivity.class);
            intent.putExtra(LoginActivity.EXTRA_LOGIN_ID, id);
            intent.putExtra(LoginActivity.EXTRA_LOGIN_EMAIL, email);
            intent.putExtra(LoginActivity.EXTRA_LOGIN_NAME, name);
            startActivity(intent);
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FinishActivity.this, MainActivity.class);
        intent.putExtra(LoginActivity.EXTRA_LOGIN_ID, id);
        intent.putExtra(LoginActivity.EXTRA_LOGIN_EMAIL, email);
        intent.putExtra(LoginActivity.EXTRA_LOGIN_NAME, name);
        startActivity(intent);
        //super.onBackPressed();
    }
}
