package org.team2d.uncle_bob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Basket.Basket;
import org.team2d.uncle_bob.Basket.BasketItem;
import org.team2d.uncle_bob.Database.DatabaseService;
import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;
import org.team2d.uncle_bob.Database.ProductsEnum;
import org.team2d.uncle_bob.Picasso.PicassoImageLoader;

import java.util.List;
import java.util.Set;


public class FragmentBasket extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentBasket.class);
    private static final String ARG_CATEGORY_ID = "org.team2d.uncle_bob.FragmentItemList.CATEGORY_ID";
    private LayoutInflater inflater = null;
    private ViewGroup container = null;
    private Bundle savedInstanceState = null;
    private View fragment;

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

        final ViewGroup fragment = (ViewGroup) inflater.inflate(R.layout.fragment_item_preview_list, null);
        this.fragment = fragment;

        getActivity().setTitle(getActivity().getString(R.string.title_fragment_basket)); // Aww, LISP.

        fillFragmentWithPreviews(fragment);

        return fragment;
    }

    private View getItemPreview(String title, String price, String imagePath) {
        final ViewGroup previewLayout = (ViewGroup) getLayoutInflater(savedInstanceState).inflate(R.layout.item_preview, null);

        final TextView titleTextView = (TextView) previewLayout.findViewById(R.id.item_preview_title);
        titleTextView.setText(title);

        final TextView priceTextView = (TextView) previewLayout.findViewById(R.id.item_preview_price);
        priceTextView.setText(price);

        final ImageView imageView = (ImageView) previewLayout.findViewById(R.id.item_preview_image);

        PicassoImageLoader.getInstance()
                .load(getActivity(), imagePath, R.drawable.noimage, R.drawable.noimage, imageView);

        return previewLayout;
    }

    private void fillFragmentWithPreviews(ViewGroup fragment) {
        final ViewGroup previewListContainer = (ViewGroup) fragment.findViewById(R.id.preview_list);

        final Set<BasketItem> items  = Basket.getInstance().getItems();

        if (!items.isEmpty())
            for (BasketItem entry : items) {
                final View itemPreview = getItemPreview(entry.getItem().getName(), entry.getPrice(this) + " для " + entry.getQuantity(), entry.getItem().getImagePath());

                previewListContainer.addView(itemPreview);
            }
        else
            previewListContainer.addView(getBasketEmptyMessage());
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
}
