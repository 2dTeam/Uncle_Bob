package org.team2d.uncle_bob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import android.util.Log;
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Database.DatabaseService;
import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;

import okhttp3.OkHttpClient;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Headers;
import okhttp3.Callback;
import okhttp3.Call;


import java.util.ArrayList;
import java.util.List;


public class FragmentItemDetails extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentItemDetails.class);
    private static final String ARG_ITEM_ID = "org.team2d.uncle_bob.FragmentItemDetails.ITEM_ID";

    private ViewGroup fragment = null;
    private int itemID = 0;
    private ItemObject item = null;

    private final List<View> onClickSubscribers = new ArrayList<>();
    private class QuantityChanger implements View.OnClickListener {
        private final int delta;

        QuantityChanger(int deltaQuantity) {
            //Log.d("kek", Integer.toString(deltaQuantity));
            this.delta = itemID;
        }

        @Override
        public void onClick(View v) {
            final TextView priceTextView = (TextView) fragment.findViewById(R.id.item_details_quantity);

            // This should be ItemORMBasketAdapter.quantity
            final int newQuantity = Integer.decode(priceTextView.getText().toString()) + delta;

            priceTextView.setText(newQuantity);
            if (newQuantity <= 0)
                clearQuantityButtons();
            else
                createQuantityButtons();
        }
    }

    public static FragmentItemDetails newInstance(int itemID) {
        final FragmentItemDetails fragment = new FragmentItemDetails();

        final Bundle categoryType = new Bundle();
        categoryType.putInt(ARG_ITEM_ID, itemID);
        fragment.setArguments(categoryType);

        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = (ViewGroup) inflater.inflate(R.layout.fragment_item_details, null);

        itemID = getArguments().getInt(ARG_ITEM_ID, 0);
        item = DatabaseService.getPizzaSortedByCost().get(itemID);

        fillFragmentWithItemDetails(fragment);

        return fragment;
    }

    private void fillFragmentWithItemDetails(ViewGroup fragment) {
        getActivity().setTitle(item.getName());

        // ItemParams has weight specified
        if (true) {
            final ViewGroup weightButtonsContainer = (ViewGroup) fragment.findViewById(R.id.item_details_weight);

            weightButtonsContainer.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_weight_label).setVisibility(View.VISIBLE);

            /// foreach weight position:
            /// final View weightButton = fragment.inflate(getActivity(), R.layout.fragment_item_details_toggle_button_weight, null);
            /// weightButtonsContainer.addView(weightButton);
        }

        // ItemParams has sauces specified
        if (true) {
            final ViewGroup sauceCheckBoxesContainer = (ViewGroup) fragment.findViewById(R.id.item_details_sauces);

            sauceCheckBoxesContainer.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_sauces_label).setVisibility(View.VISIBLE);

            /// foreach sauce position:
            /// final View sauceCheckBox = fragment.inflate(getActivity(), R.layout.fragment_item_details_checkbox_sauce, null);
            /// sauceCheckBoxesContainer.addView(sauceCheckBox);
        }

        // ItemParams has contains specified
        if (true) {
            final TextView containsText = (TextView) fragment.findViewById(R.id.item_details_contains);

            containsText.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_contains_label).setVisibility(View.VISIBLE);

            /// containsText.setText(item.text);
        }

        // Minimal price for the first time
        final TextView priceTextView = (TextView) fragment.findViewById(R.id.item_details_price);
        priceTextView.setText("" + item.getId() + ' ' + item.getOnlineId());

        final ImageView imageView = (ImageView) fragment.findViewById(R.id.item_details_image);
        imageView.setImageResource(getResources().getIdentifier(item.getImagePath(), "drawable", getActivity().getPackageName()));

        setOnClickSubscribers();
    }

    private void setOnClickSubscribers() {
        final View buttonBuy = fragment.findViewById(R.id.item_details_buy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("INFO", "run");

                OkHttpClient client = new OkHttpClient();
                Log.d("INFO", client.toString());
                Request request = new Request.Builder()
                        .url("http://publicobject.com/helloworld.txt")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }

                        System.out.println(response.body().string());
                    }
                });
            }
        });
        //onClickSubscribers.add(buttonBuy);

        final View buttonInc = fragment.findViewById(R.id.item_details_quantity_increase);
        buttonInc.setOnClickListener(new QuantityChanger(+1));
        onClickSubscribers.add(buttonInc);

        final View buttonDec = fragment.findViewById(R.id.item_details_quantity_decrease);
        buttonDec.setOnClickListener(new QuantityChanger(-1));
        onClickSubscribers.add(buttonDec);
    }

    private void clearQuantityButtons() {
        final View buttonBuy = fragment.findViewById(R.id.item_details_buy);
        buttonBuy.setVisibility(View.VISIBLE);

        final View buttonInc = fragment.findViewById(R.id.item_details_quantity_increase);
        buttonInc.setVisibility(View.GONE);
        final View textQuantity = fragment.findViewById(R.id.item_details_quantity);
        textQuantity.setVisibility(View.GONE);
        final View buttonDec = fragment.findViewById(R.id.item_details_quantity_decrease);
        buttonDec.setVisibility(View.GONE);
    }

    private void createQuantityButtons() {
        final View buttonBuy = fragment.findViewById(R.id.item_details_buy);
        buttonBuy.setVisibility(View.GONE);

        final View buttonInc = fragment.findViewById(R.id.item_details_quantity_increase);
        buttonInc.setVisibility(View.VISIBLE);
        final View textQuantity = fragment.findViewById(R.id.item_details_quantity);
        textQuantity.setVisibility(View.VISIBLE);
        final View buttonDec = fragment.findViewById(R.id.item_details_quantity_decrease);
        buttonDec.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        for (final View subscriber : onClickSubscribers)
            subscriber.setOnClickListener(null);
    }

}
