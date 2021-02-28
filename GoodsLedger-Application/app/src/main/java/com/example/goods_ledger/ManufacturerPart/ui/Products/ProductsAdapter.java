package com.example.goods_ledger.ManufacturerPart.ui.Products;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.R;
import com.example.goods_ledger.Assets.Product;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private ArrayList<Product> productsArray;

    ProductsAdapter(ArrayList<Product> productsArray) {
        this.productsArray = productsArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new ProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = productsArray.get(position);

        ProductsHolder productsHolder = (ProductsHolder) holder;

        ImageView productQRCodeImageView = productsHolder.productQRCodeImageView;
        TextView productName = productsHolder.productName;
        TextView productType = productsHolder.productType;
        TextView productID = productsHolder.productID;

        productQRCodeImageView.setImageBitmap(MainActivity.getQRcodeBitmap(product.getProductKey()));
        productName.setText(product.getProductName());
        productType.setText(product.getProductType());
        productID.setText(product.getProductID());
        productsHolder.productBatchID = product.getProductBatch();
        productsHolder.productSerialinBatch = product.getProductSerialinBatch();
        productsHolder.productManufacturingLocation = product.getProductManufacturingLocation();
        productsHolder.productManufacturingDate = product.getProductManufacturingDate();
        productsHolder.productExpiryDate = product.getProductExpiryDate();
    }

    @Override
    public int getItemCount() {

        return productsArray.size();
    }

    private class ProductsHolder extends RecyclerView.ViewHolder{
        private ImageView productQRCodeImageView;
        private LinearLayout productLinearLayout;
        private TextView productName, productType, productID;
        private Dialog qrcodePopup, productInfoPopup;
        private String productBatchID, productSerialinBatch, productManufacturingLocation, productManufacturingDate, productExpiryDate;

        public ProductsHolder(@NonNull final View itemView) {
            super(itemView);

            qrcodePopup = new Dialog(itemView.getContext());
            productInfoPopup = new Dialog(itemView.getContext());

            productQRCodeImageView = itemView.findViewById(R.id.item_products_productQRCode_ImageView);
            productLinearLayout = itemView.findViewById(R.id.item_products_LinearLayout);
            productName = itemView.findViewById(R.id.item_products_productName_TextView);
            productType = itemView.findViewById(R.id.item_products_productType_TextView);
            productID = itemView.findViewById(R.id.item_products_productID_TextView);

            productQRCodeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView productQRCodeImage;

                    qrcodePopup.setContentView(R.layout.popup_qrcode_image);
                    qrcodePopup.setCanceledOnTouchOutside(true);

                    productQRCodeImage = qrcodePopup.findViewById(R.id.popup_qrcode_image_ImageView);

                    productQRCodeImage.setImageDrawable(productQRCodeImageView.getDrawable());

                    qrcodePopup.show();
                    qrcodePopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            });

            productLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

                    productQRCodeImage.setImageDrawable(productQRCodeImageView.getDrawable());
                    productIDTextView.setText(productID.getText());
                    productNameTextView.setText(productName.getText());
                    productTypeTextView.setText(productType.getText());
                    productBatchIDTextView.setText(productBatchID);
                    productSerialinBatchTextView.setText(productSerialinBatch);
                    productManufacturingLocationTextView.setText(productManufacturingLocation);
                    productManufacturingDateTextView.setText(productManufacturingDate);
                    productExpiryDateTextView.setText(productExpiryDate);

                    productInfoPopup.show();
                    productInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            });
        }
    }
}
