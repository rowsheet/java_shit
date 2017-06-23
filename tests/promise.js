const sap = require('soap-as-promised');

var useraccounts_url = "http://localhost:9000/UserAccounts?wsdl";

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

var vendoraccounts_url = "http://localhost:9000/VendorAccounts?wsdl";

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
