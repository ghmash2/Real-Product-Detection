package com.example.goods_ledger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goods_ledger.ConsumerPart.ConsumerStartActivity;
import com.example.goods_ledger.ManufacturerPart.AddManufacturerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText fullName, username, email, password, confirmedPassword;
    private Button signupButton;
    private TextView logInTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        radioGroup = findViewById(R.id.activity_sign_up_RadioGroup);
        fullName = findViewById(R.id.activity_sign_up_fullName_EditText);
        username = findViewById(R.id.activity_sign_up_username_EditText);
        email = findViewById(R.id.activity_sign_up_email_EditText);
        password = findViewById(R.id.activity_sign_up_password_EditText);
        confirmedPassword = findViewById(R.id.activity_sign_up_confirmedPassword_EditText);
        signupButton = findViewById(R.id.activity_sign_up_signup_Button);
        logInTextView = findViewById(R.id.activity_sign_up_login_TextView);
        progressBar = findViewById(R.id.activity_sign_up_Progressbar);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                final String accountType = radioButton.getText().toString();
                final String accountName = fullName.getText().toString();
                final String accountUsername = username.getText().toString();
                final String accountEmail = email.getText().toString();
                final String accountPassword = password.getText().toString();
                final String accountConfirmedPassword = confirmedPassword.getText().toString();
                final String accountOwnerManufacturerID = "*#@%";

                if(accountName.isEmpty()){
                    fullName.setError("Please type something!");
                    return;
                }
                else if(accountUsername.isEmpty()){
                    username.setError("Please type something!");
                    return;
                }
                else if(accountEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(accountEmail).matches()){
                    email.setError("Invalid email address");
                    return;
                }
                else if(accountPassword.isEmpty() || accountPassword.length() < 6) {
                    password.setError("You must have 6 characters in your password");
                    return;
                }
                else if(accountConfirmedPassword.isEmpty() || accountConfirmedPassword.length() < 6){
                    confirmedPassword.setError("You must have 6 characters in your password");
                    return;
                }
                else if(!accountPassword.equals(accountConfirmedPassword)){
                    Toast.makeText(SignUpActivity.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_registerAccount(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);

                                    String accountKeyResponse = object.getString("accountKey").trim();
                                    String accountTokenResponse = object.getString("accountToken").trim();
                                    String accountTypeResponse = object.getString("accountType").trim();
                                    String accountNameResponse = object.getString("accountName").trim();
                                    String accountUsernameResponse = object.getString("accountUsername").trim();
                                    String accountEmailResponse = object.getString("accountEmail").trim();
                                    String accountOwnerManufacturerIDResponse = object.getString("accountOwnerManufacturerID").trim();

                                    MainActivity.getSavedValues().setAccountKey(accountKeyResponse);
                                    MainActivity.getSavedValues().setAccountToken(accountTokenResponse);
                                    MainActivity.getSavedValues().setAccountType(accountTypeResponse);
                                    MainActivity.getSavedValues().setAccountName(accountNameResponse);
                                    MainActivity.getSavedValues().setAccountUsername(accountUsernameResponse);
                                    MainActivity.getSavedValues().setAccountEmail(accountEmailResponse);
                                    MainActivity.getSavedValues().setAccountOwnerManufacturerID(accountOwnerManufacturerIDResponse);

                                    Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                                    if(accountTypeResponse.equals("Manufacturer")){

                                        startActivity(new Intent(SignUpActivity.this, AddManufacturerActivity.class));
                                        finish();
                                    }
                                    else if(accountTypeResponse.equals("Consumer")){

                                        startActivity(new Intent(SignUpActivity.this, ConsumerStartActivity.class));
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    progressBar.setVisibility(View.GONE);
                                    e.printStackTrace();
                                    Toast.makeText(SignUpActivity.this, "Registration failed! Error: " + response, Toast.LENGTH_SHORT).show();
                                    Log.d("catchError", e.toString());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.d("responseError", volleyError.toString());

                                progressBar.setVisibility(View.GONE);

                                String message = null;
                                if (volleyError instanceof NetworkError) {
                                    message = "Cannot connect to Internet...Please check your connection!";
                                } else if (volleyError instanceof ServerError) {
                                    message = "The server could not be found. Please try again after some time!!";
                                } else if (volleyError instanceof AuthFailureError) {
                                    message = "Cannot connect to Internet...Please check your connection!";
                                } else if (volleyError instanceof ParseError) {
                                    message = "Parsing error! Please try again after some time!!";
                                } else if (volleyError instanceof NoConnectionError) {
                                    message = "Cannot connect to Internet...Please check your connection!";
                                } else if (volleyError instanceof TimeoutError) {
                                    message = "Connection TimeOut! Please check your internet connection.";
                                }

                                Toast.makeText(getApplicationContext() ,message, Toast.LENGTH_LONG).show();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("accountType", accountType);
                        params.put("accountName", accountName);
                        params.put("accountUsername", accountUsername);
                        params.put("accountEmail", accountEmail);
                        params.put("accountPassword", accountPassword);
                        params.put("accountConfirmedPassword", accountConfirmedPassword);
                        params.put("accountOwnerManufacturerID", accountOwnerManufacturerID);
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}
