package com.UserSocial;

import com.UserSocial.UserAssociations.UserAssociationController;
import com.UserSocial.UserMessaging.UserMessagingController;
import com.UserSocial.UserBlacklist.UserBlacklistController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class UserSocialHandler {
    @WebMethod
    public String Testing(
            @WebParam(name="something") String something
    ) {
        return "VALUE:" + something;
    }

    @WebMethod
    public int createUserAssociation(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="secondary_account_id") int secondary_account_id,
            @WebParam(name="status") String status
    ) throws Exception {
        UserAssociationController userAssociationController = new UserAssociationController();
        return userAssociationController.createUserAssociationRsvp(
                cookie,
                secondary_account_id,
                status
        );
    }

    @WebMethod
    public int createUserMessage(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="reciever_account_id") int reciever_account_id,
            @WebParam(name="content") String content
    ) throws Exception {
        UserMessagingController userMessagingController = new UserMessagingController();
        return userMessagingController.createUserMessagingRsvp(
                cookie,
                reciever_account_id,
                content
        );
    }

    @WebMethod
    public int createUserBlacklist(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="secondary_account_id") int secondary_account_id
    ) throws Exception {
        UserBlacklistController userBlacklistController = new UserBlacklistController();
        return userBlacklistController.createUserBlacklistRsvp(
                cookie,
                secondary_account_id
        );
    }

}
