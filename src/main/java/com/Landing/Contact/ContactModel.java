package com.Landing.Contact;

import com.Common.AbstractModel;

import java.sql.PreparedStatement;

public class ContactModel extends AbstractModel {

    public ContactModel() throws Exception {}

    private String submitContactFormSQL_stage1 =
        "INSERT INTO " +
                "   contact_form " +
                "(" +
                "   business_name, " +
                "   name, " +
                "   email, " +
                "   state, " +
                "   phone_number, " +
                "   info_text, " +
                "   remote_address" +
                ") VALUES (" +
                "?,?,?,?,?,?,?)";

    public void submitContactForm(
            String remote_address,
            String business_name,
            String name,
            String email,
            String state,
            String phone_number,
            String info_text
    ) throws Exception {
        PreparedStatement stage1 = null;
        try {
            stage1 = this.DAO.prepareStatement(this.submitContactFormSQL_stage1);
            stage1.setString(1, business_name);
            stage1.setString(2, name);
            stage1.setString(3, email);
            stage1.setString(4, state);
            stage1.setString(5, phone_number);
            stage1.setString(6, info_text);
            stage1.setString(7, remote_address);
            stage1.execute();
        } catch (Exception ex) {
            System.out.println(ex);
            // Try to make this message user friendly.
            throw new Exception("Oops! Something went wrong! Please try again later.");
        }
    }
}
