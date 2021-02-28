package com.example.goods_ledger.Assets;

import android.content.SharedPreferences;

public class SavedValues {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    public SavedValues(SharedPreferences sharedpreferences) {
        this.sharedpreferences = sharedpreferences;
        this.editor = sharedpreferences.edit();
    }

    public String getAccountKey() {
        return sharedpreferences.getString("ACCOUNT_KEY", "N/A");
    }

    public void setAccountKey(String accountKey) {
        editor.putString("ACCOUNT_KEY", accountKey);
        editor.apply();    }

    public String getAccountToken() {
        return sharedpreferences.getString("ACCOUNT_TOKEN", "N/A");
    }

    public void setAccountToken(String accountToken) {
        editor.putString("ACCOUNT_TOKEN", accountToken);
        editor.apply();
    }

    public String getAccountType() {
        return sharedpreferences.getString("ACCOUNT_TYPE", "N/A");
    }

    public void setAccountType(String accountType) {
        editor.putString("ACCOUNT_TYPE", accountType);
        editor.apply();
    }

    public String getAccountName() {
        return sharedpreferences.getString("ACCOUNT_NAME", "N/A");
    }

    public void setAccountName(String accountName) {
        editor.putString("ACCOUNT_NAME", accountName);
        editor.apply();
    }

    public String getAccountUsername() {
        return sharedpreferences.getString("ACCOUNT_USERNAME", "N/A");
    }

    public void setAccountUsername(String accountUsername) {
        editor.putString("ACCOUNT_USERNAME", accountUsername);
        editor.apply();
    }

    public String getAccountEmail() {
        return sharedpreferences.getString("ACCOUNT_EMAIL", "N/A");
    }

    public void setAccountEmail(String accountEmail) {
        editor.putString("ACCOUNT_EMAIL", accountEmail);
        editor.apply();
    }

    public String getAccountPhoneNumber() {
        return sharedpreferences.getString("ACCOUNT_PHONE_NUMBER", "N/A");
    }

    public void setAccountPhoneNumber(String accountPhoneNumber) {
        editor.putString("ACCOUNT_PHONE_NUMBER", accountPhoneNumber);
        editor.apply();
    }

    public String getAccountOwnerManufacturerID() {
        return sharedpreferences.getString("ACCOUNT_OWNER_MANUFACTURER_ID", "N/A");
    }

    public void setAccountOwnerManufacturerID(String accountOwnerManufacturerID) {
        editor.putString("ACCOUNT_OWNER_MANUFACTURER_ID", accountOwnerManufacturerID);
        editor.apply();
    }

    public String getManufacturerKey() {
        return sharedpreferences.getString("MANUFACTURER_KEY", "N/A");
    }

    public void setManufacturerKey(String manufacturerKey) {
        editor.putString("MANUFACTURER_KEY", manufacturerKey);
        editor.apply();
    }

    public String getManufacturerAccountID() {
        return sharedpreferences.getString("MANUFACTURER_ACCOUNT_ID", "N/A");
    }

    public void setManufacturerAccountID(String manufacturerAccountID) {
        editor.putString("MANUFACTURER_ACCOUNT_ID", manufacturerAccountID);
        editor.apply();
    }

    public String getManufacturerName() {
        return sharedpreferences.getString("MANUFACTURER_NAME", "N/A");
    }

    public void setManufacturerName(String manufacturerName) {
        editor.putString("MANUFACTURER_NAME", manufacturerName);
        editor.apply();
    }

    public String getManufacturerTradeLicenceID() {
        return sharedpreferences.getString("MANUFACTURER_TRADE_LICENCE_ID", "N/A");
    }

    public void setManufacturerTradeLicenceID(String manufacturerTradeLicenceID) {
        editor.putString("MANUFACTURER_TRADE_LICENCE_ID", manufacturerTradeLicenceID);
        editor.apply();
    }

    public String getManufacturerLocation() {
        return sharedpreferences.getString("MANUFACTURER_LOCATION", "N/A");
    }

    public void setManufacturerLocation(String manufacturerLocation) {
        editor.putString("MANUFACTURER_LOCATION", manufacturerLocation);
        editor.apply();
    }

    public String getManufacturerFoundingDate() {
        return sharedpreferences.getString("MANUFACTURER_FOUNDING_DATE", "N/A");
    }

    public void setManufacturerFoundingDate(String manufacturerFoundingDate) {
        editor.putString("MANUFACTURER_FOUNDING_DATE", manufacturerFoundingDate);
        editor.apply();
    }

    public String getFactoriesCount() {
        return sharedpreferences.getString("FACTORIES_COUNT", "0");
    }

    public void setFactoriesCount(String factoriesCount) {
        editor.putString("FACTORIES_COUNT", factoriesCount);
        editor.apply();
    }

    public String getProductsCount() {
        return sharedpreferences.getString("PRODUCTS_COUNT", "0");
    }

    public void setProductsCount(String productsCount) {
        editor.putString("PRODUCTS_COUNT", productsCount);
        editor.apply();
    }

    public String getOwnedProductsCount() {
        return sharedpreferences.getString("OWNED_PRODUCTS_COUNT", "0");
    }

    public void setOwnedProductsCount(String ownedProductsCount) {
        editor.putString("OWNED_PRODUCTS_COUNT", ownedProductsCount);
        editor.apply();
    }

    public String getQueriedProductsCount() {
        return sharedpreferences.getString("QUERIED_PRODUCTS_COUNT", "0");
    }

    public void setQueriedProductsCount(String queriedProductsCount) {
        editor.putString("QUERIED_PRODUCTS_COUNT", queriedProductsCount);
        editor.apply();
    }
}
