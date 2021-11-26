package com.example.hiwApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button sign_up, Forgetpassword;
    ProgressBar progressBar;
    EditText registeredEmail;
    EditText registeredPassword;
    FirebaseAuth firebaseAuth_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign_up = findViewById(R.id.button2);
        progressBar = findViewById(R.id.progressBar);
        registeredEmail = findViewById(R.id.RegisteredEmail);
        registeredPassword = findViewById(R.id.RegisteredPswrd);
        Forgetpassword = findViewById(R.id.forget);

        firebaseAuth_login = FirebaseAuth.getInstance();

        Forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordReset = new AlertDialog.Builder(view.getContext());
                passwordReset.setTitle("Reset Password ?");
                passwordReset.setMessage("Enter Your email to received Reset Link");
                passwordReset.setView(resetMail);

                passwordReset.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString();
                        progressBar.setVisibility(View.VISIBLE);
                        if(TextUtils.isEmpty(mail)){
                            Toast.makeText(MainActivity.this, "Enter a email address", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }else{
                            firebaseAuth_login.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, "Reset link is sent", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Enter Valid email address" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                });

                passwordReset.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Closes the dialogBox
                    }
                });

                passwordReset.create().show();
            }
        });


    }
    public void Sign_up(View v){

        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, Sign_up.class );
        startActivity(intent);

    }
    public void Log_in(View v){

        String email = registeredEmail.getText().toString().trim();
        String password = registeredPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            registeredEmail.setError("Email is required");
            return;
        }

        if(TextUtils.isEmpty(password)){
            registeredPassword.setError("Password is required");
            return;
        }

        if(password.length()<6){
           registeredPassword.setError("Password must be six character long");
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth_login.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"sucessfully logged in",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, Log_in.class );
                    startActivity(intent);
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this,""+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    @Override
    public void onBackPressed() {
        finish();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
    @Override
    protected void onRestart() {

       super.onRestart();
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }
}