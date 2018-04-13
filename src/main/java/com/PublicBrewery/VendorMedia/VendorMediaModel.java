package com.PublicBrewery.VendorMedia;

import com.Common.AbstractModel;
import com.Common.Color;
import com.Common.VendorMedia.VendorMedia;
import com.Common.VendorMedia.VendorPageImage;
import com.Common.VendorMedia.VendorPageImageGallery;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class VendorMediaModel extends AbstractModel{

    public VendorMediaModel() throws Exception {}

    // Stage 1: load page images.
    private String loadVendorMedia_stage1 =
            "SELECT " +
                    "   vpi.id AS image_id, " +
                    "   vpi.vendor_id, " +
                    "   vpi.filename, " +
                    "   vpi.display_order AS image_display_order, " +
                    "   vpi.creation_timestamp AS image_creation_timestamp, " +
                    "   vpig.name, " +
                    "   vpig.hex_color, " +
                    "   vpig.display_order AS gallery_display_order, " +
                    "   vpig.id AS gallery_id, " +
                    "   vpig.name, " +
                    "   vpig.hex_color, " +
                    "   vpig.creation_timestamp AS gallery_creation_timestamp " +
                    "FROM " +
                    "   vendor_page_image_galleries vpig " +
                    "LEFT JOIN " +
                    "   vendor_page_images vpi " +
                    "ON " +
                    "   vpi.gallery_id = vpig.id " +
                    "WHERE " +
                    "   vpig.vendor_id = ? " +
                    "ORDER BY vpig.id DESC";

    /**
     * Right now it only loads page_imges. Maybe later there will be other media.
     *
     * @param vendor_id
     * @return
     * @throws Exception
     */
    public VendorMedia loadVendorMedia(
            int vendor_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try  {
            // Make media container.
            VendorMedia vendorMedia = new VendorMedia();
            // Query.
            preparedStatement = this.DAO.prepareStatement(loadVendorMedia_stage1);
            preparedStatement.setInt(1, vendor_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Color color = new Color();
                VendorPageImage vendorPageImage = new VendorPageImage();
                vendorPageImage.id = resultSet.getInt("image_id");
                vendorPageImage.filename = resultSet.getString("filename");
                vendorPageImage.vendor_id = vendor_id;
                vendorPageImage.gallery_id = resultSet.getInt("gallery_id");
                vendorPageImage.display_order = resultSet.getInt("image_display_order");
                int gallery_display_order = resultSet.getInt("gallery_display_order");
                VendorPageImageGallery vendorPageImageGallery = vendorMedia.pageImageGalleries.get(gallery_display_order);
                if (vendorPageImageGallery != null) {
                    vendorPageImageGallery.image_count++;
                    vendorPageImageGallery.vendorPageImages.put(vendorPageImage.display_order, vendorPageImage);
                } else {
                    vendorPageImageGallery = new VendorPageImageGallery();
                    vendorPageImageGallery.display_order = gallery_display_order;
                    vendorPageImageGallery.id = resultSet.getInt("gallery_id");
                    vendorPageImageGallery.name = resultSet.getString("name");
                    vendorPageImageGallery.hex_color = resultSet.getString("hex_color");
                    vendorPageImageGallery.text_color = color.getInverseBW(vendorPageImageGallery.hex_color);
                    vendorPageImageGallery.vendorPageImages.put(vendorPageImage.display_order, vendorPageImage);
                    if (vendorPageImage.id > 0) { // Make sure it's a real entry before incrementing counter.
                        vendorPageImageGallery.image_count++;
                    }
                    vendorMedia.pageImageGalleries.put(gallery_display_order, vendorPageImageGallery);
                }
            }
            return vendorMedia;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Unkonwn reason.
            throw new Exception("Unable to load vendor media.");
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

    private String loadVendorPageImageGallery_sql =
            "SELECT " +
                    "   vpi.id AS image_id, " +
                    "   vpi.filename, " +
                    "   vpi.vendor_id, " +
                    "   vpi.display_order AS image_display_order, " +
                    "   vpig.id AS gallery_id, " +
                    "   vpig.hex_color, " +
                    "   vpig.name, " +
                    "   vpig.display_order AS gallery_display_order " +
                    "FROM " +
                    "   vendor_page_image_galleries vpig " +
                    "LEFT JOIN " +
                    "   vendor_page_images vpi " +
                    "ON " +
                    "   vpig.id = vpi.gallery_id " +
                    "WHERE " +
                    "   vpig.id = ?";

    public VendorPageImageGallery loadVendorPageImageGallery(
            int gallery_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.DAO.prepareStatement(this.loadVendorPageImageGallery_sql);
            preparedStatement.setInt(1, gallery_id);
            resultSet = preparedStatement.executeQuery();
            VendorPageImageGallery vendorPageImageGallery = new VendorPageImageGallery();
            Color color = new Color();
            while (resultSet.next()) {
                VendorPageImage vendorPageImage = new VendorPageImage();
                vendorPageImage.id = resultSet.getInt("image_id");
                vendorPageImage.vendor_id = resultSet.getInt("vendor_id");
                vendorPageImage.display_order = resultSet.getInt("image_display_order");
                vendorPageImage.gallery_id = resultSet.getInt("gallery_id");
                vendorPageImage.filename = resultSet.getString("filename");
                if (vendorPageImageGallery.id == 0) { // not set yet.
                    vendorPageImageGallery.id = resultSet.getInt("gallery_id");
                    vendorPageImageGallery.display_order = resultSet.getInt("gallery_display_order");
                    vendorPageImageGallery.hex_color = resultSet.getString("hex_color");
                    vendorPageImageGallery.text_color = color.getInverseBW(vendorPageImageGallery.hex_color);
                    vendorPageImageGallery.name = resultSet.getString("name");
                }
                vendorPageImageGallery.vendorPageImages.put(vendorPageImage.id, vendorPageImage);
                vendorPageImageGallery.image_count++;
            }
            return vendorPageImageGallery;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why
            throw new Exception("Unable to load vendor page image gallery.");
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
}
