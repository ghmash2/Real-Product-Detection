package com.example.goods_ledger.ManufacturerPart.ui.Factories;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goods_ledger.Assets.Factory;
import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.ManufacturerPart.ui.Products.ProductsAdapter;
import com.example.goods_ledger.R;

import java.util.ArrayList;

public class FactoriesAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private ArrayList<Factory> factoriesArray;

    public FactoriesAdapter(ArrayList<Factory> factoriesArray) {
        this.factoriesArray = factoriesArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manufacturer_factories, parent, false);
        return new FactoriesAdapter.FactoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Factory factory = factoriesArray.get(position);

        FactoriesAdapter.FactoriesHolder factoriesHolder = (FactoriesAdapter.FactoriesHolder) holder;

        ImageView factoryQRCodeImageView = factoriesHolder.factoryQRCodeImageView;
        TextView factoryLocation = factoriesHolder.factoryLocation;
        TextView factoryName = factoriesHolder.factoryName;
        TextView factoryID = factoriesHolder.factoryID;

        factoryQRCodeImageView.setImageBitmap(MainActivity.getQRcodeBitmap(factory.getFactoryKey()));
        factoryLocation.setText(factory.getFactoryLocation());
        factoryName.setText(factory.getFactoryName());
        factoryID.setText(factory.getFactoryID());
        factoriesHolder.factoryManufacturerName = MainActivity.getSavedValues().getManufacturerName();
    }

    @Override
    public int getItemCount() {

        return factoriesArray.size();
    }

    private class FactoriesHolder extends RecyclerView.ViewHolder{
        private ImageView factoryQRCodeImageView;
        private LinearLayout factoryLinearLayout;
        private TextView factoryLocation, factoryName, factoryID;
        private Dialog qrcodePopup, factoryInfoPopup;
        private String factoryManufacturerName;

        public FactoriesHolder(@NonNull final View itemView) {
            super(itemView);

            qrcodePopup = new Dialog(itemView.getContext());
            factoryInfoPopup = new Dialog(itemView.getContext());

            factoryQRCodeImageView = itemView.findViewById(R.id.item_manufacturer_factories_factoryQRCode_ImageView);
            factoryLinearLayout = itemView.findViewById(R.id.item_manufacturer_factories_LinearLayout);
            factoryLocation = itemView.findViewById(R.id.item_manufacturer_factories_factoryLocation_TextView);
            factoryName = itemView.findViewById(R.id.item_manufacturer_factories_factoryName_TextView);
            factoryID = itemView.findViewById(R.id.item_manufacturer_factories_factoryID_TextView);

            factoryQRCodeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView factoryQRCodeImage;

                    qrcodePopup.setContentView(R.layout.popup_qrcode_image);
                    qrcodePopup.setCanceledOnTouchOutside(true);

                    factoryQRCodeImage = qrcodePopup.findViewById(R.id.popup_qrcode_image_ImageView);

                    factoryQRCodeImage.setImageDrawable(factoryQRCodeImageView.getDrawable());

                    qrcodePopup.show();
                    qrcodePopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            });

            factoryLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView factoryQRCodeImage;
                    TextView factoryIDTextView, factoryNameTextView, factoryLocationTextView, factoryManufacturerNameTextView;

                    factoryInfoPopup.setContentView(R.layout.popup_factory_info);
                    factoryInfoPopup.setCanceledOnTouchOutside(true);

                    factoryQRCodeImage = factoryInfoPopup.findViewById(R.id.popup_factory_info_factoryQRCode_ImageView);
                    factoryIDTextView = factoryInfoPopup.findViewById(R.id.popup_factory_info_factoryID_TextView);
                    factoryNameTextView = factoryInfoPopup.findViewById(R.id.popup_factory_info_factoryName_TextView);
                    factoryLocationTextView = factoryInfoPopup.findViewById(R.id.popup_factory_info_factoryLocation_TextView);
                    factoryManufacturerNameTextView = factoryInfoPopup.findViewById(R.id.popup_factory_info_factoryManufacturerName_TextView);

                    factoryQRCodeImage.setImageDrawable(factoryQRCodeImageView.getDrawable());
                    factoryIDTextView.setText(factoryID.getText());
                    factoryNameTextView.setText(factoryName.getText());
                    factoryLocationTextView.setText(factoryLocation.getText());
                    factoryManufacturerNameTextView.setText(factoryManufacturerName);

                    factoryInfoPopup.show();
                    factoryInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            });
        }
    }
}
