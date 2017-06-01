package com.UserSocial.UserMessaging;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class UserMessagingController extends AbstractController {
    public int createUserMessagingRsvp(
            String cookie,
            int reciever_account_id,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(reciever_account_id, "reciever_account_id");
        this.validateUserMessage(content);
        // Initialize model and create the data.
        UserMessagingModel userMessagingModel = new UserMessagingModel();
        return userMessagingModel.createUserMessage(
                cookie,
                reciever_account_id,
                content
        );
    }
}

