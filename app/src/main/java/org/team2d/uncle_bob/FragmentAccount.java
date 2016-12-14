package org.team2d.uncle_bob;

/**
 * Created by pavelrukavishnikov on 14.12.16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team2d.uncle_bob.Database.DatabaseService;
import org.team2d.uncle_bob.Database.ORM.UserData;


public class FragmentAccount extends Fragment {

    private static final Logger LOGGER = LoggerFactory.getLogger(FragmentAccount.class);
    private UserData user = UserData.getInstance();


    public static FragmentAccount newInstance() {
        return new FragmentAccount();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup fragment = (ViewGroup) inflater.inflate(R.layout.fragment_account_start, null);
        getActivity().setTitle(getString(R.string.navigation_drawer_option_account));
        Log.d("user", "name"+user.getName());
        DatabaseService.setUserInfo(getActivity(),"name", "xui", "8912");
        Log.d("user", "name"+user.getName());
        return fragment;
    }

}
