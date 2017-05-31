package com.Common;

import java.security.InvalidParameterException;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class AbstractController {

    protected void validateString(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid String.");
        }
    }

    protected void validateTimestamp(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid Timestamp");
        }
    }

    protected void validateWeekday(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
           throw new InvalidParameterException("Invalid Weekday");
        }
    }

    protected void validateText(String input)
        throws InvalidParameterException {
        if (input == null || input == "") {
            throw new InvalidParameterException("Invalid Text");
        }
    }

}
