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
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

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

        Log.d("user", "name"+user.getTel());

        EditText text_name = (EditText) fragment.findViewById(R.id.account_name);
        text_name.setText((CharSequence) user.getName());

        EditText text_phone = (EditText) fragment.findViewById(R.id.account_phone);
        text_phone.setText((CharSequence) user.getTel());

        EditText text_address = (EditText) fragment.findViewById(R.id.account_address);
        text_address.setText((CharSequence) user.getAddress());

        final View accountSaveInfo = fragment.findViewById(R.id.account_save_info);

        accountSaveInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
//        DatabaseService.setUserInfo(getActivity(),"name", "xui moskva", "88005553535");
        Log.d("user", "name"+user.getTel());
        return fragment;
    }

}
