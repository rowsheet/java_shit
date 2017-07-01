package com.PublicSearch;

import com.PublicSearch.Beer.BeerController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 6/30/17.
 */
@WebService
public class PublicSearchHandler {
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
        for (int i = 0; i < styles.length; i++) {
            System.out.println(styles[i]);
        }
        for (int i = 0; i < tastes.length; i++) {
            System.out.println(tastes[i]);
        }
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
}
