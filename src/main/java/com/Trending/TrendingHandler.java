package com.Trending;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/*
 * Created by alexanderkleinhans on 7/8/17.
 */
@WebService
public class TrendingHandler {

    @WebMethod
    public String say_hi() throws Exception {
        return "HI";
    }
    /**
     * Fetches most newly reviewed beers.
     *
     * @return
     */
/* DEPRECIATED (not using stars anymore)
    @WebMethod
    public String fetchTrendingOverview() throws Exception {
        BeersController beersController = new BeersController();
        return beersController.fetchTrendingOverview();
    }
*/

    /**
     * Fetches most newly reviewed beers.
     *
     * @param limit
     * @return
     */
/* DEPRECIATED (not using stars anymore)
    @WebMethod
    public String fetchTrendingBeers (
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset
    ) throws Exception {
        BeersController beersController = new BeersController();
        return beersController.fetchTrendingBeers(
                limit,
                offset
        );
    }
*/

    /**
     * Fetchest beers with the highest comments.
     *
     * @param limit
     * @return
     */
/* DEPRECIATED (not using stars anymore)
    @WebMethod
    public String fetchTopBeers (
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset
    ) throws Exception {
        BeersController beersController = new BeersController();
        return beersController.fetchTopBeers(
                limit,
                offset
        );
    }
*/

    /**
     * Fetchest beers with the newst creation_timestamps.
     *
     * @param limit
     * @return
     */
/* DEPRECIATED (not using stars anymore)
    @WebMethod
    public String fetchNewetBeers (
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset
    ) throws Exception {
        BeersController beersController = new BeersController();
        return beersController.fetchNewetBeers(
                limit,
                offset
        );
    }
*/

}

