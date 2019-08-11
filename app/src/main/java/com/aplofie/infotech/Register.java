package com.aplofie.infotech;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    Button button;
    EditText editname , editemail , editpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button = findViewById(R.id.buttonregister);
        editname = findViewById(R.id.name);
        editemail =findViewById(R.id.email);
        editpassword =findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });
        /* TextView textView = findViewById(R.id.textview);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/ander.ttf");
        textView.setTypeface(typeface);*/
    }


    private void register(){

        String name = editname.getText().toString();
        String email = editemail.getText().toString();
        String pass = editpassword.getText().toString();

        if (!validateEmailPass(email, pass)) {
            return;

        }
        else {
            Intent intent = new Intent(Register.this,Imageall.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("pass", pass);
            startActivity(intent);
        }
    }

    private boolean validateEmailPass(String email , String password) {
        boolean valid = true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String passPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(passPattern);
        matcher = pattern.matcher(password);


        if (TextUtils.isEmpty(email)) {
            editemail.setError("Required.");
            valid = false;
        }else if(!email.matches(emailPattern)){
            editemail.setError("Not an email id.");
            valid = false;
        } else{
            editemail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            editpassword.setError("Required.");
            valid = false;
        }else if(password.length() < 6){
            editpassword.setError("Min 6 chars.");
            valid = false;
        }
        else if(!matcher.matches()){
            editpassword.setError("Contain at least one digit.\n" +
                    "one lower case character.\n" +
                    "one upper case character.\n" +
                    "one special character from [ @ # $ % ! . ].");
            valid=false;
        }
        else {
            editpassword.setError(null);
        }

        return valid;
    }

}
