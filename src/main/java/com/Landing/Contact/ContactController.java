package com.Landing.Contact;

import com.Common.AbstractController;
import com.Landing.Contact.ContactModel;

public class ContactController extends AbstractController {
    public void submitContactForm(
        String remote_address,
        String business_name,
        String name,
        String email,
        String state,
        String phone_number,
        String info_text
    ) throws Exception {
        // Validate input parameters.
        this.validatePhone(phone_number);
        this.validateEmailAddress(email);
        this.validateState(state);
        // Blank info text if no info text.
        if (info_text == "") {
            info_text = "NA";
        }
        // Create model and execute command.
        ContactModel contactModel = new ContactModel();
        contactModel.submitContactForm(
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
