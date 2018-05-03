package com.VendorMedia.PageImages;

import com.Common.AbstractModel;
import com.Common.Color;
import com.Common.VendorMedia.*;
import com.VendorAccounts.Limit.LimitException;
import com.VendorAccounts.Limit.LimitModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class PageImageModel extends AbstractModel {

    public PageImageModel() throws Exception {}

    private String createVendorPageImageGallery_sql =
            "INSERT INTO " +
                    "   vendor_page_image_galleries (" +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color " +
                    ") VALUES (?,?,?) " +
                    "RETURNING id";

    public int createVendorPageImageGallery(
            String cookie,
            String name,
            String hex_color
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // Check limits.
            LimitModel limitModel = new LimitModel();
            limitModel.checkLimit(
                    this.vendorCookie.vendorID,
                    "vendor_page_image_galleries",
                    "media_gallery_limit"
            );
            // Do the database stuff.
            preparedStatement = this.DAO.prepareStatement(this.createVendorPageImageGallery_sql);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, hex_color);
            resultSet = preparedStatement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            if (id == 0) {
                throw new Exception("Unable to create new page image gallery.");
            }
            return id;
        } catch (LimitException ex) {
            System.out.print(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            // Try to parse.
            if (ex.getMessage().contains("vendor_page_image_galleries_vendor_id_name_idx")) {
                throw new Exception("You already have a gallery with that name!");
            }
            // Don't know why.
            System.out.println(ex.getMessage());
            throw new Exception("Unable to create new page image gallery.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String uploadVendorPageImage_sql =
        "INSERT INTO " +
                "   vendor_page_images (" +
                "   vendor_id, " +
                "   gallery_id, " +
                "   filename, " +
                "   feature_id," +
                "   display_order  " +
                ") VALUES (?,?,?,?, " +
                // This will make sure an upload get's a display order that never collides with
                // an existing display order and at least get's a display order has one if there
                // are no images yet in that gallery.
                // Without the SELECT MIN, we would collide with existing display_orders.
                // Without COALESCE, it wouldn't work if it was the first image.
                // Without CASE, the value is a record type and not an integer.
                // Fuck the world.
                "CAST(COALESCE(((SELECT MIN(display_order) from vendor_page_images WHERE gallery_id = ?) - 1), 1) AS INTEGER)" +
                ") " +
                "RETURNING id";

    public int uploadVendorPageImage (
            String cookie,
            String file_path,
            int gallery_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // Check limits.
            LimitModel limitModel = new LimitModel();
            limitModel.checkImageLimit(
                    this.vendorCookie.vendorID,
                    "vendor_page_images",
                    "gallery_image_limit",
                    "gallery",
                    gallery_id
            );
            // Do the database stuff.
            preparedStatement = this.DAO.prepareStatement(this.uploadVendorPageImage_sql);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setInt(2, gallery_id);
            preparedStatement.setString(3, file_path);
            preparedStatement.setInt(4, this.vendorCookie.requestFeatureID);
            preparedStatement.setInt(5, gallery_id);
            resultSet = preparedStatement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            if (id == 0) {
                throw new Exception("Unable to upload vendor page image.");
            }
            return  id;
        } catch (LimitException ex) {
            System.out.print(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse.
            if (ex.getMessage().contains("vendor_page_images_gallery_id_fk")) {
                throw  new Exception("You do not have permission to add images to this gallery.");
            }
            // Don't know why.
            throw new Exception("Unable to upload vendor page image.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteVendorPageImage_sql =
            "DELETE FROM " +
                    "   vendor_page_images " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    public boolean deleteVendorPageImage (
            String cookie,
            int image_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            preparedStatement = this.DAO.prepareStatement(deleteVendorPageImage_sql);
            preparedStatement.setInt(1, image_id);
            preparedStatement.setInt(2, this.vendorCookie.vendorID);
            preparedStatement.execute();
            return true;
        } catch (PageImageException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to delete vendor page image.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String loadVendorPageImageGalleryTable_sql =
            "SELECT " +
                    "   vpi.id AS image_id, " +
                    "   vpi.vendor_id, " +
                    "   vpi.filename, " +
                    "   vpi.display_order AS image_display_order, " +
                    "   vpi.creation_timestamp AS image_creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::timestamp - vpi.creation_timestamp::timestamp)) AS image_creation_days_ago, " +
                    "   vpig.name, " +
                    "   vpig.hex_color, " +
                    "   vpig.display_order AS gallery_display_order, " +
                    "   vpig.id AS gallery_id, " +
                    "   vpig.name, " +
                    "   vpig.hex_color, " +
                    "   vpig.creation_timestamp AS gallery_creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::timestamp - vpig.creation_timestamp::timestamp)) AS gallery_creation_days_ago " +
                    "FROM " +
                    "   vendor_page_image_galleries vpig " +
                    "LEFT JOIN " +
                    "   vendor_page_images vpi " +
                    "ON " +
                    "   vpi.gallery_id = vpig.id " +
                    "WHERE " +
                    "   vpig.id = ?" +
                    "AND " +
                    "   vpig.vendor_id = ?";

    public P_VendorPageImageTable loadVendorPageImageGalleryTable (
            String cookie,
            int gallery_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // Fetch data. Gallery must also be owned by vendor.
            preparedStatement = this.DAO.prepareStatement(this.loadVendorPageImageGalleryTable_sql);
            preparedStatement.setInt(1, gallery_id);
            preparedStatement.setInt(2, this.vendorCookie.vendorID);
            resultSet = preparedStatement.executeQuery();
            P_VendorPageImageTable p_vendorPageImageTable = new P_VendorPageImageTable();
            Color color = new Color();
            while (resultSet.next()) {
                // Fetch gallery info.
                p_vendorPageImageTable.gallery_name = resultSet.getString("name");
                p_vendorPageImageTable.gallery_id = resultSet.getInt("gallery_id");
                p_vendorPageImageTable.creation_timestamp = resultSet.getString("gallery_creation_timestamp");
                p_vendorPageImageTable.creation_days_ago = resultSet.getString("gallery_creation_days_ago");
                p_vendorPageImageTable.gallery_hex_color = resultSet.getString("hex_color");
                p_vendorPageImageTable.gallery_text_color = color.getInverseBW(p_vendorPageImageTable.gallery_hex_color);
                // Fetch image info.
                if (resultSet.getInt("image_id") > 0) { // Make sure it's an actual ID.
                    P_VendorPageImage p_vendorPageImage = new P_VendorPageImage();
                    p_vendorPageImage.id = resultSet.getInt("image_id");
                    p_vendorPageImage.vendor_id = this.vendorCookie.vendorID;
                    p_vendorPageImage.display_order = resultSet.getInt("image_display_order");
                    p_vendorPageImage.gallery_id = p_vendorPageImageTable.gallery_id;
                    p_vendorPageImage.filename = resultSet.getString("filename");
                    p_vendorPageImage.creation_timestamp = resultSet.getString("image_creation_timestamp");
                    p_vendorPageImage.creation_days_ago = resultSet.getString("image_creation_days_ago");
                    // Ingrement the table total image count.
                    p_vendorPageImageTable.image_count++;
                    // Add image to gallery.
                    p_vendorPageImageTable.page_images.put(p_vendorPageImage.display_order, p_vendorPageImage);
                }
            }
            return p_vendorPageImageTable;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Unknown error.
            throw new Exception("Unable to load vendor page image gallery table.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String updateVendorPageImageGalleries_stage1 =
            "UPDATE " +
                    "   vendor_page_image_galleries " +
                    "SET " +
                    "   display_order = ? " +
                    "WHERE " +
                    "   id = ? " +
                    "RETURNING vendor_id";

    private String updateVendorPageImageGalleries_stage2 =
            "UPDATE " +
                    "   vendor_page_image_galleries " +
                    "SET " +
                    "   display_order = ? " +
                    "WHERE " +
                    "   id = ?";

    public boolean updateVendorPageImageGalleries (
            String cookie,
            int[] gallery_ids
    ) throws Exception {
        PreparedStatement getMinStatement = null;
        ResultSet getMinResultSet = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /*
            Validate cookie.
             */
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            this.DAO.setAutoCommit(false);
            /*
            Ensure ownership of each resource while setting their values to negative
            to avoid collision on stage2.
             */
            int negative_display_order = 0;
            for (int i = 0; i < gallery_ids.length; i++) {
                preparedStatement = this.DAO.prepareStatement(this.updateVendorPageImageGalleries_stage1);
                preparedStatement.setInt(1, negative_display_order);
                preparedStatement.setInt(2, gallery_ids[i]);
                resultSet = preparedStatement.executeQuery();
                int vendor_id = 0;
                while (resultSet.next()) {
                    vendor_id = resultSet.getInt("vendor_id");
                }
                if (vendor_id != this.vendorCookie.vendorID) {
                    throw new PageImageException("You do not have permission to update these galleries.");
                }
                negative_display_order--;
            }
            /*
            Update the display order.
            This needs to be done in a transaction.
             */
            int display_order = 1;
            for (int i = 0; i < gallery_ids.length; i++) {
                preparedStatement = this.DAO.prepareStatement(this.updateVendorPageImageGalleries_stage2);
                preparedStatement.setInt(1, display_order);
                preparedStatement.setInt(2, gallery_ids[i]);
                preparedStatement.execute();
                display_order++;
            }
            this.DAO.commit();
            /*
            Done.
             */
            return true;
        } catch (PageImageException ex) {
            this.DAO.rollback();
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("ROLLING BACK");
            System.out.println(ex.getMessage());
            this.DAO.rollback();
            // Don't know why.
            throw new Exception("Unable to update page image galleries.");
        } finally {
            if (getMinStatement != null) {
                getMinStatement.close();
            }
            if (getMinResultSet!= null) {
                getMinResultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String updateVendorPageImageGallery_stage0 =
            "SELECT " +
                    "   MIN(display_order) - 1 AS min_display_order " +
                    "FROM " +
                    "   vendor_page_images " +
                    "WHERE " +
                    "   gallery_id = (SELECT gallery_id FROM vendor_page_images WHERE id = ?)";

    private String updateVendorPageImageGallery_stage1 =
            "UPDATE " +
                    "   vendor_page_images " +
                    "SET " +
                    "   display_order = ? " +
                    "WHERE " +
                    "   id = ? " +
                    "RETURNING vendor_id";

    private String updateVendorPageImageGallery_stage2 =
            "UPDATE " +
                    "   vendor_page_images " +
                    "SET " +
                    "   display_order = ? " +
                    "WHERE " +
                    "   id = ?";

    public boolean updateVendorPageImageGallery(
            String cookie,
            int[] image_ids
    ) throws Exception {
        PreparedStatement getMinStatement = null;
        ResultSet getMinResultSet = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /*
            Validate cookie.
             */
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            this.DAO.setAutoCommit(false);
            /*
            Get the min(display order) for an image, assuming they are all part of the
            same gallery. If anything fails, we just won't commit the transaction.
             */
            int negative_display_order = 0;
            getMinStatement = this.DAO.prepareStatement(this.updateVendorPageImageGallery_stage0);
            getMinStatement.setInt(1, image_ids[0]);
            getMinResultSet = getMinStatement.executeQuery();
            while (getMinResultSet.next()) {
                negative_display_order = getMinResultSet.getInt("min_display_order");
            }
            /*
            Ensure ownership of each resource while setting their values to negative
            to avoid collision on stage2.
             */
            for (int i = 0; i < image_ids.length; i++) {
                preparedStatement = this.DAO.prepareStatement(this.updateVendorPageImageGallery_stage1);
                preparedStatement.setInt(1, negative_display_order);
                preparedStatement.setInt(2, image_ids[i]);
                resultSet = preparedStatement.executeQuery();
                int vendor_id = 0;
                while (resultSet.next()) {
                    vendor_id = resultSet.getInt("vendor_id");
                }
                if (vendor_id != this.vendorCookie.vendorID) {
                    throw new PageImageException("You do not have permission to update this image gallery.");
                }
                negative_display_order--;
            }
            /*
            Update the display order.
            This needs to be done in a transaction.
             */
            int display_order = 1;
            for (int i = 0; i < image_ids.length; i++) {
                preparedStatement = this.DAO.prepareStatement(this.updateVendorPageImageGallery_stage2);
                preparedStatement.setInt(1, display_order);
                preparedStatement.setInt(2, image_ids[i]);
                preparedStatement.execute();
                display_order++;
            }
            this.DAO.commit();
            /*
            Done.
             */
            return true;
        } catch (PageImageException ex) {
            this.DAO.rollback();
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("ROLLING BACK");
            System.out.println(ex.getMessage());
            this.DAO.rollback();
            // Don't know why.
            throw new Exception("Unable to update page image gallery.");
        } finally {
            if (getMinStatement != null) {
                getMinStatement.close();
            }
            if (getMinResultSet!= null) {
                getMinResultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String loadVendorPageImageGalleriesTable_sql =
            "SELECT DISTINCT " +
                    "   vpig.id, " +
                    "   vpig.name, " +
                    "   vpig.hex_color, " +
                    "   vpig.display_order, " +
                    "   count(vpi.*) AS count_star, " +
                    "   vpig.creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::timestamp - vpig.creation_timestamp::timestamp)) AS creation_days_ago " +
                    "FROM " +
                    "   vendor_page_image_galleries vpig " +
                    "LEFT JOIN  " +
                    "   vendor_page_images vpi " +
                    "ON " +
                    "   vpig.id = vpi.gallery_id " +
                    "WHERE " +
                    "   vpig.vendor_id = ? " +
                    "GROUP BY 1";

    public HashMap<Integer, P_VendorPageImageGallery> loadVendorPageImageGalleriesTable (
            String cookie
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Keyed by display order.
            HashMap<Integer, P_VendorPageImageGallery> vendorPageImageGalleryHashMap = new HashMap<Integer, P_VendorPageImageGallery>();
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // Database things.
            preparedStatement = this.DAO.prepareStatement(this.loadVendorPageImageGalleriesTable_sql);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            resultSet = preparedStatement.executeQuery();
            Color color = new Color();
            while (resultSet.next()) {
                P_VendorPageImageGallery vendorPageImageGallery = new P_VendorPageImageGallery();
                vendorPageImageGallery.id = resultSet.getInt("id");
                vendorPageImageGallery.display_order = resultSet.getInt("display_order");
                vendorPageImageGallery.name = resultSet.getString("name");
                vendorPageImageGallery.hex_color = resultSet.getString("hex_color");
                vendorPageImageGallery.text_color = color.getInverseBW(vendorPageImageGallery.hex_color);
                vendorPageImageGallery.image_count = resultSet.getInt("count_star");
                vendorPageImageGallery.creation_timestamp = resultSet.getString("creation_timestamp");
                vendorPageImageGallery.creation_days_ago = resultSet.getString("creation_days_ago");
                // No page images here, even though there's a hash map for it.
                vendorPageImageGalleryHashMap.put(vendorPageImageGallery.display_order, vendorPageImageGallery);
            }
            return vendorPageImageGalleryHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to load vendor page image gallery table.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteVendorPageImageGallery_sql =
            "DELETE FROM " +
                    "   vendor_page_image_galleries " +
                    "WHERE " +
                    "   vendor_id = ? " +
                    "AND " +
                    "   id = ?";

    public boolean deleteVendorPageImageGallery (
            String cookie,
            int gallery_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // DB things.
            preparedStatement = this.DAO.prepareStatement(this.deleteVendorPageImageGallery_sql);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setInt(2, gallery_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to delete vendor page image gallery.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String setMainImageID_sql =
            "UPDATE " +
                    "   vendor_info " +
                    "SET " +
                    "   main_image_id = ? " +
                    "WHERE " +
                    "   vendor_id = ?";

    public boolean setMainImageID (
            String cookie,
            int image_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // DB things.
            preparedStatement = this.DAO.prepareStatement(this.setMainImageID_sql);
            preparedStatement.setInt(1, image_id);
            preparedStatement.setInt(2, this.vendorCookie.vendorID);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to set main main image.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String setMainGalleryID_sql =
            "UPDATE " +
            "   vendor_info " +
            "SET " +
            "   main_gallery_id = ? " +
            "WHERE " +
            "   vendor_id = ?";

    public boolean setMainGalleryID (
            String cookie,
            int gallery_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // DB things.
            preparedStatement = this.DAO.prepareStatement(this.setMainGalleryID_sql);
            preparedStatement.setInt(1, gallery_id);
            preparedStatement.setInt(2, this.vendorCookie.vendorID);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to set main gallery.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String setMainFoodGalleryID_sql =
            "UPDATE " +
                    "   vendor_info " +
                    "SET " +
                    "   main_food_gallery_id = ? " +
                    "WHERE " +
                    "   vendor_id = ?";

    public boolean setMainFoodGalleryID (
            String cookie,
            int gallery_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // DB things.
            preparedStatement = this.DAO.prepareStatement(this.setMainFoodGalleryID_sql);
            preparedStatement.setInt(1, gallery_id);
            preparedStatement.setInt(2, this.vendorCookie.vendorID);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to set main food gallery.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String setMainDrinkGalleryID_sql =
            "UPDATE " +
                    "   vendor_info " +
                    "SET " +
                    "   main_drink_gallery_id = ? " +
                    "WHERE " +
                    "   vendor_id = ?";

    public boolean setMainDrinkGalleryID (
            String cookie,
            int gallery_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // DB things.
            preparedStatement = this.DAO.prepareStatement(this.setMainDrinkGalleryID_sql);
            preparedStatement.setInt(1, gallery_id);
            preparedStatement.setInt(2, this.vendorCookie.vendorID);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to set main drink gallery.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String setMainBeerGalleryID_sql =
            "UPDATE " +
                    "   vendor_info " +
                    "SET " +
                    "   main_beer_gallery_id = ? " +
                    "WHERE " +
                    "   vendor_id = ?";

    public boolean setMainBeerGalleryID (
            String cookie,
            int gallery_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_page_images");
            // DB things.
            preparedStatement = this.DAO.prepareStatement(this.setMainBeerGalleryID_sql);
            preparedStatement.setInt(1, gallery_id);
            preparedStatement.setInt(2, this.vendorCookie.vendorID);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to set main beer gallery.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
