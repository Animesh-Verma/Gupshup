package com.aplofie.infotech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Imageall extends AppCompatActivity {

    private static final String TAG = "EmailPasswordAuth";
    private static final int SELECT_PICTURE = 100;

    private Button buttongo , buttonupload;
    ImageView imageView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;

    public String userd;
    private Uri selectedImageUri;
    public ProgressDialog progressDialog;
    userpojo pojo = new userpojo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageall);

        buttonupload = findViewById(R.id.buttonupload);
        buttongo = findViewById(R.id.go);
        imageView=findViewById(R.id.imagedp);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        buttongo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

    }

    private void registration()
    {

        Intent intent =getIntent();
        String usname = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("pass");

        registerAccount(email , password ,usname);

    }

    private void registerAccount(final String email, final String password,final String usname) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userd= FirebaseAuth.getInstance().getUid();
                            //uploadImage();
                            new CountDownTimer(5000, 1000) {
                                public void onFinish() {
                                    // When timer is finished
                                    // Execute your code here

                                    Storedata(email , password , userd , usname);
                                }

                                public void onTick(long millisUntilFinished) {
                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();

                            Intent intent = new Intent(Imageall.this , MainActivity.class);
                            startActivity(intent);
                            finish();

                            Log.d(TAG, "account created");

                        } else {
                            Log.d(TAG, "register account failed", task.getException());
                            Toast.makeText(Imageall.this,
                                    "account registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog
                        //hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        //showAppropriateOptions();
                    }
                });
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
    public void Storedata(String email , String password , String userd , String usname)
    {

        pojo.setPassword(password);
        pojo.setEmail(email);
        //pojo.setImageuri(lop);
        pojo.setUserid(userd);
        pojo.setUsername(usname);
        databaseReference.push().getKey();
        databaseReference.push().setValue(pojo);

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

                            //Toast.makeText(Imageall.this,lop, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Imageall.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "/*+(int)progress+"%"*/);
                        }
                    });
        }


    }


}


