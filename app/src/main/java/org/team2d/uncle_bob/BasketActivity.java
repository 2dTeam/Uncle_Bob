package org.team2d.uncle_bob;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.team2d.uncle_bob.Basket.Basket;

import java.util.HashMap;

public class BasketActivity extends AppCompatActivity {
    private final HashMap<Basket.ProductType, Object> basket  = Basket.getBasket();
    private static final String TAG = BasketActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        for (HashMap.Entry<Basket.ProductType, Object> entry : basket.entrySet()) {
            Log.d(TAG, "basket" + entry.toString());

        }

    }

}
