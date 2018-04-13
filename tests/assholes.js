var soap = require('soap');

var useraccounts_url = "http://localhost:9000/UserAccounts?wsdl";
var userevents_url = "http://localhost:9000/UserEvents?wsdl";
var usernotifications_url = "http://localhost:9000/UserNotifications?wsdl";
var usermessaging_url = "http://localhost:9000/UserMessaging?wsdl";
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


temp_cookie = '{"sessionKey":"/g8RLq5MVMAIYZSylrCKduCqxw71Y0oo1iS3dDarWcwywOQBqFX0m5XfCxjqCIcOSPo\\u003d","vendorID":21,"requestFeatureID":0,"accountID":24,"vendorFeatures":{"vendor_page_images_20":{"name":"vendor_page_images_20","feature_id":10,"feature_status":"enabled"},"vendor_food_ingredients":{"name":"vendor_food_ingredients","feature_id":17,"feature_status":"enabled"},"blog":{"name":"blog","feature_id":1,"feature_status":"preview"},"food_menu":{"name":"food_menu","feature_id":4,"feature_status":"preview"},"memberships":{"name":"memberships","feature_id":2,"feature_status":"preview"},"vendor_review":{"name":"vendor_review","feature_id":9,"feature_status":"enabled"},"vendor_page_images":{"name":"vendor_page_images","feature_id":11,"feature_status":"enabled"},"drink_menu":{"name":"drink_menu","feature_id":15,"feature_status":"enabled"},"vendor_drink_ingredients":{"name":"vendor_drink_ingredients","feature_id":18,"feature_status":"enabled"},"nutritional_facts":{"name":"nutritional_facts","feature_id":20,"feature_status":"enabled"},"promotions":{"name":"promotions","feature_id":3,"feature_status":"preview"},"vendor_drink_images":{"name":"vendor_drink_images","feature_id":16,"feature_status":"enabled"},"beer_menu":{"name":"beer_menu","feature_id":5,"feature_status":"enabled"},"vendor_food_images":{"name":"vendor_food_images","feature_id":13,"feature_status":"enabled"},"beer_ingredients":{"name":"beer_ingredients","feature_id":19,"feature_status":"enabled"},"vendor_beer_images":{"name":"vendor_beer_images","feature_id":12,"feature_status":"enabled"},"events":{"name":"events","feature_id":6,"feature_status":"preview"},"vendor_event_images":{"name":"vendor_event_images","feature_id":14,"feature_status":"enabled"}}}';
asshole_cookie = '{"sessionKey":"/g8RLq5MVMAIYZSylrCKduCqxw71Y0oo1iS3dDarWcwywOQBqFX0m5XfCxjqCIcOSPo\\u003d","vendorID":47,"requestFeatureID":0,"accountID":24,"vendorFeatures":{"vendor_page_images_20":{"name":"vendor_page_images_20","feature_id":10,"feature_status":"enabled"},"vendor_food_ingredients":{"name":"vendor_food_ingredients","feature_id":17,"feature_status":"enabled"},"blog":{"name":"blog","feature_id":1,"feature_status":"preview"},"food_menu":{"name":"food_menu","feature_id":4,"feature_status":"preview"},"memberships":{"name":"memberships","feature_id":2,"feature_status":"preview"},"vendor_review":{"name":"vendor_review","feature_id":9,"feature_status":"enabled"},"vendor_page_images":{"name":"vendor_page_images","feature_id":11,"feature_status":"enabled"},"drink_menu":{"name":"drink_menu","feature_id":15,"feature_status":"enabled"},"vendor_drink_ingredients":{"name":"vendor_drink_ingredients","feature_id":18,"feature_status":"enabled"},"nutritional_facts":{"name":"nutritional_facts","feature_id":20,"feature_status":"enabled"},"promotions":{"name":"promotions","feature_id":3,"feature_status":"preview"},"vendor_drink_images":{"name":"vendor_drink_images","feature_id":16,"feature_status":"enabled"},"beer_menu":{"name":"beer_menu","feature_id":5,"feature_status":"enabled"},"vendor_food_images":{"name":"vendor_food_images","feature_id":13,"feature_status":"enabled"},"beer_ingredients":{"name":"beer_ingredients","feature_id":19,"feature_status":"enabled"},"vendor_beer_images":{"name":"vendor_beer_images","feature_id":12,"feature_status":"enabled"},"events":{"name":"events","feature_id":6,"feature_status":"preview"},"vendor_event_images":{"name":"vendor_event_images","feature_id":14,"feature_status":"enabled"}}}';

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
call_api(vendormenu_url, "loadDrinkIngredients", {
	"module": "VendorMenu",
	"command": "loadDrinkIngredients",
//	"cookie": temp_cookie
	"cookie": asshole_cookie 
});
