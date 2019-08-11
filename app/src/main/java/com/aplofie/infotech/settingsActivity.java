package com.aplofie.infotech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class settingsActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private static final String TAG = "EmailPasswordAuth";
    ConstraintLayout constraintLayout;

    Button button;
    EditText editold , editnew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        firebaseAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button);
        editold =findViewById(R.id.op);
        editnew=findViewById(R.id.np);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });


    }

public void onBackPressed()
{
    Intent intent = new Intent(settingsActivity.this,MainActivity.class);
    startActivity(intent);
    finish();
}


    private void updatePassword() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        final String oldPwd = editold.getText().toString();
        final String newPwd = editnew.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPwd);
        if (!validateResetPassword(newPwd)) {
            Toast.makeText(settingsActivity.this,
                    "Invalid password, please enter valid password",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(newPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(settingsActivity.this,
                                        "Password has been updated",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "Error in updating passowrd",
                                        task.getException());
                                Toast.makeText(settingsActivity.this,
                                        "Failed to update passwrod.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(settingsActivity.this,
                            "Authentication Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean validateResetPassword(String password) {
        boolean valid = true;
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            valid = false;
        }
        return valid;
    }

}
