package com.PublicBrewery.Brewery;

import com.Common.AbstractModel;

import com.Common.Brewery;
import com.PublicBrewery.Beer.BeerModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by alexanderkleinhans on 6/9/17.
 */
public class BreweryModel extends AbstractModel {

    private String loadBreweryInfo_stage1 =
            "SELECT " +
                    "   v.id as vendor_id, " +
                    "   vi.display_name, " +
                    "   vi.about_text, " +
                    "   TO_CHAR(vi.mon_open, 'HH:MI AM') AS mon_open," +
                    "   TO_CHAR(vi.mon_close, 'HH:MI AM') AS mon_close," +
                    "   TO_CHAR(vi.tue_open, 'HH:MI AM') AS tue_open," +
                    "   TO_CHAR(vi.tue_close, 'HH:MI AM') AS tue_close," +
                    "   TO_CHAR(vi.wed_open, 'HH:MI AM') AS wed_open," +
                    "   TO_CHAR(vi.wed_close, 'HH:MI AM') AS wed_close," +
                    "   TO_CHAR(vi.thu_open, 'HH:MI AM') AS thu_open," +
                    "   TO_CHAR(vi.thu_close, 'HH:MI AM') AS thu_close," +
                    "   TO_CHAR(vi.fri_open, 'HH:MI AM') AS fri_open," +
                    "   TO_CHAR(vi.fri_close, 'HH:MI AM') AS fri_close," +
                    "   TO_CHAR(vi.sat_open, 'HH:MI AM') AS sat_open," +
                    "   TO_CHAR(vi.sat_close, 'HH:MI AM') AS sat_close," +
                    "   TO_CHAR(vi.sun_open, 'HH:MI AM') AS sun_open," +
                    "   TO_CHAR(vi.sun_close, 'HH:MI AM') AS sun_close," +
                    "   v.street_address, " +
                    "   v.city, " +
                    "   v.state, " +
                    "   v.zip, " +
                    "   vi.public_phone, " +
                    "   vi.public_email, " +
                    "   v.official_business_name " +
                    " FROM " +
                    "   vendors v " +
                    "INNER JOIN " +
                    "   vendor_info vi " +
                    "ON " +
                    "   v.id = vi.vendor_id " +
                    "WHERE v.id = ?";

    private String loadBreweryInfo_stage2 =
            "SELECT " +
                    "   display_order, " +
                    "   filename " +
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
     *
     * @param brewry_id
     * @return Brewery
     * @throws Exception
     */
    public Brewery loadBreweryInfo(
            int brewry_id
    ) throws Exception {
        // Create statments.
        PreparedStatement stage1 = null;
        PreparedStatement stage2 = null;
        try {
            // Prepare statments.
            stage1 = this.DAO.prepareStatement(this.loadBreweryInfo_stage1);
            stage2 = this.DAO.prepareStatement(this.loadBreweryInfo_stage2);
            /*
            Stage 1
             */
            stage1.setInt(1, brewry_id);
            ResultSet stage1Result = stage1.executeQuery();
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
            ResultSet stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                int display_order = stage2Result.getInt("display_order");
                String filename = stage2Result.getString("filename");
                brewery.page_images.put(display_order, filename);
            }
            /*
            Stage 3
             */
            BeerModel beerModel = new BeerModel();
            brewery.beerMenu = beerModel.loadBeerMenu(brewry_id);
            return brewery;
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception("Unable to load brewery info");
        } finally {
            if (stage1 != null) { stage1 = null; }
            if (stage2 != null) { stage2 = null; }
        }
    }
}
