/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * The sample smart contract for documentation topic:
 * Writing Your First Blockchain Application
 */

package main

/* Imports
* 4 utility libraries for formatting, handling bytes, reading and writing JSON, and string manipulation
* 2 specific Hyperledger Fabric specific libraries for Smart Contracts
 */
import (
	"bytes"
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	sc "github.com/hyperledger/fabric/protos/peer"
)

// Define the Smart Contract structure
type SmartContract struct {
}

// Define the car structure, with 4 properties.  Structure tags are used by encoding/json library
type Car struct {
	Make   string `json:"make"`
	Model  string `json:"model"`
	Colour string `json:"colour"`
	Owner  string `json:"owner"`
}

//Define all the necessary structures

type Account struct {
	AccountToken               string
	AccountType                string
	AccountName                string
	AccountUsername            string
	AccountEmail               string
	AccountPhoneNumber         string
	AccountPassword            string
	AccountOwnerManufacturerID string
	DocType                    string
}

type Product struct {
	ProductOwnerAccountID        string
	ProductManufacturerID        string
	ProductManufacturerName      string
	ProductFactoryID             string
	ProductID                    string
	ProductName                  string
	ProductType                  string
	ProductBatch                 string
	ProductSerialinBatch         string
	ProductManufacturingLocation string
	ProductManufacturingDate     string
	ProductExpiryDate            string
	DocType                      string
}

type Manufacturer struct {
	ManufacturerAccountID      string
	ManufacturerName           string
	ManufacturerTradeLicenceID string
	ManufacturerLocation       string
	ManufacturerFoundingDate   string
	DocType                    string
}

type Factory struct {
	FactoryManufacturerID string
	FactoryID             string
	FactoryName           string
	FactoryLocation       string
	DocType               string
}

/*
* The Init method is called when the Smart Contract "fabcar" is instantiated by the blockchain network
* Best practice is to have any Ledger initialization in separate function -- see initLedger()
 */
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	return shim.Success(nil)
}

/*
* The Invoke method is called as a result of an application request to run the Smart Contract "fabcar"
* The calling application program has also specified the particular smart contract function to be called, with arguments
 */
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {

	// Retrieve the requested Smart Contract function and arguments
	function, args := APIstub.GetFunctionAndParameters()
	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "queryCar" {
		return s.queryCar(APIstub, args)
	} else if function == "initLedger" {
		return s.initLedger(APIstub)
	} else if function == "createCar" {
		return s.createCar(APIstub, args)
	} else if function == "queryAllCars" {
		return s.queryAllCars(APIstub)
	} else if function == "changeCarOwner" {
		return s.changeCarOwner(APIstub, args)
	} else if function == "registerAccount" {
		return s.registerAccount(APIstub, args)
	} else if function == "addManufacturer" {
		return s.addManufacturer(APIstub, args)
	} else if function == "addFactory" {
		return s.addFactory(APIstub, args)
	} else if function == "addProduct" {
		return s.addProduct(APIstub, args)
	} else if function == "updateProductOwner" {
		return s.updateProductOwner(APIstub, args)
	} else if function == "updateAccountOwnerManufacturerID" {
		return s.updateAccountOwnerManufacturerID(APIstub, args)
	} else if function == "updateAccountToken" {
		return s.updateAccountToken(APIstub, args)
	} else if function == "updateAccount" {
		return s.updateAccount(APIstub, args)
	} else if function == "updateManufacturer" {
		return s.updateManufacturer(APIstub, args)
	} else if function == "updateFactory" {
		return s.updateFactory(APIstub, args)
	} else if function == "updateProduct" {
		return s.updateProduct(APIstub, args)
	} else if function == "queryAccountbyToken" {
		return s.queryAccountbyToken(APIstub, args)
	} else if function == "queryAccountbyEmail" {
		return s.queryAccountbyEmail(APIstub, args)
	} else if function == "queryAccountbyUsername" {
		return s.queryAccountbyUsername(APIstub, args)
	} else if function == "queryManufacturerbyAccountID" {
		return s.queryManufacturerbyAccountID(APIstub, args)
	} else if function == "queryManufacturerbyTradeLicenceID" {
		return s.queryManufacturerbyTradeLicenceID(APIstub, args)
	} else if function == "queryFactorybyManufacturerID" {
		return s.queryFactorybyManufacturerID(APIstub, args)
	} else if function == "queryFactorybyID" {
		return s.queryFactorybyID(APIstub, args)
	} else if function == "queryProductbyID" {
		return s.queryProductbyID(APIstub, args)
	} else if function == "queryProductbyCode" {
		return s.queryProductbyCode(APIstub, args)
	} else if function == "queryProductbyOwnerAccountID" {
		return s.queryProductbyOwnerAccountID(APIstub, args)
	} else if function == "queryProductbyManufacturerID" {
		return s.queryProductbyManufacturerID(APIstub, args)
	} else if function == "queryProductbyFactoryID" {
		return s.queryProductbyFactoryID(APIstub, args)
	}
	return shim.Error("Invalid Smart Contract function name.")
}

func (s *SmartContract) queryCar(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	carAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(carAsBytes)
}

func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {

	return shim.Success(nil)
}

func (s *SmartContract) createCar(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}

	var car = Car{Make: args[1], Model: args[2], Colour: args[3], Owner: args[4]}

	carAsBytes, _ := json.Marshal(car)
	APIstub.PutState(args[0], carAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) queryAllCars(APIstub shim.ChaincodeStubInterface) sc.Response {

	startKey := "CAR0"
	endKey := "CAR999"

	resultsIterator, err := APIstub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- queryAllCars:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func (s *SmartContract) changeCarOwner(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	carAsBytes, _ := APIstub.GetState(args[0])
	car := Car{}

	json.Unmarshal(carAsBytes, &car)
	car.Owner = args[1]

	carAsBytes, _ = json.Marshal(car)
	APIstub.PutState(args[0], carAsBytes)

	return shim.Success(nil)
}

//Start here

func (s *SmartContract) registerAccount(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 9 {
		return shim.Error("Incorrect number of arguments. Expecting 9")
	}

	var accountKey = args[0]
	var accountToken = args[1]
	var accountType = args[2]
	var accountName = args[3]
	var accountUsername = args[4]
	var accountEmail = args[5]
	var accountPassword = args[6]
	var accountOwnerManufacturerID = args[7]
	var docType = args[8]

	var account = Account{
		AccountToken:               accountToken,
		AccountType:                accountType,
		AccountName:                accountName,
		AccountUsername:            accountUsername,
		AccountEmail:               accountEmail,
		AccountPassword:            accountPassword,
		AccountOwnerManufacturerID: accountOwnerManufacturerID,
		DocType:                    docType}

	accountAsBytes, _ := json.Marshal(account)
	APIstub.PutState(accountKey, accountAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) addManufacturer(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 7 {
		return shim.Error("Incorrect number of arguments. Expecting 7")
	}

	var manufacturerKey = args[0]
	var manufacturerAccountID = args[1]
	var manufacturerName = args[2]
	var manufacturerTradeLicenceID = args[3]
	var manufacturerLocation = args[4]
	var manufacturerFoundingDate = args[5]
	var docType = args[6]

	var manufacturer = Manufacturer{
		ManufacturerAccountID:      manufacturerAccountID,
		ManufacturerName:           manufacturerName,
		ManufacturerTradeLicenceID: manufacturerTradeLicenceID,
		ManufacturerLocation:       manufacturerLocation,
		ManufacturerFoundingDate:   manufacturerFoundingDate,
		DocType:                    docType}

	manufacturerAsBytes, _ := json.Marshal(manufacturer)
	APIstub.PutState(manufacturerKey, manufacturerAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) addFactory(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 6 {
		return shim.Error("Incorrect number of arguments. Expecting 6")
	}

	var factoryKey = args[0]
	var factoryManufacturerID = args[1]
	var factoryID = args[2]
	var factoryName = args[3]
	var factoryLocation = args[4]
	var docType = args[5]

	var factory = Factory{
		FactoryManufacturerID: factoryManufacturerID,
		FactoryID:             factoryID,
		FactoryName:           factoryName,
		FactoryLocation:       factoryLocation,
		DocType:               docType}

	factoryAsBytes, _ := json.Marshal(factory)
	APIstub.PutState(factoryKey, factoryAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) addProduct(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 14 {
		return shim.Error("Incorrect number of arguments. Expecting 14")
	}

	var productKey = args[0]
	var productOwnerAccountID = args[1]
	var productManufacturerID = args[2]
	var productManufacturerName = args[3]
	var productFactoryID = args[4]
	var productID = args[5]
	var productName = args[6]
	var productType = args[7]
	var productBatch = args[8]
	var productSerialinBatch = args[9]
	var productManufacturingLocation = args[10]
	var productManufacturingDate = args[11]
	var productExpiryDate = args[12]
	var docType = args[13]

	var product = Product{
		ProductOwnerAccountID:        productOwnerAccountID,
		ProductManufacturerID:        productManufacturerID,
		ProductManufacturerName:      productManufacturerName,
		ProductFactoryID:             productFactoryID,
		ProductID:                    productID,
		ProductName:                  productName,
		ProductType:                  productType,
		ProductBatch:                 productBatch,
		ProductSerialinBatch:         productSerialinBatch,
		ProductManufacturingLocation: productManufacturingLocation,
		ProductManufacturingDate:     productManufacturingDate,
		ProductExpiryDate:            productExpiryDate,
		DocType:                      docType}

	productAsBytes, _ := json.Marshal(product)
	APIstub.PutState(productKey, productAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) updateProductOwner(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	var productKey = args[0]
	var productOwnerAccountID = args[1]

	productAsBytes, _ := APIstub.GetState(productKey)
	product := Product{}

	json.Unmarshal(productAsBytes, &product)
	product.ProductOwnerAccountID = productOwnerAccountID

	productAsBytes, _ = json.Marshal(product)
	APIstub.PutState(productKey, productAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) updateAccountOwnerManufacturerID(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	var accountKey = args[0]
	var accountOwnerManufacturerID = args[1]

	accountAsBytes, _ := APIstub.GetState(accountKey)
	account := Account{}

	json.Unmarshal(accountAsBytes, &account)
	account.AccountOwnerManufacturerID = accountOwnerManufacturerID

	accountAsBytes, _ = json.Marshal(account)
	APIstub.PutState(accountKey, accountAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) updateAccountToken(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	var accountKey = args[0]
	var accountToken = args[1]

	accountAsBytes, _ := APIstub.GetState(accountKey)
	account := Account{}

	json.Unmarshal(accountAsBytes, &account)
	account.AccountToken = accountToken

	accountAsBytes, _ = json.Marshal(account)
	APIstub.PutState(accountKey, accountAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) updateAccount(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}

	var accountKey = args[0]
	var accountToken = args[1]
	var accountName = args[2]
	var accountEmail = args[3]
	var accountPhoneNumber = args[4]

	accountAsBytes, _ := APIstub.GetState(accountKey)
	account := Account{}

	json.Unmarshal(accountAsBytes, &account)
	account.AccountToken = accountToken
	account.AccountName = accountName
	account.AccountEmail = accountEmail
	account.AccountPhoneNumber = accountPhoneNumber

	accountAsBytes, _ = json.Marshal(account)
	APIstub.PutState(accountKey, accountAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) updateManufacturer(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}

	var manufacturerKey = args[0]
	var manufacturerName = args[1]
	var manufacturerTradeLicenceID = args[2]
	var manufacturerLocation = args[3]
	var manufacturerFoundingDate = args[4]

	manufacturerAsBytes, _ := APIstub.GetState(manufacturerKey)
	manufacturer := Manufacturer{}

	json.Unmarshal(manufacturerAsBytes, &manufacturer)
	manufacturer.ManufacturerName = manufacturerName
	manufacturer.ManufacturerTradeLicenceID = manufacturerTradeLicenceID
	manufacturer.ManufacturerLocation = manufacturerLocation
	manufacturer.ManufacturerFoundingDate = manufacturerFoundingDate

	manufacturerAsBytes, _ = json.Marshal(manufacturer)
	APIstub.PutState(manufacturerKey, manufacturerAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) updateFactory(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting 4")
	}

	var factoryKey = args[0]
	var factoryManufacturerID = args[1]
	var factoryName = args[2]
	var factoryLocation = args[3]

	factoryAsBytes, _ := APIstub.GetState(factoryKey)
	factory := Factory{}

	json.Unmarshal(factoryAsBytes, &factory)
	factory.FactoryManufacturerID = factoryManufacturerID
	factory.FactoryName = factoryName
	factory.FactoryLocation = factoryLocation

	factoryAsBytes, _ = json.Marshal(factory)
	APIstub.PutState(factoryKey, factoryAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) updateProduct(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 8 {
		return shim.Error("Incorrect number of arguments. Expecting 8")
	}

	var productKey = args[0]
	var productOwnerAccountID = args[1]
	var productFactoryID = args[2]
	var productName = args[3]
	var productType = args[4]
	var productBatch = args[5]
	var productSerialinBatch = args[6]
	var productManufacturingLocation = args[7]
	var productManufacturingDate = args[8]
	var productExpiryDate = args[9]

	productAsBytes, _ := APIstub.GetState(productKey)
	product := Product{}

	json.Unmarshal(productAsBytes, &product)
	product.ProductOwnerAccountID = productOwnerAccountID
	product.ProductFactoryID = productFactoryID
	product.ProductName = productName
	product.ProductType = productType
	product.ProductBatch = productBatch
	product.ProductSerialinBatch = productSerialinBatch
	product.ProductManufacturingLocation = productManufacturingLocation
	product.ProductManufacturingDate = productManufacturingDate
	product.ProductExpiryDate = productExpiryDate

	productAsBytes, _ = json.Marshal(product)
	APIstub.PutState(productKey, productAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) queryAccountbyToken(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var accountToken = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"account\",\"AccountToken\":\"%v\"}}", accountToken)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryAccountbyEmail(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var accountEmail = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"account\",\"AccountEmail\":\"%v\"}}", accountEmail)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryAccountbyUsername(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var accountUsername = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"account\",\"AccountUsername\":\"%v\"}}", accountUsername)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryManufacturerbyAccountID(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var manufacturerAccountID = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"manufacturer\",\"ManufacturerAccountID\":\"%v\"}}", manufacturerAccountID)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryManufacturerbyTradeLicenceID(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var manufacturerTradeLicenceID = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"manufacturer\",\"ManufacturerTradeLicenceID\":\"%v\"}}", manufacturerTradeLicenceID)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryFactorybyID(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var factoryID = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"factory\",\"FactoryID\":\"%v\"}}", factoryID)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryFactorybyManufacturerID(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var factoryManufacturerID = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"factory\",\"FactoryManufacturerID\":\"%v\"}}", factoryManufacturerID)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryProductbyID(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var productID = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"product\",\"ProductID\":\"%v\"}}", productID)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryProductbyCode(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var productCode = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"product\",\"_id\":\"%v\"}}", productCode)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryProductbyOwnerAccountID(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var productOwnerAccountID = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"product\",\"ProductOwnerAccountID\":\"%v\"}}", productOwnerAccountID)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryProductbyManufacturerID(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var productManufacturerID = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"product\",\"ProductManufacturerID\":\"%v\"}}", productManufacturerID)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func (s *SmartContract) queryProductbyFactoryID(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	var productFactoryID = args[0]

	var queryString = fmt.Sprintf("{\"selector\":{\"DocType\":\"product\",\"ProductFactoryID\":\"%v\"}}", productFactoryID)

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)

	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)
}

func getQueryResultForQueryString(stub shim.ChaincodeStubInterface, queryString string) ([]byte, error) {

	resultsIterator, err := stub.GetQueryResult(queryString)
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	buffer, err := constructQueryResponseFromIterator(resultsIterator)
	if err != nil {
		return nil, err
	}

	return buffer.Bytes(), nil
}

func constructQueryResponseFromIterator(resultsIterator shim.StateQueryIteratorInterface) (*bytes.Buffer, error) {
	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	return &buffer, nil
}

// The main function is only relevant in unit test mode. Only included here for completeness.
func main() {

	// Create a new Smart Contract
	err := shim.Start(new(SmartContract))
	if err != nil {
		fmt.Printf("Error creating new Smart Contract: %s", err)
	}
}
