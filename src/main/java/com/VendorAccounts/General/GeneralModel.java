package com.VendorAccounts.General;

import com.Common.AbstractModel;
import com.Common.VendorCookie;
import com.VendorAccounts.VendorAuthentication.VendorAuthenticationModel;
import com.google.gson.Gson;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.text.SimpleDateFormat;

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
            int public_phone,
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
            stage2.setInt(17, public_phone);
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
            stage2.setInt(36, public_phone);
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
}
