package com.example.hiwApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JokeActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    TextView textView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_joke);
        requestQueue = Volley.newRequestQueue(this);
        textView = findViewById(R.id.text1);
        progressBar = findViewById(R.id.JokeprogressBar);

        jokes();

    }

    public void jokes(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://icanhazdadjoke.com/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonRequest.Method.GET, url, null,
                new Response.Listener<JSONObject>() {



                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        try{
                            String s = response.getString("joke");
                            textView.setText(s);
                        }catch (JSONException e){
                            textView.setText("Some error occured while getting the joke please retry");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                textView.setText("Please connect to internet");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept","application/json");
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void more(View v){
        progressBar.setVisibility(View.VISIBLE);
        jokes();

    }

    @Override
    public void onBackPressed() {
      //  finish();
        Intent intent = new Intent(this, Log_in.class );
        startActivity(intent);
    }
    @Override
    protected void onRestart() {

       super.onRestart();
      //  finish();
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }

}