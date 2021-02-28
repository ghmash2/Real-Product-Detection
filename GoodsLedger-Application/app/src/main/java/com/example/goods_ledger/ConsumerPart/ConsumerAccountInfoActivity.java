package com.example.goods_ledger.ConsumerPart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goods_ledger.MainActivity;
import com.example.goods_ledger.R;

public class ConsumerAccountInfoActivity extends AppCompatActivity {

    private ImageView profilePictureImageView;
    private TextView accountTypeTextView, accountNameTextView, accountUsernameTextView, accountEmailTextView, accountPhoneNoTextView;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_account_info);

        profilePictureImageView = findViewById(R.id.activity_consumer_account_info_profilePicture_ImageView);
        accountTypeTextView = findViewById(R.id.activity_consumer_account_info_accountType_TextView);
        accountNameTextView = findViewById(R.id.activity_consumer_account_info_accountName_TextView);
        accountUsernameTextView = findViewById(R.id.activity_consumer_account_info_accountUsername_TextView);
        accountEmailTextView = findViewById(R.id.activity_consumer_account_info_accountEmail_TextView);
        accountPhoneNoTextView = findViewById(R.id.activity_consumer_account_info_accountPhoneNo_TextView);
        editButton = findViewById(R.id.activity_consumer_account_info_edit_Button);

        profilePictureImageView.setImageBitmap(ConsumerStartActivity.getAccountUserameBitmap());

        accountTypeTextView.setText(MainActivity.getSavedValues().getAccountType());
        accountNameTextView.setText(MainActivity.getSavedValues().getAccountName());
        accountUsernameTextView.setText(MainActivity.getSavedValues().getAccountUsername());
        accountEmailTextView.setText(MainActivity.getSavedValues().getAccountEmail());
        accountPhoneNoTextView.setText(MainActivity.getSavedValues().getAccountPhoneNumber());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConsumerAccountInfoActivity.this, ConsumerEditAccountActivity.class));
            }
        });
    }
}
