package com.bmc.mgit.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.bmc.mgit.misc.Constants;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.List;

public class GetUserRepositoriesTask extends AsyncTask<Void, Void, List<Repository>> {

    private static final String TAG = GetUserRepositoriesTask.class.getSimpleName();

    private Context mContext;

    public GetUserRepositoriesTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected List<Repository> doInBackground(Void... params) {
        RepositoryService service = new RepositoryService();
        List<Repository> repositories = null;
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        try {
            repositories = service.getRepositories(prefs.getString(Constants.User.LOGIN, null));
        } catch (IOException e) {
            Log.e(TAG, "Unable to retrieve repos");
        }
        return repositories;
    }
}
