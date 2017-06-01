package com.Common;

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

}
