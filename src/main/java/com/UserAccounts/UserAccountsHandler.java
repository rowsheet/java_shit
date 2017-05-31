package com.UserAccounts;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.rmi.RemoteException;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class UserAccountsHandler {

    @WebMethod
    public String Register (
            @WebParam(name="email_address") String email_address,
            @WebParam(name="password") String password,
            @WebParam(name="confirm_password") String confirm_password
    ) throws Exception {
        return "email_address: " + email_address + "\n" +
                "password: " + password + "\n" +
                "confirm_password" + confirm_password;
    }

    @WebMethod
    public String Unregister(
            @WebParam(name="email_address") String email_address
    ) {
        return "Something";
    }

    @WebMethod
    public String Login(
            @WebParam(name="email_address") String email_address,
            @WebParam(name="password") String password
    ) {
        return "Something";
    }

    @WebMethod
    public String ReadSession(
            @WebParam(name="cookie") String cookie
    ) {
        return "Something";
    }

    @WebMethod
    public String UpdateSession(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="session_json") String session_json
    ) {
        return "Something";
    }

    @WebMethod
    public String Logout(
            @WebParam(name="cookie") String cookie
    ) {
        return "Something";
    }
}
