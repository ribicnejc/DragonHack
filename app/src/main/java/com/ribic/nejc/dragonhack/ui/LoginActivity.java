package com.ribic.nejc.dragonhack.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ribic.nejc.dragonhack.R;

public class LoginActivity extends AppCompatActivity {

    EditText mEditTextLoginEmail;
    EditText mEditTextLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mEditTextLoginEmail = (EditText) findViewById(R.id.edit_text_login_email);
        mEditTextLoginPassword = (EditText) findViewById(R.id.edit_text_login_password);

        //TODO check for prefs if password exists


    }

    public void loginMe(View view) {
        String email = mEditTextLoginEmail.getText().toString();
        String password = mEditTextLoginPassword.getText().toString();
        boolean legit = true;
        if (email.isEmpty()) {
            mEditTextLoginEmail.setError("Check your email");
            legit = false;
        }
        if (password.isEmpty()) {
            mEditTextLoginPassword.setError("Insert your password");
            legit = false;
        }
        if (!legit) return;

        //TODO fetch data if legit

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
