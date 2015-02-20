package com.bmc.mgit.task;

import android.content.Context;
import android.os.AsyncTask;

import com.bmc.mgit.DefaultClient;
import com.bmc.mgit.util.Utils;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;

public class GetLoggedInUserTask extends AsyncTask<Void, Void, User> {

    private static final String TAG = GetLoggedInUserTask.class.getSimpleName();

    private Context mContext;
    private DefaultClient mClient;

    public GetLoggedInUserTask(Context context, DefaultClient client) {
        this.mContext = context;
        this.mClient = client;
    }

    @Override
    protected User doInBackground(Void... params) {
        UserService userService = new UserService(mClient);
        try {
            return userService.getUser(Utils.getAuthLogin(mContext));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
