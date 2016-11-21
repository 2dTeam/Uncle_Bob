package org.team2d.uncle_bob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Basket.Basket;
import org.team2d.uncle_bob.Basket.BasketItem;
import org.team2d.uncle_bob.Database.DatabaseService;
import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;
import org.team2d.uncle_bob.Database.ORM.Items.ItemParams;


public class FragmentItemDetails extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentItemDetails.class);
    private static final String ARG_ITEM_ID = "org.team2d.uncle_bob.FragmentItemDetails.ITEM_ID";

    private ViewGroup fragment = null;
    private int itemID = 0;
    private ItemObject item = null;
    private BasketItem basketItem = null;

    private class QuantityChanger implements View.OnClickListener {
        private final int delta;

        QuantityChanger(int deltaQuantity) {
            this.delta = deltaQuantity;
        }

        @Override
        public void onClick(View v) {
            Basket.getInstance().addItem(basketItem);
//            final TextView priceTextView = (TextView) fragment.findViewById(R.id.item_details_quantity);
//
//            final int newQuantity = Integer.decode(priceTextView.getText().toString()) + delta;
//
//            priceTextView.setText(newQuantity);
//            if (newQuantity <= 0)
//                clearQuantityButtons();
//            else
//                createQuantityButtons();
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

        basketItem = new BasketItem(item, item.getCheapestItem());

        fillFragmentWithItemDetails(fragment);

        return fragment;
    }

    private void fillFragmentWithItemDetails(ViewGroup fragment) {
        getActivity().setTitle(item.getName());

        // ItemParams has weight specified
        if (!item.getAllItems().isEmpty()) {
            final ViewGroup weightButtonsContainer = (ViewGroup) fragment.findViewById(R.id.item_details_weight);

            weightButtonsContainer.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_weight_label).setVisibility(View.VISIBLE);

            for (final ItemParams params : item.getAllItems()) {
                final RadioButton weightButton = (RadioButton) fragment.inflate(getActivity(), R.layout.fragment_item_details_toggle_button_weight, null);
                final String buttonText = String.valueOf((int) params.getWeight()) + getString(R.string.item_details_weight_postfix);
                weightButton.setText(buttonText);
                weightButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)); // Somewhy these parameters in .xml are ignored
                weightButtonsContainer.addView(weightButton);
            }
        }

        // ItemParams has sauces specified
        if (true) {
            final ViewGroup sauceCheckBoxesContainer = (ViewGroup) fragment.findViewById(R.id.item_details_sauces);

            sauceCheckBoxesContainer.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_sauces_label).setVisibility(View.VISIBLE);

            // TODO: Replace with sauces iterator
            for (final ItemParams params : item.getAllItems()) {
                final CheckBox sauceCheckBox = (CheckBox) fragment.inflate(getActivity(), R.layout.fragment_item_details_checkbox_sauce, null);
                final String buttonText = String.valueOf((int) params.getWeight()) + getString(R.string.item_details_weight_postfix);
                sauceCheckBox.setText(item.getName());
                sauceCheckBoxesContainer.addView(sauceCheckBox);
            }
        }

        // ItemParams has contains specified
        if (true) {
            final TextView containsText = (TextView) fragment.findViewById(R.id.item_details_contains);

            containsText.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_contains_label).setVisibility(View.VISIBLE);

            containsText.setText(item.getName());
        }

        // Minimal price for the first time
        final TextView priceTextView = (TextView) fragment.findViewById(R.id.item_details_price);
        priceTextView.setText(item.getLeastPrice(this));

        final ImageView imageView = (ImageView) fragment.findViewById(R.id.item_details_image);
        imageView.setImageResource(getResources().getIdentifier(item.getImagePath(), "drawable", getActivity().getPackageName()));

        setOnClickSubscribers();
    }

    private void setOnClickSubscribers() {
        final View buttonBuy = fragment.findViewById(R.id.item_details_buy);
        buttonBuy.setOnClickListener(new QuantityChanger(+1));

        final View buttonInc = fragment.findViewById(R.id.item_details_quantity_increase);
        buttonInc.setOnClickListener(new QuantityChanger(+1));

        final View buttonDec = fragment.findViewById(R.id.item_details_quantity_decrease);
        buttonDec.setOnClickListener(new QuantityChanger(-1));
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

    }

}
