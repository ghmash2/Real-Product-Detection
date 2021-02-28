package com.example.goods_ledger.ManufacturerPart.ui.Factories;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goods_ledger.ManufacturerPart.ManufacturerStartActivity;
import com.example.goods_ledger.ManufacturerPart.ui.Products.ProductsAdapter;
import com.example.goods_ledger.R;

public class ManufacturerFactoriesFragment extends Fragment {

    private static RecyclerView factoryRecyclerView;
    private RecyclerView.Adapter factoryRecyclerViewAdapter;
    private RecyclerView.LayoutManager factoryRecyclerViewLayoutManager;

    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manufacturer_factories, container, false);

        context = container.getContext();

        factoryRecyclerView = root.findViewById(R.id.fragment_manufacturer_factories_RecyclerView);
        factoryRecyclerView.setHasFixedSize(true);

        factoryRecyclerViewLayoutManager = new LinearLayoutManager(context);

        factoryRecyclerView.setLayoutManager(factoryRecyclerViewLayoutManager);

        factoryRecyclerViewAdapter = new FactoriesAdapter(ManufacturerStartActivity.getFactoriesArray());
        factoryRecyclerView.setAdapter(factoryRecyclerViewAdapter);

        return root;
    }
}
