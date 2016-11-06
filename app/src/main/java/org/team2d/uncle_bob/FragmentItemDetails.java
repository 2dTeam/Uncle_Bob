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
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Database.DatabaseService;
import org.team2d.uncle_bob.Database.ORM.PizzaORM;

import java.util.ArrayList;
import java.util.List;


public class FragmentItemDetails extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentItemDetails.class);
    private static final String ARG_ITEM_ID = "org.team2d.uncle_bob.FragmentItemDetails.ITEM_ID";

    private final List<View> onClickSubscribers = new ArrayList<>();
    private class QuantityChanger implements View.OnClickListener {
        private final int delta;

        QuantityChanger(int deltaQuantity) {
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

    private ViewGroup fragment = null;

    private int itemID = 0;
    private PizzaORM item = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = (ViewGroup) inflater.inflate(R.layout.fragment_item_details, null);

        itemID = getArguments().getInt(ARG_ITEM_ID, 0);
        item = DatabaseService.getPizza(getContext()).get(itemID);

        fillFragmentWithItemDetails(fragment);

        return fragment;
    }

    private void fillFragmentWithItemDetails(ViewGroup fragment) {
        getActivity().setTitle(item.getName());

        // Item has weight specified
        if (true) {
            final ViewGroup weightButtonsContainer = (ViewGroup) fragment.findViewById(R.id.item_details_weight);

            weightButtonsContainer.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_weight_label).setVisibility(View.VISIBLE);

            /// foreach weight position:
            /// final View weightButton = fragment.inflate(getActivity(), R.layout.fragment_item_details_toggle_button_weight, null);
            /// weightButtonsContainer.addView(weightButton);
        }

        // Item has sauces specified
        if (true) {
            final ViewGroup sauceCheckBoxesContainer = (ViewGroup) fragment.findViewById(R.id.item_details_sauces);

            sauceCheckBoxesContainer.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_sauces_label).setVisibility(View.VISIBLE);

            /// foreach sauce position:
            /// final View sauceCheckBox = fragment.inflate(getActivity(), R.layout.fragment_item_details_checkbox_sauce, null);
            /// sauceCheckBoxesContainer.addView(sauceCheckBox);
        }

        // Item has contains specified
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
        buttonBuy.setOnClickListener(new QuantityChanger(+1));
        onClickSubscribers.add(buttonBuy);

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
