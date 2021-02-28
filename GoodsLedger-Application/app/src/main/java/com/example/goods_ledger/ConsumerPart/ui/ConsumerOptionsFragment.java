package com.example.goods_ledger.ConsumerPart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.example.goods_ledger.ConsumerPart.ConsumerAccountInfoActivity;
import com.example.goods_ledger.ConsumerPart.ConsumerEditAccountActivity;
import com.example.goods_ledger.LoginActivity;
import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.R;

import java.util.HashMap;
import java.util.Map;

public class ConsumerOptionsFragment extends Fragment {

    private LinearLayout accountInfoLinearLayout, editAccountLinearLayout, logoutLinearLayout;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_consumer_options, container, false);

        accountInfoLinearLayout = root.findViewById(R.id.fragment_consumer_options_accountInfo_linearLayout);
        editAccountLinearLayout = root.findViewById(R.id.fragment_consumer_options_editAccount_linearLayout);
        logoutLinearLayout = root.findViewById(R.id.fragment_consumer_options_logout_linearLayout);
        progressBar = root.findViewById(R.id.fragment_consumer_options_progressbar);

        final String accountKey = MainActivity.getSavedValues().getAccountKey();
        final String accountToken = "*#@%";

        accountInfoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ConsumerAccountInfoActivity.class));
            }
        });

        editAccountLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ConsumerEditAccountActivity.class));
            }
        });

        logoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getLinks().getURL_updateAccountToken(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                MainActivity.getSavedValues().setOwnedProductsCount("0");
                                MainActivity.getSavedValues().setQueriedProductsCount("0");
                                MainActivity.getSavedValues().setAccountToken(accountToken);

                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
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
                        params.put("accountKey", accountKey);
                        params.put("accountToken", accountToken);
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
        });

        return root;
    }
}