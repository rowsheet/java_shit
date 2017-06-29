const sap = require('soap-as-promised');

var useraccounts_url = "http://localhost:9000/UserAccounts?wsdl";
var publicbrewery_url = "http://localhost:9000/PublicBrewery?wsdl";
var vendoraccounts_url = "http://localhost:9000/VendorAccounts?wsdl";
/*
sap.createClient(publicbrewery_url)
	.then((client) => client.loadBeerMenuPaginated({
		"brewery_id": 21,
		"limit": 5,
		"offset": 0,
		"order_by": "color",
		"descending": true
	}).then(function(result) {
		console.log("OK");
		console.log(JSON.parse(result.return));
	}).catch(function(error) {
		console.log("ERROR");
		console.log(error);
	}));
*/
/*
sap.createClient(publicbrewery_url)
	.then((client) => client.loadFoodMenuPaginated({
		"brewery_id": 21,
		"limit": 5,
		"offset": 0,
		"order_by": "price",
		"descending": true
	}).then(function(result) {
		console.log("OK");
		console.log(JSON.parse(result.return));
	}).catch(function(error) {
		console.log("ERROR");
		console.log(error);
	}));
*/
/*
sap.createClient(publicbrewery_url)
	.then((client) => client.loadFoodMenu({
		"brewery_id": 21
	}).then(function(result) {
		console.log("OK");
		console.log(JSON.parse(result.return));
	}).catch(function(error) {
		console.log("ERROR");
		console.log(error);
	}));
*/
/*
sap.createClient(vendoraccounts_url)
	.then((client) => client.uploadVendorPageImages({
		"cookie": "something fucking stupid",
		"files": ["asdfasd","asdfasfd","asdfasdf"]
	}).then(function(result) {
		console.log("OK");
		console.log(JSON.parse(result.return));
	}).catch(function(error) {
		console.log("ERROR");
		console.log(error);
	}));
*/
/*
sap.createClient(useraccounts_url)
	.then((client) => client.userLogin({
		email_address: 	"new_account@gmail.com",
		password: 	"fuckyou123123",
		ip_address: 	"127.0.0.1"
	}).then(function(result) {
		console.log("OK");
		console.log(result.return);
	}).catch(function(error) {
		console.log("ERROR");
		console.log(error);
	}));
*/
/*
sap.createClient(useraccounts_url)
	.then((client) => client.checkUserSession({
		session_key: "18JomL0nkNHLlApgJXsldkfjsldkjfdZ4sOR2Avt0BTVyKwaQE4b8XcucnIZJ8Hxd531/Dne7c/5YmU="
	}).then(function(result) {
		console.log("OK");
		console.log(result.return);
	}).catch(function(error) {
		console.log("ERROR");
		console.log(error);
	}));
*/
/*
sap.createClient(vendoraccounts_url)
	.then((client) => client.checkVendorSession({
		"session_key": "M2uZHslzwn0GD+AEk5SwdHA3onepinh/LWDDZ9T/d2pbQT5oVJjueKLj6Y+uultvDbY="
	}).then(function(result) {
		console.log("OK");
		console.log(result.return);
	}).catch(function(error) {
		console.log("ERROR");
		console.log(error);
	}));
*/
/*
sap.createClient(vendoraccounts_url)
	.then((client) => client.updateBreweryInfo({
		cookie: "something",
		display_name: "something",
		about_text: "something",
		mon_open: "1:50 PM",
		mon_close: "12:45 AM",
		tue_open: "",
		tue_close: "",
		wed_open: "",
		wed_close: "",
		thu_open: "",
		thu_close: "",
		fri_open: "",
		fri_close: "",
		sat_open: "",
		sat_close: "",
		sun_open: "",
		sun_close: "",
		street_address: "",
		city: "",
		state: "",
		zip: "",
		public_phone: "",
		public_emai: ""
	}).then(function(result) {
		console.log("OK");
		console.log(result.return);
	}).catch(function(error) {
		console.log("ERROR");
		console.log(error);
	}));
*/
sap.createClient(useraccounts_url)
	.then((client) => client.confirmUserAccount({
		"confirmation_code": "8f210d97-0830-48d7-bee2-d8213ba8d08a"
	}).then(function(result) {
		console.log("OK");
		console.log(JSON.parse(result.return));
	}).catch(function(error) {
		console.log("ERROR");
		console.log(error);
	}));
