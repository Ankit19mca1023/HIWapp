package com.example.hiwApp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Sign_up extends AppCompatActivity {

    EditText FullName;
    EditText Phone;
    EditText UserName;
    EditText UserEmail;
    EditText UserPassword;
    EditText A_Password;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore firestoreDB;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FullName = findViewById(R.id.FullName);
        Phone = findViewById(R.id.Phone);
        UserEmail = findViewById(R.id.UserEmail);
        UserName = findViewById(R.id.UserName);
        UserPassword = findViewById(R.id.pswrd);
        A_Password = findViewById(R.id.apswrd);
        progressBar =findViewById(R.id.SignUpProgress);

        firestoreDB = FirebaseFirestore.getInstance();

        fAuth = FirebaseAuth.getInstance();

    }
    public void Sign_up(View v){

        String email=UserEmail.getText().toString().trim();
        String password=UserPassword.getText().toString().trim();
        String Apassword= A_Password.getText().toString().trim();
        String name = FullName.getText().toString();
        String phone = Phone.getText().toString();
        String userName = UserName.getText().toString();
        int flag=0;


        if(TextUtils.isEmpty(name)){
            FullName.setError("please enter your name");
            flag=1;
        }

        if(phone.length()!=10){
            Phone.setError("no. must be of 10 digits");
            flag=1;
        }
        try
        {
            float ph = Float.parseFloat(phone.trim());
        }
        catch (Exception e)
        {
            Phone.setError("Cann't be empty and enter digit only");
            flag=1;
        }

        if(TextUtils.isEmpty(email)){
            UserEmail.setError("Email is required");
            flag=1;
        }

        if(TextUtils.isEmpty(userName)){
            UserName.setError("this cannot be empty");
            flag=1;
        }


        if(TextUtils.isEmpty(password)){
            UserPassword.setError("Password is required");
            flag=1;
        }

        if(password.length()<6){
            UserPassword.setError("Password must be six character long");
            flag=1;
        }

        if (!Apassword.equals(password)){
            A_Password.setError("password doesn't match");
            flag=1;
            return;
        }



        if(flag==0){

            progressBar.setVisibility(View.VISIBLE);

            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        UserID = fAuth.getCurrentUser().getUid();

                        DocumentReference documentReference = firestoreDB.collection("users").document(UserID);

                        Map<String, Object> user = new HashMap<>();
                        user.put("uName", name );
                        user.put("uEmail",email);
                        user.put("uPhone",phone);
                        user.put("UserName",userName);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: user profile is created for "+UserID);
                                Toast.makeText(Sign_up.this,"sucessfully created",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.d(TAG, "onFailure: "+e.toString());
                                Toast.makeText(Sign_up.this,"Error"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });


                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Sign_up.this,"Error"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
    @Override
    public void onBackPressed() {
    //    finish();
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }
    @Override
    protected void onRestart() {

        super.onRestart();
     //   finish();
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }

}