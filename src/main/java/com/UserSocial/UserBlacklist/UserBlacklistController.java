package com.UserSocial.UserBlacklist;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class UserBlacklistController extends AbstractController {
    public int createUserBlacklistRsvp(
            String cookie,
            int secondary_account_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(secondary_account_id, "secondary_account_id");
        // Initialize model and create the data.
        UserBlacklistModel userAssociationModel = new UserBlacklistModel();
        return userAssociationModel.createUserBlacklist(
                cookie,
                secondary_account_id
        );
    }
}

