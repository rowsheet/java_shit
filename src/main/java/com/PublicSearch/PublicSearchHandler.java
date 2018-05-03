package com.PublicSearch;

import com.PublicSearch.BasicSearch.BasicSearchController;
import com.PublicSearch.Beer.BeerController;
import com.PublicSearch.Brewery.BreweryController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 6/30/17.
 */
@WebService
public class PublicSearchHandler {
    @WebMethod
    public String basicSearchVendors(
            @WebParam(name="keywords") String[] keywords,
            // @WebParam(name="limit") int limit, always load 9 at a time for now.
            @WebParam(name="offset") int offset
    ) throws Exception {
        BasicSearchController basicSearchController = new BasicSearchController();
        return basicSearchController.basicSearchVendors(
                keywords,
                5,
                offset
        );
    }

    @WebMethod
    public String basicSearchBeers(
            @WebParam(name="keywords") String[] keywords,
            // @WebParam(name="limit") int limit, always load 9 at a time for now.
            @WebParam(name="offset") int offset
    ) throws Exception {
        BasicSearchController basicSearchController = new BasicSearchController();
        return basicSearchController.basicSearchBeers(
                keywords,
                5,
                offset
        );
    }

    @WebMethod
    public String basicSearchFoods(
            @WebParam(name="keywords") String[] keywords,
            // @WebParam(name="limit") int limit, always load 9 at a time for now.
            @WebParam(name="offset") int offset
    ) throws Exception {
        BasicSearchController basicSearchController = new BasicSearchController();
        return basicSearchController.basicSearchFoods(
                keywords,
                5,
                offset
        );
    }

    @WebMethod
    public String basicSearchDrinks(
            @WebParam(name="keywords") String[] keywords,
            // @WebParam(name="limit") int limit, always load 9 at a time for now.
            @WebParam(name="offset") int offset
    ) throws Exception {
        BasicSearchController basicSearchController = new BasicSearchController();
        return  basicSearchController.basicSearchDrinks(
                keywords,
                5,
                offset
        );
    }

/*
    @WebMethod
    public String searchBeers(
            @WebParam(name="min_color") int min_color,
            @WebParam(name="max_color") int max_color,
            @WebParam(name="min_bitterness") int min_bitterness,
            @WebParam(name="max_bitterness") int max_bitterness,
            @WebParam(name="min_abv") int min_abv,
            @WebParam(name="max_abv") int max_abv,
            @WebParam(name="styles") String[] styles,
            @WebParam(name="tastes") String[] tastes,
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.searchBeers(
                min_color,
                max_color,
                min_bitterness,
                max_bitterness,
                min_abv,
                max_abv,
                styles,
                tastes,
                limit,
                offset
        );
    }
*/
/*
    @WebMethod
    public String searchBreweries(
        @WebParam(name="min_occupancy") int min_occupancy,
        @WebParam(name="max_occupancy") int max_occupancy,
        @WebParam(name="brewery_has") String[] brewery_has,
        @WebParam(name="brewery_friendly") String[] brewery_friendly,
        @WebParam(name="open_now") boolean open_now,
        @WebParam(name="latitude") float latitude,
        @WebParam(name="longitude") float longitude,
        @WebParam(name="radius") float radius,
        @WebParam(name="limit") int limit,
        @WebParam(name="offset") int offset
    ) throws Exception {
        System.out.println(min_occupancy);
        System.out.println(max_occupancy);
        System.out.println(brewery_has);
        System.out.println(brewery_friendly);
        System.out.println(open_now);
        System.out.println(latitude);
        System.out.println(longitude);
        System.out.println(radius);
        System.out.println(limit);
        System.out.println(offset);
        BreweryController breweryController = new BreweryController();
        return breweryController.searchBreweries(
                min_occupancy,
                max_occupancy,
                brewery_has,
                brewery_friendly,
                open_now,
                latitude,
                longitude,
                radius,
                limit,
                offset
        );
    }
*/
}
