package org.team2d.uncle_bob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import java.util.Map;


public class FragmentItemList extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentItemList.class);

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

    private LayoutInflater inflater = null;
    private ViewGroup container = null;
    private Bundle savedInstanceState = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;

        fillFragmentWithPreviews();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private View getItemPreview(String title, String price, String imagePath, @Nullable ViewGroup parent) {
        final ViewGroup previewLayout = (ViewGroup) getLayoutInflater(savedInstanceState).inflate(R.layout.item_preview, null);

        final TextView titleTextView = (TextView) previewLayout.findViewById(R.id.item_preview_title);
        titleTextView.setText(title);

        final TextView priceTextView = (TextView) previewLayout.findViewById(R.id.item_preview_price);
        priceTextView.setText(price);

        final ImageView imageView = (ImageView) previewLayout.findViewById(R.id.item_preview_image);
        imageView.setImageResource(R.drawable.pizza_placeholder_image_180x180);

        return previewLayout;
    }

    private void fillFragmentWithPreviews() {
        final ViewGroup previewListView = (ViewGroup) inflater.inflate(R.layout.preview_list_content, null);
        final ViewGroup previewListContainer = (ViewGroup) previewListView.findViewById(R.id.preview_list);

        // TODO: rewrite
        final Map<Integer, PizzaORM> pizzas  = DatabaseService.getPizza(null);

        for (final Map.Entry<Integer, PizzaORM> entry : pizzas.entrySet()) {
            final PizzaORM value = entry.getValue();
            LOGGER.info("Pizzas " + entry.getValue().getImagePath());

            final View itemPreview = getItemPreview(value.getName(), "Not implemented", value.getImagePath(), null);

            itemPreview.setOnClickListener(new ActivityChanger(value.getId(), null));

            previewListContainer.addView(itemPreview);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        for (final View subscriber : onClickSubscribers)
            subscriber.setOnClickListener(null);
    }

}
