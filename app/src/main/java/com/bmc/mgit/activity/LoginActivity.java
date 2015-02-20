package com.bmc.mgit.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bmc.mgit.AuthorizationClient;
import com.bmc.mgit.R;
import com.bmc.mgit.misc.Constants;
import com.bmc.mgit.util.ProgressGenerator;
import com.dd.processbutton.iml.ActionProcessButton;

import org.eclipse.egit.github.core.Authorization;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.OAuthService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity implements ProgressGenerator.OnCompleteListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText mUsernameField;
    private EditText mPasswordField;
    private ActionProcessButton mLoginButton;
    private ProgressGenerator mProgressGenerator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_login);

        mUsernameField = (EditText) findViewById(R.id.username);
        mPasswordField = (EditText) findViewById(R.id.password);
        mPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mProgressGenerator = new ProgressGenerator(this);
        mLoginButton = (ActionProcessButton) findViewById(R.id.button);
        mLoginButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mLoginButton.setOnClickListener(mLoginSubmissionListener);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    View.OnClickListener mLoginSubmissionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mProgressGenerator.start(mLoginButton);
            mLoginButton.setEnabled(false);
            mUsernameField.setEnabled(false);
            mPasswordField.setEnabled(false);
            new LoginTask(mUsernameField.getText().toString(),
                    mPasswordField.getText().toString()).execute();
        }
    };

    public void onComplete() {

    }

    private class LoginTask extends AsyncTask<Void, Void, Authorization> {
        private String mUserName;
        private String mPassword;
        private String mOtpCode;

        /**
         * Instantiates a new load repository list task.
         */
        public LoginTask(String userName, String password, String otpCode) {
            mUserName = userName;
            mPassword = password;
            mOtpCode = otpCode;
        }

        public LoginTask(String userName, String password) {
            mUserName = userName;
            mPassword = password;
        }

        protected void fail() {
            mLoginButton.setEnabled(true);
            mUsernameField.setEnabled(true);
            mPasswordField.setEnabled(true);
            Toast.makeText(LoginActivity.this, R.string.invalid_login, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Authorization doInBackground(Void... params) {
            GitHubClient client = new AuthorizationClient(mOtpCode);
            client.setCredentials(mUserName, mPassword);
            client.setUserAgent("mGit");

            Authorization auth = null;
            OAuthService authService = new OAuthService(client);
            List<Authorization> auths = null;
            try {
                auths = authService.getAuthorizations();
            } catch (IOException e) {
                fail();
            }
            for (Authorization authorization : auths) {
                if ("mGit".equals(authorization.getNote())) {
                    auth = authorization;
                    break;
                }
            }

            if (auth == null) {
                auth = new Authorization();
                auth.setNote("mGit");
                auth.setUrl("http://github.com/bmc08gt/build");
                List<String> scopes = new ArrayList<String>();
                scopes.add("user");
                scopes.add("repo");
                scopes.add("gist");
                auth.setScopes(scopes);

                try {
                    auth = authService.createAuthorization(auth);
                } catch (IOException e) {
                    fail();
                }
            }
            return auth;
        }

        @Override
        protected void onPostExecute(Authorization result) {
            SharedPreferences sharedPreferences = getSharedPreferences(
                    Constants.PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.User.AUTH_TOKEN, result.getToken());
            editor.putString(Constants.User.LOGIN, mUserName);
            editor.apply();

            Intent goToBackMain = new Intent(LoginActivity.this, UserRepositoriesActivity.class);
            startActivity(goToBackMain);

            finish();
        }
    }

    // POST request so that GitHub trigger the SMS for OTP
    private class DummyPostTask extends LoginTask {
        private String mUserName;
        private String mPassword;

        public DummyPostTask(String userName, String password) {
            super(userName, password);
            mUserName = userName;
            mPassword = password;
        }

        @Override
        protected Authorization doInBackground(Void... params) {
            GitHubClient client = new AuthorizationClient(null);
            client.setCredentials(mUserName, mPassword);
            client.setUserAgent("Gh4a");

            Authorization auth = new Authorization();
            auth.setNote("Gh4a");
            auth.setUrl("http://github.com/slapperwan/gh4a");
            List<String> scopes = new ArrayList<String>();
            scopes.add("user");
            scopes.add("repo");
            scopes.add("gist");
            auth.setScopes(scopes);

            OAuthService authService = new OAuthService(client);
            try {
                return authService.createAuthorization(auth);
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
            return null;
        }
    }
}
