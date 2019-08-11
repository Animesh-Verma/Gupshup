package com.aplofie.infotech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editname, editemail,editroll,editpassword,edit_confirm_password;
    Spinner spininstitute, spinyear, spinsemester;
    TextView buttonregister;
    String institute , yearins , semester;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");


        editname=findViewById(R.id.editname);
        editemail =findViewById(R.id.editemail);
        editroll=findViewById(R.id.editrollnumber);
        editpassword = findViewById(R.id.editpassword);
        edit_confirm_password=findViewById(R.id.editconfirmpassword);

        buttonregister=findViewById(R.id.buttonregister);
        spinsemester = findViewById(R.id.editsemester);
        spininstitute = findViewById(R.id.editcollege);
        spinyear = findViewById(R.id.edityear);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.institute_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spininstitute.setAdapter(adapter);

        spininstitute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                institute = parent.getItemAtPosition(position).toString();
                }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinsemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              yearins =  parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinyear.setAdapter(adapter1);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinsemester.setAdapter(adapter2);

        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editname.getText().toString();
                String email= editemail.getText().toString();
                String rollno=editroll.getText().toString();
                String password =editpassword.getText().toString();
                String confirm_password = edit_confirm_password.getText().toString();
                String institute = spininstitute.getSelectedItem().toString();
                String yearins = spinyear.getSelectedItem().toString();
                String semester = spinsemester.getSelectedItem().toString();




                userpojo pojo = new userpojo();
              //  pojo.setName(name);
                pojo.setPassword(password);
                pojo.setEmail(email);
               // pojo.setRollno(rollno);
               // pojo.setInstitute(institute);
                //pojo.setSemester(semester);
                //pojo.setYearins(yearins);

                databaseReference.push().getKey();
                databaseReference.push().setValue(pojo);

                Intent intent = new Intent(SignupActivity.this, Login_Activity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         //An item was selected. You can retrieve the selected item using
         //parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
