package com.ribic.nejc.dragonhack.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ribic.nejc.dragonhack.R;

public class RegisterActivity extends AppCompatActivity {
    EditText mEditTextRegisterName;
    EditText mEditTextRegisterSurname;
    EditText mEditTextRegisterEmail;
    EditText mEditTextRegisterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        mEditTextRegisterName = (EditText) findViewById(R.id.edit_text_register_name);
        mEditTextRegisterSurname = (EditText) findViewById(R.id.edit_text_register_surname);
        mEditTextRegisterEmail = (EditText) findViewById(R.id.edit_text_register_email);
        mEditTextRegisterPassword = (EditText) findViewById(R.id.edit_text_register_password);
    }

    public void registerMe(View view) {
        //TODO check for inputs
        //TODO fetch data
    }
}
