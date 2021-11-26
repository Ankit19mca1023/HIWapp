package com.example.hiwApp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class Log_in extends AppCompatActivity {

    ProgressBar loginprogress;
    TextView Name;
    FirebaseAuth fAuth;
    FirebaseFirestore firestoreDB;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginprogress = findViewById(R.id.loginProgressbar);
        Name = findViewById(R.id.nameid);

        firestoreDB = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        UserID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firestoreDB.collection("users").document(UserID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
               String name = (documentSnapshot.getString("uName"));
               Name.setText("Welcome "+name);
            }
        });

    }
    @Override
    public void onBackPressed() {

       // loginprogress.setVisibility(View.VISIBLE);

        // FirebaseAuth.getInstance().signOut();//logout
       // finish();
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
        // super.onBackPressed();
    }

    public void Check_up(View v){

        loginprogress.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, CheckUp.class );
        startActivity(intent);
    }
    public void RecordCard(View v){
        loginprogress.setVisibility(View.VISIBLE);

        DocumentReference docRef = firestoreDB.collection("userData").document(UserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                      //  Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        startActivity(new Intent(getApplicationContext(),ReportRecord.class));
                    } else {
                      //  Log.d(TAG, "No such document");
                        loginprogress.setVisibility(View.INVISIBLE);
                        Toast.makeText(Log_in.this,"You'r a new user so, please first click on check up",Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(Log_in.this,"Some error occured please check your internet",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void logOut(View v){
        loginprogress.setVisibility(View.VISIBLE);
        onBackPressed();

    }
    public void onjoke(View v){

        loginprogress.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, JokeActivity.class );

        startActivity(intent);
    }

    @Override
    protected void onRestart() {

        super.onRestart();

        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }
}