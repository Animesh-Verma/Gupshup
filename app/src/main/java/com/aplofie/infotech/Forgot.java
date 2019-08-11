package com.aplofie.infotech;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot extends AppCompatActivity {

    Button button;
    EditText editText;
    final String TAG = "Auth";
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        firebaseAuth= FirebaseAuth.getInstance();

        button = findViewById(R.id.resetbutton);
    editText = findViewById(R.id.sendmail);



    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendResetPasswordEmail();

        }
    });

    }


    private void sendResetPasswordEmail() {
        final String email = editText.getText().toString();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Forgot.this,
                                    "Reset password code has been emailed to "
                                            + email,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in sending reset password code",
                                    task.getException());
                            Toast.makeText(Forgot.this,
                                    "There is a problem with reset password, try later.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Intent intent  = new Intent(Forgot.this,Loginmain.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }


}
