package com.Common;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;

import java.util.regex.Pattern;
import java.util.UUID;

import com.google.gson.*;
import jnr.ffi.annotations.In;
import org.omg.CORBA.DynAnyPackage.Invalid;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class AbstractController {

    private Gson gson;

    public AbstractController() {
        this.gson = new Gson();
    }

    /**
     * Just return the Object (I think it's a POJO) or data structure that should be in com.Common and convert
     * it to JSON.
     *
     * note: All Objects here converted should be in com.Common and have no methods. These are used by models which
     * actually do have database handles and such.
     *
     * @param object
     * @return JSON
     */
    protected String returnJSON(Object object) {
        return this.gson.toJson(object);
    }

    protected void validateString(String input, String input_name)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid " + input_name + ".");
        } else if (input.length() == 0) {
            throw new InvalidParameterException("Invalid " + input_name + ".");
        }
    }

    protected void validateDate(String input, String input_name)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid " + input_name);
        }
        Pattern pattern = Pattern.compile("^\\d{4}[-]?\\d{1,2}[-]?\\d{1,2}$");
        if (!pattern.matcher(input).matches()) {
            throw new InvalidParameterException("Invalid " + input_name);
        }
        SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        try {
            simpleDateFormat.parse(input);
        } catch (Exception e) {
            throw new InvalidParameterException("Invalid " + input_name);
        }
    }

    protected void validateTimestamp(String input, String input_name)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid " + input_name);
        }
        Pattern pattern = Pattern.compile("^\\d{4}[-]?\\d{1,2}[-]?\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}[.]?\\d{1,6}$");
        if (!pattern.matcher(input).matches()) {
            throw new InvalidParameterException("Invalid " + input_name);
        }
        SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            simpleDateFormat.parse(input);
        } catch (Exception e) {
            throw new InvalidParameterException("Invalid " + input_name);
        }
    }

    protected void validateTime(String input, String input_name)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid " + input_name);
        }
        /*
        Change this when something better comes along. 01:00 AM is valide 1:00 AM is not valid.
        needs to parse to sql time.
         */
        Pattern pattern = Pattern.compile("([0][1-9]|[1][0-2]):([0-5][0-9]) (AM|PM)");
        if (!pattern.matcher(input).matches()) {
            throw new InvalidParameterException("Invalid " + input_name);
        }
    }

    protected void validateText(String input, String input_name)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid " + input_name);
        } else if (input.length() == 0) {
            throw new InvalidParameterException("Invalid " + input_name);
        }
    }

    protected void validateID(int input, String id_name)
        throws InvalidParameterException {
        if (input == 0) {
            throw new InvalidParameterException("Invalid " + id_name + ".");
        }
    }

    protected void validateBeerColor(int input)
        throws InvalidParameterException {
        if (input < 1 || input > 40) {
            throw new InvalidParameterException("Invalid beer color, out of range.");
        }
    }

    protected void validateBitterness(int input)
        throws InvalidParameterException {
        if (input < 1 || input > 90) {
            throw new InvalidParameterException("Invalid beer bitterness, out of range.");
        }
    }

    protected void validateAbv(int input)
        throws InvalidParameterException {
        if (input < 1 || input > 20) {
            throw new InvalidParameterException("Invalid beer ABV, out of range");
        }
    }

    protected void validatePrice(float input)
        throws InvalidParameterException {
        if (input < 0.01) {
            throw new InvalidParameterException("Invalid price.");
        }
    }

    protected void validateFeedLimit(int input)
        throws InvalidParameterException {
        if ((input < 0) || (input > 50)) {
            // Limit larger that 50 is some bullshit and someone's probably trying to
            // fuck with the API should blacklist that shit or give them malicious
            // data/turn on high alert tracking.
            // @TODO handle wierdly large feed limits.
            throw new InvalidParameterException("Invalid feed limit");
        }
    }

    protected void validateUserMessage(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid user message.");
        } else if (input.length() > 1000) {
            throw new InvalidParameterException("Invalid user message.");
        }
    }

    protected void validateEmailAddress(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid email address.");
        } else if (input.length() == 0){
            throw new InvalidParameterException("Invalid email address.");
        }
        // @TODO Check email address againts this regex: (?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])
    }

    protected void validatePhone(int input)
            throws InvalidParameterException {
        // @TODO check a regex.
        if (input == 0) {
            throw new InvalidParameterException("Invalid phone number");
        }
    }

    protected void validateUUID(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid UUID.");
        }
        try{
            UUID uuid = UUID.fromString(input);
        } catch (IllegalArgumentException exception){
            throw new InvalidParameterException("Invalid UUID.");
        }
    }

    /**
     * Check if confirm_password fields in registration forms are correct and acceptable length.
     * @param password
     * @param confirm_password
     * @throws InvalidParameterException
     */
    protected void validatePasswordPair(String password, String confirm_password)
            throws InvalidParameterException {
        // Ensure the passwords are not null, not empty and between 10 and 250 chars.
        if (password == null || password == "") {
            throw new InvalidParameterException("Invalid password.");
        } else if (password.length() < 8) {
            throw new InvalidParameterException("Password is too short! Must be at least 8 characters!");
        } else if (password.length() > 250) {
            throw new InvalidParameterException("Password is too long! Must be shorter than 250 characters!");
        }
        // Ensure passwords match.
        if (!password.equals(confirm_password)) {
            throw new InvalidParameterException("Passwords do not match");
        }
    }

    protected void validateZipCode(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid zip code.");
        } else if (input.length() == 0){
            throw new InvalidParameterException("Invalid zip code.");
        }
    }

    //@TODO Country zip with this.
    /*
        "GB", "GIR[ ]?0AA|((AB|AL|B|BA|BB|BD|BH|BL|BN|BR|BS|BT|CA|CB|CF|CH|CM|CO|CR|CT|CV|CW|DA|DD|DE|DG|DH|DL|DN|DT|DY|E|EC|EH|EN|EX|FK|FY|G|GL|GY|GU|HA|HD|HG|HP|HR|HS|HU|HX|IG|IM|IP|IV|JE|KA|KT|KW|KY|L|LA|LD|LE|LL|LN|LS|LU|M|ME|MK|ML|N|NE|NG|NN|NP|NR|NW|OL|OX|PA|PE|PH|PL|PO|PR|RG|RH|RM|S|SA|SE|SG|SK|SL|SM|SN|SO|SP|SR|SS|ST|SW|SY|TA|TD|TF|TN|TQ|TR|TS|TW|UB|W|WA|WC|WD|WF|WN|WR|WS|WV|YO|ZE)(\d[\dA-Z]?[ ]?\d[ABD-HJLN-UW-Z]{2}))|BFPO[ ]?\d{1,4}"
        "JE", "JE\d[\dA-Z]?[ ]?\d[ABD-HJLN-UW-Z]{2}"
        "GG", "GY\d[\dA-Z]?[ ]?\d[ABD-HJLN-UW-Z]{2}"
        "IM", "IM\d[\dA-Z]?[ ]?\d[ABD-HJLN-UW-Z]{2}"
        "US", "\d{5}([ \-]\d{4})?"
        "CA", "[ABCEGHJKLMNPRSTVXY]\d[ABCEGHJ-NPRSTV-Z][ ]?\d[ABCEGHJ-NPRSTV-Z]\d"
        "DE", "\d{5}"
        "JP", "\d{3}-\d{4}"
        "FR", "\d{2}[ ]?\d{3}"
        "AU", "\d{4}"
        "IT", "\d{5}"
        "CH", "\d{4}"
        "AT", "\d{4}"
        "ES", "\d{5}"
        "NL", "\d{4}[ ]?[A-Z]{2}"
        "BE", "\d{4}"
        "DK", "\d{4}"
        "SE", "\d{3}[ ]?\d{2}"
        "NO", "\d{4}"
        "BR", "\d{5}[\-]?\d{3}"
        "PT", "\d{4}([\-]\d{3})?"
        "FI", "\d{5}"
        "AX", "22\d{3}"
        "KR", "\d{3}[\-]\d{3}"
        "CN", "\d{6}"
        "TW", "\d{3}(\d{2})?"
        "SG", "\d{6}"
        "DZ", "\d{5}"
        "AD", "AD\d{3}"
        "AR", "([A-HJ-NP-Z])?\d{4}([A-Z]{3})?"
        "AM", "(37)?\d{4}"
        "AZ", "\d{4}"
        "BH", "((1[0-2]|[2-9])\d{2})?"
        "BD", "\d{4}"
        "BB", "(BB\d{5})?"
        "BY", "\d{6}"
        "BM", "[A-Z]{2}[ ]?[A-Z0-9]{2}"
        "BA", "\d{5}"
        "IO", "BBND 1ZZ"
        "BN", "[A-Z]{2}[ ]?\d{4}"
        "BG", "\d{4}"
        "KH", "\d{5}"
        "CV", "\d{4}"
        "CL", "\d{7}"
        "CR", "\d{4,5}|\d{3}-\d{4}"
        "HR", "\d{5}"
        "CY", "\d{4}"
        "CZ", "\d{3}[ ]?\d{2}"
        "DO", "\d{5}"
        "EC", "([A-Z]\d{4}[A-Z]|(?:[A-Z]{2})?\d{6})?"
        "EG", "\d{5}"
        "EE", "\d{5}"
        "FO", "\d{3}"
        "GE", "\d{4}"
        "GR", "\d{3}[ ]?\d{2}"
        "GL", "39\d{2}"
        "GT", "\d{5}"
        "HT", "\d{4}"
        "HN", "(?:\d{5})?"
        "HU", "\d{4}"
        "IS", "\d{3}"
        "IN", "\d{6}"
        "ID", "\d{5}"
        "IL", "\d{5}"
        "JO", "\d{5}"
        "KZ", "\d{6}"
        "KE", "\d{5}"
        "KW", "\d{5}"
        "LA", "\d{5}"
        "LV", "\d{4}"
        "LB", "(\d{4}([ ]?\d{4})?)?"
        "LI", "(948[5-9])|(949[0-7])"
        "LT", "\d{5}"
        "LU", "\d{4}"
        "MK", "\d{4}"
        "MY", "\d{5}"
        "MV", "\d{5}"
        "MT", "[A-Z]{3}[ ]?\d{2,4}"
        "MU", "(\d{3}[A-Z]{2}\d{3})?"
        "MX", "\d{5}"
        "MD", "\d{4}"
        "MC", "980\d{2}"
        "MA", "\d{5}"
        "NP", "\d{5}"
        "NZ", "\d{4}"
        "NI", "((\d{4}-)?\d{3}-\d{3}(-\d{1})?)?"
        "NG", "(\d{6})?"
        "OM", "(PC )?\d{3}"
        "PK", "\d{5}"
        "PY", "\d{4}"
        "PH", "\d{4}"
        "PL", "\d{2}-\d{3}"
        "PR", "00[679]\d{2}([ \-]\d{4})?"
        "RO", "\d{6}"
        "RU", "\d{6}"
        "SM", "4789\d"
        "SA", "\d{5}"
        "SN", "\d{5}"
        "SK", "\d{3}[ ]?\d{2}"
        "SI", "\d{4}"
        "ZA", "\d{4}"
        "LK", "\d{5}"
        "TJ", "\d{6}"
        "TH", "\d{5}"
        "TN", "\d{4}"
        "TR", "\d{5}"
        "TM", "\d{6}"
        "UA", "\d{5}"
        "UY", "\d{5}"
        "UZ", "\d{6}"
        "VA", "00120"
        "VE", "\d{4}"
        "ZM", "\d{5}"
        "AS", "96799"
        "CC", "6799"
        "CK", "\d{4}"
        "RS", "\d{6}"
        "ME", "8\d{4}"
        "CS", "\d{5}"
        "YU", "\d{5}"
        "CX", "6798"
        "ET", "\d{4}"
        "FK", "FIQQ 1ZZ"
        "NF", "2899"
        "FM", "(9694[1-4])([ \-]\d{4})?"
        "GF", "9[78]3\d{2}"
        "GN", "\d{3}"
        "GP", "9[78][01]\d{2}"
        "GS", "SIQQ 1ZZ"
        "GU", "969[123]\d([ \-]\d{4})?"
        "GW", "\d{4}"
        "HM", "\d{4}"
        "IQ", "\d{5}"
        "KG", "\d{6}"
        "LR", "\d{4}"
        "LS", "\d{3}"
        "MG", "\d{3}"
        "MH", "969[67]\d([ \-]\d{4})?"
        "MN", "\d{6}"
        "MP", "9695[012]([ \-]\d{4})?"
        "MQ", "9[78]2\d{2}"
        "NC", "988\d{2}"
        "NE", "\d{4}"
        "VI", "008(([0-4]\d)|(5[01]))([ \-]\d{4})?"
        "PF", "987\d{2}"
        "PG", "\d{3}"
        "PM", "9[78]5\d{2}"
        "PN", "PCRN 1ZZ"
        "PW", "96940"
        "RE", "9[78]4\d{2}"
        "SH", "(ASCN|STHL) 1ZZ"
        "SJ", "\d{4}"
        "SO", "\d{5}"
        "SZ", "[HLMS]\d{3}"
        "TC", "TKCA 1ZZ"
        "WF", "986\d{2}"
        "XK", "\d{5}"
        "YT", "976\d{2}"
    */

    /**
     * VALIDATE order_by column_names.
     *
     */

    //@TODO add trigger for hop_score and add it to this.
    protected void validateBeerMenuOrderBy(String input)
            throws InvalidParameterException {
            if (input == null || input == "") {
                throw new InvalidParameterException("Invalid Beer Menu Order-By");
            } else if (
                    "creation_timestamp" != input.intern() &&
                    "color" != input.intern() &&
                    "bitterness" != input.intern() &&
                    "abv" != input.intern() &&
                    "price" != input.intern()
                    ) {
                throw new InvalidParameterException("Invalid Beer Menu Order-By");
            }
    }

    protected void validateFoodMenuOrderBy(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid Food Menu Order-By");
        } else if (
                "creation_timestamp" != input.intern() &&
                        "price" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid Food Menu Order-By");
        }
    }

    /**
     * VALIDATE Postgres ENUMS
     * note: checking with == was not working and was giving false negatives, so check valid
     * enum strings with:
     *
     *      "enum" != input.intern()
     *
     * Because I want to impress you <3.
     * https://stackoverflow.com/questions/8484668/java-does-not-equal-not-working
     */


    protected void validateWeekday(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid Weekday");
        } else if (
                "monday" != input.intern() &&
                "tuesday" != input.intern() &&
                "wednesday" != input.intern() &&
                "thursday" != input.intern() &&
                "friday" != input.intern() &&
                "saturday" != input.intern() &&
                "sunday" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid Weekday");
        }
    }

    protected void validateMeetupCategory(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid Meetup Category");
        } else if (
                "sleazy_job_interview" != input.intern() &&
                "a_cold_one_with_the_boys" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid Meetup Category");
        }
    }

    protected void validateEventCategory(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid Event Category");
        } else if (
                "Grand Opening" != input.intern() &&
                "New Beer" != input.intern() &&
                "Holiday" != input.intern() &&
                "Birthday" != input.intern() &&
                "Festival" != input.intern() &&
                "Show" != input.intern() &&
                "Ladies Night" != input.intern() &&
                "Bring Your Dog" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid Event Category");
        }
    }

    protected void validateBeerStyle(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid beer style.");
        } else if (
                "Belgian Styles" != input.intern() &&
                "Bocks" != input.intern() &&
                "Brown Ales" != input.intern() &&
                "Dark Lagers" != input.intern() &&
                "Hybrid Beers" != input.intern() &&
                "India Pale Ales" != input.intern() &&
                "Pale Ales" != input.intern() &&
                "Pilseners and Pale Lagers" != input.intern() &&
                "Porters" != input.intern() &&
                "Scottish Style Ales" != input.intern() &&
                "Specialty Beers" != input.intern() &&
                "Stouts" != input.intern() &&
                "Strong Ales" != input.intern() &&
                "Wheat Beers" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid beer style.");
        }
    }

    protected void validateBeerTaste(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid beer taste.");
        } else if (
                "Sour" != input.intern() &&
                "Crisp" != input.intern() &&
                "Dark" != input.intern() &&
                "Malty" != input.intern() &&
                "Hoppy" != input.intern() &&
                "Fruity" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid beer taste.");
        }
    }

    protected void validateBeerSize(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid beer size.");
        } else if (
                "Weizen" != input.intern() &&
                "Pilsner" != input.intern() &&
                "Nonic Pint" != input.intern() &&
                "Shaker Pint" != input.intern() &&
                "Tupip Pint" != input.intern() &&
                "Willi Becher" != input.intern() &&
                "Stange" != input.intern() &&
                "Tumbler" != input.intern() &&
                "Tuplip" != input.intern() &&
                "Snifter" != input.intern() &&
                "Wine" != input.intern() &&
                "Thistle" != input.intern() &&
                "Pokal" != input.intern() &&
                "Flute" != input.intern() &&
                "Goblet" != input.intern() &&
                "Mug" != input.intern() &&
                "Dimpled Mug" != input.intern() &&
                "Takard Mug" != input.intern() &&
                "Stein" != input.intern() &&
                "Oktoberfest Mug" != input.intern() &&
                "Hopside Down" != input.intern() &&
                "Boot" != input.intern() &&
                "Yard" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid beer size.");
        }
    }

    protected void validateFoodSize(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid food size.");
        } else if (
                "Bite" != input.intern() &&
                "Snack" != input.intern() &&
                "Lunchable" != input.intern() &&
                "Shareable" != input.intern() &&
                "Drunk" != input.intern() &&
                "Gourmet" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid food size.");
        }
    }

    protected void validateMembershipCategory(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid membership category.");
        } else if (
                "gold" != input.intern() &&
                "silver" != input.intern() &&
                "bronze" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid membership category.");
        }
    }

    protected void validatePromotionCategory(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid promotion category.");
        } else if (
                "free_beer" != input.intern() &&
                "free_food" != input.intern() &&
                "free_stuff" != input.intern() &&
                "tree_tickets" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid promotion category.");
        }
    }

    protected void validateUserAssociationStatus(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid user association status.");
        } else if (
                "friends" != input.intern() &&
                "blocked" != input.intern() &&
                "favorites" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid user association status.");
        }
    }

    protected void validateStars(int input)
            throws InvalidParameterException {
        if (input < 0 || input > 5) {
            throw new InvalidParameterException("Invalid star out of range.");
        }
    }

    protected void validateState(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid state.");
        } else if (
            "AL" != input.intern() &&
            "AK" != input.intern() &&
            "AZ" != input.intern() &&
            "AR" != input.intern() &&
            "CA" != input.intern() &&
            "CO" != input.intern() &&
            "CT" != input.intern() &&
            "DE" != input.intern() &&
            "FL" != input.intern() &&
            "GA" != input.intern() &&
            "HI" != input.intern() &&
            "ID" != input.intern() &&
            "IL" != input.intern() &&
            "IN" != input.intern() &&
            "IA" != input.intern() &&
            "KS" != input.intern() &&
            "KY" != input.intern() &&
            "LA" != input.intern() &&
            "ME" != input.intern() &&
            "MD" != input.intern() &&
            "MA" != input.intern() &&
            "MI" != input.intern() &&
            "MN" != input.intern() &&
            "MS" != input.intern() &&
            "MO" != input.intern() &&
            "MT" != input.intern() &&
            "NE" != input.intern() &&
            "NV" != input.intern() &&
            "NH" != input.intern() &&
            "NJ" != input.intern() &&
            "NM" != input.intern() &&
            "NY" != input.intern() &&
            "NC" != input.intern() &&
            "ND" != input.intern() &&
            "OH" != input.intern() &&
            "OK" != input.intern() &&
            "OR" != input.intern() &&
            "PA" != input.intern() &&
            "RI" != input.intern() &&
            "SC" != input.intern() &&
            "SD" != input.intern() &&
            "TN" != input.intern() &&
            "TX" != input.intern() &&
            "UT" != input.intern() &&
            "VT" != input.intern() &&
            "VA" != input.intern() &&
            "WA" != input.intern() &&
            "WV" != input.intern() &&
            "WI" != input.intern() &&
            "WY" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid state.");
        }
    }

    protected void validateHopScore(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid hop score.");
        } else if (
                "low" != input.intern() &&
                        "single" != input.intern() &&
                        "double" != input.intern() &&
                        "tripple" != input.intern() &&
                        "quadruple" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid hop score.");
        }
    }
}
