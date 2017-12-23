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

temp_cookie = '{"sessionKey":"gWaHS6U9iBlcc/ErfzrewlbaiYm8nSrs4ivD5HIgmys+fonN+kstCPU0LPfuaj8FX1M=","vendorID":21,"requestFeatureID":0,"accountID":24,"vendorFeatures":{"drink_menu":{"name":"drink_menu","feature_id":15,"feature_status":"enabled"},"vendor_drink_images":{"name":"vendor_drink_images","feature_id":16,"feature_status":"enabled"},"vendor_page_images":{"name":"vendor_page_images","feature_id":11,"feature_status":"enabled"},"promotions":{"name":"promotions","feature_id":3,"feature_status":"preview"},"vendor_page_images_20":{"name":"vendor_page_images_20","feature_id":10,"feature_status":"enabled"},"beer_menu":{"name":"beer_menu","feature_id":5,"feature_status":"enabled"},"vendor_food_images":{"name":"vendor_food_images","feature_id":13,"feature_status":"enabled"},"vendor_beer_images":{"name":"vendor_beer_images","feature_id":12,"feature_status":"enabled"},"blog":{"name":"blog","feature_id":1,"feature_status":"preview"},"food_menu":{"name":"food_menu","feature_id":4,"feature_status":"preview"},"memberships":{"name":"memberships","feature_id":2,"feature_status":"preview"},"events":{"name":"events","feature_id":6,"feature_status":"preview"},"vendor_review":{"name":"vendor_review","feature_id":9,"feature_status":"enabled"},"vendor_event_images":{"name":"vendor_event_images","feature_id":14,"feature_status":"enabled"}}}';

function call_api(url, method, args) {
	soap.createClient(url, function(err, client) {
		if (err) {
			console.log(err);
		} else {
			client[method](args, function(err, result) {
				if (err) {
					console.log(err);
				} else {
					console.log(JSON.stringify(JSON.parse(result.return), null, 4));
				}
			});
		}
	});
}

/*
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
*/
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
	brewery_id: 21,
	limit: 100,
	offset: 1
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
	"cookie": temp_cookie,
	"name": "AA Unit Test Event",
	"start_date": "2017-12-14",
	"end_date": "2017-12-15",
// ???	"rsvp_required": false,
	"description": "Unit test thing",
	"initial_est_occupancy": 10,
	"weekdays": ["tuesday", "wednesday", "friday"],
	"event_category_id": 1 
}
soap.createClient(vendorevents_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.createEvent(args, function(err, result) {
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
	"cookie": temp_cookie,
	"id": 40,
	"name": "AA Unit Test Event",
	"start_date": "2017-12-14",
	"end_date": "2017-12-15",
// ???	"rsvp_required": false,
	"description": "Unit test thing",
	"initial_est_occupancy": 10,
	"weekdays": ["tuesday", "wednesday", "friday", "monday"],
	"event_category_id": 6 
}
soap.createClient(vendorevents_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.updateEvent(args, function(err, result) {
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
	"cookie": temp_cookie,
	"category_name": "Unit Test Category",
	"hex_color": "#ffffff"
}
soap.createClient(vendorevents_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.createEventCategory(args, function(err, result) {
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
	"cookie": temp_cookie,
	"id": 7, 
	"new_category_name": "Change Unit Test Category",
	"new_hex_color": "#efa"
}
soap.createClient(vendorevents_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.updateEventCategory(args, function(err, result) {
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
	"cookie": temp_cookie,
	"id": 3
}
soap.createClient(vendorevents_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.deleteEventCategory(args, function(err, result) {
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
	"cookie": temp_cookie,
	"category_name": "Unit Test Category",
	"hex_color": "#eeeeee"
}
soap.createClient(vendormenu_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.createFoodCategory(args, function(err, result) {
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
	"cookie": temp_cookie,
	"id": 6,
	"new_category_name": "NEW Unit Test Category",
	"new_hex_color": "#aaa"
}
soap.createClient(vendormenu_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.updateFoodCategory(args, function(err, result) {
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
	"cookie": temp_cookie,
	"id": 6
}
soap.createClient(vendormenu_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.deleteFoodCategory(args, function(err, result) {
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
	"cookie": temp_cookie,
	"name": "Unit Test Food",
	"description": "whatever",
	"price": 99.99,
	"food_sizes": ["Snack"],
	"food_category_id": 4
}
soap.createClient(vendormenu_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.createFood(args, function(err, result) {
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
	"cookie": temp_cookie,
	"id": 50,
	"name": "NEW Unit Test Food",
	"description": "whatever",
	"price": 99.99,
	"food_sizes": ["Snack"],
	"food_category_id": 4
}
soap.createClient(vendormenu_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.updateFood(args, function(err, result) {
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
	"cookie": temp_cookie,
	"id": 50
}
soap.createClient(vendormenu_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client.deleteFood(args, function(err, result) {
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
	"brewery_id": 21
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
	"cookie": temp_cookie,
	"category_name": "UNIT TEST CATEGORY",
	"hex_color": "#ffffff"
}
soap.createClient(vendormenu_url, function(err, client) {
	if (err) {
		console.log(err);
	} else {
		client["createDrinkCategory"](args, function(err, result) {
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
call_api(vendormenu_url, "createDrinkCategory",{
	"cookie": temp_cookie,
	"category_name": "NEW CATEGORY",
	"hex_color": "#eeeeee",
	"icon_enum": "na27"
});
*/
/*
call_api(vendormenu_url, "updateDrinkCategory",{
	"cookie": temp_cookie,
	"id": 4,
	"new_category_name": "ICON CHANGE",
	"new_hex_color": "#aaa",
	"new_icon_enum": "a18"
});
*/
/*
call_api(vendormenu_url, "deleteDrinkCategory",{
	"cookie": temp_cookie,
	"id": 3
});
*/
/*
call_api(vendormenu_url, "createDrink",{
	"cookie": temp_cookie,
	"name": "Drink With Icon",
	"description": "This drink has an icon.",
	"price": 87.99,
	"drink_category_id": 2,
	"hex_one": "#123",
	"hex_two": "#234",
	"hex_three": "#345",
	"hex_background": "#eee",
	"spirit_ids": [5,6,7,8],
	"drink_serve_temp": "warm",
	"servings": "5+",
	"icon_enum": "na20"
});
*/
/*
call_api(vendormenu_url, "updateDrink",{
	"cookie": temp_cookie,
	"id": 1,
	"name": "The Expensive Juice",
	"description": "This drink is updated and dank yo.",
	"price": 99.99,
	"drink_category_id": 2,
	"hex_one": "#eee",
	"hex_two": "#eee",
	"hex_three": "#aaa",
	"hex_background": "#bbb",
	"spirit_ids": [1,2,6,8,13,14,15],
	"drink_serve_temp": "on-the-rocks",
	"servings": "2",
	"icon_enum": "a1"
});
*/
/*
call_api(vendormenu_url, "deleteDrink",{
	"cookie": temp_cookie,
	"id": 2
});
*/
/*
call_api(vendormenu_url, "uploadVendorDrinkImage",{
	"cookie": temp_cookie,
	"vendor_drink_id": 1,
	"filename": "testfile.png"
});
*/
call_api(publicbrewery_url, "loadDrinkMenu",{
	"brewery_id": 21
});
/*
call_api(publicbrewery_url, "loadBreweryInfo",{
	"brewery_id": 21
});
*/
