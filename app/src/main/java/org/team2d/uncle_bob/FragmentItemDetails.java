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
import org.team2d.uncle_bob.Database.ProductsEnum;
import org.team2d.uncle_bob.Picasso.PicassoImageLoader;


public class FragmentItemDetails extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentItemDetails.class);
    private static final String ARG_ITEM_ID = "org.team2d.uncle_bob.FragmentItemDetails.ITEM_ID";
    private static final String ARG_ITEM_DETAILS_ID = "org.team2d.uncle_bob.FragmentItemDetails.ITEM_DETAILS_ID";
    private static final String ARG_CATEGORY_ID = "org.team2d.uncle_bob.FragmentItemDetails.CATEGORY_ID";
    private static final String TAG_WEIGHT_BUTTON = "org.team2d.uncle_bob.FragmentItemDetails.TAG_WEIGHT_BUTTON_";
    private static final String TAG_SAUCE_BUTTON = "org.team2d.uncle_bob.FragmentItemDetails.TAG_SAUCE_BUTTON_";

    private ViewGroup fragment = null;
    private int itemID = 0;
    private int categoryID = 0;
    private ItemObject item = null;
    private ItemParams itemDetails = null;
    private BasketItem basketItem = null;
    private QuantityButtonsWidget buyButtons = null;

    public static FragmentItemDetails newInstance(int categoryID, int itemID) {
        final FragmentItemDetails fragment = new FragmentItemDetails();

        final Bundle categoryType = new Bundle();
        categoryType.putInt(ARG_ITEM_ID, itemID);
        categoryType.putInt(ARG_CATEGORY_ID, categoryID);
        fragment.setArguments(categoryType);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = (ViewGroup) inflater.inflate(R.layout.fragment_item_details, container, false);

        itemID = getArguments().getInt(ARG_ITEM_ID, 0);
        categoryID = getArguments().getInt(ARG_CATEGORY_ID, 0);
        if (ProductsEnum.fromInt(categoryID) == ProductsEnum.PIZZA)
            item = DatabaseService.getPizzaSortedByCost().get(itemID);
        else if (ProductsEnum.fromInt(categoryID) == ProductsEnum.DRINK)
            item = DatabaseService.getDrinksSortedByCost().get(itemID);
        else if (ProductsEnum.fromInt(categoryID) == ProductsEnum.REFRESHMENT)
            item = DatabaseService.getRefreshmentsSortedByCost().get(itemID);

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
                weightButton.setTag(TAG_WEIGHT_BUTTON + params.toInt());
            }
        }

        // ItemParams has sauces specified
        if (item.getCategory() == ProductsEnum.PIZZA) {
            final ViewGroup sauceCheckBoxesContainer = (ViewGroup) fragment.findViewById(R.id.item_details_sauces);

            sauceCheckBoxesContainer.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_sauces_label).setVisibility(View.VISIBLE);

            for (final Sauce sauce : Sauce.getSauces()) {
                final CheckBox sauceCheckBox = (CheckBox) View.inflate(getActivity(), R.layout.fragment_item_details_checkbox_sauce, null);
                sauceCheckBox.setText(sauce.getTitle());
                sauceCheckBoxesContainer.addView(sauceCheckBox);
                sauceCheckBox.setOnClickListener(new SauceChanger(sauce));
                sauceCheckBox.setTag(TAG_SAUCE_BUTTON + sauce.toInt());
            }
        }

        // ItemParams has contains specified
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            final TextView containsText = (TextView) fragment.findViewById(R.id.item_details_contains);

            containsText.setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.item_details_contains_label).setVisibility(View.VISIBLE);

            containsText.setText(item.getDescription());
        }

        // Minimal price for the first time
        final TextView priceTextView = (TextView) fragment.findViewById(R.id.item_details_price);
        priceTextView.setText(item.getLeastPrice(this));

        final ImageView imageView = (ImageView) fragment.findViewById(R.id.item_details_image);
        PicassoImageLoader.getInstance()
                .load(getActivity(), item.getImagePath(), R.drawable.noimage, R.drawable.noimage, imageView);

        final LinearLayout buttonsContainer = (LinearLayout) fragment.findViewById(R.id.item_buttons_container);
        buyButtons = new QuantityButtonsWidget(getLayoutInflater(null), buttonsContainer, item, itemDetails, new Recalculator(), new UIStateMatcher());

        basketItem = buyButtons.refresh();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (itemDetails != null)
            outState.putInt(ARG_ITEM_DETAILS_ID, itemDetails.toInt());
    }

    @Override
    public void onStop() {
        super.onStop();
        buyButtons.destroyListeners();
    }

    private void calculatePrice() {
        final TextView priceTextView = (TextView) fragment.findViewById(R.id.item_details_price);
        if (basketItem.getQuantity() > 0)
            priceTextView.setText(basketItem.getPrice(this));
        else
            priceTextView.setText(basketItem.getLeastPrice(this));
    }

    private class Recalculator implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            calculatePrice();
        }
    }

    private class UIStateMatcher implements QuantityButtonsWidget.OnStateChangedListener {
        @Override
        public void act(BasketItem item) {
            if (item != null) {
                ItemParams selectedWeightOption = item.getDetails();
                Checkable weightButton = (Checkable) fragment.findViewWithTag(TAG_WEIGHT_BUTTON + selectedWeightOption.toInt());
                if (weightButton != null)
                    weightButton.setChecked(true);

                for (Sauce sauce : Sauce.getSauces()) {
                    Checkable sauceChecker = (Checkable) fragment.findViewWithTag(TAG_SAUCE_BUTTON + sauce.toInt());
                    if (sauceChecker != null)
                        if (item.getSauces().contains(sauce))
                            sauceChecker.setChecked(true);
                        else
                            sauceChecker.setChecked(false);
                }
            }
        }
    }

    private class SauceChanger implements View.OnClickListener {
        private final Sauce sauce;

        SauceChanger(Sauce sauce) {
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

        DetailsChanger(ItemParams params) {
            this.params = params;
        }

        @Override
        public void onClick(View v) {
            buyButtons.changeItemParameters(params);

            basketItem = buyButtons.refresh();
            calculatePrice();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        buyButtons.refresh();
    }
}
