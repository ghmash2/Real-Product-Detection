package com.example.goods_ledger.ManufacturerPart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.goods_ledger.Assets.Factory;
import com.example.goods_ledger.Assets.Product;
import com.example.goods_ledger.ConsumerPart.ConsumerStartActivity;
import com.example.goods_ledger.LoginActivity;
import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManufacturerStartActivity extends AppCompatActivity {

    private static Bitmap manufacturerKeyBitmap;
    private static ArrayList<Product> productsArray;
    private static ArrayList<Factory> factoriesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer_start);
        BottomNavigationView navView = findViewById(R.id.nav_manufacturer_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_manufacturer_home, R.id.navigation_manufacturer_factories, R.id.navigation_manufacturer_products, R.id.navigation_manufacturer_options)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_manufacturer_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        manufacturerKeyBitmap = MainActivity.getQRcodeBitmap(MainActivity.getSavedValues().getManufacturerKey());

        productsArray = new ArrayList<>();
        factoriesArray = new ArrayList<>();

        final String manufacturerAccountID = MainActivity.getSavedValues().getAccountKey();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_queryManufacturerbyAccountID(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Log.d("Response", response);

                            JSONObject object = jsonObject.getJSONObject("Record");

                            String manufacturerKeyResponse = jsonObject.getString("Key").trim();

                            String manufacturerAccountIDResponse = object.getString("ManufacturerAccountID").trim();
                            String manufacturerNameResponse = object.getString("ManufacturerName").trim();
                            String manufacturerTradeLicenceIDResponse = object.getString("ManufacturerTradeLicenceID").trim();
                            String manufacturerLocationResponse = object.getString("ManufacturerLocation").trim();
                            String manufacturerFoundingDateResponse = object.getString("ManufacturerFoundingDate").trim();

                            MainActivity.getSavedValues().setManufacturerKey(manufacturerKeyResponse);
                            MainActivity.getSavedValues().setManufacturerAccountID(manufacturerAccountIDResponse);
                            MainActivity.getSavedValues().setManufacturerName(manufacturerNameResponse);
                            MainActivity.getSavedValues().setManufacturerTradeLicenceID(manufacturerTradeLicenceIDResponse);
                            MainActivity.getSavedValues().setManufacturerLocation(manufacturerLocationResponse);
                            MainActivity.getSavedValues().setManufacturerFoundingDate(manufacturerFoundingDateResponse);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Log.d("catchError", e.toString());
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

                        startActivity(new Intent(ManufacturerStartActivity.this, LoginActivity.class));
                        finish();

                        Toast.makeText(getApplicationContext() ,message, Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("manufacturerAccountID", manufacturerAccountID);
                return params;
            }
        };

        stringRequest1.setRetryPolicy(MainActivity.getRetryPolicy());

        RequestQueue requestQueue1 = Volley.newRequestQueue(ManufacturerStartActivity.this);
        requestQueue1.add(stringRequest1);

        final String productManufacturerID = MainActivity.getSavedValues().getAccountOwnerManufacturerID();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_queryProductbyManufacturerID(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            MainActivity.getSavedValues().setProductsCount(Integer.toString(array.length()));

                            for (int i=0; i<array.length(); i++){

                                JSONObject jsonObject = array.getJSONObject(i);

                                String productKeyResponse = jsonObject.getString("Key").trim();
                                JSONObject object = jsonObject.getJSONObject("Record");

                                String productOwnerAccountIDResponse = object.getString("ProductOwnerAccountID").trim();
                                String productManufacturerIDResponse = object.getString("ProductManufacturerID").trim();
                                String productManufacturerNameResponse = object.getString("ProductManufacturerName").trim();
                                String productFactoryIDResponse = object.getString("ProductFactoryID").trim();
                                String productIDResponse = object.getString("ProductID").trim();
                                String productNameResponse = object.getString("ProductName").trim();
                                String productTypeResponse = object.getString("ProductType").trim();
                                String productBatchResponse = object.getString("ProductBatch").trim();
                                String productSerialinBatchResponse = object.getString("ProductSerialinBatch").trim();
                                String productManufacturingLocationResponse = object.getString("ProductManufacturingLocation").trim();
                                String productManufacturingDateResponse = object.getString("ProductManufacturingDate").trim();
                                String productExpiryDateResponse = object.getString("ProductExpiryDate").trim();

                                Product newProduct = new Product(productKeyResponse, productOwnerAccountIDResponse, productManufacturerIDResponse, productManufacturerNameResponse, productFactoryIDResponse, productIDResponse,
                                        productNameResponse, productTypeResponse, productBatchResponse, productSerialinBatchResponse, productManufacturingLocationResponse, productManufacturingDateResponse, productExpiryDateResponse);

                                productsArray.add(newProduct);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("catchError", e.toString());
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

                        Toast.makeText(getApplicationContext() ,message, Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("productManufacturerID", productManufacturerID);
                return params;
            }
        };

        stringRequest2.setRetryPolicy(MainActivity.getRetryPolicy());

        RequestQueue requestQueue2 = Volley.newRequestQueue(ManufacturerStartActivity.this);
        requestQueue2.add(stringRequest2);

        final String factoryManufacturerID = MainActivity.getSavedValues().getAccountOwnerManufacturerID();

        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_queryFactorybyManufacturerID(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            MainActivity.getSavedValues().setFactoriesCount(Integer.toString(array.length()));

                            for (int i=0; i<array.length(); i++){

                                JSONObject jsonObject = array.getJSONObject(i);

                                String factoryKeyResponse = jsonObject.getString("Key").trim();
                                JSONObject object = jsonObject.getJSONObject("Record");

                                String factoryManufacturerIDResponse = object.getString("FactoryManufacturerID").trim();
                                String factoryIDResponse = object.getString("FactoryID").trim();
                                String factoryNameResponse = object.getString("FactoryName").trim();
                                String factoryLocationResponse = object.getString("FactoryLocation").trim();

                                Factory newFactory = new Factory(factoryKeyResponse, factoryManufacturerIDResponse, factoryIDResponse, factoryNameResponse, factoryLocationResponse);

                                factoriesArray.add(newFactory);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("catchError", e.toString());
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

                        Toast.makeText(getApplicationContext() ,message, Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("factoryManufacturerID", factoryManufacturerID);
                return params;
            }
        };

        stringRequest3.setRetryPolicy(MainActivity.getRetryPolicy());

        RequestQueue requestQueue3 = Volley.newRequestQueue(ManufacturerStartActivity.this);
        requestQueue3.add(stringRequest3);
    }

    public static Bitmap getManufacturerKeyBitmap() {
        return manufacturerKeyBitmap;
    }

    public static ArrayList<Product> getProductsArray() {
        return productsArray;
    }

    public static ArrayList<Factory> getFactoriesArray() {
        return factoriesArray;
    }
}
