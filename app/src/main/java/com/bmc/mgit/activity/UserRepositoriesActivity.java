package com.bmc.mgit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bmc.mgit.BaseActivity;
import com.bmc.mgit.R;
import com.bmc.mgit.adapter.RepositoryListItemAdapter;
import com.bmc.mgit.manager.RepositoryListItemManager;
import com.bmc.mgit.model.RepositoryListItem;
import com.bmc.mgit.task.GetUserRepositoriesTask;

import org.eclipse.egit.github.core.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepositoriesActivity extends BaseActivity {

    private static final String TAG = UserRepositoriesActivity.class.getSimpleName();

    private List<Repository> repositories;

    @Override
    public String getTitleResource() {
        return getString(R.string.drawer_title_my_repos);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addLayout(R.layout.fragment_user_repos);
    }

    @Override
    public void onResume() {
        super.onResume();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.repo_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        try {
            repositories = new GetUserRepositoriesTask(this).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        RepositoryListItemAdapter mAdapter = new RepositoryListItemAdapter(
                RepositoryListItemManager.getInstance().getRepositories(repositories), R.layout.repository_item, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
