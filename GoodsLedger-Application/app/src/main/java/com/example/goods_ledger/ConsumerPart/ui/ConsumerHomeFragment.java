package com.example.goods_ledger.ConsumerPart.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
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
import com.example.goods_ledger.Assets.Product;
import com.example.goods_ledger.ConsumerPart.ConsumerAccountInfoActivity;
import com.example.goods_ledger.ConsumerPart.ConsumerStartActivity;
import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.ManufacturerPart.ManufacturerStartActivity;
import com.example.goods_ledger.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ConsumerHomeFragment extends Fragment{

    private ImageView profilePictureImageView;
    private TextView accountNameTextView, accountEmailTextView, ownedProductCountTextView, checkedProductsCountTextView;
    private LinearLayout accountSeeMoreLinearLayout, productsHereLinearLayout;
    private Button addProductsButton,checkProductsButton;
    private Dialog addProductsPopup, checkProductsPopup, profilePicturePopup, productInfoPopup;
    private String accountKey;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_consumer_home, container, false);

        profilePictureImageView = root.findViewById(R.id.fragment_consumer_home_profilePicture_ImageView);
        accountNameTextView = root.findViewById(R.id.fragment_consumer_home_accountName_TextView);
        accountEmailTextView = root.findViewById(R.id.fragment_consumer_home_accountEmail_TextView);
        ownedProductCountTextView = root.findViewById(R.id.fragment_consumer_home_ownedProductCount_TextView);
        checkedProductsCountTextView = root.findViewById(R.id.fragment_consumer_home_checkedProductsCount_TextView);
        accountSeeMoreLinearLayout = root.findViewById(R.id.fragment_consumer_home_accountSeeMore_LinearLayout);
        productsHereLinearLayout = root.findViewById(R.id.fragment_consumer_home_productsHere_LinearLayout);
        addProductsButton = root.findViewById(R.id.fragment_consumer_home_addProducts_Button);
        checkProductsButton = root.findViewById(R.id.fragment_consumer_home_checkProducts_Button);
        addProductsPopup = new Dialog(getActivity());
        checkProductsPopup = new Dialog(getActivity());
        profilePicturePopup = new Dialog(getActivity());
        productInfoPopup = new Dialog(getActivity());

        profilePictureImageView.setImageBitmap(ConsumerStartActivity.getAccountUserameBitmap());

        accountNameTextView.setText(MainActivity.getSavedValues().getAccountName());
        accountEmailTextView.setText(MainActivity.getSavedValues().getAccountEmail());
        ownedProductCountTextView.setText(MainActivity.getSavedValues().getOwnedProductsCount());
        checkedProductsCountTextView.setText(MainActivity.getSavedValues().getQueriedProductsCount());

        accountSeeMoreLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ConsumerAccountInfoActivity.class));
            }
        });

        accountKey = MainActivity.getSavedValues().getAccountKey();

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

        addProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout firstLinearLayout, secondLinearLayout;
                final ZXingScannerView scannerView;
                final Button cameraButton, resultButton;
                final ProgressBar progressBar;
                final ImageView resultImageView;
                final TextView firstTitleTextView, secondTitleTextView, resultTextView, infoTextView;

                addProductsPopup.setContentView(R.layout.popup_qrcode_scanner);
                addProductsPopup.setCanceledOnTouchOutside(true);

                firstLinearLayout = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_first_LinearLayout);
                secondLinearLayout = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_second_LinearLayout);
                scannerView = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_ZXingScannerView);
                cameraButton = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_camera_Button);
                resultButton = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_result_Button);
                progressBar = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_Progressbar);
                resultImageView = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_result_ImageView);
                firstTitleTextView = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_firstTitle_TextView);
                secondTitleTextView = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_secondTitle_TextView);
                resultTextView = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_result_TextView);
                infoTextView = addProductsPopup.findViewById(R.id.popup_qrcode_scanner_info_TextView);

                firstLinearLayout.setVisibility(View.VISIBLE);
                secondLinearLayout.setVisibility(View.INVISIBLE);
                scannerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                firstTitleTextView.setText("Add Product");
                secondTitleTextView.setText("Add Product");
                infoTextView.setText("In order to add a product first click the camera button below and then scan the QR code of the product with your phone's camera.\n Be sure to give camera permission before!");
                resultButton.setText("Own Product");

                addProductsPopup.show();
                addProductsPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addProductsPopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        scannerView.stopCamera();
                    }
                });

                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(getActivity(), "You must give camera permission!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        })
                        .check();

                cameraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firstLinearLayout.setVisibility(View.INVISIBLE);
                        scannerView.setVisibility(View.VISIBLE);
                        scannerView.startCamera();
                        scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                            @Override
                            public void handleResult(Result rawResult) {
                                scannerView.stopCamera();
                                scannerView.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);

                                String qrScannerResult = rawResult.getText().trim();

                                if(qrScannerResult.length() == 60){

                                    final String productCode = qrScannerResult;
                                    Log.d("Code", productCode);

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_queryProductbyCode(),
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        Log.d("Response", response);
                                                        JSONArray array = new JSONArray(response);

                                                        JSONObject jsonObject = array.getJSONObject(0);

                                                        final String productKeyResponse = jsonObject.getString("Key").trim();
                                                        JSONObject object = jsonObject.getJSONObject("Record");

                                                        final String productOwnerAccountIDResponse = object.getString("ProductOwnerAccountID").trim();
                                                        final String productManufacturerIDResponse = object.getString("ProductManufacturerID").trim();
                                                        final String productManufacturerNameResponse = object.getString("ProductManufacturerName").trim();
                                                        final String productFactoryIDResponse = object.getString("ProductFactoryID").trim();
                                                        final String productIDResponse = object.getString("ProductID").trim();
                                                        final String productNameResponse = object.getString("ProductName").trim();
                                                        final String productTypeResponse = object.getString("ProductType").trim();
                                                        final String productBatchResponse = object.getString("ProductBatch").trim();
                                                        final String productSerialinBatchResponse = object.getString("ProductSerialinBatch").trim();
                                                        final String productManufacturingLocationResponse = object.getString("ProductManufacturingLocation").trim();
                                                        final String productManufacturingDateResponse = object.getString("ProductManufacturingDate").trim();
                                                        final String productExpiryDateResponse = object.getString("ProductExpiryDate").trim();

                                                        if(productOwnerAccountIDResponse.equals(accountKey)){
                                                            String resultText = "You are already the owner of this product.";

                                                            progressBar.setVisibility(View.GONE);

                                                            resultTextView.setText(resultText);
                                                            resultImageView.setImageResource(R.drawable.real);
                                                            resultButton.setVisibility(View.INVISIBLE);

                                                            secondLinearLayout.setVisibility(View.VISIBLE);
                                                        } else if(productOwnerAccountIDResponse.equals("*#@%")){
                                                            String resultText = "The product is manufactured by " + productManufacturerNameResponse + " in " + productManufacturingLocationResponse + ".\n The product hasn't been owned by anyone yet.\n Want to own this product? Click the button below.";

                                                            progressBar.setVisibility(View.GONE);

                                                            resultTextView.setText(resultText);
                                                            resultImageView.setImageResource(R.drawable.real);
                                                            resultButton.setVisibility(View.VISIBLE);

                                                            resultButton.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    final String productOwnerAccountID = MainActivity.getSavedValues().getAccountKey();
                                                                    final String productKey = productCode;
                                                                    progressBar.setVisibility(View.VISIBLE);

                                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_updateProductOwner(),
                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject object = new JSONObject(response);

                                                                                        Product newOwnedProduct = new Product(productKeyResponse, productOwnerAccountIDResponse, productManufacturerIDResponse, productManufacturerNameResponse, productFactoryIDResponse, productIDResponse,
                                                                                                productNameResponse, productTypeResponse, productBatchResponse, productSerialinBatchResponse, productManufacturingLocationResponse, productManufacturingDateResponse, productExpiryDateResponse);


                                                                                        ConsumerStartActivity.getOwnedProductsArray().add(newOwnedProduct);
                                                                                        MainActivity.getSavedValues().setOwnedProductsCount(Integer.toString(1 + Integer.parseInt(MainActivity.getSavedValues().getOwnedProductsCount())));

                                                                                        ownedProductCountTextView.setText(MainActivity.getSavedValues().getOwnedProductsCount());

                                                                                        Toast.makeText(getActivity(), "Product owned successfully!", Toast.LENGTH_SHORT).show();

                                                                                        addProductsPopup.dismiss();
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                        progressBar.setVisibility(View.GONE);
                                                                                        Toast.makeText(getActivity(), "Product owning failed! Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                                                                        Log.d("catchError", e.toString());
                                                                                    }
                                                                                }
                                                                            },
                                                                            new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {
                                                                                    progressBar.setVisibility(View.GONE);
                                                                                    Toast.makeText(getActivity(), "Product owning failed! Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                                                    Log.d("responseError", error.toString());
                                                                                }
                                                                            })
                                                                    {
                                                                        @Override
                                                                        protected Map<String, String> getParams() {
                                                                            Map<String, String> params = new HashMap<>();
                                                                            params.put("productKey", productKey);
                                                                            params.put("productOwnerAccountID", productOwnerAccountID);
                                                                            return params;
                                                                        }
                                                                    };

                                                                    stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                                                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                                                    requestQueue.add(stringRequest);
                                                                }
                                                            });

                                                            secondLinearLayout.setVisibility(View.VISIBLE);
                                                        } else{
                                                            String resultText = "The product is manufactured by " + productManufacturerNameResponse + " in " + productManufacturingLocationResponse + ".\n It's owned by someone else!\n If you claim to own it, then first request the original owner of the product to change it's ownership.\n Otherwise, perhaps your product is a counterfeit product.";

                                                            progressBar.setVisibility(View.GONE);

                                                            resultTextView.setText(resultText);
                                                            resultImageView.setImageResource(R.drawable.caution);
                                                            resultButton.setVisibility(View.INVISIBLE);

                                                            secondLinearLayout.setVisibility(View.VISIBLE);
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.d("catchError", e.toString());

                                                        String resultText = "The product is a counterfeit product. You can't own it.";
                                                        progressBar.setVisibility(View.GONE);

                                                        resultTextView.setText(resultText);
                                                        resultImageView.setImageResource(R.drawable.counterfeit);
                                                        resultButton.setVisibility(View.INVISIBLE);

                                                        secondLinearLayout.setVisibility(View.VISIBLE);
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
                                            params.put("productCode", productCode);
                                            return params;
                                        }
                                    };

                                    stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                    requestQueue.add(stringRequest);

                                }
                                else{
                                    String resultText = "The code you scanned doesn't seem to be a valid code.";
                                    progressBar.setVisibility(View.GONE);

                                    resultTextView.setText(resultText);
                                    resultImageView.setImageResource(R.drawable.counterfeit);
                                    resultButton.setVisibility(View.INVISIBLE);

                                    secondLinearLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    }
                });
            }
        });

        checkProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout firstLinearLayout, secondLinearLayout;
                final ZXingScannerView scannerView;
                final Button cameraButton, resultButton;
                final ProgressBar progressBar;
                final ImageView resultImageView;
                final TextView firstTitleTextView, secondTitleTextView, resultTextView, infoTextView;

                checkProductsPopup.setContentView(R.layout.popup_qrcode_scanner);
                checkProductsPopup.setCanceledOnTouchOutside(true);


                firstLinearLayout = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_first_LinearLayout);
                secondLinearLayout = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_second_LinearLayout);
                scannerView = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_ZXingScannerView);
                cameraButton = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_camera_Button);
                resultButton = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_result_Button);
                progressBar = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_Progressbar);
                resultImageView = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_result_ImageView);
                firstTitleTextView = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_firstTitle_TextView);
                secondTitleTextView = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_secondTitle_TextView);
                resultTextView = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_result_TextView);
                infoTextView = checkProductsPopup.findViewById(R.id.popup_qrcode_scanner_info_TextView);

                firstLinearLayout.setVisibility(View.VISIBLE);
                secondLinearLayout.setVisibility(View.INVISIBLE);
                scannerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                firstTitleTextView.setText("Check Product");
                secondTitleTextView.setText("Check Product");
                infoTextView.setText("In order to check a product first click the camera button below and then scan the QR code of the product with your phone's camera.\n Be sure to give camera permission before!");
                resultButton.setText("See Details");

                checkProductsPopup.show();
                checkProductsPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                checkProductsPopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        scannerView.stopCamera();
                    }
                });

                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(getActivity(), "You must give camera permission!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        })
                        .check();

                cameraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firstLinearLayout.setVisibility(View.INVISIBLE);
                        scannerView.setVisibility(View.VISIBLE);
                        scannerView.startCamera();
                        scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                            @Override
                            public void handleResult(Result rawResult) {
                                scannerView.stopCamera();
                                scannerView.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);

                                String qrScannerResult = rawResult.getText().trim();

                                if(qrScannerResult.length() == 60){

                                    final String productCode = qrScannerResult;
                                    Log.d("Code", productCode);

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_queryProductbyCode(),
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        Log.d("Response", response);
                                                        JSONArray array = new JSONArray(response);

                                                        JSONObject jsonObject = array.getJSONObject(0);

                                                        final String productKeyResponse = jsonObject.getString("Key").trim();
                                                        JSONObject object = jsonObject.getJSONObject("Record");

                                                        String productOwnerAccountIDResponse = object.getString("ProductOwnerAccountID").trim();
                                                        String productManufacturerIDResponse = object.getString("ProductManufacturerID").trim();
                                                        String productManufacturerNameResponse = object.getString("ProductManufacturerName").trim();
                                                        final String productFactoryIDResponse = object.getString("ProductFactoryID").trim();
                                                        final String productIDResponse = object.getString("ProductID").trim();
                                                        final String productNameResponse = object.getString("ProductName").trim();
                                                        final String productTypeResponse = object.getString("ProductType").trim();
                                                        final String productBatchResponse = object.getString("ProductBatch").trim();
                                                        final String productSerialinBatchResponse = object.getString("ProductSerialinBatch").trim();
                                                        final String productManufacturingLocationResponse = object.getString("ProductManufacturingLocation").trim();
                                                        final String productManufacturingDateResponse = object.getString("ProductManufacturingDate").trim();
                                                        final String productExpiryDateResponse = object.getString("ProductExpiryDate").trim();

                                                        Product newQueriedProduct = new Product(productKeyResponse, productOwnerAccountIDResponse, productManufacturerIDResponse, productManufacturerNameResponse, productFactoryIDResponse, productIDResponse,
                                                                productNameResponse, productTypeResponse, productBatchResponse, productSerialinBatchResponse, productManufacturingLocationResponse, productManufacturingDateResponse, productExpiryDateResponse);

                                                        ConsumerStartActivity.getQueriedProductsArray().add(newQueriedProduct);
                                                        MainActivity.getSavedValues().setQueriedProductsCount(Integer.toString(1 + Integer.parseInt(MainActivity.getSavedValues().getQueriedProductsCount())));

                                                        checkedProductsCountTextView.setText(MainActivity.getSavedValues().getQueriedProductsCount());

                                                        resultButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                checkProductsPopup.dismiss();

                                                                ImageView productQRCodeImage;
                                                                TextView productIDTextView, productNameTextView, productTypeTextView, productBatchIDTextView, productSerialinBatchTextView,
                                                                        productManufacturingLocationTextView, productManufacturingDateTextView, productExpiryDateTextView;

                                                                productInfoPopup.setContentView(R.layout.popup_product_info);
                                                                productInfoPopup.setCanceledOnTouchOutside(true);

                                                                productQRCodeImage = productInfoPopup.findViewById(R.id.popup_product_info_productQRCode_ImageView);
                                                                productIDTextView = productInfoPopup.findViewById(R.id.popup_product_info_productID_TextView);
                                                                productNameTextView = productInfoPopup.findViewById(R.id.popup_product_info_productName_TextView);
                                                                productTypeTextView = productInfoPopup.findViewById(R.id.popup_product_info_productType_TextView);
                                                                productBatchIDTextView = productInfoPopup.findViewById(R.id.popup_product_info_productBatchID_TextView);
                                                                productSerialinBatchTextView = productInfoPopup.findViewById(R.id.popup_product_info_productSerialinBatch_TextView);
                                                                productManufacturingLocationTextView = productInfoPopup.findViewById(R.id.popup_product_info_productManufacturingLocation_TextView);
                                                                productManufacturingDateTextView = productInfoPopup.findViewById(R.id.popup_product_info_productManufacturingDate_TextView);
                                                                productExpiryDateTextView = productInfoPopup.findViewById(R.id.popup_product_info_productExpiryDate_TextView);

                                                                productQRCodeImage.setImageBitmap(MainActivity.getQRcodeBitmap(productCode));
                                                                productIDTextView.setText(productIDResponse);
                                                                productNameTextView.setText(productNameResponse);
                                                                productTypeTextView.setText(productTypeResponse);
                                                                productBatchIDTextView.setText(productBatchResponse);
                                                                productSerialinBatchTextView.setText(productSerialinBatchResponse);
                                                                productManufacturingLocationTextView.setText(productManufacturingLocationResponse);
                                                                productManufacturingDateTextView.setText(productManufacturingDateResponse);
                                                                productExpiryDateTextView.setText(productExpiryDateResponse);

                                                                productInfoPopup.show();
                                                                productInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            }
                                                        });

                                                        if(productOwnerAccountIDResponse.equals(accountKey)){
                                                            String resultText = "The product is a real product.\n It's manufactured by " + productManufacturerNameResponse + " in " + productManufacturingLocationResponse + ".\n You are the owner of this product.\n Want to see details of this product? Click the button below.";

                                                            progressBar.setVisibility(View.GONE);

                                                            resultTextView.setText(resultText);
                                                            resultImageView.setImageResource(R.drawable.real);
                                                            resultButton.setVisibility(View.VISIBLE);

                                                            secondLinearLayout.setVisibility(View.VISIBLE);
                                                        } else if(productOwnerAccountIDResponse.equals("*#@%")){
                                                            String resultText = "The product is a real product.\n It's manufactured by " + productManufacturerNameResponse + " in " + productManufacturingLocationResponse + ".\n The product hasn't been owned by anyone yet.\n Want to see details of this product? Click the button below.";

                                                            progressBar.setVisibility(View.GONE);

                                                            resultTextView.setText(resultText);
                                                            resultImageView.setImageResource(R.drawable.real);
                                                            resultButton.setVisibility(View.VISIBLE);

                                                            secondLinearLayout.setVisibility(View.VISIBLE);
                                                        } else{
                                                            String resultText = "The product is manufactured by " + productManufacturerNameResponse + " in " + productManufacturingLocationResponse + ".\n It's owned by someone else!\n If you claim to own it, then perhaps your product is a counterfeit product.";

                                                            progressBar.setVisibility(View.GONE);

                                                            resultTextView.setText(resultText);
                                                            resultImageView.setImageResource(R.drawable.counterfeit);
                                                            resultButton.setVisibility(View.INVISIBLE);

                                                            secondLinearLayout.setVisibility(View.VISIBLE);
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.d("catchError", e.toString());

                                                        String resultText = "The product is a counterfeit product.";
                                                        progressBar.setVisibility(View.GONE);

                                                        resultTextView.setText(resultText);
                                                        resultImageView.setImageResource(R.drawable.counterfeit);
                                                        resultButton.setVisibility(View.INVISIBLE);

                                                        secondLinearLayout.setVisibility(View.VISIBLE);
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
                                            params.put("productCode", productCode);
                                            return params;
                                        }
                                    };

                                    stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                    requestQueue.add(stringRequest);

                                }
                                else{
                                    String resultText = "The code you scanned doesn't seem to be a valid code.";
                                    progressBar.setVisibility(View.GONE);

                                    resultTextView.setText(resultText);
                                    resultImageView.setImageResource(R.drawable.counterfeit);
                                    resultButton.setVisibility(View.INVISIBLE);

                                    secondLinearLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    }
                });
            }
        });

        productsHereLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_consumer_home_to_navigation_consumer_products);
            }
        });

        return root;
    }
}