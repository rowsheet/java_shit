package com.PublicSearch.Brewery;

import com.Common.AbstractModel;
import com.Common.Brewery;
import com.Common.BrewerySearchResult;
import com.Common.Geolocation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;

/**
 * Created by alexanderkleinhans on 6/30/17.
 */
public class BreweryModel extends AbstractModel {

    public BreweryModel() throws Exception {}

    private String searchBreweriesSQL_stage1 =
            "SELECT " +
                    "   v.id AS vendor_id, " +
                    "   vi.display_name AS display_name, " +
                    "<%miles_away_column%>" + // Conditional as long as lat or long set in search query.
                    "   v.latitude AS latitude, " +
                    "   v.longitude AS longitude " +
                    "FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   vendor_info vi " +
                    "ON " +
                    "   vi.vendor_id = v.id " +
                    "WHERE " +
                    "   v.usual_est_occupancy >= ? AND " +
                    "   v.usual_est_occupancy <= ? " +
                    "<%brewery_has_clause%> " +
                    "<%brewery_friendly_clause%> " +
                    "<%geolocation_clause%> " +
                    "<%open_now_clause%> " +
                    "LIMIT ? OFFSET ?";

    /**
     * Does search for breweries based on criteria.
     * First loads the brewery_ids based on an offset-limit,
     * then loads breweries from in clause using public brewery model.
     *
     *
     *      1) Get all brewery_ids that are relevant.
     *      2) Fetch all breweries.
     *
     * @param min_occupancy
     * @param max_occupancy
     * @param brewery_has
     * @param brewery_friendly
     * @param open_now
     * @param latitude
     * @param longitude
     * @param radius
     * @return
     * @throws Exception
     */
    public BrewerySearchResult searchBreweries (
        int min_occupancy,
        int max_occupancy,
        String[] brewery_has,
        String[] brewery_friendly,
        boolean open_now,
        float latitude,
        float longitude,
        float radius,
        int limit,
        int offset
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        BrewerySearchResult brewerySearchResult = new BrewerySearchResult();
        try {
            String brewery_has_clause = "";
            if (brewery_has.length > 0) {
                if (!brewery_has[0].equals("Any")) {
                    brewery_has_clause += " AND (";
                    for (int i = 0; i < brewery_has.length; i++) {
                        brewery_has_clause += "'" + brewery_has[i] + "' = ANY(vi.brewery_has) ";
                        if (i < (brewery_has.length - 1)) {
                            brewery_has_clause += " OR ";
                        }
                    }
                    brewery_has_clause += ")";
                } // If not, don't add any conditions for "has" because the user picked them all.
            } // If not, don't add any conditions for "has" because they weren't specified.
            this.searchBreweriesSQL_stage1 = this.searchBreweriesSQL_stage1.replace(
                    "<%brewery_has_clause%>",
                    brewery_has_clause);
            String brewery_friendly_clause = "";
            if (brewery_friendly.length > 0) {
                if (!brewery_friendly[0].equals("Any")) {
                    brewery_friendly_clause+= " AND (";
                    for (int i = 0; i < brewery_friendly.length; i++) {
                        brewery_friendly_clause += "'" + brewery_friendly[i] + "' = ANY(vi.brewery_friendly) ";
                        if (i < (brewery_friendly.length - 1)) {
                            brewery_friendly_clause += " OR ";
                        }
                    }
                    brewery_friendly_clause += ")";
                } // If not, don't add any conditions for "friendly" because the user picked them all.
            } // If not, don't add any conditions for "friendly" because they weren't specified.
            this.searchBreweriesSQL_stage1 = this.searchBreweriesSQL_stage1.replace(
                    "<%brewery_friendly_clause%>",
                    brewery_friendly_clause);
            // Check if geolocation clause needs to be set. If latitude or longitude
            // are zero, this means no geolocation was set. Just return any.
            // If it does, also fetch how many miles away the brewer is for search
            // query specified lat long points.
            String geolocation_clause = "";
            String miles_away_column = "";
            if (latitude != 0 || longitude != 0) {
                // Don't need to prepare statement because latitude, longitude and radius have
                // already been validated in the controller. SQL should not be possible here.
                String latitude_string = Float.toString(latitude);
                String longitude_string = Float.toString(longitude);
                radius = radius * (float)1609.34; // Postgres needs radius in feet.
                String radius_string = Float.toString(radius);
                geolocation_clause += " AND st_distance_sphere(v.geolocation, st_makepoint(" +
                        latitude_string + "," + longitude_string + ")) < " + radius_string + " ";
                miles_away_column = "   st_distance_sphere(v.geolocation, st_makepoint(" +
                        latitude_string + "," + longitude_string + ")) / 1609.34 AS miles_away, ";
            }
            this.searchBreweriesSQL_stage1 = this.searchBreweriesSQL_stage1.replace(
                    "<%geolocation_clause%>",
                    geolocation_clause);
            this.searchBreweriesSQL_stage1 = this.searchBreweriesSQL_stage1.replace(
                    "<%miles_away_column%>",
                    miles_away_column);
            // Add the open_now clause.
            String open_now_clause = "";
            if (open_now) {
                // Get todays weekday.
                int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                String weekday_prefix = "";
                switch (day) {
                    case Calendar.SUNDAY:
                        weekday_prefix = "sun";
                        break;
                    case Calendar.MONDAY:
                        weekday_prefix = "mon";
                        break;
                    case Calendar.TUESDAY:
                        weekday_prefix = "tue";
                        break;
                    case Calendar.WEDNESDAY:
                        weekday_prefix = "wed";
                        break;
                    case Calendar.THURSDAY:
                        weekday_prefix = "thu";
                        break;
                    case Calendar.FRIDAY:
                        weekday_prefix = "fri";
                        break;
                    case Calendar.SATURDAY:
                        weekday_prefix = "sat";
                        break;
                }
                open_now_clause = " AND vi.<%weekday_prefix%>_close < LOCALTIME";
                open_now_clause = open_now_clause.replace(
                        "<%weekday_prefix%>",
                        weekday_prefix);
            }
            this.searchBreweriesSQL_stage1 = this.searchBreweriesSQL_stage1.replace(
                    "<%open_now_clause%>",
                    open_now_clause);
            stage1 = this.DAO.prepareStatement(this.searchBreweriesSQL_stage1);
            stage1.setInt(1, min_occupancy);
            stage1.setInt(2, max_occupancy);
            stage1.setInt(3, limit);
            stage1.setInt(4, offset);
            stage1Result = stage1.executeQuery();
            // Get object geolocations.
            HashMap<Integer, Geolocation> geolocationHashMap = new HashMap<Integer, Geolocation>();
            // Get IDs in clause.
            ArrayList<Integer> brewery_ids = new ArrayList<Integer>();
            while (stage1Result.next()) {
                Geolocation geolocation = new Geolocation();
                geolocation.name = stage1Result.getString("display_name");
                geolocation.latitude = stage1Result.getFloat("latitude");
                geolocation.longitude = stage1Result.getFloat("longitude");
                // If geolocation was a search term (specicified), we should have the
                // "miles away" term.
                if (latitude != 0 || longitude != 0) {
                    geolocation.miles_away = stage1Result.getFloat("miles_away");
                }
                int vendor_id = stage1Result.getInt("vendor_id");
                geolocationHashMap.put(vendor_id, geolocation);
                brewery_ids.add(vendor_id);
            }
            if (brewery_ids.size() == 0) {
                // No results found.
                return brewerySearchResult;
            }
            brewerySearchResult.geolocations = geolocationHashMap;
            // Load the actual breweries.
            // We're going to use the public brewery model (sorry...).
            int review_limit = 20; // Just limit loading top 20 reviews for brewery search.
            int image_limit = 30; // Just limit loading top 30 images for now.
            for (int brewery_id: brewery_ids) {
                Brewery brewery = new Brewery();
                com.PublicBrewery.Brewery.BreweryModel publicBreweryModel = new com.PublicBrewery.Brewery.BreweryModel();
                brewery = publicBreweryModel.loadBreweryInfoForSearch(
                        brewery_id,
                        review_limit,
                        image_limit
                );
                brewerySearchResult.breweries.put(brewery_id, brewery);
            }
            return brewerySearchResult;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Unknown error.
            throw new Exception("Unable to search breweries at this time.");
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
