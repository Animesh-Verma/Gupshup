package com.aplofie.infotech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Loginmain extends AppCompatActivity {

    public Button loginbutton , buttonregistration,forgotbutton;
    public EditText editemail , editpassword;
    public TextView tv;
    ConstraintLayout rl;

    public ProgressBar progressBar;
    private static final String TAG = "EmailPasswordAuth";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginmain);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        rl=findViewById(R.id.mmain);
        loginbutton = findViewById(R.id.loginbutton);
        buttonregistration = findViewById(R.id.registration);
        editemail =findViewById(R.id.email);
        editpassword =findViewById(R.id.password);
        tv=findViewById(R.id.ca);
        forgotbutton=findViewById(R.id.forgot);

        forgotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Loginmain.this , Forgot.class);
                startActivity(intent);
                finish();
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        register();
    }
        });

buttonregistration.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent =new Intent(Loginmain.this , Register.class);
        startActivity(intent);
        finish();
    }
});



        rl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                rl.getWindowVisibleDisplayFrame(r);
                int heightDiff = rl.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff > 100){
                    buttonregistration.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                }
                else
                {
                    buttonregistration.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Loginmain.this,Loginmain.class);
        startActivity(intent);
        this.finish();
    }

    public void onStart()
    {
        super.onStart();
        showAppropriateOptions();
    }

    private void register(){

       final String email = editemail.getText().toString();
        final String pass = editpassword.getText().toString();

        if (!validateEmailPass(email, pass)) {
            return;
            }
        else {
           performLogin(email , pass);
        }
    }


    private boolean validateEmailPass(String email , String password) {
        boolean valid = true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



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
            valid = false;}

        else {
            editpassword.setError(null);
        }

        return valid;
    }

  /*  private void performLoginOrAccountCreation(final String email, final String password){
        firebaseAuth.fetchProvidersForEmail(email).addOnCompleteListener(
                this, new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "checking to see if user exists in firebase or not");
                            ProviderQueryResult result = task.getResult();

                            if(result != null && result.getProviders()!= null
                                    && result.getProviders().size() > 0){
                                Log.d(TAG, "User exists, trying to login using entered credentials");
                                performLogin(email, password);
                            }
                        } else {
                            Log.w(TAG, "User check failed", task.getException());
                            Toast.makeText(Loginmain.this,
                                    "There is a problem, please try again later.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        //hide progress dialog
                       // hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        //showAppropriateOptions();
                    }
                });
    }*/





    private void performLogin(final String email,final String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "login success");
                            Intent intent = new Intent(Loginmain.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.e(TAG, "Login fail", task.getException());
                            Toast.makeText(Loginmain.this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog

                        //enable and disable login, logout buttons depending on signin status
                       showAppropriateOptions();
                    }
                });
    }

    public void showAppropriateOptions(){
        //hideProgressDialog();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
        Intent intent =new Intent(Loginmain.this , MainActivity.class);
        startActivity(intent);
        finish();

           // findViewById(R.id.verify_b).setEnabled(!user.isEmailVerified());
        }
    }





}
