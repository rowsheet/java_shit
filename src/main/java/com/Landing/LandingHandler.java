package com.Landing;

import com.Landing.Contact.ContactController;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@WebService
public class LandingHandler {
    @WebMethod
    public void submitContactForm (
            @WebParam(name="remote_address") String remote_address,
            @WebParam(name="business_name") String business_name,
            @WebParam(name="name") String name,
            @WebParam(name="email") String email,
            @WebParam(name="state") String state,
            @WebParam(name="phone_number") String phone_number,
            @WebParam(name="info_text") String info_text
            ) throws Exception {
        ContactController contactController = new ContactController();
        contactController.submitContactForm(
            remote_address,
            business_name,
            name,
            email,
            state,
            phone_number,
            info_text
        );
    }
}
