package com.example.goods_ledger.ManufacturerPart.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.ManufacturerPart.AddManufacturerActivity;
import com.example.goods_ledger.ManufacturerPart.ManufacturerAccountInfoActivity;
import com.example.goods_ledger.ManufacturerPart.ManufacturerStartActivity;
import com.example.goods_ledger.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManufacturerHomeFragment extends Fragment {

    private ImageView profilePictureImageView;
    private TextView manufacturerNameTextView, manufacturerTradeLicenceIDTextView, manufacturerLocationTextView,
            factoryCountTextView, productsCountTextView;
    private LinearLayout manufacturerSeeMoreLinearLayout, factoriesHereLinearLayout, productsHereLinearLayout;
    private Button addFactoriesButton, addProductsButton;
    private Dialog addFactoriesPopup, addProductsPopup, profilePicturePopup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manufacturer_home, container, false);

        profilePictureImageView = root.findViewById(R.id.fragment_manufacturer_home_profilePicture_ImageView);
        manufacturerNameTextView = root.findViewById(R.id.fragment_manufacturer_home_manufacturerName_TextView);
        manufacturerTradeLicenceIDTextView = root.findViewById(R.id.fragment_manufacturer_home_manufacturerTradeLicenceID_TextView);
        manufacturerLocationTextView = root.findViewById(R.id.fragment_manufacturer_home_manufacturerLocation_TextView);
        factoryCountTextView = root.findViewById(R.id.fragment_manufacturer_home_factoryCount_TextView);
        productsCountTextView = root.findViewById(R.id.fragment_manufacturer_home_productsCount_TextView);
        manufacturerSeeMoreLinearLayout = root.findViewById(R.id.fragment_manufacturer_home_manufacturerSeeMore_LinearLayout);
        factoriesHereLinearLayout = root.findViewById(R.id.fragment_manufacturer_home_factoriesHere_LinearLayout);
        productsHereLinearLayout = root.findViewById(R.id.fragment_manufacturer_home_productsHere_LinearLayout);
        addFactoriesButton = root.findViewById(R.id.fragment_manufacturer_home_addFactories_Button);
        addProductsButton = root.findViewById(R.id.fragment_manufacturer_home_addProducts_Button);
        addFactoriesPopup = new Dialog(getActivity());
        addProductsPopup = new Dialog(getActivity());
        profilePicturePopup = new Dialog(getActivity());

        profilePictureImageView.setImageBitmap(ManufacturerStartActivity.getManufacturerKeyBitmap());

        manufacturerNameTextView.setText(MainActivity.getSavedValues().getManufacturerName());
        manufacturerTradeLicenceIDTextView.setText(MainActivity.getSavedValues().getManufacturerTradeLicenceID());
        manufacturerLocationTextView.setText(MainActivity.getSavedValues().getManufacturerLocation());
        factoryCountTextView.setText(MainActivity.getSavedValues().getFactoriesCount());
        productsCountTextView.setText(MainActivity.getSavedValues().getProductsCount());

        manufacturerSeeMoreLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManufacturerAccountInfoActivity.class));
            }
        });

        factoriesHereLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_manufacturer_home_to_navigation_manufacturer_factories);
            }
        });

        productsHereLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_manufacturer_home_to_navigation_manufacturer_products);
            }
        });

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView productQRCodeImage;

                profilePicturePopup.setContentView(R.layout.popup_qrcode_image);
                profilePicturePopup.setCanceledOnTouchOutside(true);

                productQRCodeImage = profilePicturePopup.findViewById(R.id.popup_qrcode_image_ImageView);

                productQRCodeImage.setImageDrawable(profilePictureImageView.getDrawable());

                profilePicturePopup.show();
                profilePicturePopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        addFactoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText factoryIDEditText, factoryNameEditText, factoryLocationEditText;
                Button addButton;
                final ProgressBar progressBar;

                addFactoriesPopup.setContentView(R.layout.popup_add_factories);
                addFactoriesPopup.setCanceledOnTouchOutside(true);

                factoryIDEditText = addFactoriesPopup.findViewById(R.id.popup_add_factories_factoryID_EditText);
                factoryNameEditText = addFactoriesPopup.findViewById(R.id.popup_add_factories_factoryName_EditText);
                factoryLocationEditText = addFactoriesPopup.findViewById(R.id.popup_add_factories_factoryLocation_EditText);
                addButton = addFactoriesPopup.findViewById(R.id.popup_add_factories_addFactory_Button);
                progressBar = addFactoriesPopup.findViewById(R.id.popup_add_factories_Progressbar);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String factoryManufacturerID = MainActivity.getSavedValues().getManufacturerKey();
                        final String factoryID = factoryIDEditText.getText().toString();
                        String factoryName = factoryNameEditText.getText().toString();
                        final String factoryLocation = factoryLocationEditText.getText().toString();

                        if(factoryID.isEmpty()){
                            factoryIDEditText.setError("Please type Something!");
                            return;
                        }
                        else if(factoryLocation.isEmpty()){
                            factoryLocationEditText.setError("Please type Something!");
                            return;
                        }
                        else if(factoryName.isEmpty()){
                            factoryName = MainActivity.getSavedValues().getManufacturerName();
                        }

                        final String finalFactoryName = factoryName;

                        progressBar.setVisibility(View.VISIBLE);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_addFactory(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);

                                            String factoryKeyResponse = object.getString("factoryKey").trim();
                                            String factoryManufacturerIDResponse = object.getString("factoryManufacturerID").trim();
                                            String factoryIDResponse = object.getString("factoryID").trim();
                                            String factoryNameResponse = object.getString("factoryName").trim();
                                            String factoryLocationResponse = object.getString("factoryLocation").trim();

                                            Factory newFactory = new Factory(factoryKeyResponse, factoryManufacturerIDResponse, factoryIDResponse, factoryNameResponse, factoryLocationResponse);

                                            ManufacturerStartActivity.getFactoriesArray().add(newFactory);
                                            MainActivity.getSavedValues().setFactoriesCount(Integer.toString(1 + Integer.parseInt(MainActivity.getSavedValues().getFactoriesCount())));

                                            factoryCountTextView.setText(MainActivity.getSavedValues().getFactoriesCount());
                                            Toast.makeText(getActivity(), "Factory added successfully!", Toast.LENGTH_SHORT).show();

                                            addFactoriesPopup.dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "Factory adding failed! Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(getActivity() ,message, Toast.LENGTH_LONG).show();
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("factoryManufacturerID", factoryManufacturerID);
                                params.put("factoryID", factoryID);
                                params.put("factoryName", finalFactoryName);
                                params.put("factoryLocation", factoryLocation);
                                return params;
                            }
                        };

                        stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(stringRequest);

                    }
                });

                addFactoriesPopup.show();
                addFactoriesPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        addProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText productFactoryIDEditText, productIDEditText, productNameEditText, productTypeEditText,
                        productBatchEditText, productSerialinBatchEditText, productManufacturingDateEditText,productExpiryDateEditText;
                Button addButton;
                final ProgressBar progressBar;

                addProductsPopup.setContentView(R.layout.popup_add_products);
                addProductsPopup.setCanceledOnTouchOutside(true);

                productFactoryIDEditText = addProductsPopup.findViewById(R.id.popup_add_products_productFactoryID_EditText);
                productIDEditText = addProductsPopup.findViewById(R.id.popup_add_products_productID_EditText);
                productNameEditText = addProductsPopup.findViewById(R.id.popup_add_products_productName_EditText);
                productTypeEditText = addProductsPopup.findViewById(R.id.popup_add_products_productType_EditText);
                productBatchEditText = addProductsPopup.findViewById(R.id.popup_add_products_productBatch_EditText);
                productSerialinBatchEditText = addProductsPopup.findViewById(R.id.popup_add_products_productSerialinBatch_EditText);
                productManufacturingDateEditText = addProductsPopup.findViewById(R.id.popup_add_products_productManufacturingDate_EditText);
                productExpiryDateEditText = addProductsPopup.findViewById(R.id.popup_add_products_productExpiryDate_EditText);
                addButton = addProductsPopup.findViewById(R.id.popup_add_products_addProduct_Button);
                progressBar = addProductsPopup.findViewById(R.id.popup_add_products_Progressbar);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String  productOwnerAccountID = "*#@%";
                        final String  productManufacturerID = MainActivity.getSavedValues().getManufacturerKey();
                        final String  productManufacturerName = MainActivity.getSavedValues().getManufacturerName();
                        final String  productFactoryID = productFactoryIDEditText.getText().toString();
                        final String  productID = productIDEditText.getText().toString();
                        final String  productName = productNameEditText.getText().toString();
                        final String  productType = productTypeEditText.getText().toString();
                        final String  productBatch = productBatchEditText.getText().toString();
                        final String  productSerialinBatch = productSerialinBatchEditText.getText().toString();
                        String  productManufacturingLocation = "";
                        final String  productManufacturingDate = productManufacturingDateEditText.getText().toString();
                        final String  productExpiryDate = productExpiryDateEditText.getText().toString();

                        if(productFactoryID.isEmpty()){
                            productFactoryIDEditText.setError("Please type Something!");
                            return;
                        }
                        else if(productID.isEmpty()){
                            productIDEditText.setError("Please type Something!");
                            return;
                        }
                        else if(productName.isEmpty()){
                            productNameEditText.setError("Please type Something!");
                            return;
                        }
                        else if(productType.isEmpty()){
                            productTypeEditText.setError("Please type Something!");
                            return;
                        }
                        else if(productBatch.isEmpty()){
                            productBatchEditText.setError("Please type Something!");
                            return;
                        }
                        else if(productSerialinBatch.isEmpty()){
                            productSerialinBatchEditText.setError("Please type Something!");
                            return;
                        }
                        else if(productManufacturingDate.isEmpty()){
                            productManufacturingDateEditText.setError("Please type Something!");
                            return;
                        }
                        else if(productExpiryDate.isEmpty()){
                            productExpiryDateEditText.setError("Please type Something!");
                            return;
                        }

                        ArrayList<Factory> factoriesArray = ManufacturerStartActivity.getFactoriesArray();

                        int flag = 0;

                        for (int i = 0; i<factoriesArray.size(); i++) {
                            Factory factory = factoriesArray.get(i);

                            if (factory.getFactoryID().equals(productFactoryID)) {
                                productManufacturingLocation = factory.getFactoryLocation();
                                flag = 1;
                                break;
                            }
                        }

                        if(flag == 0){
                            Toast.makeText(getActivity(), "Factory doesn't exist", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final String finalProductManufacturingLocation = productManufacturingLocation;

                        progressBar.setVisibility(View.VISIBLE);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_addProduct(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);

                                            String productKeyResponse = object.getString("productKey").trim();
                                            String productOwnerAccountIDResponse = object.getString("productOwnerAccountID").trim();
                                            String productManufacturerIDResponse = object.getString("productManufacturerID").trim();
                                            String productManufacturerNameResponse = object.getString("productManufacturerName").trim();
                                            String productFactoryIDResponse = object.getString("productFactoryID").trim();
                                            String productIDResponse = object.getString("productID").trim();
                                            String productNameResponse = object.getString("productName").trim();
                                            String productTypeResponse = object.getString("productType").trim();
                                            String productBatchResponse = object.getString("productBatch").trim();
                                            String productSerialinBatchResponse = object.getString("productSerialinBatch").trim();
                                            String productManufacturingLocationResponse = object.getString("productManufacturingLocation").trim();
                                            String productManufacturingDateResponse = object.getString("productManufacturingDate").trim();
                                            String productExpiryDateResponse = object.getString("productExpiryDate").trim();

                                            Product newProduct = new Product(productKeyResponse, productOwnerAccountIDResponse, productManufacturerIDResponse, productManufacturerNameResponse,productFactoryIDResponse, productIDResponse,
                                                    productNameResponse, productTypeResponse, productBatchResponse, productSerialinBatchResponse, productManufacturingLocationResponse, productManufacturingDateResponse, productExpiryDateResponse);

                                            ManufacturerStartActivity.getProductsArray().add(newProduct);
                                            MainActivity.getSavedValues().setProductsCount(Integer.toString(1 + Integer.parseInt(MainActivity.getSavedValues().getProductsCount())));

                                            productsCountTextView.setText(MainActivity.getSavedValues().getProductsCount());
                                            Toast.makeText(getActivity(), "Product added successfully!", Toast.LENGTH_SHORT).show();

                                            addProductsPopup.dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "Product adding failed! Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(getActivity() ,message, Toast.LENGTH_LONG).show();
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("productOwnerAccountID", productOwnerAccountID);
                                params.put("productManufacturerID", productManufacturerID);
                                params.put("productManufacturerName", productManufacturerName);
                                params.put("productFactoryID", productFactoryID);
                                params.put("productID", productID);
                                params.put("productName", productName);
                                params.put("productType", productType);
                                params.put("productBatch", productBatch);
                                params.put("productSerialinBatch", productSerialinBatch);
                                params.put("productManufacturingLocation", finalProductManufacturingLocation);
                                params.put("productManufacturingDate", productManufacturingDate);
                                params.put("productExpiryDate", productExpiryDate);
                                return params;
                            }
                        };

                        stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(stringRequest);

                    }
                });

                addProductsPopup.show();
                addProductsPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        
        return root;
    }
}