package org.team2d.uncle_bob;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.team2d.uncle_bob.Database.DatabaseAccess;
import org.team2d.uncle_bob.Database.ORM.PizzaORM;

import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;
import static org.team2d.uncle_bob.R.id.image;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);
    private final int  PERMISSION_REQUEST_PHONE_CODE = 1;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOGGER.info("fab clicked");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // load from db pizzas
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        HashMap<Integer, PizzaORM> pizza  = databaseAccess.getPizza();
        for (HashMap.Entry<Integer, PizzaORM> entry : pizza.entrySet()) {
            int key = entry.getKey();
            Log.d(TAG, "Pizzas " + entry.getValue().getImagePath());

            ImageView image = new ImageView(MainActivity.this);

            image.setBackgroundResource(R.drawable.pizza_placeholder_image_180x180);
            linearLayout1.addView(image);

            // Adds the view to the layout
        }
        Log.d(TAG, "list" + pizza);
        databaseAccess.close();

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quotes);
//        this.listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void callUncleBob(MenuItem view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
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

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
