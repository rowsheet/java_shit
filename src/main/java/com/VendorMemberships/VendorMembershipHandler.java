package com.VendorMemberships;

import com.VendorMemberships.Memberships.MembershipController;
import com.VendorMemberships.Promotions.PromotionController;
import com.VendorMemberships.Promotions.PromotionModel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class VendorMembershipHandler {
    @WebMethod
    public String Testing(
            @WebParam(name="something") String something
    ) {
        return "VALUE:" + something;
    }

    @WebMethod
    public int createMembership(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="name") String name,
            @WebParam(name="description") String description,
            @WebParam(name="membership_categories") String[] membership_categories
    ) throws Exception {
        MembershipController membershipController = new MembershipController();
        return membershipController.createMembership(
            cookie,
            name,
            description,
            membership_categories
        );
    }

    @WebMethod
    public int createPromotion(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="membership_id") int membership_id,
            @WebParam(name="title") String title,
            @WebParam(name="description") String description,
            @WebParam(name="start_date") String start_date,
            @WebParam(name="end_date") String end_date,
            @WebParam(name="max_accounts") int max_accounts,
            @WebParam(name="promotion_categories") String[] promotion_categories
    ) throws Exception {
        PromotionController promotionController = new PromotionController();
        return promotionController.createPromotion(
            cookie,
            membership_id,
            title,
            description,
            start_date,
            end_date,
            max_accounts,
            promotion_categories
        );
    }
}
