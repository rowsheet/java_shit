package com.VendorEvents.Events;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class EventModel extends AbstractModel {

    private String updateEventSQL_stage2 =
            "SELECT " +
                    "   e.id " +
                    "FROM " +
                    "   events e " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   e.vendor_id = v.id " +
                    "WHERE " +
                    "   e.id = ? " +
                    "AND " +
                    "   v.id = ?" +
                    "LIMIT 1";

    private String updateEventSQL_stage3 =
            "UPDATE " +
                    "   events " +
                    "SET " +
                    "   name = ?, " +
                    "   start_date = ?, " +
                    "   end_date = ?, " +
                    "   rsvp_required = ?, " + //  maybe a future feature.
                    "   description = ?, " +
                    "   event_categories = ?::event_category[], " +
                    "   initial_est_occupancy = ?, " +
                    "   weekdays = ?::weekday[] " +
                    "WHERE " +
                    "   id = ?";

    private String createEventSQL =
            "INSERT INTO" +
                    "   events" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   name," +
                    "   start_date," +
                    "   end_date," +
                    "   rsvp_required," +
                    "   description," +
                    "   event_categories," +
                    "   initial_est_occupancy," +
                    "   weekdays" +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?,?::event_category[],?,?::weekday[])" +
                    "RETURNING id";

    public EventModel() throws Exception {}

    /**
     * Updates event item per request as long as sesion and user valid.
     *
     *      1) Validates session and permission.
     *      2) Ensures resource ownership.
     *      3) Updates data.
     *
     * Do all in a transaction or roll back.
     *
     * @param cookie
     * @param id
     * @param name
     * @param start_date
     * @param end_date
     * @param rsvp_required
     * @param description
     * @param event_categories
     * @param initial_est_occupancy
     * @param weekdays
     * @return
     * @throws Exception
     */
    public boolean updateEvent (
            String cookie,
            int id,
            String name,
            String start_date,
            String end_date,
            boolean rsvp_required,
            String description,
            String[] event_categories,
            int initial_est_occupancy,
            String[] weekdays
    ) throws Exception {
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            /*
            Disable auto-commit.
             */
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
            Validate session.
             */
            this.validateCookieVendorFeature(cookie, "events");
            /*
            Stage 2
            Ensure resource owndership.
             */
            stage2 = this.DAO.prepareStatement(this.updateEventSQL_stage2);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int food_id = 0;
            while (stage2Result.next()) {
                food_id = stage2Result.getInt("id");
            }
            if (food_id == 0) {
                // Event is not owned by sender of this request. This might
                // be some malicious shit. Record it and blacklist.
                // @TODO blacklist this request.
                throw new EventException("This account does not have have permission to change this beer."); // Fuck off
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.updateEventSQL_stage3);
            stage3.setString(1, name);
            stage3.setDate(2, Date.valueOf(start_date));
            stage3.setDate(3, Date.valueOf(end_date));
            stage3.setBoolean(4, false); // maybe a future feature.
            stage3.setString(5, description);
            stage3.setArray(6, this.DAO.createArrayOf("event_category", event_categories));
            stage3.setInt(7, initial_est_occupancy);
            stage3.setArray(8, this.DAO.createArrayOf("weekday", weekdays));
            stage3.setInt(9, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (EventException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            this.DAO.rollback();
            // Try to parse exception message.
            if (ex.getMessage().contains("events_vendor_id_name_idx") &&
                    ex.getMessage().contains("already exists")) {
                throw new Exception("This brewery already has an event with that name!");
            }
            if (ex.getMessage().contains("events_date_check")) {
                throw new Exception("End date of event must be after start date!");
            }
            // Unknown reason.
            throw new Exception("Unable to update event.");
        } finally {
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public int createEvent(
            String cookie,
            String name,
            String start_date,
            String end_date,
            boolean rsvp_required,
            String description,
            String[] event_categories,
            int initial_est_occupancy,
            String[] weekdays
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the vendor request and make sure "events" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookieVendorFeature(cookie, "events");
            // After we insert the record, we need to get the ID of the record back.
            preparedStatement = this.DAO.prepareStatement(this.createEventSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setInt(2, this.vendorCookie.requestFeatureID);
            preparedStatement.setString(3, name);
            preparedStatement.setDate(4, Date.valueOf(start_date));
            preparedStatement.setDate(5, Date.valueOf(end_date));
            preparedStatement.setBoolean(6, rsvp_required);
            preparedStatement.setString(7, description);
            preparedStatement.setArray(8, this.DAO.createArrayOf("event_category", event_categories));
            preparedStatement.setInt(9, initial_est_occupancy);
            preparedStatement.setArray(10, this.DAO.createArrayOf("weekday", weekdays));
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int event_id = 0;
            while (resultSet.next()) {
                event_id = resultSet.getInt("id");
            }
            if (event_id == 0) {
                throw new EventException("Unable to create event.");
            }
            return event_id;
            // Return unable exception if no id found.
        } catch (EventException ex) {
            System.out.println(ex.getMessage());
            // this needs to buble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse exception message.
            if (ex.getMessage().contains("events_vendor_id_name_idx") &&
                    ex.getMessage().contains("already exists")) {
                throw new Exception("This brewery already has an event with that name!");
            }
            if (ex.getMessage().contains("events_date_check")) {
                throw new Exception("End date of event must be after start date!");
            }
            throw new Exception(ex.getMessage());
        } finally {
            // Clean up and return.
            if (preparedStatement != null) {
                preparedStatement = null;
            }
            if (resultSet != null) {
                resultSet = null;
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

}
