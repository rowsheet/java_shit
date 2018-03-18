package com.VendorMenu.Beers;

import com.Common.AbstractController;
import com.Common.Beer;

import java.util.ArrayList;

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
        this.validateNullID(nutritional_fact_id, "id");
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
        // Remove possible duplicate tags.
        ArrayList<Integer> tag_array = this.filterTagArray(
                beer_tag_id_one,
                beer_tag_id_two,
                beer_tag_id_three,
                beer_tag_id_four,
                beer_tag_id_five
        );
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
                tag_array.get(0),
                tag_array.get(1),
                tag_array.get(2),
                tag_array.get(3),
                tag_array.get(4)
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
        this.validateNullID(nutritional_fact_id, "id");
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
        // Remove possible duplicate tags.
        ArrayList<Integer> tag_array = this.filterTagArray(
                beer_tag_id_one,
                beer_tag_id_two,
                beer_tag_id_three,
                beer_tag_id_four,
                beer_tag_id_five
        );
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
                tag_array.get(0),
                tag_array.get(1),
                tag_array.get(2),
                tag_array.get(3),
                tag_array.get(4)
        );
    }

    public int createBeerCategory(
            String cookie,
            String name,
            String hex_color,
            String description
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateHexColor(hex_color);
        this.validateString(name, "name");
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.createBeerCategory(
                cookie,
                name,
                hex_color,
                description
        );
    }

    public boolean updateBeerCategory(
            String cookie,
            int id,
            String name,
            String hex_color,
            String description
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "new_category_name.");
        this.validateHexColor(hex_color);
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.updateBeerCategory(
                cookie,
                id,
                name,
                hex_color,
                description
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

    public int uploadBeerImage (
        String cookie,
        String file_path,
        int beer_id
    ) throws Exception {
        // Validate input parameters.
        this.validateID(beer_id, "beer_id");
        this.validateString(cookie, "cookie");
        this.validateString(file_path, "file_path");
        // Initialze model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.uploadBeerImage(
                cookie,
                file_path,
                beer_id
        );
    }

    public boolean updateBeerImages (
            String cookie,
            int[] image_ids
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        for (int i = 0; i < image_ids.length; i++) {
            this.validateID(image_ids[i], "image_id");
        }
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.updateBeerImages(
                cookie,
                image_ids
        );
    }

    public boolean deleteBeerImage (
            String cookie,
            int id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "beer_image_id");
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.deleteBeerImage(
                cookie,
                id
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
        this.validateID(id, "id");
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
        this.validateID(id, "id");
        // Initialize model and return model response.
        BeerModel beerModel = new BeerModel();
        return beerModel.deleteBeerTag(
                cookie,
                id
        );
    }

}
