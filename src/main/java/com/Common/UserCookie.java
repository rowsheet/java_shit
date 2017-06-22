package com.Common;

import jnr.ffi.annotations.In;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 5/31/17.
 */
public class UserCookie {
    public int userID;
    public int userPermissionID;
    public String sessionKey;
    public String emailAddress;
    public String userName;
    public HashMap<Integer, UserPermission> userPermissions;
}
