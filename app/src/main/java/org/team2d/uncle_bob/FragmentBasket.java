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
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Basket.Basket;
import org.team2d.uncle_bob.Basket.BasketItem;
import org.team2d.uncle_bob.Basket.QuantityButtonsWidget;
import org.team2d.uncle_bob.Basket.Sauce;
import org.team2d.uncle_bob.Database.ORM.UserData;
import org.team2d.uncle_bob.Network.Network;
import org.team2d.uncle_bob.Picasso.PicassoImageLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class FragmentBasket extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentBasket.class);
    private static final String ARG_CATEGORY_ID = "org.team2d.uncle_bob.FragmentItemList.CATEGORY_ID";
    private static final String FLURRY_EVENT_BUY = "Order_Submitted";
    private static final String FLURRY_EVENT_BUY_PRICE = "Price";
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

        final UserData user = UserData.getInstance();
        final JSONObject orderJson = new JSONObject();
        final ViewGroup fragment = (ViewGroup) inflater.inflate(R.layout.fragment_basket, null);
        this.fragment = fragment;

        getActivity().setTitle(getActivity().getString(R.string.title_fragment_basket));

        fillFragmentWithPreviews(fragment);

        final View totalBuyButton = fragment.findViewById(R.id.basket_buy_button);

        totalBuyButton.setOnClickListener(new View.OnClickListener() {
            private int price;

            public void onClick(View v) {
                if (user.getName().equals("Имя") || user.getTel().equals("Контактный телефон")
                        || user.getAddress().equals("Адрес") || user.getName().isEmpty() || user.getTel().isEmpty() || user.getAddress().isEmpty()) {
                    Toast.makeText(getActivity(), "Заполните данные аккаунта",
                            Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).setContent(FragmentFactory.getAccountFragment());
                } else {
                    final Set<BasketItem> items = Basket.getInstance().getItems();
                    final LinkedList<LinkedList> order_list = new LinkedList<>();
                    if (!items.isEmpty()) {
                        for (BasketItem entry : items) {
                            LinkedList<String> temp_pizda_list = new LinkedList<>();

                            String pizzaStr = entry.getItem().getName() + " Вес: " + entry.getDetails().getWeight() +
                                    " Цена: " + entry.getPrice() + " Количество: " + entry.getQuantity();
                            temp_pizda_list.add(pizzaStr);

                            final Set<Sauce> sauces = entry.getSauces();
                            for (Sauce sauce : sauces) {
                                String sauseStr = sauce.getTitle() + " Цена: " + sauce.getPrice();
                                temp_pizda_list.add(sauseStr);
                            }
                            order_list.add(temp_pizda_list);
                        }
                        try {
                            orderJson.put("username", user.getName());
                            orderJson.put("number", user.getTel());
                            orderJson.put("address", user.getAddress());
                            orderJson.put("order", order_list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        price = Basket.getInstance().getTotalPrice();
                    }

                    Network.sendOrderToServer(orderJson, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(), "Сеть не доступна, попробуйте позже",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.d("Network!", " " + e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("JSON", " " + orderJson);
                            Log.d("Network", "Response: " + response);
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(), "Заказ принят",
                                            Toast.LENGTH_SHORT).show();
                                    acceptOrder();
                                    flurryEventBuy(price);
                                }
                            });
                            response.body().close();
                        }
                    });
                }
            }
        });
        return fragment;
    }

    private void acceptOrder() {
        Basket.getInstance().emptyBasket();
        clearBasketList();
        changeTotalVisibility(true);
        displayOrderAccepted();
    }

    private void flurryEventBuy(int price) {
        final Map<String, String> eventParams = new HashMap<>(1);
        eventParams.put(FLURRY_EVENT_BUY_PRICE, String.valueOf(price));

        FlurryAgent.logEvent(FLURRY_EVENT_BUY, eventParams);
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

        final LinearLayout saucesContainer = (LinearLayout) basketItemLayout.findViewById(R.id.item_basket_sauces_container);
        for (final Sauce sauce : item.getSauces()) {
            final ViewGroup sauceListItem = (ViewGroup) inflater.inflate(R.layout.sauce_basket_item, saucesContainer, false);
            final TextView sauceNameView = (TextView) sauceListItem.findViewById(R.id.basket_sauce_title);
            sauceNameView.setText(sauce.getTitle());
            final TextView saucePriceView = (TextView) sauceListItem.findViewById(R.id.basket_sauce_price);
            saucePriceView.setText(sauce.getPrice(this));

            saucesContainer.addView(sauceListItem, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        PicassoImageLoader.getInstance()
                .load(getActivity(), item.getItem().getImagePath(), R.drawable.noimage, R.drawable.noimage, imageView);

        return basketItemLayout;
    }

    private void displayOrderAccepted() {
        final ViewGroup previewListContainer = (ViewGroup) fragment.findViewById(R.id.content_basket);
        final View message = inflater.inflate(R.layout.basket_order_accepted, null);
        previewListContainer.addView(message);
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
            clearBasketList();
            previewListContainer.addView(getBasketEmptyMessage());
        }
    }

    private void clearBasketList() {
        final ViewGroup previewListContainer = (ViewGroup) fragment.findViewById(R.id.content_basket);
        if (previewListContainer != null)
            previewListContainer.removeAllViews();
    }

    private ViewGroup getBasketEmptyMessage() {
        final ViewGroup messageLayout = (ViewGroup) inflater.inflate(R.layout.basket_empty, null);

        final View browsePizzas = messageLayout.findViewById(R.id.empty_basket_button);
        browsePizzas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setTabViewOnStack();
            }
        });

        return messageLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String getNameForItem(BasketItem item) {
        final StringBuilder title = new StringBuilder(item.getItem().getName())
                .append(' ').append(String.valueOf(
                        (int) item.getDetails().getWeight()))
                .append(getString(R.string.item_details_weight_postfix)
                );
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
