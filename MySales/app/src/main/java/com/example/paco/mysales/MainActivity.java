package com.example.paco.mysales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private CheckBox rememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("datos", Context.MODE_PRIVATE);

        username = findViewById(R.id.txtUsername);
        username.setText(sp.getString("username", ""));

        password = findViewById(R.id.txtPassword);
        password.setText(sp.getString("password", ""));

        rememberMe = findViewById(R.id.rememberCheck);
        rememberMe.setChecked(sp.getBoolean("rememberMe", false));
    }

    public void onClickLogin(final View v){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://obscure-tundra-25627.herokuapp.com/authUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE: ", response);

                        if(response.equals("[]")){
                            alert("Error", "The username or password is wrong!");
                        }
                        else
                        {
                            setSharedPreferences(rememberMe.isChecked());
                            alert("Success", "You are logged!");

//                            Snackbar.make(v,"Success, you are logged...", Snackbar.LENGTH_LONG)
//                                    .setAction("Action", null).show();

//                            startActivity(new Intent(MainActivity.this, ItemDetailActivity.class));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // mTextView.setText("That didn't work!");             
                        Log.d("Error.RESPONSE", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void setSharedPreferences(boolean isChecked){
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);

        if (isChecked)
        {
            preferences
                    .edit()
                    .putString("username", username.getText().toString())
                    .putString("password", password.getText().toString())
                    .putBoolean("rememberMe", rememberMe.isChecked())
                    .apply();
        }
        else
        {
            preferences
                    .edit()
                    .putString("username", "")
                    .putString("password", "")
                    .putBoolean("rememberMe", rememberMe.isChecked())
                    .apply();
        }
    }

    public void alert(String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//        alertDialog.setIcon(R.drawable.iconousuario);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }
}
