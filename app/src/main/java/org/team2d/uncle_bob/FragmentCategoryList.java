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
import org.team2d.uncle_bob.Database.DatabaseService;
import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;
import org.team2d.uncle_bob.Database.ProductsEnum;

import java.util.List;


public class FragmentCategoryList extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentCategoryList.class);
    private static final String ARG_CATEGORY_ID = "org.team2d.uncle_bob.FragmentItemList.CATEGORY_ID";
    private LayoutInflater inflater = null;
    private ViewGroup container = null;
    private Bundle savedInstanceState = null;
    private View mFragment;

    private class ActivityChanger implements View.OnClickListener {
        private final ProductsEnum catID;

        ActivityChanger(ProductsEnum itemID) {
            this.catID = itemID;
        }

        @Override
        public void onClick(View v) {
            ((MainActivity) getActivity()).setContent(FragmentFactory.getItemListFragment(catID));
        }
    }

    public static FragmentCategoryList newInstance() {
        final FragmentCategoryList fragment = new FragmentCategoryList();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;

        final View fragment = inflater.inflate(R.layout.content_category_list, null);
        mFragment = fragment;

        getActivity().setTitle(getString(R.string.title_fragment_category_list));

        final View pizzaButton = fragment.findViewById(R.id.category_pizza);
        final View drinksButton = fragment.findViewById(R.id.category_drinks);
        final View saladsButton = fragment.findViewById(R.id.category_salads);
        final View refreshmentsButton = fragment.findViewById(R.id.category_refreshments);

        pizzaButton.setOnClickListener(new ActivityChanger(ProductsEnum.PIZZA));
        drinksButton.setOnClickListener(new ActivityChanger(ProductsEnum.DRINK));
        saladsButton.setOnClickListener(new ActivityChanger(ProductsEnum.SALAD));
        refreshmentsButton.setOnClickListener(new ActivityChanger(ProductsEnum.REFRESHMENT));

        return fragment;
    }


}
