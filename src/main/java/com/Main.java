/**
 * Created by alexanderkleinhans on 5/19/17.
 */
package com;
import com.Landing.LandingHandler;
import com.PublicSearch.PublicSearchHandler;
import com.ThirdPartyMenu.ThirdPartyMenuHandler;
import com.Trending.TrendingHandler;
import com.UserAccounts.UserAccountsHandler;
import com.Sessions.SessionsHandler;
import com.ThirdPartyAccounts.ThirdPartyAccountsHandler;
import com.UserEvents.UserEventsHandler;
import com.UserFavorites.UserFavoritesHandler;
import com.UserSocial.UserSocialHandler;
import com.UserNotifications.UserNotificationsHandler;
import com.UserThirdPartyCommunication.UserThirdPartyCommunicationHandler;
import com.VendorAccounts.VendorAccountsHandler;
import com.VendorBlogging.VendorBloggingHandler;
import com.VendorMemberships.VendorMembershipHandler;
import com.VendorMenu.VendorMenuHandler;
import com.VendorMedia.VendorMediaHandler;
import com.VendorEvents.VendorEventsHandler;
import com.UserVendorCommunication.UserVendorCommunicationHandler;
import com.VendorThirdPartyCommunication.VendorThirdPartyCommunicationHandler;
import com.PublicBrewery.PublicBreweryHandler;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * For the sake of verbosity, this API uses SOAP so that
 * paramaters being called by a NodeJS web client can have
 * a statically typed interface to call functions.
 *
 * The following systems with web methods, as described in
 * the API documentation, are the following:
 *
 *   User Systems:
 *
 *   1)   User Accounts
 *   2)   User Events
 *   3)   User Notifications
 *   4)   User Messaging
 *   4...)   User Favorites
 *
 *   Vendor Systems:
 *
 *   5)   Vendor Accounts
 *   6)   Vendor Blogging
 *   7)   Vendor Memberships
 *   8)   Vendor Menu
 *   8)   Vendor Media (actually #9, whatever)
 *
 *   Third Party Systems:
 *
 *   9)   Third Party Accounts
 *   10)  Third Party Menu
 *
 *   Communication Systems:
 *
 *   11)  User Vendor Communications
 *   12)  Vendor Third Party Communications
 *   13)  User Third Party Communications
 *
 *   Public Systems
 *
 *   14) Public Brewery
 *   15) Public Search
 */
@WebService()
public class Main {
//   @WebMethod
//   public String Testing(
//           @WebParam(name="something") String something
//   ) {
//     UserAccountsHandler userAccountsHandler = new UserAccountsHandler();
//     String return_val = userAccountsHandler.Testing(something);
//     userAccountsHandler = null;
//     return return_val;
//   }
  public static void main(String[] argv) {
    String base_url = "http://localhost:9000/";
    /**
     * Sessions
     */
    SessionsHandler sessionsHandler = new SessionsHandler();
    Endpoint.publish(
            base_url + "Sessions",
            sessionsHandler
    );
    /**
     * User Accounts
     */
    UserAccountsHandler userAccountsHandler = new UserAccountsHandler();
    Endpoint.publish(
            base_url + "UserAccounts",
            userAccountsHandler);
    /**
     * User Events
     */
    UserEventsHandler userEventsHandler = new UserEventsHandler();
    Endpoint.publish(
            base_url + "UserEvents",
            userEventsHandler);
    /**
     * User Notifications
     */
    UserNotificationsHandler userNotificationsHandler = new UserNotificationsHandler();
    Endpoint.publish(
            base_url + "UserNotifications",
            userNotificationsHandler);
    /**
     * User Messaging
     */
    UserSocialHandler userSocialHandler = new UserSocialHandler();
    Endpoint.publish(
            base_url + "UserMessaging",
            userSocialHandler);
    /**
     * User Favorites
     */
    UserFavoritesHandler userFavoritesHandler = new UserFavoritesHandler();
    Endpoint.publish(
            base_url + "UserFavorites",
            userFavoritesHandler);
    /**
     * Vendor Accounts
     */
    VendorAccountsHandler vendorAccountsHandler = new VendorAccountsHandler();
    Endpoint.publish(
            base_url + "VendorAccounts",
            vendorAccountsHandler);
    /**
     * Vendor Blogging
     */
    VendorBloggingHandler vendorBloggingHandler = new VendorBloggingHandler();
    Endpoint.publish(
            base_url + "VendorBlogging",
            vendorBloggingHandler);
    /**
     * Vendor Memberships
     */
    VendorMembershipHandler vendorMembershipHandler = new VendorMembershipHandler();
    Endpoint.publish(
            base_url + "VendorMemberships",
            vendorMembershipHandler);
    /**
     * Vendor Menu
     */
    VendorMenuHandler vendorMenuHandler = new VendorMenuHandler();
    Endpoint.publish(
            base_url + "VendorMenu",
            vendorMenuHandler);
    /**
     * Vendor Media
     */
    VendorMediaHandler vendorMediaHandler = new VendorMediaHandler();
    Endpoint.publish(
            base_url + "VendorMedia",
            vendorMediaHandler);
    /**
     * Vendor Events
     */
    VendorEventsHandler vendorEventsHandler = new VendorEventsHandler();
    Endpoint.publish(
            base_url + "VendorEvents",
            vendorEventsHandler);
    /**
     * Third Party Accounts
     */
    ThirdPartyAccountsHandler thirdPartyAccountsHandler = new ThirdPartyAccountsHandler();
    Endpoint.publish(
            base_url + "ThirdPartyAccounts",
            thirdPartyAccountsHandler);
    /**
     * Third Party Menu
     */
    ThirdPartyMenuHandler thirdPartyMenuHandler = new ThirdPartyMenuHandler();
    Endpoint.publish(
            base_url + "ThirdPartyMenu",
            thirdPartyAccountsHandler);
    /**
     * User Vendor Communication
     */
    UserVendorCommunicationHandler userVendorCommunicationHandler = new UserVendorCommunicationHandler();
    Endpoint.publish(
            base_url + "UserVendorCommunication",
            userVendorCommunicationHandler);
    /**
     * User Third Party Communication
     */
    UserThirdPartyCommunicationHandler userThirdPartyCommunicationHandler = new UserThirdPartyCommunicationHandler();
    Endpoint.publish(
            base_url + "UserThirdPartyCommunication",
            userThirdPartyCommunicationHandler);
    /**
     * Vendor Third Party Communication
     */
    VendorThirdPartyCommunicationHandler vendorThirdPartyCommunicationHandler = new VendorThirdPartyCommunicationHandler();
    Endpoint.publish(
            base_url + "VendorThirdPartyCommunication",
              vendorThirdPartyCommunicationHandler);
    /**
     * Public Brewery
     */
    PublicBreweryHandler publicBreweryHandler = new PublicBreweryHandler();
    Endpoint.publish(
            base_url + "PublicBrewery",
            publicBreweryHandler);

    /**
     * Public Search
     */
    PublicSearchHandler publicSearchHandler = new PublicSearchHandler();
    Endpoint.publish(
            base_url + "PublicSearch",
            publicSearchHandler
    );
    /**
     * Trending
     */
    TrendingHandler trendingHandler = new TrendingHandler();
    Endpoint.publish(
            base_url + "Trending",
            trendingHandler
    );
    /**
     * Landing Specific
     * note: Only applied to this application, such as landing page analytics
     * or contact forms, etc. Do not use this for other client apps!
     */
    LandingHandler landingHandler = new LandingHandler();
    Endpoint.publish(
            base_url + "Landing",
            landingHandler
    );
  }
}
