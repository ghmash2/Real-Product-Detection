/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { FileSystemWallet, Gateway } = require('fabric-network');
const path = require('path');
var contract = null;

const ccpPath = path.resolve(__dirname, '..', '..', '..' , 'first-network', 'connection-org1.json');

async function main() {
    try {

        // Create a new file system based wallet for managing identities.
        const walletPath = path.join(process.cwd(), 'wallet');
        const wallet = new FileSystemWallet(walletPath);
        console.log(`Wallet path: ${walletPath}`);

        // Check to see if we've already enrolled the user.
        const userExists = await wallet.exists('user1');
        if (!userExists) {
            console.log('An identity for the user "user1" does not exist in the wallet');
            console.log('Run the registerUser.js application before retrying');
            return;
        }

        // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();
        await gateway.connect(ccpPath, { wallet, identity: 'user1', discovery: { enabled: true, asLocalhost: true } });

        // Get the network (channel) our contract is deployed to.
        const network = await gateway.getNetwork('mychannel');

        // Get the contract from the network.
        contract = network.getContract('fabcar');

    } catch (error) {
        console.error(`Failed to evaluate transaction: ${error}`);
        process.exit(1);
    }
}

main();

//.......................................

const express = require('express');
const router = express.Router();
const bodyParser = require('body-parser');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

//Home
router.use(express.static(path.join(__dirname, 'views')));

router.get('/', (req, res) => {
    res.render(path.join(__dirname, 'views', 'home.ejs'));
});

router.use(bodyParser.urlencoded({ extended: true }));

router.post('/loginAccount', async (req, res) => {
    var accountUsername = String(req.body.accountUsername);

    const usernameResult1 = await contract.evaluateTransaction('queryAccountbyUsername', accountUsername);
    const usernameResult1Object = JSON.parse(usernameResult1);

    if(usernameResult1Object[0] == null){
        return res.send('Username doesn\'t exist');
    }

    const validPassword = await bcrypt.compare(String(req.body.accountPassword), usernameResult1Object[0].Record.AccountPassword);
    if(!validPassword){
        return res.send('Invalid password.');
    }

    const accountToken = jwt.sign({_id: usernameResult1Object[0].Key}, process.env.TOKEN_SECRET);

    await contract.submitTransaction('updateAccountToken', usernameResult1Object[0].Key, accountToken);

    const usernameResult2 = await contract.evaluateTransaction('queryAccountbyUsername', accountUsername);
    const usernameResult2Object = JSON.parse(usernameResult2);
    
    res.send(JSON.stringify(usernameResult2Object[0]));
});

router.post('/registerAccount', async (req, res) => {

    var accountType = String(req.body.accountType);
    var accountName = String(req.body.accountName);
    var accountUsername = String(req.body.accountUsername);
    var accountEmail = String(req.body.accountEmail);
    var accountOwnerManufacturerID = String(req.body.accountOwnerManufacturerID);

    const salt = await bcrypt.genSalt(10);
    var hashedAccountPassword = await bcrypt.hash(req.body.accountPassword, salt);
    var hashedConfirmedAccountPassword = await bcrypt.hash(req.body.accountConfirmedPassword, salt);

    const usernameResult = await contract.evaluateTransaction('queryAccountbyUsername', accountUsername);
    const usernameResultObject = JSON.parse(usernameResult);

    if(usernameResultObject[0] != null){
         return res.send('Username already exist.');
    }

    const emailResult = await contract.evaluateTransaction('queryAccountbyEmail', accountEmail);
    const emailResultObject = JSON.parse(emailResult);

    if(emailResultObject[0] != null){
         return res.send('Email already exist.');
    }

    if(hashedAccountPassword !== hashedConfirmedAccountPassword){
        return res.send('Password didn\'t match.');
    }

    var accountKeyValue = accountType + accountEmail + accountUsername;
    const newSalt = await bcrypt.genSalt(10);
    var accountKey = await bcrypt.hash(accountKeyValue, newSalt);
    var docType = "account";

    const accountToken = jwt.sign({_id: accountKey}, process.env.TOKEN_SECRET);

    hashedAccountPassword = String(hashedAccountPassword);

    await contract.submitTransaction('registerAccount', accountKey, accountToken, accountType, accountName, accountUsername, accountEmail, hashedAccountPassword, accountOwnerManufacturerID, docType);

    res.send(JSON.stringify({ accountKey, accountToken, accountType, accountName, accountUsername, accountEmail, hashedAccountPassword, accountOwnerManufacturerID, docType }));
});

router.post('/addManufacturer', async (req, res) => {
    var manufacturerAccountID = String(req.body.manufacturerAccountID);
    var manufacturerName = String(req.body.manufacturerName);    
    var manufacturerTradeLicenceID = String(req.body.manufacturerTradeLicenceID);
    var manufacturerLocation = String(req.body.manufacturerLocation);
    var manufacturerFoundingDate = String(req.body.manufacturerFoundingDate);
    var docType = "manufacturer";

    const tradeLicenceIDResult = await contract.evaluateTransaction('queryManufacturerbyTradeLicenceID', manufacturerTradeLicenceID);
    const tradeLicenceIDResultObject = JSON.parse(tradeLicenceIDResult);

    if(tradeLicenceIDResultObject[0] != null){
         return res.send('This Trade Licence belongs to someone else.');
    }

    var manufacturerKeyValue = manufacturerAccountID + manufacturerName + manufacturerTradeLicenceID;
    const salt = await bcrypt.genSalt(10);
    var manufacturerKey = await bcrypt.hash(manufacturerKeyValue, salt);

    var accountKey = manufacturerAccountID;
    var accountOwnerManufacturerID = manufacturerKey;

    await contract.submitTransaction('updateAccountOwnerManufacturerID', accountKey, accountOwnerManufacturerID);

    await contract.submitTransaction('addManufacturer', manufacturerKey, manufacturerAccountID, manufacturerName, manufacturerTradeLicenceID, manufacturerLocation, manufacturerFoundingDate, docType);

    res.send(JSON.stringify({ manufacturerKey, manufacturerAccountID, manufacturerName, manufacturerTradeLicenceID, manufacturerLocation, manufacturerFoundingDate, docType }));
});

router.post('/addFactory', async (req, res) => {
    var factoryManufacturerID = String(req.body.factoryManufacturerID);
    var factoryID = String(req.body.factoryID);
    var factoryName = String(req.body.factoryName);
    var factoryLocation = String(req.body.factoryLocation);
    var docType = "factory";

    var factoryKeyValue = factoryManufacturerID + factoryID + factoryName;
    const salt = await bcrypt.genSalt(10);
    var factoryKey = await bcrypt.hash(factoryKeyValue, salt);

    await contract.submitTransaction('addFactory', factoryKey, factoryManufacturerID, factoryID, factoryName, factoryLocation, docType);

    res.send(JSON.stringify({ factoryKey, factoryManufacturerID, factoryID, factoryName, factoryLocation, docType }));
});

router.post('/addProduct', async (req, res) => {
    var productOwnerAccountID = String(req.body.productOwnerAccountID);
    var productManufacturerID = String(req.body.productManufacturerID);
    var productManufacturerName = String(req.body.productManufacturerName);
    var productFactoryID = String(req.body.productFactoryID);
    var productID = String(req.body.productID);
    var productName = String(req.body.productName);
    var productType = String(req.body.productType);
    var productBatch = String(req.body.productBatch);
    var productSerialinBatch = String(req.body.productSerialinBatch);
    var productManufacturingLocation = String(req.body.productManufacturingLocation);
    var productManufacturingDate = String(req.body.productManufacturingDate);
    var productExpiryDate = String(req.body.productExpiryDate);
    var docType = "product"

    var productKeyValue = productManufacturerID + productFactoryID + productBatch + productID + productSerialinBatch;
    const salt = await bcrypt.genSalt(10);
    var productKey = await bcrypt.hash(productKeyValue, salt);

    await contract.submitTransaction('addProduct', productKey, productOwnerAccountID, productManufacturerID, productManufacturerName, productFactoryID, productID, productName, productType, productBatch, productSerialinBatch, productManufacturingLocation, productManufacturingDate, productExpiryDate, docType);

    res.send(JSON.stringify({ productKey, productOwnerAccountID, productManufacturerID, productManufacturerName, productFactoryID, productID, productName, productType, productBatch, productSerialinBatch, productManufacturingLocation, productManufacturingDate, productExpiryDate, docType }));
});

router.post('/updateProductOwner', async (req, res) => {
    var productKey = String(req.body.productKey);
    var productOwnerAccountID = String(req.body.productOwnerAccountID);

    await contract.submitTransaction('updateProductOwner', productKey, productOwnerAccountID);

    res.send(JSON.stringify({ productKey, productOwnerAccountID }));
});

router.post('/updateAccountToken', async (req, res) => {
    var accountKey = String(req.body.accountKey);
    var accountToken = String(req.body.accountToken);

    await contract.submitTransaction('updateAccountToken', accountKey, accountToken);

    res.send(JSON.stringify({ accountKey, accountToken }));
});

router.post('/updateAccount', async (req, res) => {
    var accountKey = String(req.body.accountKey);
    var accountToken = String(req.body.accountToken);
    var accountName = String(req.body.accountName);
    var accountEmail = String(req.body.accountEmail);
    var accountPhoneNumber = String(req.body.accountPhoneNumber);

    const emailResult = await contract.evaluateTransaction('queryAccountbyEmail', accountEmail);
    const emailResultObject = JSON.parse(emailResult);

    if(emailResultObject[0] != null){
         return res.send('Email already exist.');
    }

    await contract.submitTransaction('updateAccount', accountKey, accountToken, accountName, accountEmail, accountPhoneNumber);

    res.send(JSON.stringify({ accountKey, accountToken, accountName, accountEmail, accountPhoneNumber }));
});

router.post('/updateManufacturer', async (req, res) => {
    var manufacturerKey = String(req.body.manufacturerKey);
    var manufacturerName = String(req.body.manufacturerName);
    var manufacturerTradeLicenceID = String(req.body.manufacturerTradeLicenceID);
    var manufacturerLocation = String(req.body.manufacturerLocation);
    var manufacturerFoundingDate = String(req.body.manufacturerFoundingDate);

    await contract.submitTransaction('updateManufacturer', manufacturerKey, manufacturerName, manufacturerTradeLicenceID, manufacturerLocation, manufacturerFoundingDate);

    res.send(JSON.stringify({ manufacturerKey, manufacturerName, manufacturerTradeLicenceID, manufacturerLocation, manufacturerFoundingDate }));
});

router.post('/updateFactory', async (req, res) => {
    var factoryKey = String(req.body.factoryKey);
    var factoryManufacturerID = String(req.body.factoryManufacturerID);
    var factoryName = String(req.body.factoryName);
    var factoryLocation = String(req.body.factoryLocation);

    await contract.submitTransaction('updateFactory', factoryKey, factoryManufacturerID, factoryName, factoryLocation);

    res.send(JSON.stringify({ factoryKey, factoryManufacturerID, factoryName, factoryLocation }));
});

router.post('/updateProduct', async (req, res) => {
    var productKey = String(req.body.productKey);
    var productOwnerAccountID = String(req.body.productOwnerAccountID);
    var productFactoryID = String(req.body.productFactoryID);
    var productName = String(req.body.productName);
    var productType = String(req.body.productType);
    var productBatch = String(req.body.productBatch);
    var productSerialinBatch = String(req.body.productSerialinBatch);
    var productManufacturingLocation = String(req.body.productManufacturingLocation);
    var productManufacturingDate = String(req.body.productManufacturingDate);
    var productExpiryDate = String(req.body.productExpiryDate);

    await contract.submitTransaction('updateProduct', productKey, productOwnerAccountID, productFactoryID, productName, productType, productBatch, productSerialinBatch, productManufacturingLocation, productManufacturingDate, productExpiryDate);

    res.send(JSON.stringify({ productKey, productOwnerAccountID, productFactoryID, productName, productType, productBatch, productSerialinBatch, productManufacturingLocation, productManufacturingDate, productExpiryDate }));
});

router.post('/queryAccountbyToken', async (req, res) => {
    var accountToken = String(req.body.accountToken);

    const result = await contract.evaluateTransaction('queryAccountbyToken', accountToken);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject[0]));
});

router.post('/queryAccountbyEmail', async (req, res) => {
    var accountEmail = String(req.body.accountEmail);

    const result = await contract.evaluateTransaction('queryAccountbyEmail', accountEmail);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject[0]));});

router.post('/queryAccountbyUsername', async (req, res) => {
    var accountUsername = String(req.body.accountUsername);

    const result = await contract.evaluateTransaction('queryAccountbyUsername', accountUsername);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject[0]));
});

router.post('/queryManufacturerbyAccountID', async (req, res) => {
    var manufacturerAccountID = String(req.body.manufacturerAccountID);

    const result = await contract.evaluateTransaction('queryManufacturerbyAccountID', manufacturerAccountID);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject[0]));
});

router.post('/queryManufacturerbyTradeLicenceID', async (req, res) => {
    var manufacturerTradeLicenceID = String(req.body.manufacturerTradeLicenceID);

    const result = await contract.evaluateTransaction('queryManufacturerbyTradeLicenceID', manufacturerTradeLicenceID);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject[0]));
});

router.post('/queryFactorybyManufacturerID', async (req, res) => {
    var factoryManufacturerID = String(req.body.factoryManufacturerID);

    const result = await contract.evaluateTransaction('queryFactorybyManufacturerID', factoryManufacturerID);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject));
});

router.post('/queryFactorybyID', async (req, res) => {
    var factoryID = String(req.body.factoryID);

    const result = await contract.evaluateTransaction('queryFactorybyID', factoryID);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject));
});

router.post('/queryProductbyID', async (req, res) => {
    var productID = String(req.body.productID);

    const result = await contract.evaluateTransaction('queryProductbyID', productID);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject));
});

router.post('/queryProductbyCode', async (req, res) => {
    var productCode = String(req.body.productCode);

    const result = await contract.evaluateTransaction('queryProductbyCode', productCode);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject));
});

router.post('/queryProductbyOwnerAccountID', async (req, res) => {
    var productOwnerAccountID = String(req.body.productOwnerAccountID);

    const result = await contract.evaluateTransaction('queryProductbyOwnerAccountID', productOwnerAccountID);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject));
});

router.post('/queryProductbyManufacturerID', async (req, res) => {
    var productManufacturerID = String(req.body.productManufacturerID);

    const result = await contract.evaluateTransaction('queryProductbyManufacturerID', productManufacturerID);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject));
});

router.post('/queryProductbyFactoryID', async (req, res) => {
    var productFactoryID = String(req.body.productFactoryID);

    const result = await contract.evaluateTransaction('queryProductbyFactoryID', productFactoryID);
    const resultObject = JSON.parse(result);

    res.send(JSON.stringify(resultObject));
});

module.exports = router;