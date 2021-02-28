package com.example.goods_ledger.ConsumerPart.ui.QueriedProducts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goods_ledger.ConsumerPart.ConsumerStartActivity;
import com.example.goods_ledger.ConsumerPart.ui.OwnedProducts.OwnedProductsAdapter;
import com.example.goods_ledger.R;

public class ConsumerQueriesFragment extends Fragment {

    private RecyclerView productRecyclerView;
    private RecyclerView.Adapter productRecyclerViewAdapter;
    private RecyclerView.LayoutManager productRecyclerViewLayoutManager;

    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_products, container, false);

        context = container.getContext();

        productRecyclerView = root.findViewById(R.id.fragment_products_RecyclerView);
        productRecyclerView.setHasFixedSize(true);

        productRecyclerViewLayoutManager = new LinearLayoutManager(context);

        productRecyclerView.setLayoutManager(productRecyclerViewLayoutManager);

        productRecyclerViewAdapter = new QueriedProductsAdapter(ConsumerStartActivity.getQueriedProductsArray());
        productRecyclerView.setAdapter(productRecyclerViewAdapter);

        return root;
    }
}