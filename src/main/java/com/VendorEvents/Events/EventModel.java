package com.VendorEvents.Events;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class EventModel extends AbstractModel {

    private String deleteEventSQL_Stage3 =
            "DELETE FROM " +
                    "   events " +
                    "WHERE " +
                    "   id = ?";

    private String confirmEventOwnershipSQL =
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

    public boolean deleteEvent (
            String cookie,
            int id
    ) throws Exception {
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            /*
            Disable auto commit.
             */
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
            Validate session.
             */
            this.validateCookieVendorFeature(cookie, "events");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmEventOwnershipSQL);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int beer_id = 0;
            while (stage2Result.next()) {
                beer_id = stage2Result.getInt("id");
            }
            if (beer_id == 0) {
                // Beer is not owned by sender of this request. This might
                // be some malicious shit. Record it and blacklist.
                // @TODO blacklist this request.
                throw new EventException("This account does not have have permission to delete this event."); // Fuck off
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.deleteEventSQL_Stage3);
            stage3.setInt(1, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // Unknown error.
            throw new Exception("Unable to delete event.");
        } finally {
            /*
            Reset auto-commit to true.
             */
            this.DAO.setAutoCommit(true);
            /*
            Clean up.
             */
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
            stage2 = this.DAO.prepareStatement(this.confirmEventOwnershipSQL);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int event_id = 0;
            while (stage2Result.next()) {
                event_id = stage2Result.getInt("id");
            }
            if (event_id == 0) {
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

    private String verifyEventOwnershipSQL =
            "SELECT " +
                    "   COUNT(*) AS count_star " +
                    "FROM " +
                    "   events " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    private String uploadEventImageSQL_stage2 =
            "INSERT INTO " +
                    "   event_images" +
                    "(" +
                    "   event_id, " +
                    "   filename, " +
                    "   feature_id, " +
                    "   vendor_id" +
                    ") VALUES (" +
                    "?,?,?,?)";

    public String uploadEventImage(
            String cookie,
            String filename,
            int event_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            /*
            Validate feature permissions.
             */
            this.validateCookieVendorFeature(cookie, "vendor_event_images");
            /*
            Stage 1
            Validate Resource ownership.
             */
            stage1 = this.DAO.prepareStatement(this.verifyEventOwnershipSQL);
            stage1.setInt(1, event_id);
            stage1.setInt(2, this.vendorCookie.vendorID);
            stage1Result = stage1.executeQuery();
            int count_star = 0;
            while (stage1Result.next()) {
                count_star = stage1Result.getInt("count_star");
            }
            if (count_star == 0) {
                throw new EventException("You do not have permission to upload photos for this event.");
            }
            /*
            Create filepath.
            right now, it's just going to be:
                vendor_id/event_id
             */
            String file_path = Integer.toString(vendorCookie.vendorID) + "/" +
                    Integer.toString(event_id) + "/" + filename;
            /*
            Stage 2
            Insert new record.
             */
            stage2 = this.DAO.prepareStatement(this.uploadEventImageSQL_stage2);
            stage2.setInt(1, event_id);
            stage2.setString(2, filename);
            stage2.setInt(3, this.vendorCookie.requestFeatureID);
            stage2.setInt(4, this.vendorCookie.vendorID);
            stage2.execute();
            /*
            Done.
             */
            return file_path;
        } catch (EventException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            // Try to parse the exception.
            if (ex.getMessage().contains("event_id") &&
                    ex.getMessage().contains("filename") &&
                    ex.getMessage().contains("already exists")) {
                throw new Exception("This event already has an image named: " + filename  + ".");
            }
            // Unknown reason.
            throw new Exception("Unable to upload event image.");
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
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean updateEventImage(
            String cookie,
            int event_image_id,
            int display_order
    ) throws Exception {
        return true;
    }

    public String deleteEventImage(
            String cookie,
            int event_image_id
    ) throws Exception {
        return "something";
    }

}
