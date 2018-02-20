package com.VendorMenu.Beers;

import com.Common.AbstractController;
import com.Common.Beer;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BeerController extends AbstractController {

    public boolean deleteBeer(
            String cookie,
            int id
    ) throws Exception {
        BeerModel beerModel = new BeerModel();
        return beerModel.deleteBeer(
                cookie,
                id
        );
    }

    public boolean updateBeer (
            String cookie,
            int id,
            String name,
            int color,
            int bitterness,
            int abv,
            String beer_style,
            String[] beer_tastes,
            String description,
            float price,
            String[] beer_sizes,
            String hop_score,
            int beer_category_id,
            int nutritional_fact_id,
            int beer_tag_id_one,
            int beer_tag_id_two,
            int beer_tag_id_three,
            int beer_tag_id_four,
            int beer_tag_id_five
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "beer_id");
        this.validateString(name, "name");
        this.validateBeerColor(color);
        this.validateBitterness(bitterness);
        this.validateAbv(abv);
        this.validateBeerStyle(beer_style);
        this.validateID(beer_category_id, "beer_category_id");
        // Validate for null (empty) ids for tags. JAVA SOAP will make this zero.
        this.validateNullID(nutritional_fact_id, "nutritional_fact_id");
        this.validateNullID(beer_tag_id_one, "beer_tag_id_one");
        this.validateNullID(beer_tag_id_two, "beer_tag_id_two");
        this.validateNullID(beer_tag_id_three, "beer_tag_id_three");
        this.validateNullID(beer_tag_id_four, "beer_tag_id_four");
        this.validateNullID(beer_tag_id_five, "beer_tag_id_five");
        if (beer_tastes.length == 0) {
            throw new Exception("Must have at least one \"beer taste\".");
        }
        for (String beer_taste: beer_tastes) {
            this.validateBeerTaste(beer_taste);
        }
        this.validateText(description, "description");
        if (beer_sizes.length == 0) {
            throw new Exception("Must have at least one \"beer size\".");
        }
        for (String beer_size: beer_sizes) {
            this.validateBeerSize(beer_size);
        }
        this.validateHopScore(hop_score);
        this.validatePrice(price);
        // Initialize model and create the data.
        BeerModel beerModel = new BeerModel();
        return beerModel.updateBeer(
                cookie,
                id,
                name,
                color,
                bitterness,
                abv,
                beer_style,
                beer_tastes,
                description,
                price,
                beer_sizes,
                hop_score,
                beer_category_id,
                nutritional_fact_id,
                beer_tag_id_one,
                beer_tag_id_two,
                beer_tag_id_three,
                beer_tag_id_four,
                beer_tag_id_five
        );
    }

    public int createBeer(
            String cookie,
            String name,
            int color,
            int bitterness,
            int abv,
            String beer_style,
            String[] beer_tastes,
            String description,
            float price,
            String[] beer_sizes,
            String hop_score,
            int beer_category_id,
            int nutritional_fact_id,
            int beer_tag_id_one,
            int beer_tag_id_two,
            int beer_tag_id_three,
            int beer_tag_id_four,
            int beer_tag_id_five
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateBeerColor(color);
        this.validateBitterness(bitterness);
        this.validateAbv(abv);
        this.validateBeerStyle(beer_style);
        this.validateID(beer_category_id, "beer_category_id");
        // Validate for null (empty) ids for tags. JAVA SOAP will make this zero.
        this.validateNullID(nutritional_fact_id, "nutritional_fact_id");
        this.validateNullID(beer_tag_id_one, "beer_tag_id_one");
        this.validateNullID(beer_tag_id_two, "beer_tag_id_two");
        this.validateNullID(beer_tag_id_three, "beer_tag_id_three");
        this.validateNullID(beer_tag_id_four, "beer_tag_id_four");
        this.validateNullID(beer_tag_id_five, "beer_tag_id_five");
        if (beer_tastes.length == 0) {
            throw new Exception("Must have at least one \"beer taste\".");
        }
        for (String beer_taste: beer_tastes) {
            this.validateBeerTaste(beer_taste);
        }
        this.validateText(description, "description");
        if (beer_sizes.length == 0) {
            throw new Exception("Must have at least one \"beer size\".");
        }
        for (String beer_size: beer_sizes) {
            this.validateBeerSize(beer_size);
        }
        this.validateHopScore(hop_score);
        this.validatePrice(price);
        // Initialize model and create the data.
        BeerModel beerModel = new BeerModel();
        return beerModel.createBeer(
                cookie,
                name,
                color,
                bitterness,
                abv,
                beer_style,
                beer_tastes,
                description,
                price,
                beer_sizes,
                hop_score,
                beer_category_id,
                nutritional_fact_id,
                beer_tag_id_one,
                beer_tag_id_two,
                beer_tag_id_three,
                beer_tag_id_four,
                beer_tag_id_five
        );
    }

    public int createBeerCategory(
            String cookie,
            String category_name,
            String hex_color
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateHexColor(hex_color);
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.createBeerCategory(
                cookie,
                category_name,
                hex_color
        );
    }

    public boolean updateBeerCategory(
            String cookie,
            int id,
            String new_category_name,
            String new_hex_color
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(new_category_name, "new_category_name.");
        this.validateHexColor(new_hex_color);
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.updateBeerCategory(
                cookie,
                id,
                new_category_name,
                new_hex_color
        );
    }

    public boolean deleteBeerCategory(
            String cookie,
            int id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "beer_category_id");
        // Initialize mode and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.deleteBeerCategory(
                cookie,
                id
        );
    }

    public String uploadBeerImage (
        String cookie,
        String filename,
        int beer_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(filename, "filename");
        // Initialze model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.uploadBeerImage(
                cookie,
                filename,
                beer_id
        );
    }

    public boolean updateBeerImage (
            String cookie,
            int beer_image_id,
            int display_order
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(beer_image_id, "beer_image_id");
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.updateBeerImage(
                cookie,
                beer_image_id,
                display_order
        );
    }

    public String deleteBeerImage (
            String cookie,
            int beer_image_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(beer_image_id, "beer_image_id");
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.deleteBeerImage(
                cookie,
                beer_image_id
        );
    }

    public int createBeerTag (
            String cookie,
            String name,
            String hex_color,
            String tag_type
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateHexColor(hex_color);
        this.validateMenuItemTagType(tag_type);
        // Initialize model and create beer tag.
        BeerModel beerModel = new BeerModel();
        return beerModel.createBeerTag(
                cookie,
                name,
                hex_color,
                tag_type
        );
    }

    public Boolean updateBeerTag (
            String cookie,
            int id,
            String new_name,
            String new_hex_color,
            String new_tag_type
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "beer_tag_id");
        this.validateString(new_name, "new_name");
        this.validateHexColor(new_hex_color);
        this.validateMenuItemTagType(new_tag_type);
        // Initialize model and return model response.
        BeerModel beerModel = new BeerModel();
        return beerModel.updateBeerTag(
                cookie,
                id,
                new_name,
                new_hex_color,
                new_tag_type
        );
    }

    public Boolean deleteBeerTag (
            String cookie,
            int id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "beer_tag_id");
        // Initialize model and return model response.
        BeerModel beerModel = new BeerModel();
        return beerModel.deleteBeerTag(
                cookie,
                id
        );
    }

}
