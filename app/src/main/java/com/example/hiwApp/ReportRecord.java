package com.example.hiwApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ReportRecord extends AppCompatActivity {
    TextView Name, weightCondition, heartCondition, smokeCondition, drinkCondition, bpbmi;
    FirebaseAuth fAuth;
    FirebaseFirestore firestoreDB;
    String UserID;
    String weight;
    String height;
    String BpUp;
    String BpDown;
    String Smoke, Drink;
    float h,w;
    float bmi,hf,hff;
    int bpup, bpdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_record);

        Name = findViewById(R.id.name);
        weightCondition = findViewById(R.id.weightCondition);
        heartCondition = findViewById(R.id.heartCondition);
        smokeCondition = findViewById(R.id.smokeReport);
        drinkCondition = findViewById(R.id.drinkReport);
        bpbmi = findViewById(R.id.bpbmi);

        firestoreDB = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        UserID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firestoreDB.collection("users").document(UserID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                String s = (documentSnapshot.getString("uName"));
                s = s + "\'s Report";
                Name.setText(s);
            }
        });

        DocumentReference documentReport = firestoreDB.collection("userData").document(UserID);

        documentReport.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
               weight = documentSnapshot.getString("weight");
               height = documentSnapshot.getString("height");
               BpUp = documentSnapshot.getString("bpup");
               BpDown = documentSnapshot.getString("bpdown");
               Smoke = documentSnapshot.getString("smoke");
               Drink = documentSnapshot.getString("drink");

                try{
                    h = Float.parseFloat(height);
                    w=Float.parseFloat(weight);
                    hf= h/100;
                    hff= hf*hf;
                    bmi = w/hff;
                    bpup = Integer.parseInt(BpUp);
                    bpdown =Integer.parseInt(BpDown);

                }catch (Exception e){
                    Toast.makeText(ReportRecord.this, "Some error occured, please try again", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }

                String b=Float.toString(bmi);
                String s = "Your Bp: "+BpDown+"\\"+BpUp+"\n\n" +
                        "Your BMI: "+b;

                bpbmi.setText(s);

                //Heart conditions
                if(bpdown<=70 && bpup<=125){
                    //low
                    heartCondition.setText("Your Blood pressure is low, which is totally normal.\n\n" +
                            "You don't have to worry much this is pretty normal these days.\n\n" +
                            "You should eat protein, minerals rich food.\n\n" +
                            "Increase salt intake in your daily life.\n\n" +
                            "your heart is in good health, But your daily life style and do yoga.");
                }else if(bpdown>=81 && bpup>=125){
                    //high
                    heartCondition.setText("Your Blood pressure is high, which is not a good condition.\n\n" +
                            "But you can manage it your own its also not so worse.\n\n" +
                            "You should decrease your salt intake and other spices.\n\n" +
                            "Drink 2-3 litre of water daily.\n\n" +
                            "You should consult doctor if you feel any kind of blackouts ever.");
                }else if(bpdown>=85 && bpup>=145){
                    //hyper
                    heartCondition.setText("You have Hypertension that means your blood pressure is high on average scale.\n\n" +
                            "This could lead to a Heart disease so please consult to a doctor as soon as possible.\n\n" +
                            "Drink 2-3 litre of water daily.\n\n" +
                            "Decrease salt intake.\n\n" +
                            "Cutoff cholesterol rich and fast food.");
                }else if(bpdown<=60 && bpup<=120){
                    //hypo
                    heartCondition.setText("You have Hypotension that means Your blood pressure is low on average scale.\n\n" +
                            "if you ever have feelings of blackout then please consult doctor as soon as possible.\n\n" +
                            "Eat protein rich foods.\n\n" +
                            "Increase your salt intake.\n\n" +
                            "please don't do fasting or dieting in any form it can worsen the condition.");
                }else{
                    heartCondition.setText("kudos, Your blood pressure is completely normal.\n\n" +
                            "But now is the main course to maintain it for better health.\n\n" +
                            "So eat healthy foods, Cut off fast foods or else not eat on frequent basis.\n\n" +
                            "Drink 2-3 litre of water daily.");
                }

                //weight conditions
                if(bmi<18 && bmi>=16){
                    weightCondition.setText("You are under-weight.\n\n" +
                            "Don't worry This is not much of issue less of physical appearance.\n\n" +
                            "But now a days, physical body language also matters equal to other things.\n\n" +
                            "You should increase your diet with proper diet plan.\n\n" +
                            "You can also join gym for weight gaining.\n\n" +
                            "Eat supplements like vitamin tablets.\n\n" +
                            "Add protein rich foods to your daily routine.\n\n" +
                            "Do yoga in the morning it will give you fresh start.\n\n");
                }else if(bmi>=23 && bmi<=25){
                    weightCondition.setText("You are slightly over-weight.\n\n" +
                            "Don't worry This is not much of issue less of physical appearance.\n\n" +
                            "But now a days, physical body language also matters equal to other things.\n\n" +
                            "You should go for running or join gym as per your choice to maintain your weight.\n\n" +
                            "Some fasting or less meal can also add some points to decrease your weight.\n\n" +
                            "Drink 2-3 litre of water per day, water plays important role to breakdown fat.\n\n" +
                            "Do yoga in the morning it will give you fresh start.\n\n\n");
                }else if(bmi>25 && bmi<=30){
                    weightCondition.setText("You are over-weight.\n\n" +
                            "You should concern about your health, This condition can put in a \"Diabetes\" disease.\n\n" +
                            "You should go for running on daily basis.\n\n" +
                            "You can also join gym for weight losing.\n\n"+
                            "Some fasting or less meal can also add some points to decrease your weight.\n\n"+
                            "Avoid drinking and smoking.\n\n"+
                            "Drink 2-3 litre of water per day, water plays important role to breakdown fat.\n\n" +
                            "Do yoga in the morning it will give you fresh start.\n\n\n");
                }else if(bmi>30){
                    weightCondition.setText("You have obesity condition, that your body has too much body fat.\n\n" +
                            "You should concern about your health, This condition can put in a \"Diabetes\" disease.\n\n" +
                            "You should go for running on daily basis.\n\n" +
                            "You can also join gym for weight losing.\n\n"+
                            "Some fasting or less meal can also add some points to decrease your weight.\n\n"+
                            "Avoid drinking and smoking.\n\n"+
                            "Drink 2-3 litre of water per day, water plays important role to breakdown fat.\n\n" +
                            "Do yoga in the morning it will give you fresh start.\n\n\n");
                }else if(bmi<16){
                    weightCondition.setText("Your weight is too much less than average people.\n\n" +
                            "You should consult doctor foe this condition.\n\n" +
                            "You should increase your diet with proper diet plan.\n\n" +
                            "You can also join gym for weight gaining.\n\n" +
                            "Eat supplements like vitamin tablets on monthly basis.\n\n" +
                            "Add protein rich foods to your daily routine.\n\n" +
                            "Do yoga in the morning it will give you fresh start.\n\n\n");

                }else{
                    weightCondition.setText("kudos, Your weight is normal.\n\n" +
                            "But now is the main course to maintain it for better health.\n\n" +
                            "Do yoga or Exercise daily to maintain this weight.\n\n"+
                            "So eat healthy foods, Cut off fast foods or else not eat on frequent basis.\n\n" +
                            "Drink 2-3 litre of water daily.\n\n"+
                            "Do yoga in the morning it will give you fresh start.\n\n\n");
                }

                //somke conditions
                if(Smoke.equals("yes")){
                    smokeCondition.setText("Other things you should concern about Your heart:\n\nYou must quit smoking as soon as possible.\n\n" +
                            "This can lead to a major heart disease.\n\n" +
                            "please stop Smoking");
                }else{
                    smokeCondition.setText("Other things you should concern about Your heart :\n\nIts good to know that you don't smoke.\n\n" +
                            "This is surely a benefit to your body.\n\n" +
                            "Make sure to avoid such places where you find smoking people's.\n\n" +
                            "You can also encourage your friends to quit smoking.");
                }
                //weight conditions
                if(Drink.equals("yes")){
                    drinkCondition.setText("Healthy Drinking can show good effects.\n\n" +
                            "Too much of drinking on regular basis can lead to addiction.\n\n" +
                            "you should always take good care of drinking habits.\n\n" +
                            "Don't drink too much.\n\n" +
                            "Live a healthy life with minimal drinkng.\n\n" +
                            "Too much drinking can lead to \"Diabetes\".\n\n");
                }else{
                    drinkCondition.setText("Its good to know that you don't drink.\n\n" +
                            "But a healthy drinking can be useful to your body.\n\n" +
                            "You can keep your this habit, it will surely a benefit to you.\n\n");
                }

            }
        });
    }
    public void logOut(View v){

        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, Log_in.class );
        startActivity(intent);
    }
    @Override
    protected void onRestart() {

      super.onRestart();
     Intent intent = new Intent(this, MainActivity.class );
     startActivity(intent);
    }

}