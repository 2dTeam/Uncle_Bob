package org.team2d.uncle_bob;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.view.inputmethod.InputMethodManager;

import com.flurry.android.FlurryAgent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Basket.Basket;


public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);
    private final int PERMISSION_REQUEST_PHONE_CODE = 1;
    private static final String PRIMARY_FRAGMENT_TAG = "org.team2d.uncle_bob.MainActivity.PRIMARY_FRAGMENT_TAG";
    private static final String INITIAL_BACKSTACK_ID = "org.team2d.uncle_bob.MainActivity.INITIAL_BACKSTACK_ID";
    private static final String FLURRY_API_KEY = "K9TBMB5W768B5WQ6BBZG";
    private boolean mayShowFAB = true;
    private TabFragment mTabFragment = new TabFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            initTabFragment();
        }

        setupFAB();
        setupDrawer();

        FlurryAgent.init(this, FLURRY_API_KEY);
        FlurryAgent.onStartSession(this, FLURRY_API_KEY);
    }

    private void initTabFragment() {

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.replace(R.id.app_bar_wrapper_content_container, mTabFragment)
                .addToBackStack(INITIAL_BACKSTACK_ID)
                .commit();
    }
    public void setContent(Fragment content) {
        setContent(content, null);
    }

    public void setContent(Fragment content, String backStackID) {
        if (content.getClass() == FragmentBasket.class) {
            mayShowFAB = false;
            findViewById(R.id.fab).setVisibility(View.GONE);
        } else
            mayShowFAB = true;

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.replace(R.id.app_bar_wrapper_content_container, content, PRIMARY_FRAGMENT_TAG);

        transaction.addToBackStack(backStackID).commit();
    }

    private void setupFAB() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContent(FragmentFactory.getBasketFragment());
            }
        });

        if (!Basket.getInstance().getItems().isEmpty() && mayShowFAB)
            fab.setVisibility(View.VISIBLE);
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

    private void setFABCallbacks() {
        Basket.getInstance().setOnBasketEmptyCallback(new Basket.Callback() {
            @Override
            public void call() {
                final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setVisibility(View.GONE);
            }
        });

        Basket.getInstance().setOnBasketNotEmptyCallback(new Basket.Callback() {
            @Override
            public void call() {
                LOGGER.info("Fab can be shown?" + mayShowFAB);
                final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                if (mayShowFAB)
                    fab.setVisibility(View.VISIBLE);
            }
        });
    }

    private void removeFABCallbacks() {
        Basket.getInstance().setOnBasketEmptyCallback(null);
        Basket.getInstance().setOnBasketNotEmptyCallback(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeFABCallbacks();
        FlurryAgent.onEndSession(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFABCallbacks();
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
        try {
            if (fragment.getClass() == FragmentBasket.class) {
                mayShowFAB = false;
                findViewById(R.id.fab).setVisibility(View.GONE);
            } else
                mayShowFAB = true;

            if (Basket.getInstance().getItems().size() != 0 && mayShowFAB)
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LOGGER.info("Костыль сработал.");
        }

        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mayShowFAB = true;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
                break;
            }
            case R.id.nav_account : {
                setContent(FragmentFactory.getAccountFragment());
                break;
            }
            case R.id.nav_about : {
                setContent(FragmentFactory.getDeliveryInfo());
                break;
            }
            case R.id.nav_menu : {
                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(R.id.app_bar_wrapper_content_container, mTabFragment)
                        .addToBackStack(INITIAL_BACKSTACK_ID)
                        .commit();
                break;
            }
        }
        return true;
    }

    public void setTabViewOnStack(){
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.app_bar_wrapper_content_container, mTabFragment)
                .addToBackStack(INITIAL_BACKSTACK_ID)
                .commit();
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

    private void showExplanation(CharSequence title, CharSequence message, final String permission, final int permissionRequestCode) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

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