var soap = require('soap');

var useraccounts_url = "http://localhost:9000/UserAccounts?wsdl";
var userevents_url = "http://localhost:9000/UserEvents?wsdl";
var usernotifications_url = "http://localhost:9000/UserNotifications?wsdl";
var usermessaging_url = "http://localhost:9000/UserMessaging?wsdl";
var userfavorites_url = "http://localhost:9000/UserFavorites?wsdl";
var vendoraccounts_url = "http://localhost:9000/VendorAccounts?wsdl";
var vendorblogging_url = "http://localhost:9000/VendorBlogging?wsdl";
var vendormemberships_url = "http://localhost:9000/VendorMemberships?wsdl";
var vendormenu_url = "http://localhost:9000/VendorMenu?wsdl";
var vendormedia_url = "http://localhost:9000/VendorMedia?wsdl";
var vendorevents_url = "http://localhost:9000/VendorEvents?wsdl";
var thirdpartyaccounts_url = "http://localhost:9000/ThirdPartyAccounts?wsdl";
var thirdpartymenu_url = "http://localhost:9000/ThirdPartyMenu?wsdl";
var uservendorcommunication_url = "http://localhost:9000/UserVendorCommunication?wsdl";
var userthirdpartycommunication_url = "http://localhost:9000/UserThirdPartyCommunication?wsdl";
var vendorthirdpartycommunication_url = "http://localhost:9000/VendorThirdPartyCommunication?wsdl";
var publicbrewery_url = "http://localhost:9000/PublicBrewery?wsdl";


temp_cookie = '{"sessionKey":"+3dJe362HHzFDnzfVUTXVdY3qbQVUsZUfqqyBH6xzkvIZhCEvoRGTUNciu7bXN5rfSc=","vendorID":21,"requestFeatureID":0,"accountID":349,"vendorFeatures":{"vendor_food_ingredients": {"name":"vendor_food_ingredients","feature_id":17,"feature_status":"enabled"},"nutritional_facts": {"name": "nutritional_facts", "feature_id": 20, "feature_status": "enablded"},"vendor_drink_ingredients":{"name":"vendor_drink_ingredients","feature_id":18,"feature_status":"enabled"},"beer_ingredients":{"name":"beer_ingredients","feature_id":19,"feature_status":"enabled"},"drink_menu":{"name":"drink_menu","feature_id":15,"feature_status":"enabled"},"vendor_drink_images":{"name":"vendor_drink_images","feature_id":16,"feature_status":"enabled"},"vendor_page_images":{"name":"vendor_page_images","feature_id":11,"feature_status":"enabled"},"promotions":{"name":"promotions","feature_id":3,"feature_status":"preview"},"vendor_page_images_20":{"name":"vendor_page_images_20","feature_id":10,"feature_status":"enabled"},"beer_menu":{"name":"beer_menu","feature_id":5,"feature_status":"enabled"},"vendor_food_images":{"name":"vendor_food_images","feature_id":13,"feature_status":"enabled"},"vendor_beer_images":{"name":"vendor_beer_images","feature_id":12,"feature_status":"enabled"},"blog":{"name":"blog","feature_id":1,"feature_status":"preview"},"food_menu":{"name":"food_menu","feature_id":4,"feature_status":"preview"},"memberships":{"name":"memberships","feature_id":2,"feature_status":"preview"},"events":{"name":"events","feature_id":6,"feature_status":"preview"},"vendor_review":{"name":"vendor_review","feature_id":9,"feature_status":"enabled"},"vendor_event_images":{"name":"vendor_event_images","feature_id":14,"feature_status":"enabled"}}}';

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
call_api(vendormenu_url, "deleteBeerCategory",{ "cookie": temp_cookie,
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
	"name": "Peanuts",
	"description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a nisi tortor.",
	"source": "Organic Farms, LLC.",
	"key_words": [],
	"nutritional_fact_id": 2,
	"tag_one": 7,
	"tag_two": 7,
	"tag_three": 7, 
	"tag_four": 7,
	"tag_five": 7
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
	"food_tag_id_one": 1,
	"food_tag_id_two": 1,
	"food_tag_id_three": 4,
	"food_tag_id_four": 6,
	"food_tag_id_five": 6
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
/*
call_api(vendoraccounts_url, "getDropDowns", {
	"cookie": temp_cookie
});
*/
/*
call_api(vendoraccounts_url, "vendorLogin", {
	"module": "VendorAccounts",
	"command": "vendorLogin",
	"email_address": "example_brewing@gmail.com",
	"password": "nahannielenor",
	"ip_address": "FUCK YOU"
});
*/
/*
call_api(publicbrewery_url, "loadBeerMenu", {
	"brewery_id": 21
});
*/
/*
call_api(publicbrewery_url, "loadDrinkMenu", {
	"brewery_id": 21
});
*/
/*
call_api(vendormenu_url, "createFoodCategory", {
	"module": "VendorMenu",
	"command": "createFoodCategory",
	"cookie": temp_cookie,
	"name": "YOYO2",
	"hex_color": "#ff0010"
});
*/
/*
call_api(vendormenu_url, "updateFoodCategory", {
	"module": "VendorMenu",
	"command": "updateFoodCategory",
	"cookie": temp_cookie,
	"id": 14,
	"name": "New Test Name",
	"hex_color": "#000fff"
});
*/
/*
call_api(publicbrewery_url, "loadBeerMenu", {
	"module": "PublicBrewery",
	"command": "loadBeerMenu",
	"brewery_id": 21
});
*/
/*
call_api(vendormenu_url, "createBeerCategory", {
	"module": "VendorMenu",
	"command": "createBeerCategory",
	"cookie": temp_cookie,
	"name": "B1 Change",
	"hex_color": "#ff0010"
});
*/
/*
call_api(vendormenu_url, "updateBeerCategory", {
	"module": "VendorMenu",
	"command": "updateBeerCategory",
	"id": 10,
	"cookie": temp_cookie,
	"name": "B1 Change",
	"hex_color": "#ff0010"
});
*/
/*
call_api(vendormenu_url, "createDrinkCategory", {
	"module": "VendorMenu",
	"command": "createDrinkCategory",
	"cookie": temp_cookie,
	"name": "D1",
	"hex_color": "#ff0010"
});
*/
/*
call_api(vendormenu_url, "updateDrinkCategory", {
	"module": "VendorMenu",
	"command": "updateDrinkCategory",
	"cookie": temp_cookie,
	"id": 6,
	"name": "D1 NEW",
	"hex_color": "#ff0010"
});
*/
/*
call_api(vendoraccounts_url, "checkVendorSession", {
	"module": "VendorAccounts",
	"command": "checkVendorSession",
	"session_key": temp_cookie
});
*/
/*
call_api(vendormenu_url, "uploadBeerImage", {
	"module": "VendorMenu",
	"command": "uploadBeerImage",
	"cookie": temp_cookie,
	"file_path": "three.png",
	"beer_id": 96 
});
*/
/*
call_api(vendormenu_url, "deleteBeerImage", {
	"module": "VendorMenu",
	"command": "deleteBeerImage",
	"cookie": temp_cookie,
	"id": 163
});
*/
/*
call_api(vendormenu_url, "updateBeerImages", {
	"module": "VendorMenu",
	"command": "updateBeerImages",
	"cookie": temp_cookie,
//	"image_ids": ["198", "240", "242", "197"]
	"image_ids": [240, 242, 197, 198]
});
*/
/*
call_api(vendormenu_url, "loadFoodIngredients", {
	"module": "VendorMenu",
	"command": "loadFoodIngredients",
	"cookie": temp_cookie
});
*/
/*
call_api(publicbrewery_url, "loadFoodMenu", {
	"module": "PublicBrewery",
	"command": "loadFoodMenu",
	"brewery_id": 21 
});
*/
/*
call_api(publicbrewery_url, "loadBeerMenu", {
	"module": "PublicBrewery",
	"command": "loadBeerMenu",
	"brewery_id": 21
});
*/
/*
call_api(vendormenu_url, "loadBeerIngredients", {
	"module": "VendorMenu",
	"command": "loadBeerIngredients",
	"cookie": temp_cookie
});
*/
/*
call_api(vendormenu_url, "loadDrinkIngredients", {
	"module": "VendorMenu",
	"command": "loadDrinkIngredients",
	"cookie": temp_cookie
});
*/
/*
call_api(vendormenu_url, "deleteFoodTag", {
	"module": "VendorMenu",
	"command": "deleteFoodTag",
	"cookie": temp_cookie,
	"id": 19
});
*/
/*
call_api(vendormedia_url, "createVendorPageImageGallery", {
	"module": "VendorMedia",
	"command": "createVendorPageImageGallery",
	"cookie": temp_cookie,
	"name": "testonemoretime",
	"hex_color": "#000fff"
});
*/
/*
call_api(vendormedia_url, "uploadVendorPageImage", {
	"module": "VendorMedia",
	"command": "uploadVendorPageImage",
	"cookie": temp_cookie,
	"filename": "NEWTHING",
	"gallery_id": 28,
});
*/
/*
call_api(publicbrewery_url, "loadVendorMedia", {
	"module": "PublicBrewery",
	"command": "loadVendorMedia",
	"vendor_id": 21
});
*/
/*
call_api(vendormedia_url, "loadVendorPageImageGalleryTable", {
	"module": "VendorMedia",
	"command": "loadVendorPageImageGalleryTable",
	"cookie": temp_cookie,
	"gallery_id": 4
});
*/
/*
call_api(vendormedia_url, "loadVendorPageImageGalleriesTable", {
	"module": "VendorMedia",
	"command": "loadVendorPageImageGalleriesTable",
	"cookie": temp_cookie
});
*/
/*
call_api(publicbrewery_url, "loadVendorPageImageGallery", {
	"module": "PublicBrewery",
	"command": "loadVendorPageImageGallery",
	"gallery_id": 8
});
*/
other_cookie = '{"sessionKey":"QDknGO8C9f1hqLHzdsp3DavH6WfEDY9lK9WJGg1ZjptXuIPH13Q5RFysUOJeVezAxU8\\u003d","vendorID":21,"requestFeatureID":0,"accountID":230,"vendorFeatures":{"vendor_page_images_20":{"name":"vendor_page_images_20","feature_id":10,"feature_status":"enabled"},"vendor_food_ingredients":{"name":"vendor_food_ingredients","feature_id":17,"feature_status":"enabled"},"blog":{"name":"blog","feature_id":1,"feature_status":"preview"},"food_menu":{"name":"food_menu","feature_id":4,"feature_status":"preview"},"memberships":{"name":"memberships","feature_id":2,"feature_status":"preview"},"vendor_review":{"name":"vendor_review","feature_id":9,"feature_status":"enabled"},"vendor_page_images":{"name":"vendor_page_images","feature_id":11,"feature_status":"enabled"},"drink_menu":{"name":"drink_menu","feature_id":15,"feature_status":"enabled"},"vendor_drink_ingredients":{"name":"vendor_drink_ingredients","feature_id":18,"feature_status":"enabled"},"nutritional_facts":{"name":"nutritional_facts","feature_id":20,"feature_status":"enabled"},"promotions":{"name":"promotions","feature_id":3,"feature_status":"preview"},"vendor_drink_images":{"name":"vendor_drink_images","feature_id":16,"feature_status":"enabled"},"beer_menu":{"name":"beer_menu","feature_id":5,"feature_status":"enabled"},"vendor_food_images":{"name":"vendor_food_images","feature_id":13,"feature_status":"enabled"},"beer_ingredients":{"name":"beer_ingredients","feature_id":19,"feature_status":"enabled"},"vendor_beer_images":{"name":"vendor_beer_images","feature_id":12,"feature_status":"enabled"},"events":{"name":"events","feature_id":6,"feature_status":"preview"},"vendor_event_images":{"name":"vendor_event_images","feature_id":14,"feature_status":"enabled"}}}';
/*
call_api(vendoraccounts_url, "loadVendorAccountProfile", {
	"module": "VendorAccounts",
	"command": "loadVendorAccountProfile",
//	"cookie": temp_cookie
	"cookie": other_cookie 
});
*/
/*
call_api(vendoraccounts_url, "updateVendorAccountInfo", {
	"module": "VendorAccounts",
	"command": "updateVendorAccountInfo",
	"cookie": other_cookie,
	"public_first_name": "Sam", 
	"public_last_name": "Teng Wong", 
	"about_me": "I am a hipster." 
});
*/
/*
call_api(vendoraccounts_url, "uploadVendorAccountProfilePicture", {
	"module": "VendorAccounts",
	"command": "uploadVendorAccountProfilePicture",
	"cookie": temp_cookie,
	"filename": "fuckyou" 
});
*/
/*
call_api(vendoraccounts_url, "deleteVendorAccountProfilePicture", {
	"module": "VendorAccounts",
	"command": "deleteVendorAccountProfilePicture",
	"cookie": temp_cookie
});
*/
/*
call_api(vendoraccounts_url, "registerBreweryAccount", {
	"module": "VendorAccounts",
	"command": "registerBreweryAccount",
	"official_business_name": "DOPEAF",
	"primary_contact_first_name": "DOPEAF",
	"primary_contact_last_name": "DOPEAF",
	"primary_email": "DOPEAF",
	"password": "DOPEAF12345678",
	"confirm_password": "DOPEAF12345678",
	"street_address": "DOPEAF",
	"city": "DOPEAF",
	"state": "CO",
	"zip": 90210,
});
*/
/*
call_api(vendoraccounts_url, "vendorLogin", {
	"module": "VendorAccounts",
	"command": "vendorLogin",
	"email_address": "DOPEAF",
	"password": "nahannielenor",
	"ip_address": "fuckyou",
});
*/
/*
call_api(vendoraccounts_url, "confirmBreweryAccount", {
	"module": "VendorAccounts",
	"command": "confirmBreweryAccount",
	"confirmation_code": "924d42c3-ea85-4edc-9b53-f3babc77a22e"
});
*/
/*
call_api(vendoraccounts_url, "oauthVendorAuthorize", {
	"module": "VendorAccounts",
	"command": "oauthVendorAuthorize",
	"oauth_guid": "LMFAO",
	"oauth_provider": "TWITTER"
});
*/
/*
call_api(vendoraccounts_url, "checkVendorSession", {
	"module": "VendorAccounts",
	"command": "checkVendorSession",
	"session_key": 'eaAyDnepIVzc6Pz7zK9AYWvQDJng6DamPtQj/2K4V4zxD6eF3Mo2FfCrFuV9xIAp45k=' 
});
*/
/*
call_api(useraccounts_url, "checkUserSession", {
	"module": "UserAccounts",
	"command": "checkUserSession",
	"session_key": "mIm9xy4lQtjFG3ivCjNY0FjePFLnPJTZUnADfJbi+beLIFLOmtnvMweSso0nkCWLQ24="
});
call_api(useraccounts_url, "oauthUserAuthorize", {
	"module": "UserAccounts",
	"command": "oauthUserAuthorize",
	"oauth_guid": "THING",
	"oauth_provider": "TWITTER"
});
*/
user_cookie = '{"userID":314,"requestPermissionID":0,"sessionKey":"c5pqIEcQFDBYQ9cZZVDqTQbicSls+kg7AOcQl83Ap9JSBLj5p7Nm2d8YGF+iYfhZ52k=","emailAddress":"user_google_103274899784322853769","userName":"user_google_103274899784322853769","first_name":"Enter First Name","last_name":"Enter Last Name","about_me":"NA","profile_picture":"NA","userPermissions":{"third_party_food_review":{"name":"third_party_food_review","permission_id":12},"beer_reviews":{"name":"beer_reviews","permission_id":15},"vendor_drink_review":{"name":"vendor_drink_review","permission_id":17},"event_rsvp":{"name":"event_rsvp","permission_id":9},"friends":{"name":"friends","permission_id":4},"vendor_memberships":{"name":"vendor_memberships","permission_id":10},"vendor_review":{"name":"vendor_review","permission_id":14},"vendor_food_review":{"name":"vendor_food_review","permission_id":16},"vendor_blog_comment":{"name":"vendor_blog_comment","permission_id":11},"meetup_rsvp":{"name":"meetup_rsvp","permission_id":8},"recieve_user_messages":{"name":"recieve_user_messages","permission_id":5},"event_notifications":{"name":"event_notifications","permission_id":2},"beer_notifications":{"name":"beer_notifications","permission_id":3},"organize_meetups":{"name":"organize_meetups","permission_id":7},"meetup_notifications":{"name":"meetup_notifications","permission_id":1},"send_user_messages":{"name":"send_user_messages","permission_id":6},"third_party_reviews":{"name":"third_party_reviews","permission_id":13}}}';
/*
call_api(useraccounts_url, "loadUserAccountProfile", {
	"module": "UserAccounts",
	"command": "loadUserAccountProfile",
	"cookie": user_cookie 
});
*/
/*
call_api(useraccounts_url, "updateUserAccountInfo", {
	"module": "UserAccounts",
	"command": "updateUserAccountInfo",
	"cookie": user_cookie,
	"public_first_name": "Mr.", 
	"public_last_name": "Bean", 
	"about_me": "Something clever." 
});
*/
/*
call_api(uservendorcommunication_url, "createFoodFavorite", {
	"module": "UserVendorCommunication",
	"command": "createFoodFavorite",
	"cookie": user_cookie,
	"vendor_food_id": 128
});
*/
/*
call_api(uservendorcommunication_url, "createDrinkFavorite", {
	"module": "UserVendorCommunication",
	"command": "createDrinkFavorite",
	"cookie": user_cookie,
	"vendor_drink_id": 64
});
*/
/*
call_api(uservendorcommunication_url, "createBeerFavorite", {
	"module": "UserVendorCommunication",
	"command": "createBeerFavorite",
	"cookie": user_cookie,
	"beer_id": 128
});
*/
/*
call_api(uservendorcommunication_url, "deleteFoodFavorite", {
	"module": "UserVendorCommunication",
	"command": "deleteFoodFavorite",
	"cookie": user_cookie,
	"vendor_food_id": 128
});
*/
/*
call_api(uservendorcommunication_url, "deleteDrinkFavorite", {
	"module": "UserVendorCommunication",
	"command": "deleteDrinkFavorite",
	"cookie": user_cookie,
	"vendor_drink_id": 64
});
*/
/*
call_api(uservendorcommunication_url, "deleteBeerFavorite", {
	"module": "UserVendorCommunication",
	"command": "deleteBeerFavorite",
	"cookie": user_cookie,
	"beer_id": 128
});
*/
/*
call_api(userfavorites_url, "loadDrinkFavorites", {
	"module": "UserFavoritesHandler",
	"command": "loadDrinkFavorites",
	"cookie": user_cookie
});
*/
/*
call_api(userfavorites_url, "loadFoodFavorites", {
	"module": "UserFavoritesHandler",
	"command": "loadFoodFavorites",
	"cookie": user_cookie
});
*/
/*
call_api(userfavorites_url, "loadBeerFavorites", {
	"module": "UserFavoritesHandler",
	"command": "loadBeerFavorites",
	"cookie": user_cookie
});
*/
/*
call_api(publicbrewery_url, "loadFoodReviews",{
	"module": "PublicBrewery",
	"command": "loadFoodReviews",
	"food_id": 128 
});
*/
/*
call_api(uservendorcommunication_url, "createFoodReview",{
	"module": "UserVendorCommunication",
	"command": "createFoodReview",
	"cookie": user_cookie,
	"vendor_food_id": 128,
	"content": "This is a test review"
});
*/
/*
call_api(uservendorcommunication_url, "createDrinkReview",{
	"module": "UserVendorCommunication",
	"command": "createDrinkReview",
	"cookie": user_cookie,
	"vendor_drink_id": 64,
	"content": "This is a test review"
});
*/
/*
call_api(uservendorcommunication_url, "createBeerReview",{
	"module": "UserVendorCommunication",
	"command": "createBeerReview",
	"cookie": user_cookie,
	"beer_id": 128,
	"content": "This is a test review"
});
*/
/*
call_api(uservendorcommunication_url, "deleteFoodReview",{
	"module": "UserVendorCommunication",
	"command": "deleteFoodReview",
	"cookie": user_cookie,
	"review_id": 50,
});
*/
/*
call_api(publicbrewery_url, "loadBreweryInfo",{
	"brewery_id": 21
});
*/
/*
call_api(vendoraccounts_url, "updateBreweryInfo",{
	module: "VendorAccounts",
	command: "updateBreweryInfo",
	cookie: temp_cookie,
	about_text: "This is still a bar to demo the functionality of this application. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
	brewery_friendly: [],
	brewery_has: [],
	city: "denver",
	display_name: "Example Brewing, LLC",
	fri_close: "12:00 AM",
	fri_open: "9:00 AM",
	mon_close: "10:00 PM",
	mon_open: "9:00 AM",
	public_email: "info@example_brewing.com",
	public_phone: "(303) 123-4567",
	sat_close: "10:00 PM",
	sat_open: "11:00 AM",
	short_text_description: "This restaurant hasnt entered a description yet!",
	short_type_description: "restaurant",
	state: "CO",
	street_address: "420 Brew St.",
	sun_close: "10:00 PM",
	sun_open: "11:00 AM",
	thu_close: "10:00 PM",
	thu_open: "9:00 AM",
	tue_close: "10:00 PM",
	tue_open: "9:00 AM",
	wed_close: "10:00 PM",
	wed_open: "9:00 AM",
	zip: "80403"
});
*/
call_api(vendoraccounts_url, "updateShortCode",{
	module: "VendorAccounts",
	command: "updateShortCode",
	"cookie": temp_cookie,
	"short_code": "ABC"
});
