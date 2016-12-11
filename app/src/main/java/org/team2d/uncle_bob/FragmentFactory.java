package org.team2d.uncle_bob;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import org.team2d.uncle_bob.Database.ProductsEnum;

final class FragmentFactory {

    @NonNull
    static Fragment getDefaultFragment() {
        return getItemListFragment(ProductsEnum.PIZZA);
    }

    @NonNull
    static Fragment getBasketFragment() {
        return FragmentBasket.newInstance();
    }

    @NonNull
    static Fragment getItemDetailsFragment(int itemID) {
        return FragmentItemDetails.newInstance(itemID);
    }

    @NonNull
    static Fragment getItemListFragment(ProductsEnum category) {
        return FragmentItemList.newInstance(category);
    }

    @NonNull
    static Fragment getAccountFragment() {
        return FragmentItemDetails.newInstance(2);
    }

    @NonNull
    static Fragment getDeliveryInfo() {
        return FragmentDeliveryInfo.newInstance();
    }
}
