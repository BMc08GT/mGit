package com.bmc.mgit.misc;

/**
 * Created by bmc on 2/17/15.
 */
public class Constants {

    public static final String PREFERENCES = "preferences";
    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    public static final String USER_LEARNED_DRAWER = "navigation_drawer_learned";

    public interface User {
        public static final String LOGIN = "login";
        public static final String AUTH_TOKEN = "auth_token";
        public static final String AUTH_ID ="auth_id";
    }
}
