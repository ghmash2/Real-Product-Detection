package com.example.goods_ledger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goods_ledger.ConsumerPart.ConsumerStartActivity;
import com.example.goods_ledger.ManufacturerPart.AddManufacturerActivity;
import com.example.goods_ledger.ManufacturerPart.ManufacturerStartActivity;
import com.example.goods_ledger.Assets.Links;
import com.example.goods_ledger.Assets.SavedValues;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private SharedPreferences sharedPreferences;
    private static SavedValues savedValues;
    private static Links links;
    private static RetryPolicy retryPolicy;

    private String accountToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        sharedPreferences = context.getSharedPreferences("GOODSLEDGER_SHARED_PREFERENCES", Context.MODE_PRIVATE);
        savedValues = new SavedValues(sharedPreferences);

        links = new Links();

        retryPolicy = new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        accountToken = savedValues.getAccountToken();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isOnline()){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, links.getURL_queryAccountbyToken(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Response", response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        JSONObject object = jsonObject.getJSONObject("Record");

                                        String accountKeyResponse = jsonObject.getString("Key").trim();
                                        String accountTokenResponse = object.getString("AccountToken").trim();
                                        String accountTypeResponse = object.getString("AccountType").trim();
                                        String accountNameResponse = object.getString("AccountName").trim();
                                        String accountUsernameResponse = object.getString("AccountUsername").trim();
                                        String accountEmailResponse = object.getString("AccountEmail").trim();
                                        String accountOwnerManufacturerIDResponse = object.getString("AccountOwnerManufacturerID").trim();

                                        getSavedValues().setAccountKey(accountKeyResponse);
                                        getSavedValues().setAccountToken(accountTokenResponse);
                                        getSavedValues().setAccountType(accountTypeResponse);
                                        getSavedValues().setAccountName(accountNameResponse);
                                        getSavedValues().setAccountUsername(accountUsernameResponse);
                                        getSavedValues().setAccountEmail(accountEmailResponse);
                                        getSavedValues().setAccountOwnerManufacturerID(accountOwnerManufacturerIDResponse);

                                        if (accountToken.equals("*#@%")){

                                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                        else if(accountTypeResponse.equals("Manufacturer")){

                                            if(accountOwnerManufacturerIDResponse.equals("*#@%")){

                                                startActivity(new Intent(MainActivity.this, AddManufacturerActivity.class));
                                                finish();
                                            }
                                            else {

                                                startActivity(new Intent(MainActivity.this, ManufacturerStartActivity.class));
                                                finish();
                                            }
                                        }
                                        else if(accountTypeResponse.equals("Consumer")){

                                            startActivity(new  Intent(MainActivity.this, ConsumerStartActivity.class));
                                            finish();
                                        }
                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                        Log.d("catchError", e.toString());
                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.d("responseError", volleyError.toString());

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

                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();

                                    Toast.makeText(getApplicationContext() ,message, Toast.LENGTH_LONG).show();
                                }
                            })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("accountToken", accountToken);
                            return params;
                        }
                    };

                    stringRequest.setRetryPolicy(retryPolicy);

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                    }
                    else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();

                        Toast.makeText(getApplicationContext() ,"No internet connection", Toast.LENGTH_SHORT).show();
                    }
            }
        }, 1000);
    }

    public static SavedValues getSavedValues() {
        return savedValues;
    }

    public static Links getLinks() {
        return links;
    }

    public static RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public static
    Bitmap getQRcodeBitmap(String text){
        Bitmap qrCodeBitmap = null;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return qrCodeBitmap;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
