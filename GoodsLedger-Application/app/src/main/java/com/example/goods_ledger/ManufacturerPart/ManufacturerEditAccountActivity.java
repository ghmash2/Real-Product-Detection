package com.example.goods_ledger.ManufacturerPart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManufacturerEditAccountActivity extends AppCompatActivity {

    private ImageView profilePictureImageView;
    private EditText accountNameEditText, accountEmailEditText, accountPhoneNoEditText;
    private Button updateButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer_edit_account);

        profilePictureImageView = findViewById(R.id.activity_manufacturer_edit_account_profilePicture_ImageView);
        accountNameEditText = findViewById(R.id.activity_manufacturer_edit_account_accountName_EditText);
        accountEmailEditText = findViewById(R.id.activity_manufacturer_edit_account_accountEmail_EditText);
        accountPhoneNoEditText = findViewById(R.id.activity_manufacturer_edit_account_accountPhoneNo_EditText);
        updateButton = findViewById(R.id.activity_manufacturer_edit_account_update_Button);
        progressBar = findViewById(R.id.activity_manufacturer_edit_account_Progressbar);

        profilePictureImageView.setImageBitmap(ManufacturerStartActivity.getManufacturerKeyBitmap());

        accountNameEditText.setText(MainActivity.getSavedValues().getAccountName());
        accountEmailEditText.setText(MainActivity.getSavedValues().getAccountEmail());
        accountPhoneNoEditText.setText(MainActivity.getSavedValues().getAccountPhoneNumber());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String accountKey = MainActivity.getSavedValues().getAccountKey();
                final String accountToken = MainActivity.getSavedValues().getAccountToken();
                final String accountName = accountNameEditText.getText().toString();
                final String accountEmail = accountEmailEditText.getText().toString();
                final String accountPhoneNumber = accountPhoneNoEditText.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_updateAccount(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);

                                    String accountNameResponse = object.getString("accountName").trim();
                                    String accountEmailResponse = object.getString("accountEmail").trim();
                                    String accountPhoneNumberResponse = object.getString("accountPhoneNumber").trim();

                                    MainActivity.getSavedValues().setAccountName(accountNameResponse);
                                    MainActivity.getSavedValues().setAccountEmail(accountEmailResponse);
                                    MainActivity.getSavedValues().setAccountPhoneNumber(accountPhoneNumberResponse);

                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ManufacturerEditAccountActivity.this, "Account successfully updated!", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    progressBar.setVisibility(View.GONE);
                                    e.printStackTrace();
                                    Toast.makeText(ManufacturerEditAccountActivity.this, "Account updating failed! Error: " + response, Toast.LENGTH_SHORT).show();
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
                        params.put("accountKey", accountKey);
                        params.put("accountToken", accountToken);
                        params.put("accountName", accountName);
                        params.put("accountEmail", accountEmail);
                        params.put("accountPhoneNumber", accountPhoneNumber);
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                RequestQueue requestQueue = Volley.newRequestQueue(ManufacturerEditAccountActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }
}
