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
import org.team2d.uncle_bob.AsyncLoad.LoadDatabaseAsyncTask;
import org.team2d.uncle_bob.Database.DatabaseService;
import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;

import java.util.List;


public class FragmentItemList extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentItemList.class);
    private static final String ARG_CATEGORY_ID = "org.team2d.uncle_bob.FragmentItemList.CATEGORY_ID";
    private LayoutInflater inflater = null;
    private ViewGroup container = null;
    private Bundle savedInstanceState = null;

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

    public static FragmentItemList newInstance(int categoryID) {
        final FragmentItemList fragment = new FragmentItemList();

        final Bundle categoryType = new Bundle();
        categoryType.putInt(ARG_CATEGORY_ID, categoryID);
        fragment.setArguments(categoryType);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;

        final View fragment = inflater.inflate(R.layout.fragment_item_preview_list, null);

        // TODO: make title according to category
        getActivity().setTitle("Not implemented");
        fillFragmentWithPreviews((ViewGroup) fragment);

        return fragment;
    }

    private View getItemPreview(String title, String price, String imagePath) {
        final ViewGroup previewLayout = (ViewGroup) getLayoutInflater(savedInstanceState).inflate(R.layout.item_preview, null);

        final TextView titleTextView = (TextView) previewLayout.findViewById(R.id.item_preview_title);
        titleTextView.setText(title);

        final TextView priceTextView = (TextView) previewLayout.findViewById(R.id.item_preview_price);
        priceTextView.setText(price);

        final ImageView imageView = (ImageView) previewLayout.findViewById(R.id.item_preview_image);
        imageView.setImageResource(getResources().getIdentifier(imagePath, "drawable", getActivity().getPackageName()));

        return previewLayout;
    }

    private void fillFragmentWithPreviews(ViewGroup fragment) {
        final ViewGroup previewListContainer = (ViewGroup) fragment.findViewById(R.id.preview_list);

        // TODO: rewrite
        new LoadDatabaseAsyncTask(getActivity()).execute();
        final List <ItemObject> pizzas  = DatabaseService.getPizzaSortedByCost();

        for (ItemObject entry : pizzas) {
            final View itemPreview = getItemPreview(entry.getName(), "Not implemented", entry.getImagePath());

            itemPreview.setOnClickListener(new ActivityChanger(pizzas.indexOf(entry)));

            previewListContainer.addView(itemPreview);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}
