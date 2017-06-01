package com.VendorMemberships.Memberships;

import com.Common.AbstractController;
import com.VendorMemberships.Memberships.MembershipModel;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class MembershipController extends AbstractController {

    public int createMembership(
            String cookie,
            String name,
            String description,
            String[] membership_categories
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateText(description, "description");
        for (String membership_category: membership_categories) {
            this.validateMembershipCategory(membership_category);
        }
        // Initialize model and create the data.
        MembershipModel membershipModel = new MembershipModel();
        return membershipModel.createMembership(
                cookie,
                name,
                description,
                membership_categories
        );
    }
}

