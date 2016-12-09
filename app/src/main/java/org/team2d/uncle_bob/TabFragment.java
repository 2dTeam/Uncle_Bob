package org.team2d.uncle_bob;

/**
 * Created by nikolaev on 04.12.16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.team2d.uncle_bob.Database.ProductsEnum;

public class TabFragment extends Fragment {
    public static int int_items = 3 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.tab_layout, null);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int pos) {
                      switch (pos) {
                            case 0:
                                getActivity().setTitle(getString(R.string.category_pizza));
                                break;
                            case 1:
                                getActivity().setTitle(getString(R.string.category_refreshments));
                                break;
                            case 2:
                                getActivity().setTitle(getString(R.string.category_drinks));
                                break;

                        }
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {}

                    @Override
                    public void onPageScrollStateChanged(int pos) {}

                });

        return view;

    }


    class MyAdapter extends FragmentPagerAdapter{
        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0 : return FragmentFactory.getDefaultFragment();
                case 1 : return FragmentFactory.getItemListFragment(ProductsEnum.REFRESHMENT);
                case 2 : return FragmentFactory.getItemListFragment(ProductsEnum.DRINK);
            }
            return null;
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0 :
                    return getString(R.string.category_pizza);
                case 1 :
                    return getString(R.string.category_refreshments);
                case 2 :
                    return getString(R.string.category_drinks);
            }
            return null;
        }

    }

}