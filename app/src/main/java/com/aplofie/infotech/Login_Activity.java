package com.aplofie.infotech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Login_Activity extends AppCompatActivity {

    private static final String TAG = "EmailPasswordAuth";

    private EditText emailET;
    private EditText passwordET;
    private TextView textView;
    private ImageView imagedp;
    private EditText username;




    private static final int SELECT_PICTURE = 2;
    private static final String TAAG = "MainActivity";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;



public String dd;
   // public String lop;
   public String userd;
    private Uri selectedImageUri;
    private Uri downloaduri;
    @VisibleForTesting
    public ProgressDialog progressDialog;
    userpojo pojo = new userpojo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        imagedp=findViewById(R.id.imagedp);
        username = findViewById(R.id.username);
        firebaseAuth = FirebaseAuth.getInstance();



        setButtonListeners();
    }




    private void setButtonListeners(){
        //login button
        findViewById(R.id.login_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegistrationLogin();

            }
        });
        //reset password - for unauthenticated user
        findViewById(R.id.rest_password_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResetPasswordEmail();
            }
        });

        imagedp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();

            }
        });

        //logout button
      /*  findViewById(R.id.logout_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });*/
findViewById(R.id.buttongo).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(Login_Activity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
});
        //Verify email button
        findViewById(R.id.verify_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailVerificationMsg();

            }
        });

        //update password - for signed in user
     /*   findViewById(R.id.update_password_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });*/

        //Order functionality to show how to secure firestore data
        //using firebase authentication and firestore security rules
        /*findViewById(R.id.order_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Login_Activity.this, MainActivity.class);
                startActivity(i);
            }
        });*/
    }
    @Override
    public void onStart() {
        super.onStart();
        showAppropriateOptions();
    }
    private void  handleRegistrationLogin(){
        final String email = emailET.getText().toString();
        final String password = passwordET.getText().toString();

        if (!validateEmailPass(email, password)) {
            return;
        }

        //show progress dialog
        showProgressDialog();

        //perform login and account creation depending on existence of email in firebase
        performLoginOrAccountCreation(email, password);
    }
    private void performLoginOrAccountCreation(final String email, final String password){
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
                            }else{
                                Log.d(TAG, "User doesn't exist, creating account");
                                registerAccount(email, password);
                            }
                        } else {
                            Log.w(TAG, "User check failed", task.getException());
                            Toast.makeText(Login_Activity.this,
                                    "There is a problem, please try again later.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        showAppropriateOptions();
                    }
                });
    }
    private void performLogin(final String email,final String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "login success");

                            databaseReference = FirebaseDatabase.getInstance().getReference("User");
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        } else {
                            Log.e(TAG, "Login fail", task.getException());
                            Toast.makeText(Login_Activity.this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        showAppropriateOptions();
                    }
                });
    }
    private void registerAccount(final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userd= FirebaseAuth.getInstance().getUid();
                            uploadImage();
                            new CountDownTimer(5000, 1000) {
                                public void onFinish() {
                                    // When timer is finished
                                    // Execute your code here
                                    Storedata(email , password , userd);
                                }

                                public void onTick(long millisUntilFinished) {
                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();

                            Log.d(TAG, "account created");

                        } else {
                            Log.d(TAG, "register account failed", task.getException());
                            Toast.makeText(Login_Activity.this,
                                    "account registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        showAppropriateOptions();
                    }
                });
    }
    private boolean validateEmailPass(String email , String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailET.setError("Required.");
            valid = false;
        }else if(!email.contains("@")){
            emailET.setError("Not an email id.");
            valid = false;
        } else{
            emailET.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Required.");
            valid = false;
        }else if(password.length() < 6){
            passwordET.setError("Min 6 chars.");
            valid = false;
        }else {
            passwordET.setError(null);
        }

        return valid;
    }
   /* private boolean validateResetPassword(String password) {
        boolean valid = true;
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            valid = false;
        }
        return valid;
    }*/
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait!");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    public void showAppropriateOptions(){
        hideProgressDialog();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            findViewById(R.id.login_items).setVisibility(View.GONE);
            findViewById(R.id.logout_items).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_b).setEnabled(!user.isEmailVerified());
        } else {
            findViewById(R.id.login_items).setVisibility(View.VISIBLE);
            findViewById(R.id.logout_items).setVisibility(View.GONE);
        }
    }



    private void sendEmailVerificationMsg() {


        findViewById(R.id.verify_b).setEnabled(false);
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.verify_b).setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(Login_Activity.this,
                                    "Verification email has been sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in sending verification email",
                                    task.getException());
                            Toast.makeText(Login_Activity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //non-singed in user reset password email
    private void sendResetPasswordEmail() {
        final String email = ((EditText) findViewById(R.id.reset_password_email))
                .getText().toString();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Login_Activity.this,
                                    "Reset password code has been emailed to "
                                            + email,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in sending reset password code",
                                    task.getException());
                            Toast.makeText(Login_Activity.this,
                                    "There is a problem with reset password, try later.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

  /*  private void updatePassword() {

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final String newPwd = ((EditText) findViewById(R.id.update_password_t)).getText().toString();
        if(!validateResetPassword(newPwd)){
            Toast.makeText(Login_Activity.this,
                    "Invalid password, please enter valid password",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        user.updatePassword(newPwd)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login_Activity.this,
                                    "Password has been updated",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in updating passowrd",
                                    task.getException());
                            Toast.makeText(Login_Activity.this,
                                    "Failed to update passwrod.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/
   /* private void logOut() {
        firebaseAuth.signOut();
        showAppropriateOptions();
    }*/
    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }



    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                 selectedImageUri = data.getData();
                //lop = selectedImageUri.getPath();

                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    //textView.setText(lop);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    ((ImageView) findViewById(R.id.imagedp)).setImageURI(selectedImageUri);
                }
            }
        }
    }


    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    public void Storedata(String email , String password , String userd)
    {
        String  usname = username.getText().toString();
        Log.e("Step" , "in storedaata");
        pojo.setPassword(password);
        pojo.setEmail(email);
        //pojo.setImageuri(lop);
        pojo.setUserid(userd);
        pojo.setUsername(usname);
        databaseReference.push().getKey();
        databaseReference.push().setValue(pojo);
        Log.e("Step" , "After Store adada");

    }







    public void uploadImage() {

        if(selectedImageUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                        String lop=taskSnapshot.getDownloadUrl().toString();

                           pojo.setImageuri(lop);




                            Toast.makeText(Login_Activity.this,lop, Toast.LENGTH_LONG).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Login_Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }


    }





/*
private void downloadimg(){
    final File localFile = File.createTempFile("images", "jpg");
    storageReference.getFile(localFile)
            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Successfully downloaded data to local file
                    ImageView dpp;
                    dpp =findViewById(R.id.imagedplogout);
                    Glide.with(Login_Activity.this).load(localFile).into(dpp);
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle failed download
            // ...
        }
    });

}*/

}


