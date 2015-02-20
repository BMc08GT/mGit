package com.bmc.mgit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bmc.mgit.activity.LoginActivity;
import com.bmc.mgit.adapter.NavigationDrawerAdapter;
import com.bmc.mgit.misc.Constants;
import com.bmc.mgit.task.GetLoggedInUserTask;
import com.bmc.mgit.util.Utils;

import org.eclipse.egit.github.core.User;

import java.util.concurrent.ExecutionException;


public abstract class BaseActivity extends ActionBarActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private SharedPreferences mPrefs;
    private String[] mDrawerEntries;
    private String[] mDrawerValues;

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private DrawerLayout mDrawer;

    private ActionBarDrawerToggle mDrawerToggle;

    private User mUser;
    private DefaultClient mClient;

    public abstract String getTitleResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawerEntries = getResources().getStringArray(R.array.navigation_drawer_entries);
        mDrawerValues = getResources().getStringArray(R.array.navigation_drawer_values);

        setContentView(R.layout.fragment_navigation_drawer);

        mPrefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        if (mPrefs.getString(Constants.User.AUTH_TOKEN, null) == null) {
            Intent timeToLogin = new Intent(this, LoginActivity.class);
            timeToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(timeToLogin);
        }

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitleResource());

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mClient = new DefaultClient();
        mClient.setOAuth2Token(Utils.getAuthToken(this));

        try {
            mUser = new GetLoggedInUserTask(this, mClient).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            mUser = null;
        }

        Log.d(TAG, "name=" + mUser.getName() + ", email=" + mUser.getEmail());

        mAdapter = new NavigationDrawerAdapter(
                mDrawerEntries, /*ICONS,*/
                mUser.getName(), mUser.getEmail(),
                new BitmapDrawable(getResources(), Utils.urlToBitmap(this, mUser.getAvatarUrl())));
        mRecyclerView.setAdapter(mAdapter);

        mDrawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        }; // Drawer Toggle Object Made
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawer.setStatusBarBackgroundColor(getResources().getColor(R.color.dark));
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.logout:
                Utils.logout(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addLayout(int layout) {
        LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDrawer.addView(layoutInflater.inflate(
                        layout, null), 0,
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
