**Application API**
===============

Public Functions
----------------

**Breweries**
 - Load Brewery Page
 - Load Top Beers
 - Load Beers By Style
 - Load Beer Details
 - Load Top Foods
 - Load Foods By Size
 - Load Food Details
 - Load Images
 - Load Events
 - Load Member Promotions
 - Load Event Promotions
 - Load Top Posts
 - Load Posts By Tag
 - Load People
 - Load Top Brewery Reviews
 - Load Best Brewery Reviews
 - Load Top Beer Reviews
 - Load 




User Functions
--------------

**User Accounts:**
 - Register
 - Unregister
 - *Login*
 - *Read Session*
 - *Update Session*
 - *Logout*

**User Events:**
 - Meetup
 - Meetup RSVP
 - Event RSPV

**User Notifications:**
 - Meetup Notification
 - Event Notification
 - Beer Notification

**User Social:**
 - User Association
 - User Message
 - User Message Blacklist

**User Membership:**
 - Memberships
 - Promotions

Vendor Functions:
----------------


**Vendor Accounts:**
 - *Vendor Login*
 - *Read Vendor Session*
 - *Update Vendor Session*
 - *Vendor Logout*
 - Vendor Entity
 - Vendor Account Association
 - Vendor Feature Association

**Vendor Blogging:**
 - Vendor Blog

**Vendor Memberships:**
 - Vendor Membership
 - Vendor Membership Promotion

**Vendor Menu:**
 - Beer
 - Vendor Food

**Vendor Events**
 - Vendor Events

Third Party Functions
---------------------

**Third Party Accounts:**
 - *Third Party Login*
 - *Read Third Party Session*
 - *Update Third Party Session*
 - *Third Party Logout*
 - Third Party Entity
 - Third Party Account Association
 - Third Party Feature Association

**Third Party Menu:**
 - Third Party Food
 - Third Party Food Review

User Vendor Shared Functions
----------------------------


**User Vendor Communication:**
 - Vendor Review
 - Vendor Beer Review
 - Vendor Food Review
 - Vendor Account Membership
 - Vendor Account Promotion
 - Vendor Blog Comment
 - Vendor Blog Comment Reply

Vendor Third Party Shared Functions
-----------------------------------

**Vendor Third Party Communication:**
 - Vendor Third Party Message
 - Vendor Third Party Reservation

User Third Party Shared Functions
---------------------------------

**User Third Party Communication:**
 - Third Party Review




--------------------



API
===


**user accounts**

                *register
                *unregister
                *login
                *read_session
                *update_session
                *logout

**user events**

                create_meetup
                *read_meetups
                *read_meetups_by_category
                *read_meetups_by_user
                *read_meetups_by_weekday
                *read_meetups_by_time_range
                create_meetup_rsvp
                create_event_rsvp

**user notifications**

                create_meetup_notification
                create_event_notification
                create_beer_notification

**user social**

                create_user_association
                create_user_message
                create_user_message blacklist

**user memberships**

                *request_membership
                *cancel_membership

**vendor accounts**

                *vendor_login
                *read_vendor_session
                *update_vendor_session
                *vendor_logout
                create_vendor_entity
                create_vendor_account association
                create_vendor_feature_association

**vendor blogging**

                create_vendor_blog

**vendor memberships**

                create_vendor_membership
                create_vendor_membership_promotion
                *approve_membership
                *revoke_membership

**vendor menu**

                create_beer
                create_vendor_food

**vendor events**

                create_vendor_event

**third party accounts**

                *third_party_login
                *read_third_party_session
                *update_third_party_session
                *third_party_logout
                *create_third_party entity
                *create_third_party_account_association
                *create_third_party_feature_association

**third party menu**

                *create_third_party_food
                *create_third_party_food_review

**user vendor communication**

                create_vendor_review
                create_beer_review
                create_vendor_food_review
                create_vendor_account_membership
                create_vendor_account_promotion
                create_vendor_blog_comment
                create_vendor_blog_comment_reply

**vendor third party communication**

                *create_vendor_third_party_message
                *create_vendor_third_party_reservation

**user third party communication**

                *create_third_party_review
