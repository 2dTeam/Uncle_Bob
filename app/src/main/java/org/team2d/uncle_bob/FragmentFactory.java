package org.team2d.uncle_bob;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import org.team2d.uncle_bob.Database.ProductsEnum;

final public class FragmentFactory {
    public static Fragment getCurrentFragment() {
        return currentFragment;
    }

    private static Fragment currentFragment = getDefaultFragment();
    private FragmentFactory() {}

    @NonNull
    public static Fragment getDefaultFragment() {
        return getItemListFragment(ProductsEnum.PIZZA);
    }

    @NonNull
    public static Fragment getBasketFragment(/* TODO: some arguments, maybe? */) {
        return null;
    }

    @NonNull
    public static Fragment getItemDetailsFragment(int itemID) {
        Fragment frg = FragmentItemDetails.newInstance(itemID);
        currentFragment = frg;
        return frg;
    }

    @NonNull
    public static Fragment getItemListFragment(ProductsEnum category) {
        Fragment frg = FragmentItemList.newInstance(category);
        currentFragment = frg;
        return frg;
    }

    @NonNull
    public static Fragment getCategoryListFragment() {
        return FragmentCategoryList.newInstance();
    }

    @NonNull
    public static Fragment getHistoryFragment() {
        return null;
    }

    @NonNull
    public static Fragment getAccountFragment() {
        return null;
    }

    @NonNull
    public static Fragment getContactsFragment() {
        return null;
    }

    @NonNull
    public static Fragment getSalesFragment() {
        return null;
    }
    @NonNull
    public static Fragment getDeliveryInfo() {
        Fragment frg = FragmentDeliveryInfo.newInstance();
        currentFragment = frg;
        return frg;
    }
}
