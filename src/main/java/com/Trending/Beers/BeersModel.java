package com.Trending.Beers;

import com.Common.AbstractModel;
import com.Common.BeerSearchResult;
import com.Common.BeerReview;
import com.Common.BeerImage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 7/8/17.
 */
public class BeersModel extends AbstractModel {

    public BeersModel() throws Exception {}

    private String fetchTrendingBeersSQL =
            "SELECT DISTINCT " +
                    "   beer_id, " +
                    "   MAX(creation_timestamp) " +
                    "FROM " +
                    "   beer_reviews " +
                    "GROUP BY " +
                    "   beer_id " +
                    "ORDER BY " +
                    "   MAX(creation_timestamp) DESC " +
                    "LIMIT ? OFFSET ? ";

    /**
     * Fetches most newly reviewed beers.
     *
     *      1) Select all the beer_ids based on news reviews
     *      2) User private method for fetch all beers in required info.
     *
     * @param limit
     * @return
     */
    public HashMap<Integer, BeerSearchResult> fetchTrendingBeers (
            int limit,
            int offset
    ) throws Exception {
        HashMap<Integer, BeerSearchResult> beerSearchResultHashMap = new HashMap<Integer, BeerSearchResult>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.DAO.prepareStatement(fetchTrendingBeersSQL);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();
            String beer_ids = "";
            while (resultSet.next()) {
                int beer_id = resultSet.getInt("beer_id");
                String beer_id_string = Integer.toString(beer_id);
                beer_ids += beer_id_string + ",";
            }
            beer_ids += ")";
            if (beer_ids == ")") {
                // There are no trending beers for some reason.
                return beerSearchResultHashMap;
            }
            beer_ids = beer_ids.replace(",)", ")");
            // Fetch all the beers.
            return this.fetchAllBeersFromIDs(beer_ids);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Unknown reason.
            throw new Exception("Unable to fetch trending beers.");
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

    private String fetchTopBeersSQL =
            "SELECT DISTINCT " +
                    "   beer_id, " +
                    "   AVG(stars) " +
                    "FROM " +
                    "   beer_reviews " +
                    "GROUP BY " +
                    "   beer_id " +
                    "ORDER BY " +
                    "   AVG(stars) DESC " +
                    "LIMIT ? OFFSET ? ";

    /**
     * Fetchest beers with the highest average reviews.
     *
     * @param limit
     * @return
     */
    public HashMap<Integer, BeerSearchResult> fetchTopBeers (
            int limit,
            int offset
    ) throws Exception {
        HashMap<Integer, BeerSearchResult> beerSearchResultHashMap = new HashMap<Integer, BeerSearchResult>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.DAO.prepareStatement(fetchTopBeersSQL);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();
            String beer_ids = "";
            while (resultSet.next()) {
                int beer_id = resultSet.getInt("beer_id");
                String beer_id_string = Integer.toString(beer_id);
                beer_ids += beer_id_string + ",";
            }
            beer_ids += ")";
            if (beer_ids == ")") {
                // There are no beers for some reason.
                return beerSearchResultHashMap;
            }
            beer_ids = beer_ids.replace(",)", ")");
            // Fetch all the beers.
            return this.fetchAllBeersFromIDs(beer_ids);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Unknown reason.
            throw new Exception("Unable to fetch top beers.");
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

    private String fetchNewestBeersSQL =
            "SELECT " +
                    "   id " +
                    "FROM " +
                    "   beers " +
                    "ORDER BY " +
                    "   creation_timestamp DESC " +
                    "LIMIT ? OFFSET ? ";

    /**
     * Fetchest beers with the newst creation_timestamps.
     *
     * @param limit
     * @return
     */
    public HashMap<Integer, BeerSearchResult> fetchNewetBeers (
            int limit,
            int offset
    ) throws Exception {
        HashMap<Integer, BeerSearchResult> beerSearchResultHashMap = new HashMap<Integer, BeerSearchResult>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.DAO.prepareStatement(fetchNewestBeersSQL);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();
            String beer_ids = "";
            while (resultSet.next()) {
                int beer_id = resultSet.getInt("id");
                String beer_id_string = Integer.toString(beer_id);
                beer_ids += beer_id_string + ",";
            }
            beer_ids += ")";
            if (beer_ids == ")") {
                // There are no beers for some reason.
                return beerSearchResultHashMap;
            }
            beer_ids = beer_ids.replace(",)", ")");
            // Fetch all the beers.
            return this.fetchAllBeersFromIDs(beer_ids);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Unknown reason.
            throw new Exception("Unable to fetch newest beers.");
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

    private String fetchAllBeersFromIDsSQL =
            "SELECT " +
                    "   b.id as beer_id, " +
                    "   b.vendor_id, " +
                    "   b.name, " +
                    "   b.color, " +
                    "   b.bitterness, " +
                    "   b.abv, " +
                    "   b.beer_style, " +
                    "   b.beer_tastes, " +
                    "   b.description, " +
                    "   b.price, " +
                    "   b.beer_sizes, " +
                    "   b.hop_score, " +
                    "   vi.display_name AS vendor_display_name, " +
                    "   vi.public_phone, " +
                    "   vi.public_email, " +
                    "   v.street_address, " +
                    "   v.city, " +
                    "   v.state, " +
                    "   v.zip, " +
                    // Calcualte how many days ago the beer was posted so
                    // we can put a "new" tag on it if we want.
                    "   DATE_PART('day', now()::date) - DATE_PART('day', b.creation_timestamp::date) as days_ago " +
                    "FROM " +
                    "   beers b " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   b.vendor_id = v.id " +
                    "LEFT JOIN " +
                    "   vendor_info vi " +
                    "ON " +
                    "   vi.vendor_id = v.id " +
                    "WHERE " +
                    "   b.id IN (";

    private String fetchAllBeerReviewsFromIDsSQL =
            "SELECT " +
                    "   br.id as review_id, " +
                    "   br.account_id, " +
                    "   br.beer_id, " +
                    "   br.stars, " +
                    "   br.content, " +
                    // Create the username where it's null.
                    "   COALESCE(a.username, concat('user'::text, TRIM(LEADING FROM to_char(a.id, '999999999'))::text)) as username," +
                    // Calculate how many days ago the post was posted.
                    "   DATE_PART('day', now()::date) - DATE_PART('day', br.creation_timestamp::date) as days_ago " +
                    "FROM " +
                    "   beer_reviews br " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   br.account_id = a.id " +
                    "WHERE " +
                    "   br.beer_id IN (";

    private String fetchAllBeerImagesFromIDsSQL =
            "SELECT " +
                    "   beer_id, " +
                    "   display_order, " +
                    "   filename " +
                    "FROM " +
                    "   beer_images " +
                    "WHERE " +
                    "   beer_id IN (";

    private HashMap<Integer, BeerSearchResult> fetchAllBeersFromIDs (
            String in_caluse
    ) throws Exception {
        PreparedStatement fetchAllBeersStatement = null;
        ResultSet fetchAllBeersResultSet = null;
        PreparedStatement fetchAllReviewsStatement = null;
        ResultSet fetchAllReviewsResultSet = null;
        PreparedStatement fetchAllImagesStatement = null;
        ResultSet fetchAllImagesResultSet = null;
        try {
            HashMap<Integer, BeerSearchResult> beerSearchResultHashMap = new HashMap<Integer, BeerSearchResult>();
            /*
            Fetch all beers.
             */
            this.fetchAllBeersFromIDsSQL += in_caluse;
            System.out.print(fetchAllBeersFromIDsSQL);
            fetchAllBeersStatement = this.DAO.prepareStatement(this.fetchAllBeersFromIDsSQL);
            fetchAllBeersResultSet = fetchAllBeersStatement.executeQuery();
            while (fetchAllBeersResultSet.next()) {
                BeerSearchResult beerSearchResult = new BeerSearchResult();
                beerSearchResult.beer_id = fetchAllBeersResultSet.getInt("beer_id");
                beerSearchResult.vendor_id = fetchAllBeersResultSet.getInt("vendor_id");
                beerSearchResult.name = fetchAllBeersResultSet.getString("name");
                beerSearchResult.color = fetchAllBeersResultSet.getInt("color");
                beerSearchResult.bitterness = fetchAllBeersResultSet.getInt("bitterness");
                beerSearchResult.abv = fetchAllBeersResultSet.getInt("abv");
                beerSearchResult.beer_style = fetchAllBeersResultSet.getString("beer_style");
                // Beer tastes are an array of enums in postgres.
                Array beer_tastes = fetchAllBeersResultSet.getArray("beer_tastes");
                String[] str_beer_tastes = (String[]) beer_tastes.getArray();
                beerSearchResult.beer_tastes = str_beer_tastes;
                beerSearchResult.description = fetchAllBeersResultSet.getString("description");
                beerSearchResult.price = fetchAllBeersResultSet.getFloat("price");
                // Beer sizes are an array of enums in postgres.
                Array beer_sizes = fetchAllBeersResultSet.getArray("beer_sizes");
                String[] str_beer_sizes = (String[]) beer_sizes.getArray();
                beerSearchResult.beer_sizes = str_beer_sizes;
                beerSearchResult.hop_score = fetchAllBeersResultSet.getString("hop_score");
                beerSearchResult.days_ago = fetchAllBeersResultSet.getInt("days_ago");
                // Get vendor info here too since this doesn't expand to one-to-many from the beer entity.
                beerSearchResult.vendor_display_name = fetchAllBeersResultSet.getString("vendor_display_name");
                beerSearchResult.vendor_public_phone = fetchAllBeersResultSet.getString("public_phone");
                beerSearchResult.vendor_public_email = fetchAllBeersResultSet.getString("public_email");
                beerSearchResult.vendor_street_address = fetchAllBeersResultSet.getString("street_address");
                beerSearchResult.vendor_city = fetchAllBeersResultSet.getString("city");
                beerSearchResult.vendor_state = fetchAllBeersResultSet.getString("state");
                beerSearchResult.vendor_zip = fetchAllBeersResultSet.getString("zip");
                beerSearchResultHashMap.put(beerSearchResult.beer_id, beerSearchResult);
            }
            /*
            Note:
                Last three stages are copy-pasted from PublicBrewery beer model.
                sorry! I was short on time!
             */
            /*
            Stage 2
            Get all the beer reviews where beer_ids are in as where clause.
             */
            this.fetchAllBeerReviewsFromIDsSQL += in_caluse;
            fetchAllReviewsStatement = this.DAO.prepareStatement(this.fetchAllBeerReviewsFromIDsSQL);
            fetchAllReviewsResultSet = fetchAllReviewsStatement.executeQuery();
            while (fetchAllReviewsResultSet.next()) {
                BeerReview beerReview = new BeerReview();
                beerReview.review_id = fetchAllReviewsResultSet.getInt("review_id");
                beerReview.account_id = fetchAllReviewsResultSet.getInt("account_id");
                beerReview.beer_id = fetchAllReviewsResultSet.getInt("beer_id");
                beerReview.stars = fetchAllReviewsResultSet.getInt("stars");
                beerReview.content = fetchAllReviewsResultSet.getString("content");
                beerReview.username = fetchAllReviewsResultSet.getString("username");
                beerReview.days_ago = fetchAllReviewsResultSet.getInt("days_ago");
                // Add the beer review to the appropriate beer.
                beerSearchResultHashMap.get(beerReview.beer_id).reviews.add(beerReview);
            }
            /*
            Stage 3
            Get all the images where beer_ids in where clause.
             */
            this.fetchAllBeerImagesFromIDsSQL += in_caluse;
            fetchAllImagesStatement = this.DAO.prepareStatement(this.fetchAllBeerImagesFromIDsSQL);
            fetchAllImagesResultSet = fetchAllImagesStatement.executeQuery();
            while (fetchAllImagesResultSet.next()) {
                BeerImage beerImage = new BeerImage();
                beerImage.beer_id = fetchAllImagesResultSet.getInt("beer_id");
                beerImage.display_order = fetchAllImagesResultSet.getInt("display_order");
                beerImage.filename = fetchAllImagesResultSet.getString("filename");
                beerSearchResultHashMap.get(beerImage.beer_id).images.put(beerImage.display_order, beerImage);
            }
            /*
            Stage 4
             */
            // Go through each beer and calculate the review star averages.
            for (BeerSearchResult beer : beerSearchResultHashMap.values()) {
                if (beer.reviews.size() > 0) {
                    float total = 0;
                    for (BeerReview beerReview : beer.reviews) {
                        total += (float) beerReview.stars;
                    }
                    beer.review_average = total / (float) beer.reviews.size();
                    beer.review_count = beer.reviews.size();
                }
            }
            return beerSearchResultHashMap;
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            // Unkown reason.
            throw new Exception("Unable to fetch beers.");
        } finally {
            if (fetchAllBeersStatement != null) {
                fetchAllBeersStatement.close();
            }
            if (fetchAllBeersResultSet != null) {
                fetchAllBeersResultSet.close();
            }
            if (fetchAllReviewsStatement != null) {
                fetchAllReviewsStatement.close();
            }
            if (fetchAllReviewsResultSet != null) {
                fetchAllReviewsResultSet.close();
            }
            if (fetchAllImagesStatement != null) {
                fetchAllImagesStatement.close();
            }
            if (fetchAllImagesResultSet != null) {
                fetchAllImagesStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

}
