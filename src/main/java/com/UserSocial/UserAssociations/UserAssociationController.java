package com.UserSocial.UserAssociations;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class UserAssociationController extends AbstractController {
    public int createUserAssociationRsvp(
            String cookie,
            int secondary_account_id,
            String status
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(secondary_account_id, "secondary_account_id");
        this.validateUserAssociationStatus(status);
        // Initialize model and create the data.
        UserAssociationModel userAssociationModel = new UserAssociationModel();
        return userAssociationModel.createUserAssociation(
            cookie,
            secondary_account_id,
            status
        );
    }
}

