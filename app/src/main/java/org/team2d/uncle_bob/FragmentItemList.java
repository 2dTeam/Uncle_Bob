package org.team2d.uncle_bob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
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
import org.team2d.uncle_bob.Picasso.PicassoImageLoader;

import java.util.List;


public class FragmentItemList extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentItemList.class);
    private static final String ARG_CATEGORY_ID = "org.team2d.uncle_bob.FragmentItemList.CATEGORY_ID";
    private LayoutInflater inflater = null;
    private ViewGroup container = null;
    private Bundle savedInstanceState = null;
    private View mFragment;

    private final BroadcastReceiver UBBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fillFragmentWithPreviews((ViewGroup) mFragment);
        }
    };

    private class ActivityChanger implements View.OnClickListener {
        private final int iID;

        ActivityChanger(int itemID) {
            this.iID = itemID;
        }

        @Override
        public void onClick(View v) {
            ((MainActivity) getActivity()).setContent(FragmentFactory.getItemDetailsFragment(iID));
        }
    }

    public static FragmentItemList newInstance(ProductsEnum category) {
        final FragmentItemList fragment = new FragmentItemList();

        final Bundle categoryType = new Bundle();
        categoryType.putInt(ARG_CATEGORY_ID, category.toInt());
        fragment.setArguments(categoryType);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;

        final View fragment = inflater.inflate(R.layout.fragment_item_preview_list, container, false);
        mFragment = fragment;

        getActivity().setTitle(getTitleForCategory(ProductsEnum.fromInt(getArguments().getInt(ARG_CATEGORY_ID, 0)))); // Aww, LISP.

        UBIntentService.startActionLoadDB(getActivity());

        return fragment;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
//        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
    }

    // TODO If this is used somewhere else, move the code to ProductsEnum
    private String getTitleForCategory(ProductsEnum category) {
        if (category == ProductsEnum.PIZZA)
            return getString(R.string.category_pizza);
        if (category == ProductsEnum.DRINK)
            return getString(R.string.category_drinks);
        if (category == ProductsEnum.SALAD)
            return getString(R.string.category_salads);
        if (category == ProductsEnum.REFRESHMENT)
            return getString(R.string.category_refreshments);
        return getString(R.string.report_as_a_bug);
    }

    private View getItemPreview(String title, String price, String imagePath, String description) {
        final ViewGroup previewLayout = (ViewGroup) getLayoutInflater(savedInstanceState).inflate(R.layout.item_preview, null);

        final TextView titleTextView = (TextView) previewLayout.findViewById(R.id.item_preview_title);
        titleTextView.setText(title);

        final TextView descriptionTextView = (TextView) previewLayout.findViewById(R.id.item_description);
        descriptionTextView.setText(description);

        final TextView priceTextView = (TextView) previewLayout.findViewById(R.id.item_preview_price);
        priceTextView.setText(price);

        final ImageView imageView = (ImageView) previewLayout.findViewById(R.id.item_preview_image);

        PicassoImageLoader.getInstance()
                .load(getActivity(), imagePath, R.drawable.noimage, R.drawable.noimage, imageView);

        return previewLayout;
    }

    private void fillFragmentWithPreviews(ViewGroup fragment) {
        ViewGroup previewListContainer = (ViewGroup) fragment.findViewById(R.id.preview_list);

        final List <ItemObject> pizzas  = DatabaseService.getPizzaSortedByCost();

        for (ItemObject entry : pizzas) {
            final View itemPreview = getItemPreview(entry.getName(), entry.getLeastPrice(this),
                    entry.getImagePath(),entry.getDescription());

            itemPreview.setOnClickListener(new ActivityChanger(pizzas.indexOf(entry)));

            previewListContainer.addView(itemPreview);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(UBBroadcastReceiver,
                new IntentFilter(UBIntentService.DB_LOADED));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(UBBroadcastReceiver);
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        ViewGroup innerContainer;
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 20;
        }
        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.getItemDetailsFragment(position);
        }

    }

}