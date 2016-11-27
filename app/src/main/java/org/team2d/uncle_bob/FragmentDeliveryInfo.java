package org.team2d.uncle_bob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FragmentDeliveryInfo extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentDeliveryInfo.class);

    public static FragmentDeliveryInfo newInstance() {
        return new FragmentDeliveryInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup fragment = (ViewGroup) inflater.inflate(R.layout.fragment_delivery_info, null);
        getActivity().setTitle(getString(R.string.navigation_drawer_option_about));
        return fragment;
    }



}
