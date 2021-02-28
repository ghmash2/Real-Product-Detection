package com.example.goods_ledger.ManufacturerPart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.R;

public class ManufacturerAccountInfoActivity extends AppCompatActivity {

    private ImageView profilePictureImageView;
    private TextView accountTypeTextView, accountNameTextView, accountUsernameTextView,
            accountEmailTextView, accountPhoneNoTextView, manufacturerNameTextView, manufacturerTradeLicenceIDTextView;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer_account_info);

        profilePictureImageView = findViewById(R.id.activity_manufacturer_account_info_profilePicture_ImageView);
        accountTypeTextView = findViewById(R.id.activity_manufacturer_account_info_accountType_TextView);
        accountNameTextView = findViewById(R.id.activity_manufacturer_account_info_AccountName_TextView);
        accountUsernameTextView = findViewById(R.id.activity_manufacturer_account_info_AccountUsername_TextView);
        accountEmailTextView = findViewById(R.id.activity_manufacturer_account_info_AccountEmail_TextView);
        accountPhoneNoTextView = findViewById(R.id.activity_manufacturer_account_info_AccountPhoneNo_TextView);
        manufacturerNameTextView = findViewById(R.id.activity_manufacturer_account_info_manufacturerName_TextView);
        manufacturerTradeLicenceIDTextView = findViewById(R.id.activity_manufacturer_account_info_manufacturerTradeLicenceID_TextView);
        editButton = findViewById(R.id.activity_manufacturer_account_info_edit_Button);

        profilePictureImageView.setImageBitmap(ManufacturerStartActivity.getManufacturerKeyBitmap());

        accountTypeTextView.setText(MainActivity.getSavedValues().getAccountType());
        accountNameTextView.setText(MainActivity.getSavedValues().getAccountName());
        accountUsernameTextView.setText(MainActivity.getSavedValues().getAccountUsername());
        accountEmailTextView.setText(MainActivity.getSavedValues().getAccountEmail());
        accountPhoneNoTextView.setText(MainActivity.getSavedValues().getAccountPhoneNumber());
        manufacturerNameTextView.setText(MainActivity.getSavedValues().getManufacturerName());
        manufacturerTradeLicenceIDTextView.setText(MainActivity.getSavedValues().getManufacturerTradeLicenceID());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManufacturerAccountInfoActivity.this, ManufacturerEditAccountActivity.class));
            }
        });
    }
}
