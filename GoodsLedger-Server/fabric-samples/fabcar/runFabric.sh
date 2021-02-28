./startFabric.sh go

echo "Entering into javascript folder"

cd javascript

echo "Deleting node_modules"

rm -rf node_modules

echo "Installing all the required node modules"

npm install

sleep 5

echo "Deleting wallet"

rm -rf wallet

echo "Enrolling admin"

node enrollAdmin.js

echo "Registering user"

node registerUser.js

echo "Starting server"

npm start
