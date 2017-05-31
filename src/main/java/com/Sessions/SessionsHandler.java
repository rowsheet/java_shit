package com.Sessions;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class SessionsHandler {
    @WebMethod
    public String getSession (
            @WebParam(name="cookie") String something
    ) {
        return "something";
    }
}
