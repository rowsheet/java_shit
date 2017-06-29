package com.VendorAccounts.General;

import com.Common.AbstractModel;
import com.Common.VendorCookie;
import com.VendorAccounts.VendorAuthentication.VendorAuthenticationModel;
import com.google.gson.Gson;

import javax.print.attribute.standard.PresentationDirection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/24/17.
 */
public class GeneralModel extends AbstractModel {

    private String updateBreweryInfoSQL_stage2 =
            "INSERT INTO " +
                    "   vendor_info (" +
                    "   display_name, " +
                    "   about_text, " +
                    "   mon_open, " +
                    "   mon_close, " +
                    "   tue_open, " +
                    "   tue_close, " +
                    "   wed_open, " +
                    "   wed_close, " +
                    "   thu_open, " +
                    "   thu_close, " +
                    "   fri_open, " +
                    "   fri_close, " +
                    "   sat_open, " +
                    "   sat_close, " +
                    "   sun_open, " +
                    "   sun_close, " +
                    "   public_phone, " +
                    "   public_email," +
                    "   vendor_id " +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                    "ON CONFLICT (vendor_id) DO UPDATE " +
                    "SET " +
                    "   display_name = ?, " +
                    "   about_text = ?, " +
                    "   mon_open = ?, " +
                    "   mon_close = ?, " +
                    "   tue_open = ?, " +
                    "   tue_close = ?, " +
                    "   wed_open = ?, " +
                    "   wed_close = ?, " +
                    "   thu_open = ?, " +
                    "   thu_close = ?, " +
                    "   fri_open = ?, " +
                    "   fri_close = ?, " +
                    "   sat_open = ?, " +
                    "   sat_close = ?, " +
                    "   sun_open = ?, " +
                    "   sun_close = ?, " +
                    "   public_phone = ?, " +
                    "   public_email = ?";

    private String updateBreweryInfoSQL_stage3 =
            "UPDATE " +
                    "   vendors " +
                    "SET " +
                    "   street_address = ?, " +
                    "   city = ?, " +
                    "   state = ?::us_state, " +
                    "   zip = ? " +
                    "WHERE " +
                    "   id = ?";

    public GeneralModel() throws Exception {}

    /**
     * Check session and get back the vendorID, then update the vendor_info table
     * where the vendor_id matches.
     *
     *      1) Validates session and permissions.
     *      2) Updates vendor_info table.
     *      3) Update vendors table.
     *
     *
     * Do all steps in a transaction or roll black.
     *
     * @param cookie
     * @param display_name
     * @param about_text
     * @param mon_open
     * @param mon_close
     * @param tue_open
     * @param tue_close
     * @param wed_open
     * @param wed_close
     * @param thu_open
     * @param thu_close
     * @param fri_open
     * @param fri_close
     * @param sat_open
     * @param sat_close
     * @param sun_open
     * @param sun_close
     * @param street_address
     * @param city
     * @param state
     * @param zip
     * @param public_phone
     * @param public_email
     * @return
     * @throws Exception
     */
    public boolean updateBreweryInfo(
            String cookie,
            String display_name,
            String about_text,
            String mon_open,
            String mon_close,
            String tue_open,
            String tue_close,
            String wed_open,
            String wed_close,
            String thu_open,
            String thu_close,
            String fri_open,
            String fri_close,
            String sat_open,
            String sat_close,
            String sun_open,
            String sun_close,
            String street_address,
            String city,
            String state,
            String zip,
            String public_phone,
            String public_email
    ) throws Exception {
        PreparedStatement stage2 = null;
        PreparedStatement stage3 = null;
        try {
            /*
            Disable auto-commit.
             */
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
            Validate session.
            This model does not need a feature since vendor info is applicable to all vendors.
             */
            VendorAuthenticationModel vendorAuthenticationModel = new VendorAuthenticationModel();
            Gson gson = new Gson();
            VendorCookie vendorCookie = gson.fromJson(cookie, VendorCookie.class);
            vendorAuthenticationModel.checkVendorSession(vendorCookie.sessionKey);
            /*
            Stage 2
            Update vendor_info.
             */
            stage2 = this.DAO.prepareStatement(this.updateBreweryInfoSQL_stage2);
            stage2.setString(1, display_name);
            stage2.setString(2, about_text);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            stage2.setTime(3, new Time(simpleDateFormat.parse(mon_open).getTime()));
            stage2.setTime(4, new Time(simpleDateFormat.parse(mon_close).getTime()));
            stage2.setTime(5, new Time(simpleDateFormat.parse(tue_open).getTime()));
            stage2.setTime(6, new Time(simpleDateFormat.parse(tue_close).getTime()));
            stage2.setTime(7, new Time(simpleDateFormat.parse(wed_open).getTime()));
            stage2.setTime(8, new Time(simpleDateFormat.parse(wed_close).getTime()));
            stage2.setTime(9, new Time(simpleDateFormat.parse(thu_open).getTime()));
            stage2.setTime(10, new Time(simpleDateFormat.parse(thu_close).getTime()));
            stage2.setTime(11, new Time(simpleDateFormat.parse(fri_open).getTime()));
            stage2.setTime(12, new Time(simpleDateFormat.parse(fri_close).getTime()));
            stage2.setTime(13, new Time(simpleDateFormat.parse(sat_open).getTime()));
            stage2.setTime(14, new Time(simpleDateFormat.parse(sat_close).getTime()));
            stage2.setTime(15, new Time(simpleDateFormat.parse(sun_open).getTime()));
            stage2.setTime(16, new Time(simpleDateFormat.parse(sun_close).getTime()));
            stage2.setString(17, public_phone);
            stage2.setString(18, public_email);
            stage2.setInt(19, vendorCookie.vendorID);
            stage2.setString(20, display_name);
            stage2.setString(21, about_text);
            stage2.setTime(22, new Time(simpleDateFormat.parse(mon_open).getTime()));
            stage2.setTime(23, new Time(simpleDateFormat.parse(mon_close).getTime()));
            stage2.setTime(24, new Time(simpleDateFormat.parse(tue_open).getTime()));
            stage2.setTime(25, new Time(simpleDateFormat.parse(tue_close).getTime()));
            stage2.setTime(26, new Time(simpleDateFormat.parse(wed_open).getTime()));
            stage2.setTime(27, new Time(simpleDateFormat.parse(wed_close).getTime()));
            stage2.setTime(28, new Time(simpleDateFormat.parse(thu_open).getTime()));
            stage2.setTime(29, new Time(simpleDateFormat.parse(thu_close).getTime()));
            stage2.setTime(30, new Time(simpleDateFormat.parse(fri_open).getTime()));
            stage2.setTime(31, new Time(simpleDateFormat.parse(fri_close).getTime()));
            stage2.setTime(32, new Time(simpleDateFormat.parse(sat_open).getTime()));
            stage2.setTime(33, new Time(simpleDateFormat.parse(sat_close).getTime()));
            stage2.setTime(34, new Time(simpleDateFormat.parse(sun_open).getTime()));
            stage2.setTime(35, new Time(simpleDateFormat.parse(sun_close).getTime()));
            stage2.setString(36, public_phone);
            stage2.setString(37, public_email);
            stage2.execute();
            /*
            Stage 3
            Update vendors table.
             */
            stage3 = this.DAO.prepareStatement(this.updateBreweryInfoSQL_stage3);
            stage3.setString(1, street_address);
            stage3.setString(2, city);
            stage3.setString(3, state);
            stage3.setString(4, zip);
            stage3.setInt(5, vendorCookie.vendorID);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (Exception ex) {
            System.out.print(ex.fillInStackTrace());
            System.out.print(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // Unknown error.
            throw new Exception("Unable to create beer.");
        } finally {
            /*
            Clean up.
             */
            if (stage2 != null) {
                stage2.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String uploadVendorPageImagesSQL_stage1 =
            "SELECT " +
                    "   COUNT(*) AS count_star " +
                    "FROM " +
                    "   vendor_page_images " +
                    "WHERE " +
                    "   vendor_id = ?";

    private String uploadVendorPageImagesSQL_stage3 =
            "INSERT INTO " +
                    "   vendor_page_images " +
                    "(" +
                    "   vendor_id, " +
                    "   filename," +
                    "   feature_id " +
                    ") VALUES (" +
                    "?,?,?)";

    /**
     * Inserts into vendor page images directory.
     *
     * Set's the display order to an increment of whatever the max display order is for images
     * of this vendor.
     *
     * Do each image one by one in a transaction and roll back on failure.
     *
     * NOTE: although "vendor_page_images_[number]" is a feature, the only on in
     * the check constraint is actually "vendor_page_images".
     * This means that users will not be able to unload, but if vendor_page_images_[number]
     * is not meant, a job will have to notify them and manually delete if wanted.
     *
     * @param cookie
     * @param filename
     * @return directory path for image for vendorID.
     * @throws Exception
     */
    public String uploadVendorPageImage(
        String cookie,
        String filename
    ) throws Exception {
        // Initialize prepared statements because we're going to need
        // to clean them up in the finally block.
        // We'll need one for the check of how many images are already
        // there and one for each image.
        PreparedStatement stage1 = null;
        PreparedStatement stage3 = null;
        // We only need two result sets, for the first query of how many
        // images this vendor already has and the max display order.
        ResultSet stage1Result = null;
        try {
            // Start a transaction and disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate the cookie and get the vendorID.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // Find out how many page images they have. If it's over a certain amount,
            // they need to have certain permissions.
            stage1 = this.DAO.prepareStatement(this.uploadVendorPageImagesSQL_stage1);
            stage1.setInt(1, this.vendorCookie.vendorID);
            stage1Result = stage1.executeQuery();
            int image_count = 0;
            while (stage1Result.next()) {
                image_count = stage1Result.getInt("count_star");
            }
            // Depending on the image count, see if the user has the
            // permissions it needs.
            if (image_count > 20) {
                // This is not set in check constraints, so validate with extra so the
                // original requestFeatureID "vendor_page_images" remains the same.
                this.validateCookieVendorFeature(cookie, "vendor_page_images_20", true);
            }
            //@TODO add other counts for more images.
            // Insert the record.
            stage3 = this.DAO.prepareStatement(this.uploadVendorPageImagesSQL_stage3);
            stage3.setInt(1, this.vendorCookie.vendorID);
            stage3.setString(2, this.getDirPathForVendorID(this.vendorCookie.vendorID) + filename);
            stage3.setInt(3, this.vendorCookie.requestFeatureID);
            stage3.execute();
            // Done. Commit.
            this.DAO.commit();
            // Return the directory path we want to put the file in. In the future, we might
            // make this more sophisticated to optimize file serving but for now, just set it
            // to the vendor_id.
            return this.getDirPathForVendorID(this.vendorCookie.vendorID);
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // Try to parse exception message.
            if (ex.getMessage().contains("already exists")) {
                throw new Exception("Sorry! You already have a photo called " + filename + ". We don't want to overwrite it.");
            }
            // Unknown reason.
            throw new Exception("Unable to upload file.");
        } finally {
            // Clean up prepeared statemtns.
            if (stage1 != null) {
                stage1.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            // Clean up result set.
            if (stage1Result != null) {
                 stage1Result.close();
            }
            // Clean up DAO.
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String verifyVendorPageImageOwnershipSQL =
            "SELECT " +
                    "   filename " + // also used in delete.
                    "FROM " +
                    "   vendor_page_images " +
                    "WHERE " +
                    "   vendor_id = ? " +
                    "AND " +
                    "   id = ?";

    private String updateVendorPageImageSQL_stage2 =
            "UPDATE " +
                    "   vendor_page_images " +
                    "SET " +
                    "   show_in_main_gallery = ?, " +
                    "   show_in_main_slider = ?, " +
                    "   display_order = ? " +
                    "WHERE " +
                    "   id = ?";
    /**
     * Updates page image data where id matches.
     *
     *      1) Verify resource ownership.
     *      2) Update data
     *
     * Return excetpion if something goes wrong.
     *
     * @param cookie
     * @param show_in_main_gallery
     * @param show_in_main_slider
     * @param display_order
     * @return
     * @throws Exception
     */
    public boolean updateVendorPageImage(
            String cookie,
            int image_id,
            boolean show_in_main_gallery,
            boolean show_in_main_slider,
            int display_order
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Verify cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            /*
            Stage 1
             */
            stage1 = this.DAO.prepareStatement(this.verifyVendorPageImageOwnershipSQL);
            stage1.setInt(1, this.vendorCookie.vendorID);
            stage1.setInt(2, image_id);
            String filename = null;
            stage1Result = stage1.executeQuery();
            while (stage1Result.next()) {
                filename = stage1Result.getString("filename");
            }
            if (filename == null) {
                throw new GeneralException("You don't have permission to update this image.");
            }
            /*
             Stage 2
              */
            stage2 = this.DAO.prepareStatement(this.updateVendorPageImageSQL_stage2);
            stage2.setBoolean(1, show_in_main_gallery);
            stage2.setBoolean(2, show_in_main_slider);
            stage2.setInt(3, display_order);
            stage2.setInt(4, image_id);
            stage2.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse error.
            if (ex.getMessage().contains("already exists") && ex.getMessage().contains("display_order")) {
                throw new Exception("Sorry! You can't have two images with the same display order.");
            }
            // Unknown error.
            throw new Exception("Could not delete image.");
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
        }
    }

    private String deleteVendorPageImageSQL_stage2 =
            "DELETE FROM " +
                    "   vendor_page_images " +
                    "WHERE " +
                    "   id = ?";

    public String deleteVenodrPageImage(
            String cookie,
            int image_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Verify cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            /*
            Stage 1
             */
            stage1 = this.DAO.prepareStatement(this.verifyVendorPageImageOwnershipSQL);
            stage1.setInt(1, this.vendorCookie.vendorID);
            stage1.setInt(2, image_id);
            String filename = null;
            stage1Result = stage1.executeQuery();
            while (stage1Result.next()) {
                filename = stage1Result.getString("filename");
            }
            if (filename == null) {
                throw new GeneralException("You don't have permission to delete this image.");
            }
            /*
             Stage 2
              */
            stage2 = this.DAO.prepareStatement(this.deleteVendorPageImageSQL_stage2);
            stage2.setInt(1, image_id);
            stage2.execute();
            return filename;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Unknown error.
            throw new Exception(ex.getMessage());
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
        }
    }

    private String getDirPathForVendorID(int vendor_id) {
        return Integer.toString(vendor_id) + "/";
    }

}
