package com.example.goods_ledger.ManufacturerPart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddManufacturerActivity extends AppCompatActivity {

    private EditText manufacturerNameEditText, manufacturerTradeLicenceIDEditText,
            manufacturerLocationEditText, manufacturerFoundingDateEditText;
    private Button manufacturerAddButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manufacturer);

        manufacturerNameEditText = findViewById(R.id.activity_add_manufacturer_manufacturerName_EditText);
        manufacturerTradeLicenceIDEditText = findViewById(R.id.activity_add_manufacturer_manufacturerTradeID_EditText);
        manufacturerLocationEditText = findViewById(R.id.activity_add_manufacturer_manufacturerLocation_EditText);
        manufacturerFoundingDateEditText = findViewById(R.id.activity_add_manufacturer_manufacturerFoundingDate_EditText);
        manufacturerAddButton = findViewById(R.id.activity_add_manufacturer_addManufacturer_Button);
        progressBar = findViewById(R.id.activity_add_manufacturer_progressbar);

        final String manufacturerAccountID = MainActivity.getSavedValues().getAccountKey();

        manufacturerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String manufacturerName = manufacturerNameEditText.getText().toString();
                final String manufacturerTradeLicenceID = manufacturerTradeLicenceIDEditText.getText().toString();
                final String manufacturerLocation = manufacturerLocationEditText.getText().toString();
                final String manufacturerFoundingDate = manufacturerFoundingDateEditText.getText().toString();

                if(manufacturerName.isEmpty()){
                    manufacturerNameEditText.setError("Please type Something!");
                    return;
                }
                else if(manufacturerTradeLicenceID.isEmpty()){
                    manufacturerTradeLicenceIDEditText.setError("Please type Something!");
                    return;
                }
                else if(manufacturerLocation.isEmpty()){
                    manufacturerLocationEditText.setError("Please type Something!");
                    return;
                }
                else if(manufacturerFoundingDate.isEmpty()){
                    manufacturerFoundingDateEditText.setError("Please type Something!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_addManufacturer(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);

                                    String manufacturerKeyResponse = object.getString("manufacturerKey").trim();
                                    String manufacturerAccountIDResponse = object.getString("manufacturerAccountID").trim();
                                    String manufacturerNameResponse = object.getString("manufacturerName").trim();
                                    String manufacturerTradeLicenceIDResponse = object.getString("manufacturerTradeLicenceID").trim();
                                    String manufacturerLocationResponse = object.getString("manufacturerLocation").trim();
                                    String manufacturerFoundingDateResponse = object.getString("manufacturerFoundingDate").trim();

                                    MainActivity.getSavedValues().setManufacturerKey(manufacturerKeyResponse);
                                    MainActivity.getSavedValues().setManufacturerAccountID(manufacturerAccountIDResponse);
                                    MainActivity.getSavedValues().setManufacturerName(manufacturerNameResponse);
                                    MainActivity.getSavedValues().setManufacturerTradeLicenceID(manufacturerTradeLicenceIDResponse);
                                    MainActivity.getSavedValues().setManufacturerLocation(manufacturerLocationResponse);
                                    MainActivity.getSavedValues().setManufacturerFoundingDate(manufacturerFoundingDateResponse);

                                    Toast.makeText(AddManufacturerActivity.this, "Manufacturer adding successful!", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(AddManufacturerActivity.this, ManufacturerStartActivity.class));
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(AddManufacturerActivity.this, "Manufacturer adding failed! Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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
                        params.put("manufacturerAccountID", manufacturerAccountID);
                        params.put("manufacturerName", manufacturerName);
                        params.put("manufacturerTradeLicenceID", manufacturerTradeLicenceID);
                        params.put("manufacturerLocation", manufacturerLocation);
                        params.put("manufacturerFoundingDate", manufacturerFoundingDate);
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                RequestQueue requestQueue = Volley.newRequestQueue(AddManufacturerActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }
}
