package com.bmc.mgit.manager;

import android.content.Context;
import android.util.Log;

import com.bmc.mgit.model.RepositoryListItem;

import org.eclipse.egit.github.core.Repository;

import java.util.ArrayList;
import java.util.List;

public class RepositoryListItemManager {

    private static final String TAG = RepositoryListItemManager.class.getSimpleName();

    private static RepositoryListItemManager sInstance;
    private List<RepositoryListItem> repositories;

    public static RepositoryListItemManager getInstance() {
        if (sInstance == null) {
            sInstance = new RepositoryListItemManager();
        }
        return sInstance;
    }

    public List<RepositoryListItem> getRepositories(List<Repository> repos) {
        if (repositories == null) {
            repositories = new ArrayList<>();

            int numRepos = repos.size();

            for (int i = 0; i < numRepos; i++) {
                RepositoryListItem repo = new RepositoryListItem();
                repo.mName = repos.get(i).getName();
                repo.mDescription = repos.get(i).getDescription();
                repo.mLanguage = repos.get(i).getLanguage();
                repo.mWatchers = repos.get(i).getWatchers();
                repo.mForks = repos.get(i).getForks();
                repositories.add(repo);
            }
        }
        return repositories;
    }
}
