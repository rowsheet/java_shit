package com.Common;

import jnr.ffi.annotations.In;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 5/31/17.
 */
public class UserCookie {
    public int userID;
    public int requestPermissionID;
    public String sessionKey;
    public String emailAddress;
    public String userName;
    public String first_name;
    public String last_name;
    public String about_me;
    public String profile_picture;
    public HashMap<String, UserPermission> userPermissions;
    public UserCookie() {
        this.userID = 0;
        this.requestPermissionID = 0;
        this.sessionKey = "";
        this.emailAddress = "NA";
        this.userName = "NA";
        this.first_name = "NA";
        this.last_name = "NA";
        this.about_me = "NA";
        this.profile_picture = "NA";
        this.userPermissions = new HashMap<String, UserPermission>();
    }
}
