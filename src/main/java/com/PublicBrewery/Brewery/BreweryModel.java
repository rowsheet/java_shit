package com.PublicBrewery.Brewery;

import com.Common.AbstractModel;

import com.Common.Brewery;
import com.Common.VendorPageImage;
import com.PublicBrewery.Beer.BeerModel;
import com.PublicBrewery.Food.FoodModel;
import com.PublicBrewery.Events.EventModel;
import com.PublicBrewery.Reviews.ReviewController;
import com.PublicBrewery.Reviews.ReviewModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
                    "   v.official_business_name " +
                    " FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   vendor_info vi " +
                    "ON " +
                    "   v.id = vi.vendor_id " +
                    "WHERE v.id = ?";

    private String loadBreweryInfo_stage2 =
            "SELECT " +
                    "   display_order, " +
                    "   filename, " +
                    "   TO_CHAR(creation_timestamp, 'MM-DD-YYYY HH24:MI:SS') AS upload_date, " +
                    "   id as image_id," +
                    "   show_in_main_gallery, " +
                    "   show_in_main_slider " +
                    "FROM " +
                    "   vendor_page_images " +
                    "WHERE " +
                    "   vendor_id = ?";

    public BreweryModel() throws Exception {}

    /**
     * First load all of the info for the vendor where vendor_id matches.
     * Next load all image info where vendor_id matchs.
     *
     *      1) Load all vendor info.
     *      2) Load all images.
     *      3) Load all beers and beer reviews for this vendor (using Beer Model).
     *      4) Load all vendor foods and food reviews for this vendor (using Food Model).
     *      5) Load all events.
     *      6) Load all breweries.
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
            int review_limit
    ) throws Exception {
        // Create statments.
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        try {
            // Prepare statments.
            stage1 = this.DAO.prepareStatement(this.loadBreweryInfo_stage1);
            stage2 = this.DAO.prepareStatement(this.loadBreweryInfo_stage2);
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
                brewery.official_business_name = stage1Result.getString("official_business_name");
            }
            /*
            Stage 2
             */
            stage2.setInt(1, brewry_id);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                VendorPageImage vendorPageImage = new VendorPageImage();
                vendorPageImage.filename = stage2Result.getString("filename");
                vendorPageImage.upload_date = stage2Result.getString("upload_date");
                vendorPageImage.image_id = stage2Result.getInt("image_id");
                vendorPageImage.display_order = stage2Result.getInt("display_order");
                vendorPageImage.show_in_main_slider = stage2Result.getBoolean("show_in_main_slider");
                vendorPageImage.show_in_main_gallery = stage2Result.getBoolean("show_in_main_gallery");
                brewery.page_images.put(vendorPageImage.display_order, vendorPageImage);
            }
            /*
            Stage 3
             */
            BeerModel beerModel = new BeerModel();
            brewery.beerMenu = beerModel.loadBeerMenuPaginated(brewry_id, beer_limit, 0, "creation_timestamp", true);
            /*
            Stage 4
             */
            FoodModel foodModel = new FoodModel();
            brewery.foodMenu = foodModel.loadFoodMenuPaginated(brewry_id, food_limit, 0, "creation_timestamp", true);
            /*
            Stage 5
             */
            EventModel eventModel = new EventModel();
            brewery.events = eventModel.loadEvents(brewry_id, event_limit, 0);
            /*
            Stage 6
             */
            ReviewModel reviewModel = new ReviewModel();
            brewery.reviews = reviewModel.loadBreweryReviews(brewry_id, review_limit, 0);
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
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
