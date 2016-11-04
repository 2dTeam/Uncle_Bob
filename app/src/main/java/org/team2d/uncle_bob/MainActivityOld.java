package org.team2d.uncle_bob;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Basket.Basket;
import org.team2d.uncle_bob.Database.DatabaseService;
import org.team2d.uncle_bob.Database.ORM.PizzaORM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivityOld extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivityOld.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivityOld.class);
    private final int PERMISSION_REQUEST_PHONE_CODE = 1;
    private static final HashMap<Basket.ProductType, Object> BASKET = Basket.getBasket();

    private final List<View> onClickSubscribers = new ArrayList<>();
    private class ActivityChanger implements View.OnClickListener {
        private final int iID;
        private final Context text;

        ActivityChanger(int itemID, Context context) {
            this.iID = itemID;
            text = context;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(text, DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_ITEM_ID, iID);

            startActivity(intent);
        }
    }

    public int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        onClickSubscribers.add(fab);
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

        fillActivityWithPreviews();
		
		HashMap<Integer, PizzaORM> pizza  = DatabaseService.getPizza(this);
		for (HashMap.Entry<Integer, PizzaORM> entry : pizza.entrySet()) {
            BASKET.put(Basket.ProductType.PIZZA, entry.getValue());
            int key = entry.getKey();
            String name = entry.getValue().getName();
            LOGGER.debug("Pizzas " + entry.getValue().getImagePath());
        }
    }

    private View getItemPreview(String title, String price, String imagePath, @Nullable ViewGroup parent) {
        final ViewGroup previewLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.item_preview, null);

        TextView titleTextView = (TextView) previewLayout.findViewById(R.id.item_preview_title);
        titleTextView.setText(title);

        TextView priceTextView = (TextView) previewLayout.findViewById(R.id.item_preview_price);
        priceTextView.setText(price);

        ImageView imageView = (ImageView) previewLayout.findViewById(R.id.item_preview_image);
        imageView.setImageResource(getResourceId(imagePath, "drawable", getPackageName()));

        return previewLayout;
    }

    private void fillActivityWithPreviews() {
        final ViewGroup contentWrapper = (ViewGroup) findViewById(R.id.app_bar_wrapper_content_container);
        final ViewGroup previewListView = (ViewGroup) getLayoutInflater().inflate(R.layout.preview_list_content, null);
        final ViewGroup previewListContainer = (ViewGroup) previewListView.findViewById(R.id.preview_list);

        HashMap<Integer, PizzaORM> pizzas  = DatabaseService.getPizza(this);

        for (final HashMap.Entry<Integer, PizzaORM> entry : pizzas.entrySet()) {
            final PizzaORM value = entry.getValue();
            LOGGER.info("Pizzas " + entry.getValue().getImagePath());

            final View itemPreview = getItemPreview(value.getName(), "Not implemented", value.getImagePath(), null);

            itemPreview.setOnClickListener(new ActivityChanger(value.getId(), this));

            previewListContainer.addView(itemPreview);
        }

        contentWrapper.addView(previewListView);
    }

    @Override
    protected void onStop() {
        super.onStop();

        for (View subscriber : onClickSubscribers)
            subscriber.setOnClickListener(null);
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
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_basket : {
                Intent intent = new Intent(this, BasketActivity.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
