package com.ribic.nejc.dragonhack.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ribic.nejc.dragonhack.R;
import com.ribic.nejc.dragonhack.utils.NetworkUtils;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_LOGIN_EMAIL = "com.nejc.ribic.login_email";
    public static final String EXTRA_LOGIN_NAME = "com.nejc.ribic.login_name";
    public static final String EXTRA_LOGIN_ID = "com.nejc.ribic.login_name";


    EditText mEditTextLoginEmail;
    EditText mEditTextLoginPassword;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mEditTextLoginEmail = (EditText) findViewById(R.id.edit_text_login_email);
        mEditTextLoginPassword = (EditText) findViewById(R.id.edit_text_login_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_login);
    }

    public void loginMe(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        String email = mEditTextLoginEmail.getText().toString();
        String password = mEditTextLoginPassword.getText().toString();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetworkUtils.loginUser(email, password);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String name = "";
                        String email = "";
                        String id = "";
                        try{
                            name = response.getString("name");
                            email = response.getString("email");
                            id = response.getInt("id") + "";
                        } catch (Exception e){
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(EXTRA_LOGIN_NAME, name);
                        intent.putExtra(EXTRA_LOGIN_EMAIL, email);
                        intent.putExtra(EXTRA_LOGIN_ID, id);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Login was successful", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Some field were wrong filled!", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(jsonObjReq);
    }

    public void registerMe(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
