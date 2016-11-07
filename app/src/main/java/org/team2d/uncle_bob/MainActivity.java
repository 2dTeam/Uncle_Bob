package org.team2d.uncle_bob;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    // TODO: make some proper logging?
    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);
    private final int PERMISSION_REQUEST_PHONE_CODE = 1;
    private static final String PRIMARY_FRAGMENT_TAG = "org.team2d.uncle_bob.MainActivity.PRIMARY_FRAGMENT_TAG";

    // TODO: Consider moving into utility class
    // Oops! How to move non-static AppCompatActivity.getResources() method?
    public int getResourceId(String VariableName, String Resourcename, String PackageName) {
        try {
            return getResources().getIdentifier(VariableName, Resourcename, PackageName);
        } catch (final Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupFAB();
        setupDrawer();

        setContent(FragmentFactory.getDefaultFragment());
    }

    public void setContent(Fragment content) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        final Fragment currentContent = getSupportFragmentManager().findFragmentByTag(PRIMARY_FRAGMENT_TAG);
        if (currentContent != null)
            transaction.remove(currentContent);

        transaction.add(R.id.app_bar_wrapper_content_container, content, PRIMARY_FRAGMENT_TAG);
        // TODO: change title, etc according to new content and backstack.

        transaction.addToBackStack(null).commit();
    }

    private void setupFAB() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // TODO: will this lead to memory leakage?
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOGGER.info("fab clicked");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupDrawer() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }


    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // TODO: closing app? You are doing it wrong.
            if (getSupportFragmentManager().getBackStackEntryCount() == 1)
                getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        final int id = item.getItemId();
        switch (id) {
            case R.id.nav_basket : {
                setContent(FragmentFactory.getBasketFragment());
            }
            case R.id.nav_sales  : {
                setContent(FragmentFactory.getSalesFragment());
            }
            case R.id.nav_account : {
                setContent(FragmentFactory.getAccountFragment());
            }
            case R.id.nav_history : {
                setContent(FragmentFactory.getHistoryFragment());
            }
            case R.id.nav_menu : {
                setContent(FragmentFactory.getCategoryListFragment());
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_PHONE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callUncleBob(null);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // TODO: Consider moving into utility class
    // Again, without breaking Activity.startActivity()...
    public void callUncleBob(MenuItem menuItem) {
        final Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +  getString(R.string.pizza_shop_tel)));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            showExplanation("Permission Needed", "Rationale", Manifest.permission.CALL_PHONE, PERMISSION_REQUEST_PHONE_CODE);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_PHONE_CODE);
            return;
        }
        startActivity(callIntent);
    }

    // TODO: Consider moving into utility class
    // Maybe inner class?
    private void showExplanation(CharSequence title, CharSequence message, final String permission, final int permissionRequestCode) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // TODO: will this lead to memory leakage?
        builder.setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, permissionRequestCode);
    }

}
