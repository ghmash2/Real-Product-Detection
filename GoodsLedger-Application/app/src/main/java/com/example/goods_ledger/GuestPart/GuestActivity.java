package com.example.goods_ledger.GuestPart;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.goods_ledger.Assets.Product;
import com.example.goods_ledger.ConsumerPart.ConsumerStartActivity;
import com.example.goods_ledger.LoginActivity;
import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.R;
import com.example.goods_ledger.SignUpActivity;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class GuestActivity extends AppCompatActivity {

    private TextView logInTextView, signUpTextView;
    private Button checkProductsButton;
    private Dialog checkProductsPopup, productInfoPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        logInTextView = findViewById(R.id.guest_activity_logIn_TextView);
        signUpTextView = findViewById(R.id.guest_activity_signUp_TextView);
        checkProductsButton = findViewById(R.id.guest_activity_checkProducts_Button);
        checkProductsPopup = new Dialog(this);
        productInfoPopup = new Dialog(this);

        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuestActivity.this, LoginActivity.class));
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuestActivity.this, SignUpActivity.class));
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

                Dexter.withActivity(GuestActivity.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(GuestActivity.this, "You must give camera permission!", Toast.LENGTH_SHORT).show();
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

                                                        if(productOwnerAccountIDResponse.equals("*#@%")){
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

                                                    Toast.makeText(GuestActivity.this ,message, Toast.LENGTH_LONG).show();
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

                                    RequestQueue requestQueue = Volley.newRequestQueue(GuestActivity.this);
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
    }
}
