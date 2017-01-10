package com.twsela.driver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.twsela.driver.ApiRequests;
import com.twsela.driver.R;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.controllers.ActiveUserController;
import com.twsela.driver.models.responses.LoginResponse;
import com.twsela.driver.utils.AppUtils;
import com.twsela.driver.utils.FirebaseUtils;
import com.twsela.driver.utils.Utils;

public class LoginActivity extends ParentActivity {
    private ActiveUserController activeUserController;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // obtain main objects
        activeUserController = new ActiveUserController(this);

        // init views
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        // add listeners
        btnLogin.setOnClickListener(this);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    preLogin();
                    return true;
                }
                return false;
            }
        });

        // check if ha logged in user
        if (activeUserController.hasLoggedInUser()) {
            openMainActivity();
        }

    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                preLogin();
                break;
        }
    }

    private void preLogin() {
        // prepare params
        String username = Utils.getText(etUsername);
        String password = Utils.getText(etPassword);

        // validate params
        if (Utils.isEmpty(username) || Utils.isEmpty(password)) {
            Utils.showShortToast(this, getString(R.string.please_fill_all_fields));
            return;
        }

        hideKeyboard();

        // check internet connection
        if (hasInternetConnection()) {
            login(username, password);
        } else {
            Utils.showShortToast(this, R.string.no_internet_connection);
        }
    }

    private void login(String username, String password) {
        // show progress
        showProgressDialog();

        // get firebase token
        String firebaseToken = FirebaseUtils.getFirebaseToken();

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.login(this, this, username, password, firebaseToken);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();
        LoginResponse loginResponse = (LoginResponse) response;
        if (loginResponse.isSuccess() && loginResponse.getContent() != null) {
            // save him
            activeUserController.setUser(loginResponse.getContent());
            activeUserController.save();

            // open main activity
            openMainActivity();
        } else {
            // show msg
            String msg = AppUtils.getResponseMsg(this, loginResponse, R.string.make_sure_of_username_and_password);
            Utils.showShortToast(this, msg);
        }
    }
}