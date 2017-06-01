package com.Common;

import jnr.ffi.annotations.In;

import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.regex.Pattern;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class AbstractController {

    public enum Weekday {

    }

    protected void validateString(String input, String input_name)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid " + input_name + ".");
        }
    }

    protected void validateTimestamp(String input, String input_name)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid " + input_name + " Timestamp");
        }
        Pattern pattern = Pattern.compile("^\\d{4}[-]?\\d{1,2}[-]?\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}[.]?\\d{1,6}$");
        if (!pattern.matcher(input).matches()) {
            throw new InvalidParameterException("Invalid " + input_name + " Timestamp");
        }
        SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            simpleDateFormat.parse(input);
        } catch (Exception e) {
            throw new InvalidParameterException("Invalid " + input_name + " Timestamp");
        }
    }

    protected void validateText(String input, String input_name)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid " + input_name + " Text");
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
        if (input < 0 || input > 40) {
            throw new InvalidParameterException("Invalid beer color, out of range.");
        }
    }

    protected void validateBitterness(int input)
        throws InvalidParameterException {
        if (input < 0 || input > 90) {
            throw new InvalidParameterException("Invalid beer bitterness, out of range.");
        }
    }

    protected void validateAbv(int input)
        throws InvalidParameterException {
        if (input < 0 || input > 20) {
            throw new InvalidParameterException("Invalid beer ABV, out of range");
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
                "grand_opening" != input.intern() &&
                "new_beer" != input.intern() &&
                "holiday" != input.intern() &&
                "birthday" != input.intern() &&
                "festival" != input.intern() &&
                "show" != input.intern() &&
                "ladies_night" != input.intern() &&
                "something_dogs " != input.intern()
                ) {
            throw new InvalidParameterException("Invalid Event Category");
        }
    }

    protected void validateBeerStyle(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid beer style.");
        } else if (
                "belgian_styles" != input.intern() &&
                "bocks" != input.intern() &&
                "brown_ales" != input.intern() &&
                "dark_lagers" != input.intern() &&
                "hybrid_beers" != input.intern() &&
                "india_pale_ales" != input.intern() &&
                "pale_ales" != input.intern() &&
                "pilseners_and_pale_lagers" != input.intern() &&
                "porters" != input.intern() &&
                "scottish_style_ales" != input.intern() &&
                "specialty_beers" != input.intern() &&
                "stouts" != input.intern() &&
                "strong_ales" != input.intern() &&
                "wheat_beers" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid beer style.");
        }
    }

    protected void validateBeerTaste(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid beer taste.");
        } else if (
                "sour" != input.intern() &&
                "crips" != input.intern() &&
                "dark" != input.intern() &&
                "malty" != input.intern() &&
                "hoppy" != input.intern() &&
                "fruity" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid beer taste.");
        }
    }

    protected void validateBeerSize(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid beer size.");
        } else if (
                "weizen" != input.intern() &&
                "pilsner" != input.intern() &&
                "nonic_pint" != input.intern() &&
                "shaker_pint" != input.intern() &&
                "tupip_pint" != input.intern() &&
                "willi_becher" != input.intern() &&
                "stange" != input.intern() &&
                "tumbler" != input.intern() &&
                "tulip" != input.intern() &&
                "snifter" != input.intern() &&
                "wine" != input.intern() &&
                "thistle" != input.intern() &&
                "pokal" != input.intern() &&
                "flute" != input.intern() &&
                "goblet" != input.intern() &&
                "mug" != input.intern() &&
                "dimpled_mug" != input.intern() &&
                "takard_mug" != input.intern() &&
                "stein" != input.intern() &&
                "oktoberfest_mug" != input.intern() &&
                "hopside_down" != input.intern() &&
                "boot" != input.intern() &&
                "yard" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid beer size.");
        }
    }

    protected void validateFoodSize(String input)
            throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid food size.");
        } else if (
                "bite" != input.intern() &&
                "snack" != input.intern() &&
                "lunchable" != input.intern() &&
                "shareable" != input.intern() &&
                "drunk" != input.intern()
                ) {
            throw new InvalidParameterException("Invalid food size.");
        }
    }
}
