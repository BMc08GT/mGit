package com.bmc.mgit.model;

import org.eclipse.egit.github.core.User;

/**
 * Created by bmc on 2/18/15.
 */
public class RepositoryListItem {

    public String mName;
    public String mDescription;
    public User mOwner;
    public String mLanguage;
    public int mWatchers;
    public int mForks;
}
