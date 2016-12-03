package org.team2d.uncle_bob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Basket.Basket;
import org.team2d.uncle_bob.Basket.BasketItem;
import org.team2d.uncle_bob.Basket.QuantityButtonsWidget;
import org.team2d.uncle_bob.Network.Network;
import org.team2d.uncle_bob.Picasso.PicassoImageLoader;

import java.io.IOException;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class FragmentBasket extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentBasket.class);
    private static final String ARG_CATEGORY_ID = "org.team2d.uncle_bob.FragmentItemList.CATEGORY_ID";
    private LayoutInflater inflater = null;
    private ViewGroup container = null;
    private Bundle savedInstanceState = null;
    private View fragment;
    private final View.OnClickListener onBuyButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public static FragmentBasket newInstance() {
        final FragmentBasket fragment = new FragmentBasket();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;

        final ViewGroup fragment = (ViewGroup) inflater.inflate(R.layout.fragment_basket, null);
        this.fragment = fragment;

        getActivity().setTitle(getActivity().getString(R.string.title_fragment_basket)); // Aww, LISP.

        fillFragmentWithPreviews(fragment);

        //@TODO example of working with network
        JSONObject mockJson = new JSONObject();
        try {
            mockJson.put("username", "Николаев Виталий");
        } catch (JSONException e){
            e.printStackTrace();
        }

        Network.sendOrderToServer(mockJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Network", "Response: " + response);
                response.body().close();
            }
        });
        // ENDOFEXAMPLE

        return fragment;
    }

    private View getItemPreview(BasketItem item) {
        final ViewGroup basketItemLayout = (ViewGroup) getLayoutInflater(savedInstanceState).inflate(R.layout.item_basket, null);

        final TextView titleTextView = (TextView) basketItemLayout.findViewById(R.id.item_basket_title);
        titleTextView.setText(getNameForItem(item));

        final TextView priceTextView = (TextView) basketItemLayout.findViewById(R.id.item_basket_price);
        priceTextView.setText(item.getPrice(this));

        final ImageView imageView = (ImageView) basketItemLayout.findViewById(R.id.item_basket_image);

        final TextView descriptionTextView = (TextView) basketItemLayout.findViewById(R.id.item_basket_description);
        descriptionTextView.setText(item.getItem().getDescription());

        final LinearLayout buttonsContainer = (LinearLayout) basketItemLayout.findViewById(R.id.item_basket_quantity_widget_container);
        final QuantityButtonsWidget buyButtons = new QuantityButtonsWidget(getLayoutInflater(null), buttonsContainer, item.getItem(), item.getDetails(), new TotalRecalculator(), new StateMatcher(basketItemLayout));

        PicassoImageLoader.getInstance()
                .load(getActivity(), item.getItem().getImagePath(), R.drawable.noimage, R.drawable.noimage, imageView);

        return basketItemLayout;
    }

    private void fillFragmentWithPreviews(ViewGroup fragment) {
        final ViewGroup previewListContainer = (ViewGroup) fragment.findViewById(R.id.content_basket);

        final Set<BasketItem> items  = Basket.getInstance().getItems();

        if (!items.isEmpty()) {
            for (BasketItem entry : items) {
                final View itemPreview = getItemPreview(entry);

                previewListContainer.addView(itemPreview, previewListContainer.getChildCount() - 2);
            }
            changeTotalVisibility(false);
            setTotalPrice(Basket.getInstance().getTotalPrice(this));
        } else {
            changeTotalVisibility(true);
            previewListContainer.addView(getBasketEmptyMessage());
        }
    }

    // TODO: Make it inflated from xml, moron!
    private View getBasketEmptyMessage() {
        final TextView message = new TextView(getActivity());
        message.setText("Упс! корзина пуста, да и вёрстка поехала...");
        return message;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String getNameForItem(BasketItem item) {
        final StringBuilder title = new StringBuilder(item.getItem().getName()).append(' ').append(String.valueOf((int) item.getDetails().getWeight())).append(getString(R.string.item_details_weight_postfix));
        return title.toString();
    }

    private void setTotalPrice(String toBeAppended) {
        final TextView totalTextView = (TextView) fragment.findViewById(R.id.basket_total_price);
        final String total = getString(R.string.basket_price_prefix) + toBeAppended;
        totalTextView.setText(total);
    }

    private void changeTotalVisibility(boolean shouldBeHidden) {
        final View totalTextView = fragment.findViewById(R.id.basket_total_price);
        final View totalBuyButton = fragment.findViewById(R.id.basket_buy_button);
        if (shouldBeHidden) {
            totalBuyButton.setVisibility(View.GONE);
            totalTextView.setVisibility(View.GONE);
        } else {
            totalBuyButton.setVisibility(View.VISIBLE);
            totalTextView.setVisibility(View.VISIBLE);
        }
    }

    private FragmentBasket getThis() {
        return this;
    }

    private class TotalRecalculator implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setTotalPrice(Basket.getInstance().getTotalPrice(getThis()));
        }
    }

    private class StateMatcher implements QuantityButtonsWidget.OnStateChangedListener {
        private final ViewGroup itemContainer;

        private StateMatcher(ViewGroup itemContainer) {
            this.itemContainer = itemContainer;
        }

        @Override
        public void act(BasketItem item) {
            if (item.getQuantity() == 0) {
//                LOGGER.info(itemContainer.toString());
//                LOGGER.info(itemContainer.getParent().toString());
                //((ViewGroup) itemContainer.getParent()).removeView(itemContainer);
                itemContainer.setVisibility(View.GONE);

                if (Basket.getInstance().getItems().size() == 0) {
                    final ViewGroup previewListContainer = (ViewGroup) fragment.findViewById(R.id.content_basket);
                    changeTotalVisibility(true);

                    previewListContainer.addView(getBasketEmptyMessage());
                }

            } else {
                final TextView priceTextView = (TextView) itemContainer.findViewById(R.id.item_basket_price);
                priceTextView.setText(item.getPrice(getThis()));
            }
        }
    }
}
