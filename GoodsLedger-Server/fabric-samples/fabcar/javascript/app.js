const express = require('express');
const app = express();
const dotenv = require('dotenv');

dotenv.config();

//Import Routes
const homeRoute = require('./routes/home');

app.use('/', homeRoute);
    
app.listen(3000, () => console.log('Server Up and Running'));