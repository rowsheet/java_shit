/*
NOTE:
1) @TODO THIS NEEDS TO BE MIGRATED TO "VENDOR" AND NOT "BREWERY".

2) I AM THROWING AWAY LIMITS AND OFFSETS FOR PUBLIC FETCHING OF INDIVIDUAL
VENDORS. IF A VENDORS PUBLIC PROFILE IS LOADED, THE ENTIRE THING WILL
BE LOADED (at least for beer, food, and drink menus).

IF THIS BECOMES A LONG-TERM PERFORMANCE ISSUE, EXTRA PUBLIC MODELS WILL
HIT MATERIALIZED, DISTRIBUTED DATA-STORES WHEN THE TIME COMES. (reviews will probably
be ported to a seperate service).
 */
package com.PublicBrewery.Brewery;

import com.Common.AbstractModel;

import com.Common.PublicVendor.Brewery;
import com.Common.VendorMedia.VendorMedia;
import com.Common.VendorMedia.VendorPageImageGallery;
import com.PublicBrewery.Beer.BeerModel;
import com.PublicBrewery.Food.FoodModel;
import com.PublicBrewery.Events.EventModel;
import com.PublicBrewery.Reviews.ReviewModel;
import com.PublicBrewery.Drink.DrinkModel;
import com.Common.VendorMedia.VendorPageImage;
import com.PublicBrewery.VendorMedia.VendorMediaModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;

/**
 * Created by alexanderkleinhans on 6/9/17.
 */
public class BreweryModel extends AbstractModel {

    private String loadBreweryInfo_stage1 =
            "SELECT " +
                    "   v.id as vendor_id, " +
                    "   COALESCE(vi.display_name, 'NA') AS display_name," +
                    "   COALESCE(vi.about_text, 'NA') AS about_text, " +
                    "   COALESCE(TO_CHAR(vi.mon_open, 'HH:MI AM'), '00:00:00') AS mon_open," +
                    "   COALESCE(TO_CHAR(vi.mon_close, 'HH:MI AM'), '00:00:00') AS mon_close," +
                    "   COALESCE(TO_CHAR(vi.tue_open, 'HH:MI AM'), '00:00:00') AS tue_open," +
                    "   COALESCE(TO_CHAR(vi.tue_close, 'HH:MI AM'), '00:00:00') AS tue_close," +
                    "   COALESCE(TO_CHAR(vi.wed_open, 'HH:MI AM'), '00:00:00') AS wed_open," +
                    "   COALESCE(TO_CHAR(vi.wed_close, 'HH:MI AM'), '00:00:00') AS wed_close," +
                    "   COALESCE(TO_CHAR(vi.thu_open, 'HH:MI AM'), '00:00:00') AS thu_open," +
                    "   COALESCE(TO_CHAR(vi.thu_close, 'HH:MI AM'), '00:00:00') AS thu_close," +
                    "   COALESCE(TO_CHAR(vi.fri_open, 'HH:MI AM'), '00:00:00') AS fri_open," +
                    "   COALESCE(TO_CHAR(vi.fri_close, 'HH:MI AM'), '00:00:00') AS fri_close," +
                    "   COALESCE(TO_CHAR(vi.sat_open, 'HH:MI AM'), '00:00:00') AS sat_open," +
                    "   COALESCE(TO_CHAR(vi.sat_close, 'HH:MI AM'), '00:00:00') AS sat_close," +
                    "   COALESCE(TO_CHAR(vi.sun_open, 'HH:MI AM'), '00:00:00') AS sun_open," +
                    "   COALESCE(TO_CHAR(vi.sun_close, 'HH:MI AM'), '00:00:00') AS sun_close," +
                    "   v.street_address, " +
                    "   v.city, " +
                    "   v.state, " +
                    "   v.zip, " +
                    "   COALESCE(vi.public_phone, 'NA') AS public_phone, " +
                    "   COALESCE(vi.public_email, 'NA') AS public_email, " +
                    "   v.official_business_name," +
                    "   COALESCE(v.google_maps_address, 'NA') AS google_maps_address, " +
                    "   COALESCE(v.latitude, 0.0) AS latitude, " +
                    "   COALESCE(v.longitude, 0.0) AS longitude, " +
                    "   COALESCE(v.google_maps_zoom, 0) AS google_maps_zoom," +
                    "   vi.brewery_has," +
                    "   vi.brewery_friendly," +
                    "   vi.short_type_description, " +
                    "   vi.short_text_description, " +
                    "   v.short_code, " +
                    "   vi.main_image_id, " +
                    "   vi.main_gallery_id," +
                    "   vpi.filename AS main_image_filename, " +
                    "   vpi.display_order AS main_image_display_order, " +
                    "   vpi.gallery_id AS main_image_gallery_id " +
                    " FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   vendor_info vi " +
                    "ON " +
                    "   v.id = vi.vendor_id " +
                    "LEFT JOIN " +
                    "   vendor_page_images vpi " +
                    "ON " +
                    "   vi.main_image_id = vpi.id " +
                    "WHERE v.id = ?";

    public BreweryModel() throws Exception {}

    /**
     * This is the overloaded (or underloaded?) function for loadBreweryInfo that
     * defaults loading all information fully (including beer_menu, food_menu, and events).
     *
     * The overloaded function is to allow a disable of that so that a lighter version of
     * this model loading can be used in search.
     *
     * @param brewry_id
     * @param beer_limit
     * @param food_limit
     * @param image_limit
     * @param event_limit
     * @param review_limit
     * @return
     * @throws Exception
     */
    public Brewery loadBreweryInfo(
            int brewry_id,
            int beer_limit, //@TODO implement
            int food_limit, //@TODO implement
            int image_limit, //@TODO implement
            int event_limit,
            int review_limit,
            int drink_limit //@TODO implement
    ) throws Exception {
        // Don't skip anything (beers, foods or events).
        // By setting these to false, it's an affirmative flag to load them.
        // This is used in an overloading function for search to load a
        // "lighter" version of the data.
        boolean skip_beers = false;
        boolean skip_foods = false;
        boolean skip_events = false;
        boolean skip_drinks = false;
        return this.loadBreweryInfo(
            brewry_id,
            beer_limit,
            food_limit,
            image_limit,
            event_limit,
            review_limit,
            drink_limit,
            skip_beers,
            skip_foods,
            skip_events,
            skip_drinks
        );
    }

    /**
     * Literally just loads brewery info without any limits on beers, foods
     * or events because they are all skipped.
     *
     * @param brewery_id
     * @return
     * @throws Exception
     */
    public Brewery loadBreweryInfoForSearch(
            int brewery_id,
            int review_limit,
            int image_limit,
            int drink_limit
    ) throws Exception {
        int beer_limit = 0; // Doesn't matter.
        int food_limit = 0; // Doesn't matter.
        int event_limit = 0; // Doesn't matter.
        boolean skip_beers = true;
        boolean skip_foods = true;
        boolean skip_events = true;
        boolean skip_drinks = true;
        return this.loadBreweryInfo(
                brewery_id,
                beer_limit,
                food_limit,
                image_limit,
                event_limit,
                review_limit,
                drink_limit,
                skip_beers,
                skip_foods,
                skip_events,
                skip_drinks
        );
    }

    /**
     * First load all of the info for the vendor where vendor_id matches.
     * Next load all image info where vendor_id matchs.
     *
     * Note: Also has the option to skip loading of:
     *
     *      beers, events, or foods (for search)
     *
     *      1) Load all vendor info.
     *      2) Load all images.
     *      3) Load all beers and beer reviews          (Option to skip).
     *      4) Load all vendor foods and food reviews   (Option to skip).
     *      5) Load all events                          (Option to skip).
     *      6) Load all reviews.
     *
     * @param brewry_id
     * @return Brewery
     * @throws Exception
     */
    public Brewery loadBreweryInfo(
            int brewry_id,
            int beer_limit, //@TODO implement
            int food_limit, //@TODO implement
            int image_limit, //@TODO implement
            int event_limit,
            int review_limit,
            int drink_limit,
            boolean skip_beers,
            boolean skip_foods,
            boolean skip_events,
            boolean skip_drinks
    ) throws Exception {
        // Create statments.
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        try {
            // Prepare statments.
            stage1 = this.DAO.prepareStatement(this.loadBreweryInfo_stage1);
            /*
            Stage 1
             */
            stage1.setInt(1, brewry_id);
            stage1Result = stage1.executeQuery();
            Brewery brewery = new Brewery();
            if (!stage1Result.isBeforeFirst()) {
                throw new Exception("Unknwon vendor_id.");
            }
            while (stage1Result.next()) {
                brewery.vendor_id = stage1Result.getInt("vendor_id");
                brewery.official_business_name = stage1Result.getString("official_business_name");
                brewery.display_name = stage1Result.getString("display_name");
                brewery.about_text = stage1Result.getString("about_text");
                brewery.mon_open = stage1Result.getString("mon_open");
                brewery.mon_close = stage1Result.getString("mon_close");
                brewery.tue_open = stage1Result.getString("tue_open");
                brewery.tue_close = stage1Result.getString("tue_close");
                brewery.wed_open = stage1Result.getString("wed_open");
                brewery.wed_close = stage1Result.getString("wed_close");
                brewery.thu_open = stage1Result.getString("thu_open");
                brewery.thu_close = stage1Result.getString("thu_close");
                brewery.fri_open = stage1Result.getString("fri_open");
                brewery.fri_close = stage1Result.getString("fri_close");
                brewery.sat_open = stage1Result.getString("sat_open");
                brewery.sat_close = stage1Result.getString("sat_close");
                brewery.sun_open = stage1Result.getString("sun_open");
                brewery.sun_close = stage1Result.getString("sun_close");
                brewery.street_address = stage1Result.getString("street_address");
                brewery.city = stage1Result.getString("city");
                brewery.state = stage1Result.getString("state");
                brewery.zip = stage1Result.getString("zip");
                brewery.public_phone = stage1Result.getString("public_phone");
                brewery.public_email = stage1Result.getString("public_email");
                brewery.google_maps_address = stage1Result.getString("google_maps_address");
                brewery.latitude = stage1Result.getFloat("latitude");
                brewery.longitude = stage1Result.getFloat("longitude");
                brewery.google_maps_zoom = stage1Result.getInt("google_maps_zoom");
                brewery.short_type_description = stage1Result.getString("short_type_description");
                if (brewery.short_type_description == null) {
                    brewery.short_type_description = "NA";
                }
                brewery.short_text_description = stage1Result.getString("short_text_description");
                if (brewery.short_text_description == null) {
                    brewery.short_text_description = "NA";
                }
                brewery.short_code = stage1Result.getString("short_code");
                if (brewery.short_code == null) {
                    brewery.short_code = "";
                }
                brewery.main_image_id = stage1Result.getInt("main_image_id");
                brewery.main_gallery_id = stage1Result.getInt("main_gallery_id");
                // Brewery "has" items are an array of enums in postgres.
                Array brewery_has = stage1Result.getArray("brewery_has");
                String [] str_brewery_has = new String[]{};
                if (brewery_has != null) {
                    str_brewery_has = (String[]) brewery_has.getArray();
                    brewery.brewery_has = str_brewery_has;
                }
                // Brewery "friendly" items are an array of enums in postgres.
                Array brewery_friendly = stage1Result.getArray("brewery_friendly");
                String[] str_brewery_friendly = new String[]{};
                if (brewery_friendly != null) {
                    str_brewery_friendly = (String[]) brewery_friendly.getArray();
                    brewery.brewery_friendly = str_brewery_friendly;
                }
                // Get the page image.
                VendorPageImage vendorPageImage = new VendorPageImage();
                vendorPageImage.id = brewery.main_image_id;
                vendorPageImage.filename = stage1Result.getString("main_image_filename");
                vendorPageImage.gallery_id = stage1Result.getShort("main_image_gallery_id");
                vendorPageImage.display_order = stage1Result.getInt("main_image_display_order");
                vendorPageImage.vendor_id = brewery.vendor_id;
                brewery.main_image = vendorPageImage;
            }
            /*
            Stage 2
             */
            VendorMediaModel vendorMediaModel = new VendorMediaModel();
            brewery.main_gallery = vendorMediaModel.loadVendorPageImageGallery(brewery.main_gallery_id);
            /*
            Stage 3
             */
            if (!skip_beers) {
                BeerModel beerModel = new BeerModel();
                // brewery.beerMenu = beerModel.loadBeerMenuPaginated(brewry_id, beer_limit, 0, "creation_timestamp", true);
                // No more "limit" "offset" for vendor-specific individual public fetches.
                brewery.beerMenu = beerModel.loadBeerMenu(brewry_id);
            }
            /*
            Stage 4
             */
            if (!skip_foods) {
                FoodModel foodModel = new FoodModel();
                // brewery.foodMenu = foodModel.loadFoodMenuPaginated(brewry_id, food_limit, 0, "creation_timestamp", true);
                // No more "limit" "offset" for vendor-specific individual public fetches.
                brewery.foodMenu = foodModel.loadFoodMenu(brewry_id);
            }
            /*
            Stage 5
             */
            if (!skip_events) {
                EventModel eventModel = new EventModel();
                brewery.events = eventModel.loadEvents(brewry_id, event_limit, 0);
            }
            /*
            Stage 6
             */
            ReviewModel reviewModel = new ReviewModel();
            brewery.reviews = reviewModel.loadBreweryReviews(brewry_id, review_limit, 0);
            /*
            Stage 7
             */
            if (!skip_drinks) {
                DrinkModel drinkModel = new DrinkModel();
                brewery.drinkMenu = drinkModel.loadDrinkMenu(brewry_id); // No limits on drinks yet.
            }
            return brewery;
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception("Unable to load brewery info");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
