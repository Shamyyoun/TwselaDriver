package com.twsela.driver.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import com.twsela.driver.R;
import com.twsela.driver.controllers.ActiveUserController;
import com.twsela.driver.fragments.HomeFragment;
import com.twsela.driver.utils.DialogUtils;
import com.twsela.driver.utils.Utils;

public class MainActivity extends ParentActivity {
    private static int DRAWER_GRAVITY = Gravity.LEFT;

    private ActiveUserController activeUserController;
    private DrawerLayout drawerLayout;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // customize toolbar
        setToolbarIcon(R.drawable.menu_icon);
        createOptionsMenu(R.menu.menu_main);

        // create active user controller
        activeUserController = new ActiveUserController(this);

        // init drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // load home fragment if required
        if (savedInstanceState == null) {
            loadHomeFragment();
        }
    }

    private void loadHomeFragment() {
        // create the home fragment if possible and load it
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }

        loadFragment(R.id.container, homeFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onMenuIcon();
                return true;

            case R.id.action_logout:
                onLogout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onMenuIcon() {
        if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            closeMenuDrawer();
        } else {
            drawerLayout.openDrawer(DRAWER_GRAVITY);
        }
    }

    public void closeMenuDrawer() {
        drawerLayout.closeDrawer(DRAWER_GRAVITY);
    }

    private void onLogout() {
        // check if he is online
        if (activeUserController.getUser().isOnline()) {
            // show msg and return
            Utils.showLongToast(this, R.string.you_cant_logout_when_you_are_online);
            return;
        }

        // show confirm dialog
        DialogUtils.showConfirmDialog(this, R.string.logout_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logout();
            }
        }, null);
    }

    private void logout() {
        // logout in active user controller
        activeUserController = new ActiveUserController(this);
        activeUserController.logout();

        // goto splash activity
        Intent splashIntent = new Intent(this, SplashActivity.class);
        startActivity(splashIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            closeMenuDrawer();
        } else {
            super.onBackPressed();
        }
    }
}