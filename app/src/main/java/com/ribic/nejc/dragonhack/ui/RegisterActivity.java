package com.ribic.nejc.dragonhack.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class RegisterActivity extends AppCompatActivity {
    EditText mEditTextRegisterName;
    EditText mEditTextRegisterSurname;
    EditText mEditTextRegisterEmail;
    EditText mEditTextRegisterPassword;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        mEditTextRegisterName = (EditText) findViewById(R.id.edit_text_register_name);
        mEditTextRegisterSurname = (EditText) findViewById(R.id.edit_text_register_surname);
        mEditTextRegisterEmail = (EditText) findViewById(R.id.edit_text_register_email);
        mEditTextRegisterPassword = (EditText) findViewById(R.id.edit_text_register_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_register);
    }

    public void registerMe(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        String email = mEditTextRegisterEmail.getText().toString();
        String pass = mEditTextRegisterPassword.getText().toString();
        String name = mEditTextRegisterName.getText().toString() + "%20" + mEditTextRegisterSurname.getText().toString();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetworkUtils.registerUser(email, pass, name);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterActivity.this, "fuuuck", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(jsonObjReq);
    }
}
