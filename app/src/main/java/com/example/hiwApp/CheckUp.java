package com.example.hiwApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CheckUp extends AppCompatActivity {

     TextView Height, Weight, Age, PushUp, BpUp , BpDown;
     ProgressBar progressBar;
     FirebaseAuth fAuth;
     FirebaseFirestore firestoreDB;
     String UserID;
     String smoke;
     String drink;
     Boolean radioS=true, radioD=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_up);

        Height = findViewById(R.id.height);
        Weight = findViewById(R.id.weight);
        Age = findViewById(R.id.age);
        PushUp = findViewById(R.id.pushup);
        BpUp = findViewById(R.id.upbp);
        BpDown = findViewById(R.id.downbp);
        progressBar = findViewById(R.id.CheckprogressBar);



        firestoreDB = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

    }

    public void clear(View v){
        Height.setText("");
        Weight.setText("");
        Age.setText("");
        PushUp.setText("");
        BpUp.setText("");
        BpDown.setText("");
    }

    public void yesSmoke(View v){
        smoke="yes";
        radioS=false;
        Toast.makeText(CheckUp.this, "yes", Toast.LENGTH_SHORT).show();
    }

    public void yesDrink(View v){
        drink="yes";
        radioD=false;
        Toast.makeText(CheckUp.this, "yes", Toast.LENGTH_SHORT).show();
    }

    public void noSmoke(View v){
        smoke="no";
        radioS=false;
        Toast.makeText(CheckUp.this, "no", Toast.LENGTH_SHORT).show();
    }

    public void noDrink(View v){
        drink="no";
        radioD=false;
        Toast.makeText(CheckUp.this, "no", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
       // finish();
        Intent intent = new Intent(this, Log_in.class );
        startActivity(intent);
    }
    public void record(View v){

        String height = Height.getText().toString().trim();
        String weight = Weight.getText().toString().trim();
        String age = Age.getText().toString().trim();
        String pushUp = PushUp.getText().toString().trim();
        String bpUp = BpUp.getText().toString().trim();
        String bpDown =BpDown.getText().toString().trim();

        boolean flag = true;

        int h,w,a,push,bpd,bpu;

       if(TextUtils.isEmpty(height)){
           Height.setError("This can't be empty");
           flag=false;
       }
        try
        {
            h = Integer.parseInt(height.trim());
        }
        catch (Exception e)
        {
            Height.setError("Please enter digit only");
            flag=false;
        }
        if(TextUtils.isEmpty(age)){
            Age.setError("This can't be empty");
            flag=false;
        }
        try
        {
            a = Integer.parseInt(age.trim());
        }
        catch (Exception e)
        {
            Age.setError("Please enter digit only");
            flag=false;
        }
       if(TextUtils.isEmpty(weight)){
            Weight.setError("This can't be empty");
            flag=false;
       }
        try
        {
            w = Integer.parseInt(weight.trim());
        }
        catch (Exception e)
        {
            Weight.setError("Please enter digit only");
            flag=false;
        }
       if(TextUtils.isEmpty(pushUp)){
            PushUp.setError("This can't be empty");
            flag=false;
       }
        try
        {
            push = Integer.parseInt(pushUp.trim());
        }
        catch (Exception e)
        {
            PushUp.setError("Please enter digit only");
            flag=false;
        }

       if(TextUtils.isEmpty(bpDown)){
           BpDown.setError("This can't be empty");
            flag=false;
       }
        try
        {
            bpd = Integer.parseInt(bpDown.trim());
        }
        catch (Exception e)
        {
            BpDown.setError("Please enter digit only");
            flag=false;
        }

       if(TextUtils.isEmpty(bpUp)){
           BpUp.setError("This can't be empty");
            flag=false;
       }
        try
        {
            bpu = Integer.parseInt(bpUp.trim());
        }
        catch (Exception e)
        {
            BpUp.setError("Please enter digit only");
            flag=false;
        }

       if(radioD || radioS){
           Toast.makeText(CheckUp.this, "please select yes or no in smoke/drink field", Toast.LENGTH_SHORT).show();
           flag=false;
       }


       if(flag){

           progressBar.setVisibility(View.VISIBLE);

           Map<String, Object> user = new HashMap<>();
           user.put("height",height);
           user.put("weight", weight );
           user.put("age",age);
           user.put("pushup",pushUp);
           user.put("bpdown",bpDown);
           user.put("bpup",bpUp);
           user.put("smoke",smoke);
           user.put("drink",drink);

           UserID = fAuth.getCurrentUser().getUid();
           DocumentReference documentReference = firestoreDB.collection("userData").document(UserID);

           documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void unused) {
                   Toast.makeText(CheckUp.this, "Success", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getApplicationContext(),ReportRecord.class));
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   progressBar.setVisibility(View.INVISIBLE);
                   Toast.makeText(CheckUp.this,"Some error ocured", Toast.LENGTH_SHORT).show();
               }
           });
       }


    }
    @Override
    protected void onRestart() {

        super.onRestart();
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }
}