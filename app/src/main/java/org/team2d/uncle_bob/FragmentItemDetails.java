package org.team2d.uncle_bob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Basket.BasketItem;
import org.team2d.uncle_bob.Basket.QuantityButtonsWidget;
import org.team2d.uncle_bob.Basket.Sauce;
import org.team2d.uncle_bob.Database.DatabaseService;
import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;
import org.team2d.uncle_bob.Database.ORM.Items.ItemParams;


public class FragmentItemDetails extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentItemDetails.class);
    private static final String ARG_ITEM_ID = "org.team2d.uncle_bob.FragmentItemDetails.ITEM_ID";
    private static final String ARG_ITEM_DETAILS_ID = "org.team2d.uncle_bob.FragmentItemDetails.ITEM_DETAILS_ID";

    private ViewGroup fragment = null;
    private int itemID = 0;
    private ItemObject item = null;
    private ItemParams itemDetails = null;
    private BasketItem basketItem = null;
    private QuantityButtonsWidget buyButtons = null;

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

        if (savedInstanceState != null)
            itemDetails = ItemParams.fromInt(savedInstanceState.getInt(ARG_ITEM_DETAILS_ID, 0));
        if (itemDetails == null)
            itemDetails = item.getCheapestItem();

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
                final RadioButton weightButton = (RadioButton) View.inflate(getActivity(), R.layout.fragment_item_details_toggle_button_weight, null);
                final String buttonText = String.valueOf((int) params.getWeight()) + getString(R.string.item_details_weight_postfix);
                weightButton.setText(buttonText);
                weightButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)); // Somewhy these parameters in .xml are ignored
                weightButtonsContainer.addView(weightButton);
                weightButton.setOnClickListener(new DetailsChanger(params));
            }
        }

        // ItemParams has sauces specified
        if (true) {
            final ViewGroup sauceCheckBoxesContainer = (ViewGroup) fragment.findViewById(R.id.item_details_sauces);

            sauceCheckBoxesContainer.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_sauces_label).setVisibility(View.VISIBLE);

            for (final Sauce sauce : Sauce.getSauces()) {
                final CheckBox sauceCheckBox = (CheckBox) fragment.inflate(getActivity(), R.layout.fragment_item_details_checkbox_sauce, null);
                sauceCheckBox.setText(sauce.getTitle());
                sauceCheckBoxesContainer.addView(sauceCheckBox);
                sauceCheckBox.setOnClickListener(new SauceChanger(sauce));
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

        final LinearLayout buttonsContainer = (LinearLayout) fragment.findViewById(R.id.item_buttons_container);
        buyButtons = new QuantityButtonsWidget(getLayoutInflater(null), buttonsContainer, item, itemDetails, new Recalculator());

        basketItem = buyButtons.refresh();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_ITEM_DETAILS_ID, itemDetails.toInt());
    }

    @Override
    public void onStop() {
        super.onStop();

        buyButtons.destroyListeners();
        // remove other listeners
    }

    private void calculatePrice() {
        final TextView priceTextView = (TextView) fragment.findViewById(R.id.item_details_price);
        if (basketItem.getQuantity() > 0)
            priceTextView.setText(basketItem.getPrice(this));
        else
            priceTextView.setText(basketItem.getItem().getLeastPrice(this));
    }

    private class Recalculator implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            calculatePrice();
        }
    }

    private class SauceChanger implements View.OnClickListener {
        private final Sauce sauce;

        public SauceChanger(Sauce sauce) {
            this.sauce = sauce;
        }

        @Override
        public void onClick(View v) {
            if (((Checkable) v).isChecked())
                basketItem.addSauce(sauce);
            else
                basketItem.removeSauce(sauce);

            calculatePrice();
        }
    }

    private class DetailsChanger implements View.OnClickListener {
        private final ItemParams params;

        public DetailsChanger(ItemParams params) {
            this.params = params;
        }

        @Override
        public void onClick(View v) {
            buyButtons.changeItemParameters(params);

            basketItem = buyButtons.refresh();
            calculatePrice();
        }
    }

}
