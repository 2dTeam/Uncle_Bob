package org.team2d.uncle_bob;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.team2d.uncle_bob.Basket.Basket;
import org.team2d.uncle_bob.Database.ORM.PizzaORM;

import java.util.HashMap;

public class BasketActivity extends MainActivityOld {
    private final HashMap<Basket.ProductType, Object> basket  = Basket.getBasket();
    private static final String TAG = BasketActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        for (HashMap.Entry<Basket.ProductType, ?> entry : basket.entrySet()) {
            Basket.ProductType key = entry.getKey();
            switch (key) {
                case PIZZA : {
                    PizzaORM piz = ((PizzaORM) entry.getValue());
                    Log.d(TAG, "basket contains: " +  piz.getName());
                }

            }
            Log.d(TAG, "basket contains: " + key );

        }

    }

}
