var soap = require('soap');

var useraccounts_url = "http://localhost:9000/UserAccounts?wsdl";
var userevents_url = "http://localhost:9000/UserEvents?wsdl";
var usernotifications_url = "http://localhost:9000/UserNotifications?wsdl";
var usermessaging_url = "http://localhost:9000/UserMessaging?wsdl";
var vendoraccounts_url = "http://localhost:9000/VendorAccounts?wsdl";
var vendorblogging_url = "http://localhost:9000/VendorBlogging?wsdl";
var vendormemberships_url = "http://localhost:9000/VendorMemberships?wsdl";
var vendormenu_url = "http://localhost:9000/VendorMenu?wsdl";
var vendorevents_url = "http://localhost:9000/VendorEvents?wsdl";
var thirdpartyaccounts_url = "http://localhost:9000/ThirdPartyAccounts?wsdl";
var thirdpartymenu_url = "http://localhost:9000/ThirdPartyMenu?wsdl";
var uservendorcommunication_url = "http://localhost:9000/UserVendorCommunication?wsdl";
var userthirdpartycommunication_url = "http://localhost:9000/UserThirdPartyCommunication?wsdl";
var vendorthirdpartycommunication_url = "http://localhost:9000/VendorThirdPartyCommunication?wsdl";
var publicbrewery_url = "http://localhost:9000/PublicBrewery?wsdl";

args = {
	brewery_id: 41 
}
soap.createClient(publicbrewery_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.loadBreweryInfo(args, function(err, result) {
			if (err) {
				console.log(err);
			} else {
				console.log(JSON.stringify(JSON.parse(result.return), null, 4));
			}
		});
	}
});
/*
args = {
	brewery_id: 21
}
soap.createClient(publicbrewery_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.loadBeerMenu(args, function(err, result) {
			if (err) {
				console.log(err);
			} else {
				console.log(result);
			}
		});
	}
});
*/
/*
args = {
	brewery_id: 21
}
soap.createClient(publicbrewery_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.loadFoodMenu(args, function(err, result) {
			if (err) {
				console.log(err);
			} else {
				console.log(JSON.stringify(JSON.parse(result.return), null, 4));
			}
		});
	}
});
*/
/*
args = {
	brewery_id: 21,
	limit: 5,
	offset: 0	
}
soap.createClient(publicbrewery_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.loadEvents(args, function(err, result) {
			if (err) {
				console.log(err);
			} else {
				console.log(JSON.stringify(JSON.parse(result.return), null, 4));
			}
		});
	}
});
*/
/*
args = {
	brewery_id: 21,
	limit: 5,
	offset: 0	
}
soap.createClient(publicbrewery_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.loadBreweryReviews(args, function(err, result) {
			if (err) {
				console.log(err);
			} else {
				console.log(JSON.stringify(JSON.parse(result.return), null, 4));
			}
		});
	}
});
*/
/*
args = {
	email_address: "Something@gmail.com",
	ip_address: "127.0.0.2"
}
soap.createClient(useraccounts_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.googleSignIn(args, function(err, result) {
			if (err) {
				console.log(err);
			} else {
				console.log(JSON.stringify(JSON.parse(result.return), null, 4));
			}
		});
	}
});
*/
/*
args = {
	email_address: "new_account@gmail.com",
	password: "fuckyou123123",
	confirm_password: "fuckyou123123",
	first_name: "First",
	last_name: "Last",
	over_21: true
}
soap.createClient(useraccounts_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.registerUserAccount(args, function(err, result) {
			if (err) {
				console.log(err);
			} else {
				console.log(JSON.stringify(JSON.parse(result.return), null, 4));
			}
		});
	}
});
*/
/*
args = {
	confirmation_code: "1bf87581-5fd3-483d-88e2-5dc5dc425dc4"
}
soap.createClient(useraccounts_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.confirmUserAccount(args, function(err, result) {
			if (err) {
				console.log(err);
			} else {
				console.log(JSON.stringify(JSON.parse(result.return), null, 4));
			}
		});
	}
});
*/
/*
args = {
	email_address: "new_account@gmail.com",
	password: "fuckyou123123",
	ip_address: "127.0.0.1"
}
soap.createClient(useraccounts_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.userLogin(args, function(err, result) {
			if (err) {
				console.log(err);
			} else {
				console.log(JSON.stringify(JSON.parse(result.return), null, 4));
			}
		});
	}
});
*/
