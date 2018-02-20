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

temp_cookie = '{"sessionKey":"gWaHS6U9iBlcc/ErfzrewlbaiYm8nSrs4ivD5HIgmys+fonN+kstCPU0LPfuaj8FX1M=","vendorID":21,"requestFeatureID":0,"accountID":24,"vendorFeatures":{"vendor_food_ingredients": {"name":"vendor_food_ingredients","feature_id":17,"feature_status":"enabled"},"nutritional_facts": {"name": "nutritional_facts", "feature_id": 20, "feature_status": "enablded"},"vendor_drink_ingredients":{"name":"vendor_drink_ingredients","feature_id":18,"feature_status":"enabled"},"beer_ingredients":{"name":"beer_ingredients","feature_id":19,"feature_status":"enabled"},"drink_menu":{"name":"drink_menu","feature_id":15,"feature_status":"enabled"},"vendor_drink_images":{"name":"vendor_drink_images","feature_id":16,"feature_status":"enabled"},"vendor_page_images":{"name":"vendor_page_images","feature_id":11,"feature_status":"enabled"},"promotions":{"name":"promotions","feature_id":3,"feature_status":"preview"},"vendor_page_images_20":{"name":"vendor_page_images_20","feature_id":10,"feature_status":"enabled"},"beer_menu":{"name":"beer_menu","feature_id":5,"feature_status":"enabled"},"vendor_food_images":{"name":"vendor_food_images","feature_id":13,"feature_status":"enabled"},"vendor_beer_images":{"name":"vendor_beer_images","feature_id":12,"feature_status":"enabled"},"blog":{"name":"blog","feature_id":1,"feature_status":"preview"},"food_menu":{"name":"food_menu","feature_id":4,"feature_status":"preview"},"memberships":{"name":"memberships","feature_id":2,"feature_status":"preview"},"events":{"name":"events","feature_id":6,"feature_status":"preview"},"vendor_review":{"name":"vendor_review","feature_id":9,"feature_status":"enabled"},"vendor_event_images":{"name":"vendor_event_images","feature_id":14,"feature_status":"enabled"}}}';

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
	"name": "FOR DELETE",
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
	"id": 15,
	"name": "TEST UPDATE",
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
	"icon_enum": "a1",
	"nutritional_fact_id": 2,
	"drink_tag_id_one": 1
});
*/
/*
call_api(vendormenu_url, "deleteDrink",{
	"cookie": temp_cookie,
	"id": 15
});
*/
/*
call_api(vendormenu_url, "uploadVendorDrinkImage",{
	"cookie": temp_cookie,
	"vendor_drink_id": 1,
	"filename": "testfile.png"
});
*/
/*
call_api(publicbrewery_url, "loadDrinkMenu",{
	"brewery_id": 21
});
*/
/*
call_api(publicbrewery_url, "loadBreweryInfo",{
	"brewery_id": 21
});
*/
/*
call_api(vendormenu_url, "createBeerCategory",{
	"cookie": temp_cookie,
	"category_name": "Dark Beers",
	"hex_color": "#333"
});
*/
/*
call_api(vendormenu_url, "updateBeerCategory",{
	"cookie": temp_cookie,
	"id": 7,
	"new_category_name": "Lager Beers",
	"new_hex_color": "#ccc"
});
*/
/*
call_api(vendormenu_url, "deleteBeerCategory",{
	"cookie": temp_cookie,
	"id": 3 
});
*/
/*
call_api(vendormenu_url, "createBeer",{
	"cookie": temp_cookie,
	"name": "Test With Tags",
	"color": "23",
	"bitterness": "13",
	"abv": "17",
	"beer_style": "Brown-Ales",
	"beer_tastes": ["Malty","Hoppy"],
	"description": "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
	"price": "3.23",
	"beer_sizes": ["Pilsner","Wine","Flute","Dimpled-Mug"],
	"hop_score": "tripple",
	"beer_category_id": 6,
	"beer_tag_id_one": 4,
	"nutritional_fact_id": 2
});
*/
/*
call_api(vendormenu_url, "updateBeer",{
	"cookie": temp_cookie,
	"id": 92, // new id for test with tags.
	"name": "UPDATED Beer With Tags",
	"color": "23",
	"bitterness": "13",
	"abv": "17",
	"beer_style": "Brown-Ales",
	"beer_tastes": ["Malty","Hoppy"],
	"description": "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
	"price": "3.23",
	"beer_sizes": ["Pilsner","Wine","Flute","Dimpled-Mug"],
	"hop_score": "tripple",
	"beer_category_id": 6,
	"beer_tag_id_one": 4,
	"nutritional_fact_id": 2
});
*/
/*
call_api(vendormenu_url, "deleteBeer", {
	"cookie": temp_cookie,
	"id": 93 
});
*/
/*
call_api(publicbrewery_url, "loadBeerMenu", {
	"brewery_id": 21
});
*/
/*
call_api(vendormenu_url, "createFoodIngredient", {
	"cookie": temp_cookie,
	"name": "Delete Me",
	"description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a nisi tortor.",
	"source": "Organic Farms, LLC.",
	"key_words": ["Organic", "Gluten-Free", "Free-Range"]
});
*/
/*
call_api(vendormenu_url, "updateFoodIngredient", {
	"cookie": temp_cookie,
	"food_ingredient_id": 6,
	"name": "Normal Ham",
	"description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a nisi tortor.",
	"source": "Organic Farms, LLC.",
	"key_words": []
});
*/
/*
call_api(vendormenu_url, "deleteFoodIngredient", {
	"cookie": temp_cookie,
	"food_ingredient_id": 12
});
*/
/*
call_api(vendormenu_url, "createFoodIngredientAssociation", {
	"cookie": temp_cookie,
	"food_ingredient_id": 11,
	"vendor_food_id": 56
});
*/
/*
call_api(vendormenu_url, "deleteFoodIngredientAssociation", {
	"cookie": temp_cookie,
	"food_ingredient_id": 11,
	"vendor_food_id": 56
});
*/
/*
call_api(vendormenu_url, "createDrinkIngredient", {
	"cookie": temp_cookie,
	"name": "Boxed Expensive Water",
	"description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a nisi tortor.",
	"source": "The Expensive Company, LLC.",
	"key_words": ["Organic", "Gluten-Free", "Free-Range", "Expensive"]
});
*/
/*
call_api(vendormenu_url, "updateDrinkIngredient", {
	"cookie": temp_cookie,
	"drink_ingredient_id": 5,
	"name": "Expensive Water",
	"description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a nisi tortor.",
	"source": "Organic Farms, LLC.",
	"key_words": ["The Expensive Company"]
});
*/
/*
call_api(vendormenu_url, "deleteDrinkIngredient", {
	"cookie": temp_cookie,
	"drink_ingredient_id": 6
});
*/
/*
call_api(vendormenu_url, "createDrinkIngredientAssociation", {
	"cookie": temp_cookie,
	"drink_ingredient_id": 5,
	"vendor_drink_id": 12 
});
*/
/*
call_api(vendormenu_url, "deleteDrinkIngredientAssociation", {
	"cookie": temp_cookie,
	"drink_ingredient_id": 5,
	"vendor_drink_id": 12 
});
*/
/*
call_api(vendormenu_url, "createBeerIngredient", {
	"cookie": temp_cookie,
	"name": "Delete Me",
	"description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a nisi tortor.",
	"source": "Milk Stout Prep Company.",
	"key_words": []
});
*/
/*
call_api(vendormenu_url, "updateBeerIngredient", {
	"cookie": temp_cookie,
	"beer_ingredient_id": 6,
	"name": "Something Organic",
	"description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a nisi tortor.",
	"source": "Organic Farms, LLC.",
	"key_words": ["The Expensive Company"]
});
*/
/*
call_api(vendormenu_url, "deleteBeerIngredient", {
	"cookie": temp_cookie,
	"beer_ingredient_id": 6
});
*/
/*
call_api(vendormenu_url, "createBeerIngredientAssociation", {
	"cookie": temp_cookie,
	"beer_ingredient_id": 2,
	"beer_id": 45
});
*/
/*
call_api(vendormenu_url, "deleteBeerIngredientAssociation", {
	"cookie": temp_cookie,
	"beer_ingredient_id": 2,
	"beer_id": 45
});
*/
/*
call_api(vendormenu_url, "createNutritionalFact", {
	"cookie": temp_cookie,
	"profile_name": "Unit Test Profile",
	"serving_size": 10,
	"calories": 10, 
	"calories_from_fat": 10,
	"total_fat": 10,
	"saturated_fat": 10,
	"trans_fat": 10,
	"cholesterol": 10,
	"sodium": 10,
	"total_carbs": 10,
	"dietary_fiber": 10,
	"sugar": 10,
	"protein": 10,
	"vitamin_a": 10,
	"vitamin_b": 10,
	"vitamin_c": 10,
	"vitamin_d": 10,
	"calcium": 10,
	"iron": 10
});
*/
/*
call_api(vendormenu_url, "updateNutritionalFact", {
	"cookie": temp_cookie,
	"nutritional_fact_id": 3,
	"profile_name": "Change TEst",
	"serving_size": 9,
	"calories": 9, 
	"calories_from_fat": 9,
	"total_fat": 9,
	"saturated_fat": 9,
	"trans_fat": 9,
	"cholesterol": 9,
	"sodium": 9,
	"total_carbs": 9,
	"dietary_fiber": 9,
	"sugar": 9,
	"protein": 9,
	"vitamin_a": 9,
	"vitamin_b": 9,
	"vitamin_c": 9,
	"vitamin_d": 9,
	"calcium": 9,
	"iron":9 
});
*/
/*
call_api(vendormenu_url, "deleteNutritionalFact", {
	"cookie": temp_cookie,
	"nutritional_fact_id": 3
});
*/
/*
call_api(vendormenu_url, "associateFoodNutritionalFact", {
	"cookie": temp_cookie,
	"vendor_food_id": 41,
	"nutritional_fact_id": 2
});
*/
/*
call_api(vendormenu_url, "disassociateFoodNutritionalFact", {
	"cookie": temp_cookie,
	"vendor_food_id": 41
});
*/
/*
call_api(vendormenu_url, "associateDrinkNutritionalFact", {
	"cookie": temp_cookie,
	"vendor_drink_id": 11,
	"nutritional_fact_id": 2
});
*/
/*
call_api(vendormenu_url, "disassociateDrinkNutritionalFact", {
	"cookie": temp_cookie,
	"vendor_drink_id": 11
});
*/
/*
call_api(vendormenu_url, "associateBeerNutritionalFact", {
	"cookie": temp_cookie,
	"beer_id": 46,
	"nutritional_fact_id": 2
});
*/
/*
call_api(vendormenu_url, "disassociateBeerNutritionalFact", {
	"cookie": temp_cookie,
	"beer_id": 46
});
*/
/*
call_api(publicbrewery_url, "loadBreweryInfo",{
	"brewery_id": 21
});
*/
/*
call_api(vendormenu_url, "createFood", {
	"cookie": temp_cookie,
	"name": "FOR DELETE",
	"description": "Temp Test",
	"price": 99.99,
	"food_sizes": ["Small"],
	"food_category_id": 4,
	"nutritional_fact_id": 2,
	"food_tag_id_one": 1	
});
*/
/*
call_api(vendormenu_url, "updateFood", {
	"cookie": temp_cookie,
	"id": 68,
	"name": "UPDATE FOR DELETE",
	"description": "Temp Test",
	"price": 99.99,
	"food_sizes": ["Small"],
	"food_category_id": 4,
	"nutritional_fact_id": 2,
	"food_tag_id_one": 1	
});
*/
/*
call_api(vendormenu_url, "deleteFood", {
	"cookie": temp_cookie,
	"id": 68	
});
*/
/*
call_api(vendormenu_url, "createBeerTag", {
	"cookie": temp_cookie,
	"name": "For Delete",
	"hex_color": "#ffffff",
	"tag_type": "ingredient"
});
*/
/*
call_api(vendormenu_url, "updateBeerTag", {
	"cookie": temp_cookie,
	"id": 4,
	"new_name": "New Name",
	"new_hex_color": "#ccc",
	"new_tag_type": "ingredient"
});
*/
/*
call_api(vendormenu_url, "deleteBeerTag", {
	"cookie": temp_cookie,
	"id": 9
});
*/
/*
call_api(vendormenu_url, "createFoodTag", {
	"cookie": temp_cookie,
	"name": "For Delete",
	"hex_color": "#ffffff",
	"tag_type": "ingredient"
});
*/
/*
call_api(vendormenu_url, "updateFoodTag", {
	"cookie": temp_cookie,
	"id": 1,
	"new_name": "New Name",
	"new_hex_color": "#ccc",
	"new_tag_type": "ingredient"
});
*/
/*
call_api(vendormenu_url, "deleteFoodTag", {
	"cookie": temp_cookie,
	"id": 2
});
*/
/*
call_api(vendormenu_url, "createDrinkTag", {
	"cookie": temp_cookie,
	"name": "For Delete",
	"hex_color": "#ffffff",
	"tag_type": "ingredient"
});
*/
/*
call_api(vendormenu_url, "updateDrinkTag", {
	"cookie": temp_cookie,
	"id": 1,
	"new_name": "New Name",
	"new_hex_color": "#ccc",
	"new_tag_type": "ingredient"
});
*/
/*
call_api(vendormenu_url, "deleteDrinkTag", {
	"cookie": temp_cookie,
	"id": 2
});
*/
